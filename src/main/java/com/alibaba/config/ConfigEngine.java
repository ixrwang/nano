package com.alibaba.config;

import com.alibaba.fastjson.JSONReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * ConfigEngine : TODO: yuuji
 * yuuji 5:42 PM 11/29/13
 */
public class ConfigEngine {

    private static Map<String, PageConfig> pageConfigMap = new HashMap<String, PageConfig>();

    public static PageConfig getPageConfig(String pageName) throws IOException {
        if (pageConfigMap.containsKey(pageName)) {
            PageConfig pageConfig = pageConfigMap.get(pageName);
            long lastModified = configLastModified(pageName);
            if(lastModified == pageConfig.getLastModified()) {
                return pageConfig;
            } else {
                pageConfigMap.remove(pageName);
            }
        }
        PageConfig pageConfig = config(pageName);
        pageConfigMap.put(pageName, pageConfig);
        return pageConfig;
    }

    private static long configLastModified(String pageName) {
        try {
            File pageConfigFile = new File(configURL(pageName).toURI());
            return pageConfigFile.lastModified();
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }


    private static PageConfig config(String pageName) throws IOException {
        InputStream inputStream = configURL(pageName).openStream();
        JSONReader reader = new JSONReader(new InputStreamReader(inputStream));
        PageConfig pageConfig = reader.readObject(PageConfig.class);
        pageConfig.setLastModified(configLastModified(pageName));
        return pageConfig;
    }

    private static URL configURL(String pageName) {
        URL url = ConfigEngine.class.getResource("/config/page-" + pageName + ".json");
        return url;
    }

}
