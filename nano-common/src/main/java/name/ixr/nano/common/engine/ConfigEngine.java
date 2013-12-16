package name.ixr.nano.common.engine;

import name.ixr.nano.common.config.AppConfig;
import name.ixr.nano.common.config.BaseConfig;
import name.ixr.nano.common.config.LayoutConfig;
import name.ixr.nano.common.config.PageConfig;
import com.alibaba.fastjson.JSONReader;
import name.ixr.nano.common.utils.ResourceUtils;
import name.ixr.nano.common.utils.ResourceUtils.Resource;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

/**
 * ConfigEngine : TODO: yuuji
 * yuuji 5:42 PM 11/29/13
 */
public class ConfigEngine {

    private static Map<String, AppConfig> appConfigMap = new HashMap<String, AppConfig>();
    private static Map<String, PageConfig> pageConfigMap = new HashMap<String, PageConfig>();
    private static Map<String, LayoutConfig> layoutConfigMap = new HashMap<String, LayoutConfig>();

    public static LayoutConfig getLayoutConfig(String name) {
        if (layoutConfigMap.containsKey(name)) {
            LayoutConfig config = layoutConfigMap.get(name);
            return config;
        }
        Resource resource = ResourceUtils.getClassPathResource("layout/" + name + ".vm");
        if (!resource.exists()) {
            resource = ResourceUtils.getClassPathResource("layout/" + name + "/");
        }
        loadLayoutConfig(resource);
        return layoutConfigMap.get(name);
    }

    public static PageConfig getPageConfig(String name) {
        if (pageConfigMap.containsKey(name)) {
            PageConfig config = pageConfigMap.get(name);
            return config;
        }
        Resource resource = ResourceUtils.getClassPathResource("config/page-" + name + ".json");
        loadPageConfig(resource);
        return pageConfigMap.get(name);
    }

    public static AppConfig getAppConfig(String name) {
        if (appConfigMap.containsKey(name)) {
            AppConfig config = appConfigMap.get(name);
            return config;
        }
        Resource resource = ResourceUtils.getClassPathResource("apps/" + name + "/");
        loadAppConfig(resource);
        return appConfigMap.get(name);
    }

    private static void loadLayoutConfig(Resource resource) {
        LayoutConfig config = new LayoutConfig();
        if (!resource.isDirectory()) {
            config.setName(resource.getName());
            config.setView(resource);
        } else {
            loadBaseConfig(config, resource);
        }
        layoutConfigMap.put(config.getName(), config);
    }

    private static void loadPageConfig(Resource resource) {
        try {
            String name = StringUtils.substringAfter(resource.getName(), "page-");
            InputStream stream = ResourceUtils.getInputStream(resource);
            JSONReader reader = new JSONReader(new InputStreamReader(stream));
            PageConfig pageConfig = reader.readObject(PageConfig.class);
            pageConfig.setPageName(name);
            pageConfigMap.put(name, pageConfig);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private static void loadAppConfig(Resource resource) {
        if (!resource.isDirectory()) {
            return;
        }
        AppConfig config = new AppConfig();
        loadBaseConfig(config, resource);
        appConfigMap.put(config.getName(), config);
    }

    private static void loadBaseConfig(BaseConfig config, Resource resource) {
        Resource view = ResourceUtils.getClassPathResource(resource.getPath() + "view.vm");
        if (view.exists()) {
            config.setView(view);
        }
        Resource js = ResourceUtils.getClassPathResource(resource.getPath() + "view.js");
        if (view.exists()) {
            config.setJs(js);
        }
        Resource css = ResourceUtils.getClassPathResource(resource.getPath() + "view.css");
        if (view.exists()) {
            config.setCss(css);
        }
        Resource i18n = ResourceUtils.getClassPathResource(resource.getPath() + "i18n/");
        if (view.exists()) {
            config.setI18n(i18n);
        }
        config.setName(resource.getName());
    }
}
