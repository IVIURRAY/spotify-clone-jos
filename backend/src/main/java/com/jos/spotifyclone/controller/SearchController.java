package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.model.*;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api/search")
@RestController
public class SearchController {
    @Autowired
    SpotifyConnect spotifyConnect;

    //TODO ${ARTIST_NAME_HERE} needs value storing elsewhere where this controller can access the search term to return searched for artist data
    //http://localhost:8080/api/search/artist?id=drake
    @GetMapping("/artist")
    public Map<String, Object> searchArtistController(@RequestParam String id) throws ParseException, IOException, SpotifyWebApiException {
        var response = spotifyConnect.getSpotifyApi().searchArtists(id).build().execute();

        List<ArtistModel> list = new ArrayList<>();
        for(Artist artist : response.getItems()){
            ExternalUrl externalUrl = artist.getExternalUrls();
            Followers followers = artist.getFollowers();
            String[] genres = artist.getGenres();
            Image[] images = artist.getImages();
            String artistName = artist.getName();

            list.add(new ArtistModel(externalUrl, followers, genres, images, artistName));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Artist", list);
        return map;
    }

    //http://localhost:8080/api/search/album?id=arianagrande
    @GetMapping("/album")
    public Map<String, Object> searchAlbumController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().searchAlbums(id).build().execute();

        List<AlbumModel> list = new ArrayList<>();
        for(AlbumSimplified album : response.getItems()){
            String name = album.getName();
            List<String> artist = new ArrayList<>();
            Image[] image = album.getImages();
            ExternalUrl externalUrl = album.getExternalUrls();

            ArtistSimplified[] artistArray  = album.getArtists();
            for(ArtistSimplified artistSimplified : artistArray){
                artist.add(artistSimplified.getName());
            }
            list.add(new AlbumModel(name, artist, image, externalUrl));
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("Album", list);
        return map;
    }

    //http://localhost:8080/api/search/episode?id=lauv
    @GetMapping("/episode")
    public Map<String, Object> searchEpisodeController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().searchEpisodes(id).build().execute();

        List<EpisodeModel> list = new ArrayList<>();
        for(EpisodeSimplified search : response.getItems()){
            String name = search.getName();
            String[] language = search.getLanguages();
            Image[] images = search.getImages();
            ExternalUrl externalUrls = search.getExternalUrls();
            String description = search.getDescription();

            list.add(new EpisodeModel(name, language, images, externalUrls, description));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Episode", list);
        return map;
    }

    //http://localhost:8080/api/search/show?id=bieber
    @GetMapping("/show")
    public Map<String, Object> searchShowController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().searchShows(id).build().execute();

        List<ShowModel> list = new ArrayList<>();
        for(ShowSimplified show : response.getItems()){
            String description = show.getDescription();
            String name = show.getName();
            ExternalUrl externalUrls = show.getExternalUrls();

            list.add(new ShowModel(description, name, externalUrls));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Show", list);
        return map;
    }

    //http://localhost:8080/api/search/playlist?id=bieber
    @GetMapping("/playlist")
    public Map<String, Object> searchPlaylistController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().searchPlaylists(id).build().execute();

        List<PlaylistModel> list = new ArrayList<>();
        for(PlaylistSimplified playlist : response.getItems()){
            String href = playlist.getHref();
            ExternalUrl externalUrls = playlist.getExternalUrls();
            String playlistName = playlist.getName();
            PlaylistTracksInformation tracks = playlist.getTracks();
            Image[] playlistCover = playlist.getImages();

            list.add(new PlaylistModel(href, externalUrls, playlistName, tracks, playlistCover));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Playlist", list);
        return map;
    }

    //http://localhost:8080/api/search/track?id=positions
    @GetMapping("/track")
    public Map<String, Object> searchTrackController(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().searchTracks(id).build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(Track track : response.getItems()){
            String name = track.getName();
            ExternalUrl externalUrls = track.getExternalUrls();

            List<String> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getArtists();
            for(ArtistSimplified artistSimplified : artists){
                artistsList.add(artistSimplified.getName());
            }

            List<AlbumModel> albumList  = new ArrayList<>();
            String albumName = track.getAlbum().getName();
            Image[] image = track.getAlbum().getImages();
            ExternalUrl externalUrlsAlbum = track.getAlbum().getExternalUrls();

            albumList.add(new AlbumModel(albumName, artistsList, image, externalUrlsAlbum));

            list.add(new TrackModel(name, externalUrls, artistsList, albumList));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Tracks", list);
        return map;
    }
}
