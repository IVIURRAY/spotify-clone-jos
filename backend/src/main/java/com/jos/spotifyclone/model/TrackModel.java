package com.jos.spotifyclone.model;

import com.wrapper.spotify.model_objects.specification.ExternalUrl;

import java.util.List;

public class TrackModel {
    String name;
    ExternalUrl externalUrls;

    List<Object> artists;
    List<AlbumModel> albums;

    public TrackModel(String name, ExternalUrl externalUrls, List<Object> artists, List<AlbumModel> albums) {
        this.name = name;
        this.externalUrls = externalUrls;
        this.artists = artists;
        this.albums = albums;
    }

    public TrackModel(String name, ExternalUrl externalUrls, List<Object> artists) {
        this.name = name;
        this.externalUrls = externalUrls;
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public ExternalUrl getExternalUrls() {
        return externalUrls;
    }

    public List<Object> getArtists() {
        return artists;
    }

    public List<AlbumModel> getAlbums() {
        return albums;
    }
}
