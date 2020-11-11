package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("api/browse")
@RestController
public class BrowseController {

    @Autowired
    SpotifyConnect spotifyConnect;

    //https://developer.spotify.com/console/get-available-genre-seeds/
    //http://localhost:8080/api/user/recommended?seed=emo
    @GetMapping("/recommended")
    public @ResponseBody
    Recommendations getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var availableGenreSeeds = spotifyConnect.getSpotifyApi().getAvailableGenreSeeds();
        return spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();
    }

    @GetMapping("/newReleases")
    public @ResponseBody
    Paging<AlbumSimplified> newReleases() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfNewReleases().build().execute();
    }

    //http://localhost:8080/api/browse/getAlbum?id=5zT1JLIj9E57p3e1rFm9Uq
    @GetMapping("/getAlbum")
    public @ResponseBody
    Album getAlbum(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAlbum(id).build().execute();
    }

    //http://localhost:8080/api/browse/getAlbumTrack?id=5zT1JLIj9E57p3e1rFm9Uq
    @GetMapping("/getAlbumTrack")
    public @ResponseBody
    Paging<TrackSimplified> getAlbumTrack(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAlbumsTracks(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralAlbums?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getSeveralAlbums")
    public @ResponseBody
    Album[] getSeveralAlbums(@RequestParam String[] id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralAlbums(id).build().execute();
    }

    //http://localhost:8080/api/browse/getArtist?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtist")
    public @ResponseBody
    Artist getArtist(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getArtist(id).build().execute();
    }

    //http://localhost:8080/api/browse/getArtistsAlbums?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtistsAlbums")
    public @ResponseBody
    Paging<AlbumSimplified> getArtistsAlbums(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getArtistsAlbums(id).build().execute();
    }

    //http://localhost:8080/api/browse/getArtistsTopTracks?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtistsTopTracks")
    public @ResponseBody
    Track[] getArtistsTopTracks(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        CountryCode countryCode = CountryCode.US;
        return spotifyConnect.getSpotifyApi().getArtistsTopTracks(id,countryCode).build().execute();
    }

    //http://localhost:8080/api/browse/getArtistsRelatedArtists?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtistsRelatedArtists")
    public @ResponseBody
    Artist[] getArtistsRelatedArtists(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getArtistsRelatedArtists(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralArtists?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getSeveralArtists")
    public @ResponseBody
    Artist[] getSeveralArtists(@RequestParam String[] id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralArtists(id).build().execute();
    }

    //http://localhost:8080/api/browse/getListOfCategories  run this first to find categories
    //http://localhost:8080/api/browse/getCategory?id=classical
    @GetMapping("/getCategory")
    public @ResponseBody
    Category getCategory(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCategory(id).build().execute();
    }

    //http://localhost:8080/api/browse/getListOfCategories  run this first to find categories
    //http://localhost:8080/api/browse/getCategoryPlaylist?id=classical
    @GetMapping("/getCategoryPlaylist")
    public @ResponseBody
    Paging<PlaylistSimplified> getCategoryPlaylist(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCategorysPlaylists(id).build().execute();
    }

    @GetMapping("/getListOfCategories")
    public @ResponseBody
    Paging<Category> getListOfCategories() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCategories().build().execute();
    }

    @GetMapping("/getListOfFeaturedPlaylists")
    public @ResponseBody
    FeaturedPlaylists getListOfFeaturedPlaylists() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfFeaturedPlaylists().build().execute();
    }

    //http://localhost:8080/api/browse/getEpisode?id=4GI3dxEafwap1sFiTGPKd1
    @GetMapping("/getEpisode")
    public @ResponseBody
    Episode getEpisode(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getEpisode(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralEpisodes?id=4GI3dxEafwap1sFiTGPKd1
    @GetMapping("/getSeveralEpisodes")
    public @ResponseBody
    Episode[] getSeveralEpisodes(@RequestParam String[] id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralEpisodes(id).build().execute();
    }

    //http://localhost:8080/api/browse/getShow?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/getShow")
    public @ResponseBody
    Show getShow(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getShow(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralShows?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/getSeveralShows")
    public @ResponseBody
    ShowSimplified[] getShow(@RequestParam String[] id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralShows(id).build().execute();
    }

    //http://localhost:8080/api/browse/getShowsEpisodes?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/getShowsEpisodes")
    public @ResponseBody
    Paging<EpisodeSimplified> getShowsEpisodes(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getShowEpisodes(id).build().execute();
    }
}
