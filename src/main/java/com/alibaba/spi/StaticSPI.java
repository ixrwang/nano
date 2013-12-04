package com.alibaba.spi;

import com.alibaba.config.ConfigEngine;
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

    public HttpResponse invoke(HttpRequest req) {
        if ("apps".equals(req.getUriBefore(1))) {
            return new AppStaticSPI().invoke(req);
        }
        return new HttpResponse(HttpResponseStatus.NOT_FOUND, null);
    }
}
