package com.alibaba.config;

import java.io.File;
import java.util.List;

/**
 * PageConfig : TODO: yuuji
 * yuuji 12:05 PM 11/25/13
 */
public class PageConfig {
    private String pageName;
    private String view;
    private String header;
    private String footer;
    private String title;
    private List<SegmentConfig> segments;
    private long lastModified;
    private File file;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

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

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SegmentConfig> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentConfig> segments) {
        this.segments = segments;
    }
}
