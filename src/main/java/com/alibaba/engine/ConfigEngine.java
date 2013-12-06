package com.alibaba.engine;

import com.alibaba.config.AppConfig;
import com.alibaba.config.BaseConfig;
import com.alibaba.config.LayoutConfig;
import com.alibaba.config.PageConfig;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.utils.ResourceUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * ConfigEngine : TODO: yuuji
 * yuuji 5:42 PM 11/29/13
 */
public class ConfigEngine {

    private static Map<String, AppConfig> appConfigMap = new HashMap<String, AppConfig>();
    private static Map<String, PageConfig> pageConfigMap = new HashMap<String, PageConfig>();
    private static Map<String, LayoutConfig> layoutConfigMap = new HashMap<String, LayoutConfig>();

    static {
        loadPageConfig();
        loadAppConfig();
        loadLayoutConfig();
    }

    public static LayoutConfig getLayoutConfig(String name) {
        if (layoutConfigMap.containsKey(name)) {
            LayoutConfig config = layoutConfigMap.get(name);
            if (lastModified(config)) {
                return config;
            }
        }
        File file = ResourceUtils.getFile("/layout/" + name + ".vm");
        if (!file.exists()) {
            file = ResourceUtils.getFile("/layout/" + name);
        }
        loadLayoutConfig(file);
        return layoutConfigMap.get(name);
    }

    public static PageConfig getPageConfig(String name) {
        if (pageConfigMap.containsKey(name)) {
            PageConfig config = pageConfigMap.get(name);
            if (config.getLastModified() > config.getFile().lastModified()) {
                return config;
            }
        }
        File file = ResourceUtils.getFile("/config/page-" + name + ".json");
        loadPageConfig(file);
        return pageConfigMap.get(name);
    }

    public static AppConfig getAppConfig(String name) {
        if (appConfigMap.containsKey(name)) {
            AppConfig config = appConfigMap.get(name);
            if (lastModified(config)) {
                return config;
            }
        }
        File file = ResourceUtils.getFile("/apps/" + name);
        loadAppConfig(file);
        return appConfigMap.get(name);
    }


    private static void loadLayoutConfig() {
        File base = ResourceUtils.getFile("/layout/");
        if (!base.exists() || !base.isDirectory()) {
            return;
        }
        File[] files = base.listFiles();
        for (File file : files) {
            loadLayoutConfig(file);
        }
    }

    private static void loadLayoutConfig(File file) {
        LayoutConfig config = new LayoutConfig();
        if (file.isFile()) {
            config.setName(StringUtils.substringBefore(file.getName(), "."));
            config.setView(file);
            config.setFile(file);
            config.setLastModified(new Date().getTime());
        } else {
            loadBaseConfig(config, file);
        }
        layoutConfigMap.put(config.getName(), config);
    }

    private static void loadPageConfig() {
        File base = ResourceUtils.getFile("/config/");
        if (!base.exists() || !base.isDirectory()) {
            return;
        }
        File[] files = base.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().indexOf("page-") == 0
                    && StringUtils.substringBefore(file.getName(), ".").equals("json")) {
                loadPageConfig(file);
            }
        }
    }

    private static void loadPageConfig(File file) {
        try {
            String name = StringUtils.substringBetween(file.getName(), "page-", ".");
            InputStream stream = new FileInputStream(file);
            JSONReader reader = new JSONReader(new InputStreamReader(stream));
            PageConfig pageConfig = reader.readObject(PageConfig.class);
            pageConfig.setFile(file);
            pageConfig.setPageName(name);
            pageConfig.setLastModified(new Date().getTime());
            pageConfigMap.put(name, pageConfig);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private static void loadAppConfig() {
        File base = ResourceUtils.getFile("/apps/");
        if (!base.exists() || !base.isDirectory()) {
            return;
        }
        File[] files = base.listFiles();
        for (File file : files) {
            loadAppConfig(file);
        }
    }

    private static void loadAppConfig(File file) {
        if (!file.isDirectory()) {
            return;
        }
        AppConfig config = new AppConfig();
        loadBaseConfig(config, file);
        appConfigMap.put(config.getName(), config);
    }

    private static void loadBaseConfig(BaseConfig config, File base) {
        File[] files = base.listFiles();
        for (File file : files) {
            if ("view.vm".equals(file.getName())) {
                config.setView(file);
            } else if ("view.js".equals(file.getName())) {
                config.setJs(file);
            } else if ("view.css".equals(file.getName())) {
                config.setCss(file);
            } else if("i18n".equals(file.getName())) {
                config.setI18n(file);
            }
        }
        config.setFile(base);
        config.setName(base.getName());
        config.setLastModified(new Date().getTime());
    }

    private static boolean lastModified(BaseConfig config) {
        if (config.getView() != null
                && config.getView().lastModified() > config.getLastModified()) {
            return false;
        } else if (config.getJs() != null
                && config.getJs().lastModified() > config.getLastModified()) {
            return false;
        } else if (config.getCss() != null
                && config.getCss().lastModified() > config.getLastModified()) {
            return false;
        }
        return true;
    }
}
