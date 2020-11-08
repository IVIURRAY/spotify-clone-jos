package com.jos.spotifyclone.controller;


import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.User;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/recentTracks/")
    public @ResponseBody
    PagingCursorbased<PlayHistory> getRecentTracks() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().build().execute();
    }
    
    @GetMapping("/recommended/")
    public @ResponseBody
    Recommendations getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var availableGenreSeeds = spotifyConnect.getSpotifyApi().getAvailableGenreSeeds();
        return spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();
    }

}


