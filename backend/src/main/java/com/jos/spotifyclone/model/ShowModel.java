package com.jos.spotifyclone.model;

import com.wrapper.spotify.model_objects.specification.ExternalUrl;

public class ShowModel {
    String description;
    String name;
    ExternalUrl externalUrls;

    public ShowModel(String description, String name, ExternalUrl externalUrls) {
        this.description = description;
        this.name = name;
        this.externalUrls = externalUrls;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public ExternalUrl getExternalUrls() {
        return externalUrls;
    }
}
