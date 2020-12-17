package com.jos.spotifyclone.controller;

import com.jos.spotifyclone.model.AlbumModel;
import com.jos.spotifyclone.model.ArtistModel;
import com.jos.spotifyclone.model.EpisodeModel;
import com.jos.spotifyclone.model.TrackModel;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("api/browse")
@RestController
public class BrowseController {

    @Autowired
    SpotifyConnect spotifyConnect;

    //https://developer.spotify.com/console/get-available-genre-seeds/
    //http://localhost:8080/api/browse/recommended?seed=emo
    @GetMapping("/recommended")
    public Map<String, Object> getRecommended(@RequestParam String seed) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getRecommendations().seed_genres(seed).build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(TrackSimplified rec : response.getTracks()){
            String name = rec.getName();
            ExternalUrl externalUrl = rec.getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            for(ArtistSimplified artistSimplified : rec.getArtists()){
                artistsList.add(artistSimplified.getName());
                artistsList.add(artistSimplified.getExternalUrls());
            }

            list.add(new TrackModel(name, externalUrl, artistsList));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Recommended", list);
        return map;
    }

    //http://localhost:8080/api/browse/new-releases
    @GetMapping("/new-releases")
    public Map<String, Object> newReleases() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getListOfNewReleases().build().execute();

        List<AlbumModel> list = new ArrayList<>();
        for(AlbumSimplified album : response.getItems()){
            String name = album.getName();
            Image[] image = album.getImages();
            ExternalUrl externalUrl = album.getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            var artists = album.getArtists();
            for(ArtistSimplified artist : artists){
                artistsList.add(artist.getName());
                artistsList.add(artist.getExternalUrls());
            }
            list.add(new AlbumModel(name, artistsList, image, externalUrl));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("New releases", list);
        return map;
    }

    //http://localhost:8080/api/browse/album?id=5zT1JLIj9E57p3e1rFm9Uq
    @GetMapping("/album")
    public Map<String, Object> getAlbum(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getAlbum(id).build().execute();
        var albums = response.getTracks();

        List<AlbumModel> list = new ArrayList<>();
        String name = response.getName();
        Image[] image = response.getImages();
        ExternalUrl externalUrl = response.getExternalUrls();

        List<Object> tracks = new ArrayList<>();
        TrackSimplified[] tracksItems = albums.getItems();
        for(TrackSimplified track : tracksItems){
            tracks.add(track.getName());
            tracks.add(track.getExternalUrls());
        }


        List<Object> artistList = new ArrayList<>();
        var artists = response.getArtists();
        for(ArtistSimplified artist : artists){
            artistList.add(artist.getName());
            artistList.add(artist.getExternalUrls());
        }
        list.add(new AlbumModel(name, artistList, image, externalUrl));

        Map<String, Object> map = new HashMap<>();
        map.put("Album", list);
        map.put("Tracks", tracks);
        return map;
    }

