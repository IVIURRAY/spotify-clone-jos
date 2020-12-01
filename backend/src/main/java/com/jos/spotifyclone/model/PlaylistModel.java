package com.jos.spotifyclone.model;

import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.Image;

public class PlaylistModel {
    String href;
    ExternalUrl externalUrls;
    String name;
    PlaylistTracksInformation tracks;
    Image[] playlistCover;

    public PlaylistModel(String href, ExternalUrl externalUrls, String name, PlaylistTracksInformation tracks, Image[] playlistCover) {
        this.href = href;
        this.externalUrls = externalUrls;
        this.name = name;
        this.tracks = tracks;
        this.playlistCover = playlistCover;
    }

    public PlaylistModel(String href, ExternalUrl externalUrls, String name, Image[] playlistCover) {
        this.href = href;
        this.externalUrls = externalUrls;
        this.name = name;
        this.playlistCover = playlistCover;
    }

    public Image[] getPlaylistCover() {
        return playlistCover;
    }

    public String getHref() {
        return href;
    }

    public ExternalUrl getExternalUrls() {
        return externalUrls;
    }

    public String getName() {
        return name;
    }

    public PlaylistTracksInformation getTracks() {
        return tracks;
    }
}
