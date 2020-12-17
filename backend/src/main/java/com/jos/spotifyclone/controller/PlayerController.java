package com.jos.spotifyclone.controller;

import com.google.gson.JsonArray;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    public Map<String, Object> currentlyPlaying() throws ParseException, SpotifyWebApiException, IOException {
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
            List<Object> artistsList = new ArrayList<>();
            for(ArtistSimplified artist : artists){
                artistsList.add(artist.getName());
                artistsList.add(artist.getExternalUrls());
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

    /**
     *
     * @param market
     * Optional. An ISO 3166-1 alpha-2 country code or the string from_token.
     * Provide this parameter if you want to apply Track Relinking.
     * For episodes, if a valid user access token is specified in the request header, the country associated with
     * the user account will take priority over this parameter.
     */
    @GetMapping("/playback")
    public Map<String, Object> currentPlayback(@RequestParam(required = false, defaultValue = "US") String market) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getInformationAboutUsersCurrentPlayback()
                .setQueryParameter("market", market)
                .build().execute();
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


    /**
     * Only premium
     * http://localhost:8080/api/player/add-item?uri=2I9t0e3gLAKOi5syoHxP46
     *
     * @param uri
     * Required. The uri of the item to add to the queue. Must be a track or an episode uri.
     *
     * @param device_id
     * Optional. The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/add")
    public String addItemToPlayback(@RequestParam String uri, @RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().addItemToUsersPlaybackQueue(uri).build().execute();
        return "Successfully added " + uri + " track to queue.";
    }

    //Only premium
    //http://localhost:8080/api/player/pause
    @GetMapping("/pause")
    public String pausePlayback() throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().pauseUsersPlayback().build().execute();
        return "Playback is now paused.";
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/seek?position_ms=10000
     *
     * @param position_ms
     * Required.The position in milliseconds to seek to.
     * Must be a positive number.
     * Passing in a position that is greater than the length of the track will cause the player to start playing the next song.
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/seek")
    public String seekPlayback(@RequestParam int position_ms, @RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().seekToPositionInCurrentlyPlayingTrack(position_ms).build().execute();
        return "Started playback from " + position_ms;
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/repeat?state=track
     *
     * @param state
     * Required.
     * track, context or off.
     * track will repeat the current track.
     * context will repeat the current context.
     * off will turn repeat off.
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/repeat")
    public String repeatPlayback(@RequestParam String state, @RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().setRepeatModeOnUsersPlayback(state).build().execute();
        return "State of repeat is now: " + state;
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/volume?volume_percent=50
     *
     * @param volume_percent
     * Required.
     * Integer.
     * The volume to set. Must be a value from 0 to 100 inclusive.
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/volume")
    public String volumePlayback(@RequestParam int volume_percent, @RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().setVolumeForUsersPlayback(volume_percent).build().execute();
        return "Volume is now " + volume_percent;
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/next
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/next")
    public String nextPlayback(@RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().skipUsersPlaybackToNextTrack().build().execute();
        return "Skipped to next track";
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/previous
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/previous")
    public String previousPlayback(@RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().skipUsersPlaybackToNextTrack().build().execute();
        return "Skipped to previous track";
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/play
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/play")
    public String startPlayback(@RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().startResumeUsersPlayback().build().execute();
        return "Started/resumed playback";
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/shuffle?state=true
     *
     * @param state
     * Required
     * true : Shuffle user’s playback
     * false : Do not shuffle user’s playback.
     *
     * @param device_id
     * Optional.
     * The id of the device this command is targeting.
     * If not supplied, the user’s currently active device is the target.
     */
    @GetMapping("/shuffle")
    public String shufflePlayback(@RequestParam boolean state, @RequestParam(required = false) String device_id) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().toggleShuffleForUsersPlayback(state).build().execute();
        if(state){
            return "Shuffle is on.";
        }
        return "Shuffle is off.";
    }

    /**
     * Only premium
     * http://localhost:8080/api/player/?device_ids=74ASZWbe4lXaubB36ztrGX&play=true
     *
     * @param device_ids
     * Required.
     * A JSON array containing the ID of the device on which playback should be started/transferred.
     * For example:{device_ids:["74ASZWbe4lXaubB36ztrGX"]}
     * Note: Although an array is accepted, only a single device_id is currently supported.
     * Supplying more than one will return 400 Bad Request
     *
     * @param play
     * Optional.
     * true: ensure playback happens on new device.
     * false or not provided: keep the current playback state.
     */
    @GetMapping("/transfer")
    public ModelAndView transferPlayback(@RequestParam JsonArray device_ids, @RequestParam(required = false, defaultValue = "true") boolean play) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().transferUsersPlayback(device_ids).setQueryParameter("play", play).build().execute();
        return new ModelAndView("redirect:playback");
    }
}
