package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.IModelObject;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RequestMapping("api/search")
@RestController
public class SearchController {
    @Autowired
    SpotifyConnect spotifyConnect;

    //TODO ${ARTIST_NAME_HERE} needs value storing elsewhere where this controller can access the search term to return searched for artist data
    //http://localhost:8080/api/search/artist?id=drake
    @GetMapping("/artist")
    public @ResponseBody
    IModelObject searchArtistController(@RequestParam String id) throws ParseException, IOException, SpotifyWebApiException {
        return spotifyConnect.getSpotifyApi().searchArtists(id).build().execute();
    }

    //http://localhost:8080/api/search/album?id=arianagrande
    @GetMapping("/album")
    public @ResponseBody
    Paging<AlbumSimplified> searchAlbumController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().searchAlbums(id).build().execute();
    }

    //http://localhost:8080/api/search/episode?id=lauv
    @GetMapping("/episode")
    public @ResponseBody
    Paging<EpisodeSimplified> searchEpisodeController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().searchEpisodes(id).build().execute();
    }

    //http://localhost:8080/api/search/show?id=bieber
    @GetMapping("/show")
    public @ResponseBody
    Paging<ShowSimplified> searchShowController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().searchShows(id).build().execute();
    }

    //http://localhost:8080/api/search/playlist?id=bieber
    @GetMapping("/playlist")
    public @ResponseBody
    Paging<PlaylistSimplified> searchPlaylistController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().searchPlaylists(id).build().execute();
    }

    //http://localhost:8080/api/search/track?id=positions
    @GetMapping("/track")
    public @ResponseBody
    Paging<Track> searchTrackController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().searchTracks(id).build().execute();
    }
}
