package com.alibaba.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * I18nUtils : TODO: yuuji
 * yuuji 4:20 PM 12/5/13
 */
public class I18nUtils {
    public static Properties bundle(File file, Locale locale) {
        Properties bundle = new Properties();
        try {
            File bundleFile = new File(file.getPath() + "/bundle_" + locale.getLanguage() + ".properties");
            if (!bundleFile.exists()) {
                bundleFile = new File(file.getPath() + "/bundle.properties");
            }
            bundle.load(new FileInputStream(bundleFile));
        } catch (IOException e) {

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
