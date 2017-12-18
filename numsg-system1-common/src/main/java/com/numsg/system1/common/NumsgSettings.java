package com.numsg.system1.common;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gaoqiang on 2017/12/18.
 */
@ConfigurationProperties(prefix = "numsg")
public class NumsgSettings {
    private String url1;
    private String url2;

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }
}