    //http://localhost:8080/api/browse/albums/tracks?album_id=5zT1JLIj9E57p3e1rFm9Uq
    @GetMapping("/albums/tracks")
    public Map<String, Object> getAlbumTrack(@RequestParam String album_id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getAlbumsTracks(album_id).build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(TrackSimplified albumTrack : response.getItems()){
            String name = albumTrack.getName();
            ExternalUrl externalUrls = albumTrack.getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            var artists = albumTrack.getArtists();
            for(ArtistSimplified artist : artists){
                artistsList.add(artist.getName());
                artistsList.add(artist.getExternalUrls());
            }

            list.add(new TrackModel(name, externalUrls, artistsList));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Tracks", list);
        return map;
    }

    //http://localhost:8080/api/browse/albums?ids=48I4Jtcqu5K5jZWadn035d
    @GetMapping("/albums")
    public Map<String, Object> getSeveralAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getSeveralAlbums(ids).build().execute();

        List<AlbumModel> list = new ArrayList<>();
        for(Album album : response){
            String name = album.getName();
            Image[] image = album.getImages();
            ExternalUrl externalUrl = album.getExternalUrls();

            List<Object> artistList = new ArrayList<>();
            var artists = album.getArtists();
            for(ArtistSimplified artist : artists){
                artistList.add(artist.getName());
                artistList.add(artist.getExternalUrls());
            }
            list.add(new AlbumModel(name, artistList, image, externalUrl));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Albums", list);
        return map;
    }

    //http://localhost:8080/api/browse/artist?artist_id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/artist")
    public Map<String, Object> getArtist(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getArtist(artist_id).build().execute();

        List<ArtistModel> list = new ArrayList<>();
        ExternalUrl externalUrl = response.getExternalUrls();
        Followers followers = response.getFollowers();
        String[] genres = response.getGenres();
        Image[] images = response.getImages();
        String artistName = response.getName();

        list.add(new ArtistModel(externalUrl, followers, genres, images, artistName));

        Map<String, Object> map = new HashMap<>();
        map.put("Artist", list);
        return map;
    }

    //http://localhost:8080/api/browse/artists/albums?artist_id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/artists/albums")
    public Map<String, Object> getArtistsAlbums(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getArtistsAlbums(artist_id).build().execute();

        List<AlbumModel> list = new ArrayList<>();
        for(AlbumSimplified artistAlbum : response.getItems()){
            String name = artistAlbum.getName();
            Image[] image = artistAlbum.getImages();
            ExternalUrl externalUrl = artistAlbum.getExternalUrls();

            List<Object> artistList = new ArrayList<>();
            ArtistSimplified[] artists = artistAlbum.getArtists();
            for(ArtistSimplified artist : artists){
                artistList.add(artist.getName());
                artistList.add(artist.getExternalUrls());
            }
            list.add(new AlbumModel(name, artistList, image, externalUrl));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Artists albums", list);
        return map;
    }

    //http://localhost:8080/api/browse/artists/top?artist_id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/artists/top")
    public Map<String, Object> getArtistsTopTracks(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        CountryCode countryCode = CountryCode.US;
        Track[] topTracks = spotifyConnect.getSpotifyApi().getArtistsTopTracks(artist_id,countryCode).build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(Track track : topTracks){
            String name = track.getName();
            ExternalUrl externalUrls = track.getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getArtists();
            for(ArtistSimplified artist : artists){
                artistsList.add(artist.getName());
            }

            List<AlbumModel> albumsList = new ArrayList<>();

            AlbumSimplified albums = track.getAlbum();
            String nameAlbum = albums.getName();
            List<Object> artistAlbumList = new ArrayList<>();
            ArtistSimplified[] artistOfAlbum = albums.getArtists();
            for(ArtistSimplified ar : artistOfAlbum){
                artistAlbumList.add(ar.getName());
                artistAlbumList.add(ar.getExternalUrls());
            }
            Image[] imageAlbum = albums.getImages();
            ExternalUrl externalUrlAlbum = albums.getExternalUrls();
            albumsList.add(new AlbumModel(nameAlbum, artistAlbumList, imageAlbum, externalUrlAlbum));

            list.add(new TrackModel(name, externalUrls, artistsList, albumsList));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Top tracks", list);
        return map;
    }

    //http://localhost:8080/api/browse/artists/related?artist_id=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/artists/related")
    public Map<String, Object> getArtistsRelatedArtists(@RequestParam String artist_id) throws ParseException, SpotifyWebApiException, IOException {
        Artist[] response = spotifyConnect.getSpotifyApi().getArtistsRelatedArtists(artist_id).build().execute();

        List<ArtistModel> list = new ArrayList<>();
        for(Artist artist : response){
            ExternalUrl externalUrl = artist.getExternalUrls();
            Followers followers = artist.getFollowers();
            String[] genres = artist.getGenres();
            Image[] images = artist.getImages();
            String artistName = artist.getName();

            list.add(new ArtistModel(externalUrl, followers, genres, images, artistName));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Related artists", list);
        return map;
    }

    //http://localhost:8080/api/browse/artists?ids=0LcJLqbBmaGUft1e9Mm8HV
    @GetMapping("/artists")
    public Map<String, Object> getSeveralArtists(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        Artist[] response = spotifyConnect.getSpotifyApi().getSeveralArtists(ids).build().execute();

        List<ArtistModel> list = new ArrayList<>();
        for(Artist artist : response){
            ExternalUrl externalUrl = artist.getExternalUrls();
            Followers followers = artist.getFollowers();
            String[] genres = artist.getGenres();
            Image[] images = artist.getImages();
            String artistName = artist.getName();

            list.add(new ArtistModel(externalUrl, followers, genres, images, artistName));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Artists", list);
        return map;
    }

    //http://localhost:8080/api/browse/categories  run this first to find categories
    //http://localhost:8080/api/browse/category?id=classical
    @GetMapping("/category")
    public Map<String, Object> getCategory(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        Category response = spotifyConnect.getSpotifyApi().getCategory(id).build().execute();
        String categoryName = response.getName();
        String href = response.getHref();
        Map<String, Object> map = new HashMap<>();
        map.put("Category name", categoryName);
        map.put("href", href);
        return map;
    }

    //http://localhost:8080/api/browse/categories  run this first to find categories
    //http://localhost:8080/api/browse/categories/playlist?id=classical
    @GetMapping("/categories/playlist")
    public Map<String, Object> getCategoryPlaylist(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getCategorysPlaylists(id).build().execute();

        List<Object> list = new ArrayList<>();
        for(PlaylistSimplified playlist : response.getItems()){
            ExternalUrl externalUrl = playlist.getExternalUrls();
            String name = playlist.getName();

            list.add(name);
            list.add(externalUrl);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Details", list);
        return map;
    }

    @GetMapping("/categories")
    public Map<String, Object> getListOfCategories() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getListOfCategories().build().execute();

        List<String> list = new ArrayList<>();
        for(Category category : response.getItems()){
            String href = category.getHref();
            String name = category.getName();

            list.add(href);
            list.add(name);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("List of categories", list);
        return map;
    }

    //http://localhost:8080/api/browse/episode?id=4GI3dxEafwap1sFiTGPKd1
    @GetMapping("/episode")
    public Map<String, Object> getEpisode(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        Episode response = spotifyConnect.getSpotifyApi().getEpisode(id).build().execute();

        String name = response.getName();
        String[] language = response.getLanguages();
        Image[] images = response.getImages();
        ExternalUrl externalUrls = response.getExternalUrls();
        String description = response.getDescription();

        Map<String, Object> map = new HashMap<>();
        map.put("Episode name", name);
        map.put("Language", language);
        map.put("Images", images);
        map.put("External urls", externalUrls);
        map.put("Description", description);
        return map;
    }

    //http://localhost:8080/api/browse/episodes?ids=4GI3dxEafwap1sFiTGPKd1
    @GetMapping("/episodes")
    public Map<String, Object> getSeveralEpisodes(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        Episode response = spotifyConnect.getSpotifyApi().getEpisode(ids).build().execute();

        String name = response.getName();
        String[] language = response.getLanguages();
        Image[] images = response.getImages();
        ExternalUrl externalUrls = response.getExternalUrls();
        String description = response.getDescription();

        Map<String, Object> map = new HashMap<>();
        map.put("Episode name", name);
        map.put("Language", language);
        map.put("Images", images);
        map.put("External urls", externalUrls);
        map.put("Description", description);
        return map;
    }

    //http://localhost:8080/api/browse/show?id=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/show")
    public Map<String, Object> getShow(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        Show response = spotifyConnect.getSpotifyApi().getShow(id).build().execute();
        String description = response.getDescription();
        String name = response.getName();
        ExternalUrl externalUrls = response.getExternalUrls();

        Map<String, Object> map = new HashMap<>();
        map.put("Description", description);
        map.put("Name", name);
        map.put("External urls", externalUrls);
        return map;
    }

    //http://localhost:8080/api/browse/shows?ids=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/shows")
    public Map<String, Object> getSeveralShows(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        Show response = spotifyConnect.getSpotifyApi().getShow(ids).build().execute();
        String description = response.getDescription();
        String name = response.getName();
        ExternalUrl externalUrls = response.getExternalUrls();

        Map<String, Object> map = new HashMap<>();
        map.put("Description", description);
        map.put("Name", name);
        map.put("External urls", externalUrls);
        return map;
    }

    //http://localhost:8080/api/browse/shows/episodes?ids=5AvwZVawapvyhJUIx71pdJ
    @GetMapping("/shows/episodes")
    public Map<String, Object> getShowsEpisodes(@RequestParam String ids) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getShowEpisodes(ids).build().execute();

        List<EpisodeModel> list = new ArrayList<>();
        for(EpisodeSimplified episode : response.getItems()){
            String name = episode.getName();
            String[] language = episode.getLanguages();
            Image[] images = episode.getImages();
            ExternalUrl externalUrls = episode.getExternalUrls();
            String description = episode.getDescription();

            list.add(new EpisodeModel(name, language, images, externalUrls, description));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Shows episodes", list);
        return map;
    }

    //http://localhost:8080/api/browse/track?id=01iyCAUm8EvOFqVWYJ3dVX
    @GetMapping("/track")
    public Map<String, Object> getTrack(@RequestParam String id) throws ParseException, SpotifyWebApiException, IOException {
        Track response = spotifyConnect.getSpotifyApi().getTrack(id).build().execute();

        List<TrackModel> list = new ArrayList<>();
        String name = response.getName();
        ExternalUrl externalUrls = response.getExternalUrls();

        List<Object> artistsList = new ArrayList<>();
        ArtistSimplified[] artists = response.getArtists();
        for(ArtistSimplified artist : artists){
            artistsList.add(artist.getName());
            artistsList.add(artist.getExternalUrls());
        }

        List<AlbumModel> albumsList = new ArrayList<>();
        AlbumSimplified albums = response.getAlbum();
        String nameAlbum = albums.getName();
        Image[] imageAlbum = albums.getImages();
        ExternalUrl externalUrlAlbum = albums.getExternalUrls();
        List<Object> artistOfAlbumList = new ArrayList<>();
        ArtistSimplified[] artistOfAlbum = albums.getArtists();
        for(ArtistSimplified artistSimplified : artistOfAlbum){
            artistOfAlbumList.add(artistSimplified.getName());
            artistOfAlbumList.add(artistSimplified.getExternalUrls());
        }
        albumsList.add(new AlbumModel(nameAlbum, artistOfAlbumList, imageAlbum, externalUrlAlbum));

        list.add(new TrackModel(name, externalUrls, artistsList, albumsList));

        Map<String, Object> map = new HashMap<>();
        map.put("Track", list);
        return map;
    }

    //http://localhost:8080/api/browse/tracks?ids=01iyCAUm8EvOFqVWYJ3dVX
    @GetMapping("/tracks")
    public Map<String, Object> getSeveralTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        Track[] response = spotifyConnect.getSpotifyApi().getSeveralTracks(ids).build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(Track track : response){
            String name = track.getName();
            ExternalUrl externalUrls = track.getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getArtists();
            for(ArtistSimplified artist : artists){
                artistsList.add(artist.getName());
            }

            List<AlbumModel> albumsList = new ArrayList<>();
            AlbumSimplified albums = track.getAlbum();
            String nameAlbum = albums.getName();
            Image[] imageAlbum = albums.getImages();
            ExternalUrl externalUrlAlbum = albums.getExternalUrls();
            List<Object> artistOfAlbumList = new ArrayList<>();
            ArtistSimplified[] artistOfAlbum = albums.getArtists();
            for(ArtistSimplified artistSimplified : artistOfAlbum){
                artistOfAlbumList.add(artistSimplified.getName());
                artistOfAlbumList.add(artistSimplified.getExternalUrls());
            }
            albumsList.add(new AlbumModel(nameAlbum, artistOfAlbumList, imageAlbum, externalUrlAlbum));

            list.add(new TrackModel(name, externalUrls, artistsList, albumsList));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Track", list);
        return map;
    }

    @GetMapping("/genre-seeds")
    public String[] getGenreSeeds() throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getAvailableGenreSeeds().build().execute();
    }

    //http://localhost:8080/api/browse/search?name=abba&type=artist
    @GetMapping("/search")
    public Map<String, Object> searchItems(@RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        SearchResult searchResult = spotifyConnect.getSpotifyApi().searchItem(name, "album,track,playlist").build().execute();
        AlbumSimplified[] albums = searchResult.getAlbums().getItems();

        //using streams
        List<AlbumModel> streamAlbum = Arrays.stream(albums)
                .map(i -> new AlbumModel(i.getName(),
                        Arrays.stream(i.getArtists())
                .map(ArtistSimplified::getName)
                .collect(Collectors.toList()), i.getImages(), i.getExternalUrls()))
                .collect(Collectors.toList());


        //for loop
        List<AlbumModel> newList = new ArrayList<>();
        for(AlbumSimplified i: albums){
            List<Object> artists = new ArrayList<>();
            ArtistSimplified[] artistArray = i.getArtists();
            for(ArtistSimplified artistSimplified : artistArray){
                artists.add(artistSimplified.getName());
                artists.add(artistSimplified.getExternalUrls());
            }
            newList.add(new AlbumModel(i.getName(), artists, i.getImages(), i.getExternalUrls()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Albums", streamAlbum);
        return map;
    }
}
