package com.alibaba.spi;

import com.alibaba.config.ConfigEngine;
import com.alibaba.config.PageConfig;
import com.alibaba.config.SegmentConfig;
import com.alibaba.utils.ContentType;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.InputStream;
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
            for (SegmentConfig segmentConfig : segmentsConfig) {
                for (String appName : segmentConfig.getApps()) {
                    if (apps.contains(appName)) {
                        continue;
                    }
                    try {
                        InputStream inputStream = this.getClass().getResource("/apps/" + appName + "/view." + suffix).openStream();
                        res.content().writeBytes(inputStream, inputStream.available());
                    } catch (Exception ex) {
                    }
                    apps.add(appName);
                }
            }
        } catch (Exception ex) {
            return new HttpResponse(HttpResponseStatus.NOT_FOUND);
        }
        return res;
    }
}
