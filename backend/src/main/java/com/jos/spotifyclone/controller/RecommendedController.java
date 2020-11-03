package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/user")
public class RecommendedController {
    @Autowired
    SpotifyConnect spotifyConnect;

    //https://developer.spotify.com/console/get-available-genre-seeds/
    //http://localhost:8080/api/user/recommended/?seed=emo
    @GetMapping("/recommended/")
    public @ResponseBody
    Recommendations getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var availableGenreSeeds = spotifyConnect.getSpotifyApi().getAvailableGenreSeeds();
        return spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();
    }

}
