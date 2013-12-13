package com.alibaba.utils;

import com.alibaba.utils.model.Resource;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * I18nUtils : TODO: yuuji
 * yuuji 4:20 PM 12/5/13
 */
public class I18nUtils {

    public static Properties bundle(Resource resource, Locale locale) {
        Properties bundle = new Properties();
        try {
            Resource bundleResource = ResourceUtils.getClassPathResource(resource.getPath() + "bundle_" + locale.getLanguage() + ".properties");
            if (!bundleResource.exists()) {
                bundleResource = ResourceUtils.getClassPathResource(resource.getPath() + "bundle.properties");
            }
            if (bundleResource.exists()) {
                bundle.load(ResourceUtils.getInputStream(bundleResource));
            }
        } catch (IOException e) {
            throw new Error(e);
        }
        return bundle;
    }

    public static Locale toLocale(String language) {
        if (StringUtils.isNotBlank(language)) {
            if (language.indexOf("zh") > -1) {
                return new Locale("zh");
            }
        }
        return new Locale("en");
    }
}
