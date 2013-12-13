package com.alibaba.config;

import com.alibaba.utils.model.Resource;

/**
 * BaseConfig : TODO: yuuji
 * yuuji 2:09 PM 12/5/13
 */
public class BaseConfig {

    private String name;
    private Resource view;
    private Resource js;
    private Resource css;
    private Resource i18n;

    public Resource getI18n() {
        return i18n;
    }

    public void setI18n(Resource i18n) {
        this.i18n = i18n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resource getJs() {
        return js;
    }

    public void setJs(Resource js) {
        this.js = js;
    }

    public Resource getCss() {
        return css;
    }

    public void setCss(Resource css) {
        this.css = css;
    }

    public Resource getView() {
        return view;
    }

    public void setView(Resource view) {
        this.view = view;
    }
}
