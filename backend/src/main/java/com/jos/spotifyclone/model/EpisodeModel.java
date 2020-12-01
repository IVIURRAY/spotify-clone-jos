package com.jos.spotifyclone.model;

import com.wrapper.spotify.model_objects.specification.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.Image;

public class EpisodeModel {
    String name;
    String[] language;
    Image[] images;
    ExternalUrl externalUrls;
    String description;

    public EpisodeModel(String name, String[] language, Image[] images, ExternalUrl externalUrls, String description) {
        this.name = name;
        this.language = language;
        this.images = images;
        this.externalUrls = externalUrls;
        this.description = description;
    }

    public String getName() {
        return name;
    }


    public String[] getLanguage() {
        return language;
    }

    public Image[] getImages() {
        return images;
    }

    public ExternalUrl getExternalUrls() {
        return externalUrls;
    }

    public String getDescription() {
        return description;
    }
}
