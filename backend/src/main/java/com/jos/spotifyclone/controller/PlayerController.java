package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.model.TrackModel;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.ExternalUrl;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api/player")
@RestController
public class PlayerController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/playing")
    public Map<String, Object> currentlyPlayings() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersCurrentlyPlayingTrack().build().execute();

        ExternalUrl externalUrl = response.getItem().getExternalUrls();
        String playingRn = response.getItem().getName();

        Map<String, Object> map = new HashMap<>();
        map.put("Currently playing", playingRn);
        map.put("External URL", externalUrl);

        return map;
    }

    @GetMapping("/recent")
    public Map<String, Object> getRecentTracks() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(PlayHistory track : response.getItems()){
            String name = track.getTrack().getName();
            ExternalUrl externalUrls = track.getTrack().getExternalUrls();

            ArtistSimplified[] artists = track.getTrack().getArtists();
            List<String> artistsList = new ArrayList<>();
            for(ArtistSimplified artist : artists){
                artistsList.add(artist.getName());
            }
            list.add(new TrackModel(name, externalUrls, artistsList));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Recent tracks",list);
        return map;
    }

    @GetMapping("/available-devices")
    public Device[] availableDevices() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getUsersAvailableDevices().build().execute();
    }

    @GetMapping("/playback")
    public Map<String, Object> currentPlayback() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getInformationAboutUsersCurrentPlayback().build().execute();
        String deviceName = response.getDevice().getName();
        String deviceType = response.getDevice().getType();
        Integer volumeLevel = response.getDevice().getVolume_percent();
        Boolean deviceActive  = response.getDevice().getIs_active();
        Boolean isPlaying = response.getIs_playing();
        Map<String, Object> map = new HashMap<>();
        IPlaylistItem playlistItem = response.getItem();
        String name = playlistItem.getName();
        ExternalUrl externalUrl = playlistItem.getExternalUrls();
        map.put("Device name", deviceName);
        map.put("Device type", deviceType);
        map.put("Volume level", volumeLevel);
        map.put("Device active", deviceActive);
        map.put("Is playing", isPlaying);
        map.put("Track name", name);
        map.put("External url", externalUrl);

        return map;
    }
}
