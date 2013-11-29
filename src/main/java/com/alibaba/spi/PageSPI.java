package com.alibaba.spi;

import com.alibaba.config.PageConfig;
import com.alibaba.config.SegmentConfig;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.utils.HtmlElement;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PageSPI : TODO: yuuji
 * yuuji 5:14 PM 11/20/13
 */
public class PageSPI {

    private static VelocityEngine engine;

    static {
        engine = new VelocityEngine();
        engine.init();
    }

    private PageConfig config(String pageName) throws IOException {
        InputStream inputStream = this.getClass().getResource("/config/page-" + pageName + ".json").openStream();
        JSONReader reader = new JSONReader(new InputStreamReader(inputStream));
        return reader.readObject(PageConfig.class);
    }

    private HtmlElement mergeLayout(Context context, String name) {
        return new HtmlElement(merge(context, "src/main/resources/layout/" + name + ".vm"));
    }

    private HtmlElement mergeApp(Context context, String name) {
        return new HtmlElement(merge(context, "src/main/resources/apps/" + name + "/view.vm"));
    }

    private String merge(Context context, String name) {
        StringWriter writer = new StringWriter();
        Template template = engine.getTemplate(name);
        template.merge(context, writer);
        return writer.toString();
    }

    public HttpResponse invoke(HttpRequest req) {
        String hosts = req.getHost();
        String pageName = req.getUriBefore(1);
        HttpResponse res = new HttpResponse();
        try {
            PageConfig pageConfig = config(pageName);
            Context context = new VelocityContext();
            context.put("pageName", pageName);
            context.put("title", pageConfig.getTitle());
            context.put("header", mergeLayout(new VelocityContext(), pageConfig.getHeader()));
            context.put("footer", mergeLayout(new VelocityContext(), pageConfig.getFooter()));
            List<SegmentConfig> segmentsConfig = pageConfig.getSegments();
            List<HtmlElement> segments = new ArrayList<HtmlElement>();
            for (SegmentConfig segmentConfig : segmentsConfig) {
                List<HtmlElement> apps = new ArrayList<HtmlElement>();
                for (String appName : segmentConfig.getApps()) {
                    apps.add(mergeApp(new VelocityContext(), appName));
                }
                VelocityContext layoutContext = new VelocityContext();
                layoutContext.put("apps", apps);
                HtmlElement layout = mergeLayout(layoutContext, segmentConfig.getLayout());
                segments.add(layout);
            }
            context.put("segments", segments);
            List<String> js = new ArrayList<String>();
            js.add("http://" + hosts + "/static/" + pageName + ".js?v=" + new Date().getTime());
            List<String> css = new ArrayList<String>();
            css.add("http://" + hosts + "/static/" + pageName + ".css?v=" + new Date().getTime());
            context.put("js", js);
            context.put("css", css);
            res.content().writeBytes(merge(context, "src/main/resources/config/" + pageConfig.getView() + ".vm").getBytes());
        } catch (Exception ex) {
            return new HttpResponse(HttpResponseStatus.NOT_FOUND);
        }
        return res;
    }
}
