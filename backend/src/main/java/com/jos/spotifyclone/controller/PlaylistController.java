package com.jos.spotifyclone.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("api/user")
@RestController
public class PlaylistController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/playlist/")
    public @ResponseBody Paging<PlaylistSimplified> playlistsOfCurrentUser() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute();
    }
}
