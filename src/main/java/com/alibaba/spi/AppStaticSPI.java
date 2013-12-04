package com.alibaba.spi;

import com.alibaba.config.ConfigEngine;
import com.alibaba.config.PageConfig;
import com.alibaba.config.SegmentConfig;
import com.alibaba.utils.ContentType;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import com.googlecode.htmlcompressor.compressor.Compressor;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.googlecode.htmlcompressor.compressor.YuiCssCompressor;
import com.googlecode.htmlcompressor.compressor.YuiJavaScriptCompressor;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * AppStaticSPI : TODO: yuuji
 * yuuji 10:54 AM 12/4/13
 */
public class AppStaticSPI {

    public HttpResponse invoke(HttpRequest req) {
        String suffix = req.getUriSuffix(1);
        req.setUri(req.getReferer());
        String pageName = req.getUriBefore(1);
        HttpResponse res = new HttpResponse();
        res.setContentType(ContentType.findContentType(suffix));
        try {
            PageConfig pageConfig = ConfigEngine.getPageConfig(pageName);
            List<SegmentConfig> segmentsConfig = pageConfig.getSegments();
            List<String> apps = new ArrayList<String>();
            StringWriter writer = new StringWriter();
            for (SegmentConfig segmentConfig : segmentsConfig) {
                for (String appName : segmentConfig.getApps()) {
                    if (apps.contains(appName)) {
                        continue;
                    }
                    try {
                        String path = "/apps/" + appName + "/view." + suffix;
                        InputStream inputStream = this.getClass().getResource(path).openStream();
                        writer.write(IOUtils.toString(inputStream));
                        writer.flush();
                    } catch (Exception ex) {
                    }
                    apps.add(appName);
                }
            }
            Compressor compressor = null;
            if ("js".equals(suffix)) {
                compressor = new YuiJavaScriptCompressor();
            } else if ("css".equals(suffix)) {
                compressor = new YuiCssCompressor();
            }
            res.content().writeBytes(compressor.compress(writer.toString()).getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HttpResponse(HttpResponseStatus.NOT_FOUND);
        }
        return res;
    }
}
