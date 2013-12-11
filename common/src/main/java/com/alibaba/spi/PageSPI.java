package com.alibaba.spi;

import com.alibaba.config.*;
import com.alibaba.engine.ConfigEngine;
import com.alibaba.base.RequestContext;
import com.alibaba.utils.*;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.*;
import java.util.*;

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

    private Context initContext(PageConfig pageConfig) {
        Context context = new VelocityContext();
        context.put("title", pageConfig.getTitle());
        context.put("pageName", pageConfig.getPageName());
        context.put("pageConfig", pageConfig);
        File headerFile = ConfigEngine.getLayoutConfig(pageConfig.getHeader()).getFile();
        String headerHtml = merge(new VelocityContext(), headerFile);
        context.put("header", new HtmlElement(headerHtml));
        File footerFile = ConfigEngine.getLayoutConfig(pageConfig.getFooter()).getFile();
        String footerHtml = merge(new VelocityContext(), footerFile);
        context.put("footer", new HtmlElement(footerHtml));
        RequestContext.setContext(context);
        return context;
    }

    public HttpResponse invoke() {
        HttpRequest req = RequestContext.request();
        String hosts = req.getHost();
        String pageName = req.getUriBefore(1);
        HttpResponse res = new HttpResponse();
        boolean isPageJs = false, isPageCss = false;
        try {
            PageConfig pageConfig = ConfigEngine.getPageConfig(pageName);
            Context context = initContext(pageConfig);
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
                    Context appContext = new VelocityContext(context);
                    if (appConfig.getI18n() != null) {
                        appContext.put("i18n", I18nUtils.bundle(appConfig.getI18n(), req.getLocale()));
                    }
                    apps.add(new HtmlElement(merge(appContext, appConfig.getView())));
                }
                LayoutConfig layoutConfig = ConfigEngine.getLayoutConfig(segmentConfig.getLayout());
                Context layoutContext = new VelocityContext(context);
                layoutContext.put("apps", apps);
                if (layoutConfig.getI18n() != null) {
                    layoutContext.put("i18n", I18nUtils.bundle(layoutConfig.getI18n(), req.getLocale()));
                }
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
            if (layoutConfig.getI18n() != null) {
                context.put("i18n", I18nUtils.bundle(layoutConfig.getI18n(), req.getLocale()));
            }
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
