package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("api/player")
@RestController
public class PlayerController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/currentlyPlaying")
    public @ResponseBody
    CurrentlyPlaying currentlyPlayings() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersCurrentlyPlayingTrack().build().execute();
    }

    @GetMapping("/recentTracks")
    public @ResponseBody
    PagingCursorbased<PlayHistory> getRecentTracks() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().build().execute();
    }

    @GetMapping("/availableDevices")
    public @ResponseBody
    Device[] availableDevices() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersAvailableDevices().build().execute();
    }

    @GetMapping("/currentPlayback")
    public @ResponseBody
    CurrentlyPlayingContext currentPlayback() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getInformationAboutUsersCurrentPlayback().build().execute();
    }
}
