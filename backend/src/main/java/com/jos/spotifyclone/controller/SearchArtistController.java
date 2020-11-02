package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.IModelObject;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequestMapping("api/search/artist")
@RestController
public class SearchArtistController<ArtistSearchRequest> {

    @Autowired
    SpotifyConnect spotifyConnect;

    //TODO ${ARTIST_NAME_HERE} needs value storing elsewhere where this controller can access the search term to return searched for artist data
    @GetMapping
    public @ResponseBody
    IModelObject searchArtistController() throws ParseException, IOException, SpotifyWebApiException {
        return spotifyConnect.getSpotifyApi().searchArtists("${ARTIST_NAME_HERE}").build().execute();
    }
}
