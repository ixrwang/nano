package com.alibaba.config;

import java.io.File;

/**
 * BaseConfig : TODO: yuuji
 * yuuji 2:09 PM 12/5/13
 */
public class BaseConfig {

    private String name;
    private File js;
    private File css;
    private File view;
    private long lastModified;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getJs() {
        return js;
    }

    public void setJs(File js) {
        this.js = js;
    }

    public File getCss() {
        return css;
    }

    public void setCss(File css) {
        this.css = css;
    }

    public File getView() {
        return view;
    }

    public void setView(File view) {
        this.view = view;
    }
}
