package com.alibaba.spi;

import com.alibaba.config.*;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.utils.HtmlElement;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import com.alibaba.utils.ResourceUtils;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * PageSPI : TODO: yuuji
 * yuuji 5:14 PM 11/20/13
 */
public class PageSPI {

    private static VelocityEngine engine;

    static {
        try {
            Properties properties = new Properties();
            properties.load(ResourceUtils.getInputStream("/velocity.properties"));
            engine = new VelocityEngine();
            engine.init(properties);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private String merge(Context context, File file) {
        String name = ResourceUtils.getClassPath(file);
        StringWriter writer = new StringWriter();
        Template template = engine.getTemplate(name);
        template.merge(context, writer);
        return writer.toString();
    }

    private Context context(PageConfig pageConfig) {
        Context context = new VelocityContext();
        context.put("title", pageConfig.getTitle());
        context.put("pageName", pageConfig.getPageName());
        File headerFile = ConfigEngine.getLayoutConfig(pageConfig.getHeader()).getFile();
        String headerHtml = merge(new VelocityContext(), headerFile);
        context.put("header", new HtmlElement(headerHtml));
        File footerFile = ConfigEngine.getLayoutConfig(pageConfig.getFooter()).getFile();
        String footerHtml = merge(new VelocityContext(), footerFile);
        context.put("footer", new HtmlElement(footerHtml));
        return context;
    }

    public HttpResponse invoke(HttpRequest req) {
        String hosts = req.getHost();
        String pageName = req.getUriBefore(1);
        HttpResponse res = new HttpResponse();
        boolean isPageJs = false, isPageCss = false;
        try {
            PageConfig pageConfig = ConfigEngine.getPageConfig(pageName);
            Context context = context(pageConfig);
            List<SegmentConfig> segmentsConfig = pageConfig.getSegments();
            List<HtmlElement> segments = new ArrayList<HtmlElement>();
            for (SegmentConfig segmentConfig : segmentsConfig) {
                List<HtmlElement> apps = new ArrayList<HtmlElement>();
                for (String appName : segmentConfig.getApps()) {
                    AppConfig appConfig = ConfigEngine.getAppConfig(appName);
                    if (appConfig.getJs() != null) {
                        isPageJs = true;
                    }
                    if (appConfig.getCss() != null) {
                        isPageCss = true;
                    }
                    apps.add(new HtmlElement(merge(context, appConfig.getView())));
                }
                Context layoutContext = new VelocityContext();
                layoutContext.put("apps", apps);
                LayoutConfig layoutConfig = ConfigEngine.getLayoutConfig(segmentConfig.getLayout());
                if (layoutConfig.getJs() != null) {
                    isPageJs = true;
                }
                if (layoutConfig.getCss() != null) {
                    isPageCss = true;
                }
                String layoutHtml = merge(layoutContext, layoutConfig.getView());
                segments.add(new HtmlElement(layoutHtml));
            }
            context.put("segments", segments);
            LayoutConfig layoutConfig = ConfigEngine.getLayoutConfig(pageConfig.getView());
            if (isPageJs || layoutConfig.getJs() != null) {
                String pageJs = "http://" + hosts + "/static/page.js?v=" + pageConfig.getLastModified();
                context.put("js", new String[]{pageJs});
            }
            if (isPageCss || layoutConfig.getCss() != null) {
                String pageCss = "http://" + hosts + "/static/page.css?v=" + pageConfig.getLastModified();
                context.put("css", new String[]{pageCss});
            }
            String html = merge(context, layoutConfig.getView());
            HtmlCompressor htmlCompressor = new HtmlCompressor();
            html = htmlCompressor.compress(html);
            res.content().writeBytes(html.getBytes());
        } catch (Exception ex) {
            HttpResponse response = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.content().writeBytes(ex.toString().getBytes());
            return response;
        }
        return res;
    }
}
