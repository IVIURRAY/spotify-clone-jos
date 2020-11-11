package com.jos.spotifyclone.controller;


import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("api/user")
@RestController
public class UserController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/profile")
    public
    User currentUserProfile() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersProfile().build().execute();
    }

    @GetMapping("/playlist")
    public Paging<PlaylistSimplified> playlistsOfCurrentUser() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute();
    }

    @GetMapping("/followedArtists")
    public PagingCursorbased<Artist> followedArtists() throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().getUsersFollowedArtists(type).build().execute();
    }

    @GetMapping("/savedAlbums")
    public Paging<SavedAlbum> savedAlbums() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersSavedAlbums().build().execute();
    }

    @GetMapping("/savedShows")
    public Paging<SavedShow> savedShows() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersSavedShows().build().execute();
    }

    @GetMapping("/savedTracks")
    public Paging<SavedTrack> savedTracks() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersSavedTracks().build().execute();
    }

    //run http://localhost:8080/api/user/savedAlbums/ first to find id's
    //http://localhost:8080/api/user/removeAlbums?ids=<replace with albums id>
    @GetMapping("/removeAlbums")
    public String removeAlbums(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeAlbumsForCurrentUser(ids).build().execute();
    }

    //run http://localhost:8080/api/user/savedShows/ first to find id's
    //http://localhost:8080/api/user/removeShows?ids=<replace with shows id>
    @GetMapping("/removeShows")
    public String removeShows(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeUsersSavedShows(ids).build().execute();
    }

    //run http://localhost:8080/api/user/savedTracks/ first to find id's
    //http://localhost:8080/api/user/removeTracks?ids=<replace with tracks id>
    @GetMapping("/removeTracks")
    public String removeTracks(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeUsersSavedTracks(ids).build().execute();
    }
}


