package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.special.SearchResult;
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
    public Recommendations getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var availableGenreSeeds = spotifyConnect.getSpotifyApi().getAvailableGenreSeeds();
        return spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();
    }

    @GetMapping("/newReleases")
    public Paging<AlbumSimplified> newReleases() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfNewReleases().build().execute();
    }

    //http://localhost:8080/api/browse/getAlbum?id=5zT1JLIj9E57p3e1rFm9Uq
    @GetMapping("/getAlbum")
    public Album getAlbum(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAlbum(id).build().execute();
    }

    //http://localhost:8080/api/browse/getAlbumTrack?id=5zT1JLIj9E57p3e1rFm9Uq
    @GetMapping("/getAlbumTrack")
    public Paging<TrackSimplified> getAlbumTrack(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAlbumsTracks(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralAlbums?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getSeveralAlbums")
    public Album[] getSeveralAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralAlbums(ids).build().execute();
    }

    //http://localhost:8080/api/browse/getArtist?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtist")
    public Artist getArtist(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getArtist(artist_id).build().execute();
    }

    //http://localhost:8080/api/browse/getArtistsAlbums?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtistsAlbums")
    public Paging<AlbumSimplified> getArtistsAlbums(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getArtistsAlbums(artist_id).build().execute();
    }

    //http://localhost:8080/api/browse/getArtistsTopTracks?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtistsTopTracks")
    public Track[] getArtistsTopTracks(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        CountryCode countryCode = CountryCode.US;
        return spotifyConnect.getSpotifyApi().getArtistsTopTracks(artist_id,countryCode).build().execute();
    }

    //http://localhost:8080/api/browse/getArtistsRelatedArtists?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getArtistsRelatedArtists")
    public Artist[] getArtistsRelatedArtists(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getArtistsRelatedArtists(artist_id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralArtists?id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/getSeveralArtists")
    public Artist[] getSeveralArtists(@RequestParam String[] id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralArtists(id).build().execute();
    }

    //http://localhost:8080/api/browse/getListOfCategories  run this first to find categories
    //http://localhost:8080/api/browse/getCategory?id=classical
    @GetMapping("/getCategory")
    public Category getCategory(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCategory(id).build().execute();
    }

    //http://localhost:8080/api/browse/getListOfCategories  run this first to find categories
    //http://localhost:8080/api/browse/getCategoryPlaylist?id=classical
    @GetMapping("/getCategoryPlaylist")
    public Paging<PlaylistSimplified> getCategoryPlaylist(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCategorysPlaylists(id).build().execute();
    }

    @GetMapping("/getListOfCategories")
    public Paging<Category> getListOfCategories() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCategories().build().execute();
    }

    @GetMapping("/getListOfFeaturedPlaylists")
    public FeaturedPlaylists getListOfFeaturedPlaylists() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfFeaturedPlaylists().build().execute();
    }

    //http://localhost:8080/api/browse/getPlaylist?playlist_id=3AGOiaoRXMSjswCLtuNqv5
    @GetMapping("/getPlaylist")
    public Playlist getPlaylist(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getPlaylist(playlist_id).build().execute();
    }

    //http://localhost:8080/api/browse/getPlaylistItems?playlist_id=37i9dQZF1DX4fpCWaHOned
    @GetMapping("/getPlaylistItems")
    public Paging<PlaylistTrack> getPlaylistItems(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getPlaylistsItems(playlist_id).build().execute();
    }

    //http://localhost:8080/api/browse/getPlaylistImage?playlist_id=3AGOiaoRXMSjswCLtuNqv5
    @GetMapping("/getPlaylistImage")
    public Image[] getPlaylistImage(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getPlaylistCoverImage(playlist_id).build().execute();
    }

    //http://localhost:8080/api/browse/getEpisode?id=4GI3dxEafwap1sFiTGPKd1
    @GetMapping("/getEpisode")
    public Episode getEpisode(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getEpisode(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralEpisodes?id=4GI3dxEafwap1sFiTGPKd1
    @GetMapping("/getSeveralEpisodes")
    public Episode[] getSeveralEpisodes(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralEpisodes(ids).build().execute();
    }

    //http://localhost:8080/api/browse/getShow?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/getShow")
    public Show getShow(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getShow(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralShows?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/getSeveralShows")
    public ShowSimplified[] getShow(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralShows(ids).build().execute();
    }

    //http://localhost:8080/api/browse/getShowsEpisodes?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/getShowsEpisodes")
    public Paging<EpisodeSimplified> getShowsEpisodes(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getShowEpisodes(ids).build().execute();
    }

    //http://localhost:8080/api/browse/getTrack?id=01iyCAUm8EvOFqVWYJ3dVX
    @GetMapping("/getTrack")
    public Track getTrack(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getTrack(id).build().execute();
    }

    //http://localhost:8080/api/browse/getSeveralTracks?id=01iyCAUm8EvOFqVWYJ3dVX
    @GetMapping("/getSeveralTracks")
    public Track[] getSeveralTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getSeveralTracks(ids).build().execute();
    }

    @GetMapping("/getGenreSeeds")
    public String[] getGenreSeeds() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAvailableGenreSeeds().build().execute();
    }

    //http://localhost:8080/api/browse/searchItems?name=abba&type=artist
    //http://localhost:8080/api/browse/searchItems?name=positions&type=track
    @GetMapping("/searchItems")
    public SearchResult searchItems(@RequestParam String name, @RequestParam String type) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().searchItem(name, type).build().execute();
    }
}
