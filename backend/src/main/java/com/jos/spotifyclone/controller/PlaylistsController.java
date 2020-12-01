package com.jos.spotifyclone.controller;

import com.google.gson.JsonArray;
import com.jos.spotifyclone.model.PlaylistModel;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/playlists")
public class PlaylistsController {

    @Autowired
    SpotifyConnect spotifyConnect;

    //http://localhost:8080/api/playlists/change-name?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&name=<FUTURE NAME>
    @GetMapping("/change-name")
    public String changePlaylistName(@RequestParam String playlist_id,@RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).name(name).build().execute();
        return "The name of the " + playlist_id  + " playlist is now " + name + ".";
    }

    //http://localhost:8080/api/playlists/change-visibilityy?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&public_=<TRUE/FALSE>
    @GetMapping("/change-visibility")
    public String changePlaylistVisibility(@RequestParam String playlist_id,@RequestParam boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).public_(public_).build().execute();
        if(public_){
            return "Your " + playlist_id + " playlist is now public.";
        }
        return "Your " + playlist_id + " playlist is not public.";
    }

    //http://localhost:8080/api/playlists/change-collaborative?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&collaborative=<TRUE/FALSE>
    @GetMapping("/change-collaborative")
    public String changePlaylistCollab(@RequestParam String playlist_id,@RequestParam boolean collaborative) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).collaborative(collaborative).build().execute();
        if(collaborative){
            return "Your " + playlist_id + " playlist is now collaborative.";
        }
        return "Your " + playlist_id + " playlist is not collaborative.";
    }

    //http://localhost:8080/api/playlists/change-description?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&description=<FUTURE DESCRIPTION>
    @GetMapping("/change-description")
    public String changePlaylistDescription(@RequestParam String playlist_id,@RequestParam String description) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).description(description).build().execute();
        return "The description of your " + playlist_id + " has changed.";
    }

    //http://localhost:8080/api/playlists/change-details?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&name=<FUTURE NAME>&description=<FUTURE DESCRIPTION>&collab=<TRUE/FALSE>&public_=<TRUE/FALSE>
    @GetMapping("/change-details")
    public String changePlaylist(@RequestParam String playlist_id,@RequestParam String name, @RequestParam String description, @RequestParam boolean collab, @RequestParam boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).name(name).description(description).collaborative(collab).public_(public_).build().execute();
        return "The playlist with the id " + playlist_id + " has been modified.";
    }

    //http://localhost:8080/api/playlists/create-playlist?playlist_id=<THE ID OF YOUR OWN ACCOUNT>&name=<FUTURE NAME>
    @GetMapping("/create")
    public String createPlaylist(@RequestParam String user_id, @RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        return "You created a new playlist with the name " + name + ".";
    }

    //https://png-pixel.com/   a small base64 image that wont crash due to too long header
    //http://localhost:8080/api/playlists/upload-custom-cover?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&location=iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8zsDwHwAE5QH4waZ7uQAAAABJRU5ErkJggg==
    @GetMapping("/upload-custom-cover")
    public String uploadCustomPlaylistCover(@RequestParam String playlist_id, @RequestParam String data) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().uploadCustomPlaylistCoverImage(playlist_id).image_data(data).build().execute();
    }

    @GetMapping("/add-items-to")
    public SnapshotResult addItemsTo(@RequestParam String playlist_id, @RequestParam String[] uris) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().addItemsToPlaylist(playlist_id,uris).build().execute();
    }

    @GetMapping("/remove-items-from")
    public SnapshotResult removeItemsFrom(@RequestParam String playlist_id, @RequestParam JsonArray tracks) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeItemsFromPlaylist(playlist_id,tracks).build().execute();
    }

    @GetMapping("/reorder-items-from")
    public SnapshotResult reorderItemsFrom(@RequestParam String playlist_id, @RequestParam int range_start, @RequestParam int range_length, @RequestParam int insertBefore) throws ParseException, SpotifyWebApiException, IOException {
        return  spotifyConnect.getSpotifyApi().reorderPlaylistsItems(playlist_id,range_start,insertBefore).build().execute();
    }

    @GetMapping("/replace-items-from")
    public String replaceItemsFrom(@RequestParam String playlist_id, @RequestParam String[] uris) throws ParseException, SpotifyWebApiException, IOException {
        return  spotifyConnect.getSpotifyApi().replacePlaylistsItems(playlist_id, uris).build().execute();
    }

    @GetMapping("/featured-playlists")
    public Map<String,Object> getListOfFeaturedPlaylists() throws ParseException, SpotifyWebApiException, IOException {
        FeaturedPlaylists response = spotifyConnect.getSpotifyApi().getListOfFeaturedPlaylists().build().execute();

        List<PlaylistModel> list = new ArrayList<>();
        for(PlaylistSimplified playlist : response.getPlaylists().getItems()){
            String href = playlist.getHref();
            ExternalUrl externalUrls = playlist.getExternalUrls();
            String playlistName = playlist.getName();
            PlaylistTracksInformation tracks = playlist.getTracks();
            Image[] playlistCover = playlist.getImages();

            list.add(new PlaylistModel(href, externalUrls, playlistName, tracks, playlistCover));
        }

        Map<String,Object> map = new HashMap<>();
        map.put("Featured playlists", list);
        return map;
    }

    //http://localhost:8080/api/playlists/getPlaylist?playlist_id=3AGOiaoRXMSjswCLtuNqv5
    @GetMapping("/playlist")
    public Map<String, Object> getPlaylist(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        Playlist response = spotifyConnect.getSpotifyApi().getPlaylist(playlist_id).build().execute();

        List<PlaylistModel> list = new ArrayList<>();
        for(PlaylistTrack playlist : response.getTracks().getItems()){
            String href = playlist.getTrack().getHref();
            ExternalUrl externalUrls = playlist.getTrack().getExternalUrls();
            String trackName = playlist.getTrack().getName();
            Image[] playlistImage = response.getImages();
            list.add(new PlaylistModel(href, externalUrls, trackName, playlistImage));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Playlist", list);
        return map;
    }

    //http://localhost:8080/api/playlists/playlist-items?playlist_id=37i9dQZF1DX4fpCWaHOned
    @GetMapping("/playlist-items")
    public Map<String, Object> getPlaylistItems(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getPlaylistsItems(playlist_id).build().execute();

        List<PlaylistModel> list = new ArrayList<>();
        for(PlaylistTrack item : response.getItems()){
            String href = item.getTrack().getHref();
            ExternalUrl externalUrls = item.getTrack().getExternalUrls();
            String trackName = item.getTrack().getName();
            Image[] playlistCover = getPlaylistImage(playlist_id);

            list.add(new PlaylistModel(href, externalUrls, trackName, playlistCover));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Tracks",list);
        return map;
   }

    //http://localhost:8080/api/playlists/playlist-image?playlist_id=3AGOiaoRXMSjswCLtuNqv5
    @GetMapping("/playlist-image")
    public Image[] getPlaylistImage(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getPlaylistCoverImage(playlist_id).build().execute();
    }
}
