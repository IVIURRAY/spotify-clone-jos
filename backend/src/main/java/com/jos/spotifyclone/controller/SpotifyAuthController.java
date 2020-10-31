package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RequestMapping("api/spotify-auth")
@RestController
public class SpotifyAuthController  {

    @Autowired
    private SpotifyConnect spotifyConnect;

    @GetMapping
    public RedirectView handleAuthCode(@RequestParam String code) throws ParseException, SpotifyWebApiException, IOException {
        spotifyConnect.addAuthCode(code);

        return new RedirectView("example");
    }

}
