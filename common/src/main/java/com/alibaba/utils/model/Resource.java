package com.alibaba.utils.model;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yuuji on 12/13/13.
 */
public class Resource {
    private String path;
    private boolean isDirectory;
    private boolean exists;

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean exists() {
        return exists;
    }

    public String getName() {
        if(isDirectory()) {
            return FilenameUtils.getName(path.substring(0,path.length() - 1));
        }
        return StringUtils.substringBefore(FilenameUtils.getName(path), ".");
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}