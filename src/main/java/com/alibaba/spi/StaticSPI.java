package com.alibaba.spi;

import com.alibaba.config.PageConfig;
import com.alibaba.config.SegmentConfig;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.utils.ContentType;
import com.alibaba.utils.HttpRequest;
import com.alibaba.utils.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * StaticSPI : TODO: yuuji
 * yuuji 9:41 AM 11/26/13
 */
public class StaticSPI {
    private PageConfig config(String pageName) throws IOException {
        InputStream inputStream = this.getClass().getResource("/config/page-" + pageName + ".json").openStream();
        JSONReader reader = new JSONReader(new InputStreamReader(inputStream));
        return reader.readObject(PageConfig.class);
    }

    public HttpResponse invoke(HttpRequest req) {
        String pageName = req.getUriBefore(1);
        String suffix = req.getUriSuffix(1);
        HttpResponse res = new HttpResponse();
        res.setContentType(ContentType.findContentType(suffix));
        try {
            PageConfig pageConfig = config(pageName);
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
