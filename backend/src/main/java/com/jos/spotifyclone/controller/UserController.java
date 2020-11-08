package com.jos.spotifyclone.controller;


import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
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

    @GetMapping("/profile/")
    public @ResponseBody
    User currentUserProfile() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersProfile().build().execute();
    }

    //https://developer.spotify.com/console/get-available-genre-seeds/
    //http://localhost:8080/api/user/recommended/?seed=emo
    @GetMapping("/recommended/")
    public @ResponseBody
    Recommendations getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var availableGenreSeeds = spotifyConnect.getSpotifyApi().getAvailableGenreSeeds();
        return spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();
    }

    @GetMapping("/recentTracks/")
    public @ResponseBody
    PagingCursorbased<PlayHistory> getRecentTracks() throws ParseException, SpotifyWebApiException, IOException {
        //System.out.println((spotifyConnect.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().build().execute()));
        return spotifyConnect.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().build().execute();
    }

    @GetMapping("/playlist/")
    public @ResponseBody Paging<PlaylistSimplified> playlistsOfCurrentUser() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute();
    }
}


