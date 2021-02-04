package com.solantec.contro.config;

import com.solantec.contro.key.StringKey;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class WebResourceDefindation extends ResourceDefindation {

    @NotEmpty
    private String path;
    private String fallbackUri;

    public String getFallbackUri() {
        return fallbackUri;
    }

    public void setFallbackUri(String fallbackUri) {
        this.fallbackUri = fallbackUri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        if (key == null){
            this.key = new StringKey(path);
        }
        this.path = path;
    }
}
