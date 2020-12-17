package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.AudioAnalysis;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/audio")
public class AudioController {

    @Autowired
    SpotifyConnect spotifyConnect;

    /**
     * http://localhost:8080/api/audio/analysis?id=01iyCAUm8EvOFqVWYJ3dVX
     *
     * @param id
     * Required.
     * The Spotify ID for the track.
     */
    @GetMapping("/analysis")
    public AudioAnalysis getAudioAnalysisForTrack(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAudioAnalysisForTrack(id).build().execute();
    }

    /**
     * http://localhost:8080/api/audio/audio-features-track?id=01iyCAUm8EvOFqVWYJ3dVX
     *
     * @param id
     * Required.
     * The Spotify ID for the track.
     */
    @GetMapping("/audio-features-track")
    public AudioFeatures getAudioFeaturesForTrack(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAudioFeaturesForTrack(id).build().execute();
    }

    /**
     * http://localhost:8080/api/audio/audio-features-tracks?ids=01iyCAUm8EvOFqVWYJ3dVX
     *
     * @param ids
     * Required.
     * A comma-separated list of the Spotify IDs for the tracks.
     * Maximum: 100 IDs.
     */
    @GetMapping("/audio-features-tracks")
    public AudioFeatures[] getAudioFeaturesForSeveralTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAudioFeaturesForSeveralTracks(ids).build().execute();
    }
}
