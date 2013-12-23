package name.ixr.nano.common.spi;

import name.ixr.nano.common.config.BaseConfig;
import name.ixr.nano.common.engine.ConfigEngine;
import name.ixr.nano.common.config.PageConfig;
import name.ixr.nano.common.config.SegmentConfig;
import name.ixr.nano.common.context.RequestContext;
import name.ixr.nano.common.context.ContentType;
import name.ixr.nano.common.context.HttpRequest;
import name.ixr.nano.common.context.HttpResponse;
import name.ixr.nano.common.utils.ResourceUtils;
import com.googlecode.htmlcompressor.compressor.Compressor;
import com.googlecode.htmlcompressor.compressor.YuiCssCompressor;
import com.googlecode.htmlcompressor.compressor.YuiJavaScriptCompressor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashSet;

/**
 * PageStaticSPI : TODO: yuuji
 * yuuji 5:39 PM 12/4/13
 */
@Component
public class PageStaticSPI extends UrlRoutingSPI{

    @Override
    public String route() {
        return "/static/page.{suffix:(js|css)}";
    }

    public void invoke() throws Exception {
        HttpRequest req = RequestContext.request();
        HttpResponse res = RequestContext.response();
        String suffix = req.getUriSuffix(1);
        req.setUri(req.getReferer());
        String pageName = req.getUriBefore(1);
        res.setContentType(ContentType.findContentType(suffix));
        PageConfig pageConfig = ConfigEngine.getPageConfig(pageName);
        StringWriter writer = new StringWriter();
        write(ConfigEngine.getLayoutConfig(pageConfig.getView()), writer, suffix);
        HashSet<String> apps = new HashSet<String>();
        for (SegmentConfig segmentConfig : pageConfig.getSegments()) {
            write(ConfigEngine.getLayoutConfig(segmentConfig.getLayout()), writer, suffix);
            for (String appName : segmentConfig.getApps()) {
                if (apps.contains(appName)) {
                    continue;
                }
                apps.add(appName);
                write(ConfigEngine.getAppConfig(appName), writer, suffix);
            }
        }
        Compressor compressor = null;
        if ("js".equals(suffix)) {
            compressor = new YuiJavaScriptCompressor();
        } else if ("css".equals(suffix)) {
            compressor = new YuiCssCompressor();
        }
        res.content().writeBytes(compressor.compress(writer.toString()).getBytes());
    }

    public void write(BaseConfig layoutConfig, StringWriter writer, String suffix) {
        try {
            if ("js".equals(suffix)
                    && layoutConfig.getJs() != null
                    && layoutConfig.getJs().exists()) {
                InputStream stream = ResourceUtils.getInputStream(layoutConfig.getJs());
                writer.write(IOUtils.toString(stream));
            } else if ("css".equals(suffix)
                    && layoutConfig.getCss() != null
                    && layoutConfig.getCss().exists()) {
                InputStream stream = ResourceUtils.getInputStream(layoutConfig.getCss());
                writer.write(IOUtils.toString(stream));
            }
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
}
