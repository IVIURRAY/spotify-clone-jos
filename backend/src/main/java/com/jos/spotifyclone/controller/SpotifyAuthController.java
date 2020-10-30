package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("api/spotify-auth")
@RestController
public class SpotifyAuthController  {

    private final SpotifyConnect spotifyConnect;

    @Autowired
    public SpotifyAuthController(SpotifyConnect spotifyConnect) {
        this.spotifyConnect = spotifyConnect;
    }

    @GetMapping
    public void handleAuthCode(@RequestParam String code) throws ParseException, SpotifyWebApiException, IOException {
        System.out.println("This is the auth code:" + code);
        SpotifyConnect.addAuthCode(code);
    }

}
