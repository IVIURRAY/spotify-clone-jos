package com.jos.spotifyclone.model;

import com.wrapper.spotify.model_objects.specification.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.Followers;
import com.wrapper.spotify.model_objects.specification.Image;

public class ArtistModel {
    ExternalUrl externalUrl;
    Followers followers;
    String[] genres;
    Image[] images;
    String artistName;

    public ArtistModel(ExternalUrl externalUrl, Followers followers, String[] genres, Image[] images, String artistName) {
        this.externalUrl = externalUrl;
        this.followers = followers;
        this.genres = genres;
        this.images = images;
        this.artistName = artistName;
    }

    public ExternalUrl getExternalUrl() {
        return externalUrl;
    }

    public Followers getFollowers() {
        return followers;
    }

    public String[] getGenres() {
        return genres;
    }

    public Image[] getImages() {
        return images;
    }

    public String getArtistName() {
        return artistName;
    }
}
