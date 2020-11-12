package com.jos.spotifyclone.controller;


import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.personalization.interfaces.IArtistTrackModelObject;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("api/user")
@RestController
public class UserController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/profile")
    public User currentUserProfile() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersProfile().build().execute();
    }

    //http://localhost:8080/api/user/anotherUserProfile?id=provalio
    @GetMapping("/anotherUserProfile")
    public User anotherUserProfile(@RequestParam String user_ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersProfile(user_ids).build().execute();
    }

    @GetMapping("/playlist")
    public Paging<PlaylistSimplified> playlistsOfCurrentUser() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute();
    }

    @GetMapping("/followedArtists")
    public PagingCursorbased<Artist> followedArtists() throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().getUsersFollowedArtists(type).build().execute();
    }

    //http://localhost:8080/api/user/checkFollowArtistOrUser?ids=1l0mKo96Jh9HVYONcRl3Yp
    @GetMapping("/checkFollowArtistOrUser")
    public Boolean[] checkFollowArtistOrUser(@RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(type,user_ids).build().execute();
    }

    //http://localhost:8080/api/user/followArtistOrUser?ids=1l0mKo96Jh9HVYONcRl3Yp
    @GetMapping("/followArtistOrUser")
    public String followArtistOrUser(@RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().followArtistsOrUsers(type,user_ids).build().execute();
    }

    //http://localhost:8080/api/user/unfollowArtistOrUser?user_ids=1l0mKo96Jh9HVYONcRl3Yp
    @GetMapping("/unfollowArtistOrUser")
    public String unfollowArtistOrUser(@RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().unfollowArtistsOrUsers(type,user_ids).build().execute();
    }

    //http://localhost:8080/api/user/checkUsersFollowPlaylist?playlistId=3AGOiaoRXMSjswCLtuNqv5&ownerId=abbaspotify&ids=abbaspotify
    @GetMapping("/checkUsersFollowPlaylist")
    public Boolean[] checkUsersFollowPlaylist(String ownerId, @RequestParam String playlistId, @RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().checkUsersFollowPlaylist(ownerId, playlistId, user_ids).build().execute();
    }

    @GetMapping("/followPlaylist")
    public String followPlaylist(@RequestParam String playlistId, @RequestParam(required = false) boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().followPlaylist(playlistId, public_).build().execute();
    }

    @GetMapping("/unfollowPlaylist")
    public String unfollowPlaylist(@RequestParam(required = false) String ownerId, @RequestParam String playlistId) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().unfollowPlaylist(ownerId, playlistId).build().execute();
    }

    @GetMapping("/savedAlbums")
    public Paging<SavedAlbum> savedAlbums() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getCurrentUsersSavedAlbums().build().execute();
    }

    @GetMapping("/savedShows")
    public Paging<SavedShow> savedShows() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersSavedShows().build().execute();
    }

    @GetMapping("/savedTracks")
    public Paging<SavedTrack> savedTracks() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersSavedTracks().build().execute();
    }

    //run http://localhost:8080/api/user/savedAlbums/ first to find id's
    //http://localhost:8080/api/user/removeAlbums?ids=<replace with albums id>
    @GetMapping("/removeAlbums")
    public String removeAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeAlbumsForCurrentUser(ids).build().execute();
    }

    //run http://localhost:8080/api/user/savedShows/ first to find id's
    //http://localhost:8080/api/user/removeShows?ids=<replace with shows id>
    @GetMapping("/removeShows")
    public String removeShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeUsersSavedShows(ids).build().execute();
    }

    //run http://localhost:8080/api/user/savedTracks/ first to find id's
    //http://localhost:8080/api/user/removeTracks?ids=<replace with tracks id>
    @GetMapping("/removeTracks")
    public String removeTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeUsersSavedTracks(ids).build().execute();
    }

    //http://localhost:8080/api/search/album?id=arianagrande
    //http://localhost:8080/api/user/saveAlbums?ids=<replace with albums id>
    @GetMapping("/saveAlbums")
    public String saveAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().saveAlbumsForCurrentUser(ids).build().execute();
    }

    //http://localhost:8080/api/search/show?id=bieber
    //http://localhost:8080/api/user/saveShows?ids=<replace with shows id>
    @GetMapping("/saveShows")
    public String saveShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().saveShowsForCurrentUser(ids).build().execute();
    }

    //http://localhost:8080/api/search/track?id=positions
    //http://localhost:8080/api/user/saveTracks?ids=<replace with tracks id>
    @GetMapping("/saveTracks")
    public String saveTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().saveTracksForUser(ids).build().execute();
    }

    @GetMapping("/checkSavedAlbums")
    public Boolean[] checkSavedAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().checkUsersSavedAlbums(ids).build().execute();
    }

    @GetMapping("/checkSavedShows")
    public Boolean[] checkSavedShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().checkUsersSavedShows(ids).build().execute();
    }

    @GetMapping("/checkSavedTracks")
    public Boolean[] checkSavedTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().checkUsersSavedTracks(ids).build().execute();
    }

    @GetMapping("/getTopArtists")
    public Paging<Artist> getTopArtists() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersTopArtists().build().execute();
    }

    @GetMapping("/getTopTracks")
    public Paging<Track> getTopTracks() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersTopTracks().build().execute();
    }

    @GetMapping("/getTopArtistsAndTracks")
    public Paging<IArtistTrackModelObject> getTopArtistsAndTracks() throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        return spotifyConnect.getSpotifyApi().getUsersTopArtistsAndTracks(type).build().execute();
    }

    @GetMapping("/getListOfAnotherUserPlaylists")
    public Paging<PlaylistSimplified> getListOfAnotherUserPlaylists(@RequestParam String user_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getListOfUsersPlaylists(user_id).build().execute();
    }
}


