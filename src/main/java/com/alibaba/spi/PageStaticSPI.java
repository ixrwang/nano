package com.alibaba.spi;

import com.alibaba.config.BaseConfig;
import com.alibaba.config.ConfigEngine;
import com.alibaba.config.PageConfig;
import com.alibaba.config.SegmentConfig;
import com.alibaba.utils.ContentType;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import com.googlecode.htmlcompressor.compressor.Compressor;
import com.googlecode.htmlcompressor.compressor.YuiCssCompressor;
import com.googlecode.htmlcompressor.compressor.YuiJavaScriptCompressor;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * PageStaticSPI : TODO: yuuji
 * yuuji 5:39 PM 12/4/13
 */
public class PageStaticSPI {

    public HttpResponse invoke(HttpRequest req) {
        String suffix = req.getUriSuffix(1);
        req.setUri(req.getReferer());
        String pageName = req.getUriBefore(1);
        HttpResponse res = new HttpResponse();
        res.setContentType(ContentType.findContentType(suffix));
        try {
            PageConfig pageConfig = ConfigEngine.getPageConfig(pageName);
            StringWriter writer = new StringWriter();
            write(ConfigEngine.getLayoutConfig(pageConfig.getView()), writer, suffix);
            HashSet<String> apps = new HashSet<String>();
            for (SegmentConfig segmentConfig : pageConfig.getSegments()) {
                write(ConfigEngine.getLayoutConfig(segmentConfig.getLayout()), writer, suffix);
                for (String appName : segmentConfig.getApps()) {
                    if(apps.contains(appName)) {
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
        } catch (Exception ex) {
            HttpResponse response = new HttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.content().writeBytes(ex.toString().getBytes());
            return response;
        }
        return res;
    }

    public void write(BaseConfig layoutConfig, StringWriter writer, String suffix) {
        try {
            if ("js".equals(suffix) && layoutConfig.getJs() != null) {
                writer.write(IOUtils.toString(new FileInputStream(layoutConfig.getJs())));
            } else if ("css".equals(suffix) && layoutConfig.getCss() != null) {
                writer.write(IOUtils.toString(new FileInputStream(layoutConfig.getCss())));
            }
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
}
