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
    public @ResponseBody
    User currentUserProfile() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersProfile().build().execute();
    }

    //https://developer.spotify.com/console/get-available-genre-seeds/
    //http://localhost:8080/api/user/recommended?seed=emo
    @GetMapping("/recommended")
    public @ResponseBody
    Recommendations getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var availableGenreSeeds = spotifyConnect.getSpotifyApi().getAvailableGenreSeeds();
        return spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();
    }

    @GetMapping("/playlist")
    public @ResponseBody Paging<PlaylistSimplified> playlistsOfCurrentUser() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute();
    }

    @GetMapping("/followedArtists")
    public @ResponseBody PagingCursorbased<Artist> followedArtists() throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().getUsersFollowedArtists(type).build().execute();
    }

    @GetMapping("/savedAlbums")
    public @ResponseBody Paging<SavedAlbum> savedAlbums() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersSavedAlbums().build().execute();
    }

    @GetMapping("/savedShows")
    public @ResponseBody Paging<SavedShow> savedShows() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersSavedShows().build().execute();
    }

    @GetMapping("/savedTracks")
    public @ResponseBody Paging<SavedTrack> savedTracks() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersSavedTracks().build().execute();
    }

    //run http://localhost:8080/api/user/savedAlbums/ first to find id's
    //http://localhost:8080/api/user/removeAlbums?ids=<replace with albums id>
    @GetMapping("/removeAlbums")
    public @ResponseBody String removeAlbums(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeAlbumsForCurrentUser(ids).build().execute();
    }

    //run http://localhost:8080/api/user/savedShows/ first to find id's
    //http://localhost:8080/api/user/removeShows?ids=<replace with shows id>
    @GetMapping("/removeShows")
    public @ResponseBody
    String removeShows(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeUsersSavedShows(ids).build().execute();
    }

    //run http://localhost:8080/api/user/savedTracks/ first to find id's
    //http://localhost:8080/api/user/removeTracks?ids=<replace with tracks id>
    @GetMapping("/removeTracks")
    public @ResponseBody
    String removeTracks(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeUsersSavedTracks(ids).build().execute();
    }

    @GetMapping("/newReleases")
    public @ResponseBody Paging<AlbumSimplified> newReleases() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfNewReleases().build().execute();
    }
}


