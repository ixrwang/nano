package com.alibaba.utils;

import org.apache.velocity.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ResourceUtils : TODO: yuuji
 * yuuji 9:41 AM 12/5/13
 */
public class ResourceUtils {

    private static String basePath = ResourceUtils.class.getResource("/").getFile();


    public static File getFile(String path) {
        return new File(basePath + StringUtils.normalizePath(path));
    }

    public static String getClassPath(File file) {
        return org.apache.commons.lang3.StringUtils.substringAfter(file.getPath(), basePath);
    }

    public static InputStream getInputStream(String path) {
        try {
            File file = getFile(path);
            if (!file.exists()) {
                return null;
            }
            return new FileInputStream(file);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

    public static long getLastModified(String path) {
        File file = getFile(path);
        return file.lastModified();
    }
}
