package com.jos.spotifyclone.model;

import com.wrapper.spotify.model_objects.specification.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.Image;

import java.util.List;

public class AlbumModel {
    String name;
    List<String> artist;
    Image[] image;
    ExternalUrl externalUrl;

    public AlbumModel(String name, List<String> artist, Image[] image, ExternalUrl externalUrl) {
        this.name = name;
        this.artist = artist;
        this.image = image;
        this.externalUrl = externalUrl;
    }

    public AlbumModel(String name, List<String> artist, Image[] image, ExternalUrl externalUrl, List<Object> tracks) {
        this.name = name;
        this.artist = artist;
        this.image = image;
        this.externalUrl = externalUrl;
    }

    public ExternalUrl getExternalUrl() {
        return externalUrl;
    }

    public Image[] getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public List<String> getArtist() {
        return artist;
    }
}
