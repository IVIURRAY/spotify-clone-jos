package com.jos.spotifyclone.controller;

import com.google.gson.JsonArray;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Playlist;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/playlists")
public class PlaylistsController {

    @Autowired
    SpotifyConnect spotifyConnect;

    //http://localhost:8080/api/playlists/changeName?id=<THE ID OF YOUR OWN PLAYLIST>&name=<FUTURE NAME>
    @GetMapping("/changeName")
    public String changePlaylistName(@RequestParam String playlist_id,@RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).name(name).build().execute();
    }

    //http://localhost:8080/api/playlists/changeVisibility?id=<THE ID OF YOUR OWN PLAYLIST>&public_=<TRUE/FALSE>
    @GetMapping("/changeVisibility")
    public String changePlaylistVisibility(@RequestParam String playlist_id,@RequestParam boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).public_(public_).build().execute();
    }

    //http://localhost:8080/api/playlists/changeCollaborative?id=<THE ID OF YOUR OWN PLAYLIST>&collaborative=<TRUE/FALSE>
    @GetMapping("/changeCollaborative")
    public String changePlaylistCollab(@RequestParam String playlist_id,@RequestParam boolean collaborative) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).collaborative(collaborative).build().execute();
    }

    //http://localhost:8080/api/playlists/changeDescription?id=<THE ID OF YOUR OWN PLAYLIST>&description=<FUTURE DESCRIPTION>
    @GetMapping("/changeDescription")
    public String changePlaylistDescription(@RequestParam String playlist_id,@RequestParam String description) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).description(description).build().execute();
    }

    //http://localhost:8080/api/playlists/changeDescription?id=<THE ID OF YOUR OWN PLAYLIST>&description=<FUTURE DESCRIPTION>&collab=<TRUE/FALSE>&public_=<TRUE/FALSE>
    @GetMapping("/changeAll")
    public String changePlaylist(@RequestParam String playlist_id,@RequestParam String name, @RequestParam String description, @RequestParam boolean collab, @RequestParam boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).name(name).description(description).collaborative(collab).public_(public_).build().execute();
    }

    //http://localhost:8080/api/playlists/createPlaylist?id=<THE ID OF YOUR OWN ACCOUNT>&name=<FUTURE NAME>
    @GetMapping("/createPlaylist")
    public Playlist createPlaylist(@RequestParam String user_id, @RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().createPlaylist(user_id, name).build().execute();
    }

    //https://png-pixel.com/   a small base64 image that wont crash due to too long header
    //http://localhost:8080/api/playlists/uploadCustomPlaylistCover?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&location=iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8zsDwHwAE5QH4waZ7uQAAAABJRU5ErkJggg==
    @GetMapping("/uploadCustomPlaylistCover")
    public String uploadCustomPlaylistCover(@RequestParam String playlist_id, @RequestParam String data) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().uploadCustomPlaylistCoverImage(playlist_id).image_data(data).build().execute();
    }

    @GetMapping("/addItemsTo")
    public SnapshotResult addItemsTo(@RequestParam String playlist_id, @RequestParam String[] uris) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().addItemsToPlaylist(playlist_id,uris).build().execute();
    }

    @GetMapping("/removeItemsFrom")
    public SnapshotResult removeItemsFrom(@RequestParam String playlist_id, @RequestParam JsonArray tracks) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeItemsFromPlaylist(playlist_id,tracks).build().execute();
    }

    @GetMapping("/reorderItemsFrom")
    public SnapshotResult reorderItemsFrom(@RequestParam String playlist_id, @RequestParam int range_start, @RequestParam int range_length, @RequestParam int insertBefore) throws ParseException, SpotifyWebApiException, IOException {
        return  spotifyConnect.getSpotifyApi().reorderPlaylistsItems(playlist_id,range_start,insertBefore).build().execute();
    }

    @GetMapping("/replaceItemsFrom")
    public String replaceItemsFrom(@RequestParam String playlist_id, @RequestParam String[] uris) throws ParseException, SpotifyWebApiException, IOException {
        return  spotifyConnect.getSpotifyApi().replacePlaylistsItems(playlist_id, uris).build().execute();
    }
}
