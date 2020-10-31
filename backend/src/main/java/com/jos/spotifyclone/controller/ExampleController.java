package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("api/example")
@RestController
public class ExampleController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping
    void handleGet() throws ParseException, SpotifyWebApiException, IOException {
        System.out.println("This is you!");
        System.out.println(spotifyConnect.getSpotifyApi().getCurrentUsersProfile().build().execute().toString());
    }

}
