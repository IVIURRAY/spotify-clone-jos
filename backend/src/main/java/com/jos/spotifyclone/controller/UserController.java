package com.jos.spotifyclone.controller;


import com.jos.spotifyclone.model.*;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RequestMapping("api/user")
@RestController
public class UserController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/profile")
    public Map<String,Object> currentUserProfile() throws ParseException, SpotifyWebApiException, IOException {
        User response = spotifyConnect.getSpotifyApi().getCurrentUsersProfile().build().execute();
        String displayName = response.getDisplayName();
        String birthdate = response.getBirthdate();
        CountryCode country = response.getCountry();
        Image[] profilePicture = response.getImages();

        Map<String,Object> map = new HashMap<>();
        map.put("Display name", displayName);
        map.put("Birthdate", birthdate);
        map.put("Country", country);
        map.put("Profile picture", profilePicture);

        return map;

    }

    //http://localhost:8080/api/user/another-user?user_ids=provalio
    @GetMapping("/another-user")
    public Map<String,Object> anotherUserProfile(@RequestParam String user_ids) throws ParseException, SpotifyWebApiException, IOException {
        User response = spotifyConnect.getSpotifyApi().getUsersProfile(user_ids).build().execute();
        String displayName = response.getDisplayName();
        String birthdate = response.getBirthdate();
        CountryCode country = response.getCountry();
        Image[] profilePicture = response.getImages();

        Map<String,Object> map = new HashMap<>();
        map.put("Display name", displayName);
        map.put("Birthdate", birthdate);
        map.put("Country", country);
        map.put("Profile picture", profilePicture);

        return map;
    }

    @GetMapping("/playlist")
    public Map<String,Object> playlistsOfCurrentUser() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute();
        String href = response.getHref();

        List<PlaylistModel> list = new ArrayList<>();
        for (PlaylistSimplified playlist : response.getItems()){
            ExternalUrl externalUrls = playlist.getExternalUrls();
            String playlistName = playlist.getName();
            PlaylistTracksInformation tracks = playlist.getTracks();
            Image[] playlistCover = playlist.getImages();

            list.add(new PlaylistModel(href, externalUrls, playlistName, tracks, playlistCover));
        }

        Map<String,Object> map = new HashMap<>();
        map.put("User playlists", list);

        return map;
    }

    @GetMapping("/followed-artists")
    public Map<String,Object> followedArtists() throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        var response = spotifyConnect.getSpotifyApi().getUsersFollowedArtists(type).build().execute();

        List<ArtistModel> list = new ArrayList<>();
        for (Artist artist : response.getItems()){
            ExternalUrl externalUrl = artist.getExternalUrls();
            Followers followers = artist.getFollowers();
            String[] genres = artist.getGenres();
            Image[] images = artist.getImages();
            String artistName = artist.getName();

        list.add(new ArtistModel(externalUrl, followers, genres, images, artistName));
        }

        Map<String,Object> map = new HashMap<>();
        map.put("Followed artists", list);

        return map;
    }

    //http://localhost:8080/api/user/check-follow-artist-user?user_ids=1l0mKo96Jh9HVYONcRl3Yp
    @GetMapping("/check-follow-artist-user")
    public String checkFollowArtistOrUser(@RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        Boolean[] response = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(type,user_ids).build().execute();
        for (Boolean b : response){
            if(b){
                //regex removes square brackets with any content between them
                return "You are already following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
            }
        }
        return "You are not following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
    }

    //http://localhost:8080/api/user/follow-artist-user?user_ids=1l0mKo96Jh9HVYONcRl3Yp
    @GetMapping("/follow-artist-user")
    public String followArtistOrUser(@RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        String response = spotifyConnect.getSpotifyApi().followArtistsOrUsers(type,user_ids).build().execute();
        Boolean[] responseCheck = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(type,user_ids).build().execute();
        for (Boolean b : responseCheck){
            if(b){
                //regex removes square brackets with any content between them
                return "Success! You are now following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
            }
        }
        return "You are not following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
    }

    //http://localhost:8080/api/user/unfollow-artist-user?user_ids=1l0mKo96Jh9HVYONcRl3Yp
    @GetMapping("/unfollow-artist-user")
    public String unfollowArtistOrUser(@RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        final ModelObjectType type = ModelObjectType.ARTIST;
        String response = spotifyConnect.getSpotifyApi().unfollowArtistsOrUsers(type,user_ids).build().execute();
        Boolean[] responseCheck = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(type,user_ids).build().execute();
        for (Boolean b : responseCheck){
            if(b){
                //regex removes square brackets with any content between them
                return "Success! You are now following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
            }
        }
        return "You are not following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1") + " anymore.";
    }

    //http://localhost:8080/api/user/check-follow-playlist?ownerId=abbaspotify&playlistId=3AGOiaoRXMSjswCLtuNqv5&user_ids=abbaspotify
    @GetMapping("/check-follow-playlist")
    public String checkUsersFollowPlaylist(@RequestParam String ownerId, @RequestParam String playlistId, @RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        Boolean[] response = spotifyConnect.getSpotifyApi().checkUsersFollowPlaylist(ownerId, playlistId, user_ids).build().execute();
        for (Boolean b : response){
            if(b){
                return "The users are following " + playlistId.replaceAll("\\[(.*?)\\]", "$1");
            }
        }
        return "The users are not following " + playlistId.replaceAll("\\[(.*?)\\]", "$1");
    }

    //http://localhost:8080/api/user/follow-playlist?playlistId=3AGOiaoRXMSjswCLtuNqv5
    @GetMapping("/follow-playlist")
    public String followPlaylist(@RequestParam String playlistId, @RequestParam(required = false) boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().followPlaylist(playlistId, public_).build().execute();
        return "Success! You are now following the " + playlistId + " playlist.";
    }

    //http://localhost:8080/api/user/unfollow-playlist?ownerId=abbaspotify&playlistId=3AGOiaoRXMSjswCLtuNqv5
    @GetMapping("/unfollow-playlist")
    public String unfollowPlaylist(String ownerId, @RequestParam String playlistId) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().unfollowPlaylist(ownerId, playlistId).build().execute();
        return "You are not following " + playlistId + " anymore.";
    }

    @GetMapping("/saved-albums")
    public Map<String, Object> savedAlbums() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getCurrentUsersSavedAlbums().build().execute();

        List<AlbumModel> list = new ArrayList<>();
        for(SavedAlbum album : response.getItems()){
            String name = album.getAlbum().getName();
            Image[] image = album.getAlbum().getImages();
            ExternalUrl externalUrls = album.getAlbum().getExternalUrls();

            List<String> artists = new ArrayList<>();
            ArtistSimplified[] artistArray = album.getAlbum().getArtists();
            for(ArtistSimplified artistSimplified : artistArray){
                artists.add(artistSimplified.getName());
            }
            list.add(new AlbumModel(name, artists, image, externalUrls));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Saved albums ", list);
        return map;
    }

    @GetMapping("/saved-shows")
    public Map<String, Object> savedShows() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersSavedShows().build().execute();

        List<ShowModel> list = new ArrayList<>();
        for(SavedShow show : response.getItems()){
            String description = show.getShow().getDescription();
            String name = show.getShow().getName();
            ExternalUrl externalUrls = show.getShow().getExternalUrls();

            list.add(new ShowModel(description, name, externalUrls));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Saved shows ", list);
        return map;
    }

    @GetMapping("/saved-tracks")
    public Map<String, Object> savedTracks() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersSavedTracks().build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(SavedTrack track : response.getItems()){
            String name = track.getTrack().getName();
            ExternalUrl externalUrls = track.getTrack().getExternalUrls();

            List<String> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getTrack().getArtists();
            for(ArtistSimplified artistSimplified : artists){
                artistsList.add(artistSimplified.getName());
            }

            List<AlbumModel> albumList  = new ArrayList<>();
            String albumName = track.getTrack().getAlbum().getName();
            Image[] image = track.getTrack().getAlbum().getImages();
            ExternalUrl externalUrlsAlbum = track.getTrack().getAlbum().getExternalUrls();

            albumList.add(new AlbumModel(albumName, artistsList, image, externalUrlsAlbum));

            list.add(new TrackModel(name, externalUrls, artistsList, albumList));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Saved tracks ", list);
        return map;
    }

    //run http://localhost:8080/api/user/saved-albums/ first to find id's
    //http://localhost:8080/api/user/remove-albums?ids=<replace with albums id>
    @GetMapping("/remove-albums")
    public String removeAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().removeAlbumsForCurrentUser(ids).build().execute();
        return "Success! Album/s with id/s " + ids + "was/were deleted.";
    }

    //run http://localhost:8080/api/user/saved-shows/ first to find id's
    //http://localhost:8080/api/user/remove-shows?ids=<replace with shows id>
    @GetMapping("/remove-shows")
    public String removeShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().removeUsersSavedShows(ids).build().execute();
        return "Success! Show/s with id/s " + ids + "was/were deleted.";
    }

    //run http://localhost:8080/api/user/saved-tracks/ first to find id's
    //http://localhost:8080/api/user/remove-tracks?ids=<replace with tracks id>
    @GetMapping("/remove-tracks")
    public String removeTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().removeUsersSavedTracks(ids).build().execute();
        return "Success! Track/s with id/s " + ids + "was/were deleted.";
    }

    //http://localhost:8080/api/search/album?id=arianagrande
    //http://localhost:8080/api/user/save-albums?ids=<replace with albums id>
    @GetMapping("/save-albums")
    public String saveAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().saveAlbumsForCurrentUser(ids).build().execute();
        return "Success! Album/s with id/s " + ids + "was/were saved.";
    }

    //http://localhost:8080/api/search/show?id=bieber
    //http://localhost:8080/api/user/save-shows?ids=<replace with shows id>
    @GetMapping("/save-shows")
    public String saveShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().saveShowsForCurrentUser(ids).build().execute();
        return "Success! Show/s with id/s " + ids + "was/were saved.";
    }

    //http://localhost:8080/api/search/track?id=positions
    //http://localhost:8080/api/user/save-tracks?ids=<replace with tracks id>
    @GetMapping("/save-tracks")
    public String saveTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().saveTracksForUser(ids).build().execute();
        return "Success! Track/s with id/s " + ids + "was/were saved.";
    }

    //http://localhost:8080/api/user/check-saved-albums/?ids=66CXWjxzNUsdJxJ2JdwvnR
    @GetMapping("/check-saved-albums")
    public String checkSavedAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        Boolean[] response = spotifyConnect.getSpotifyApi().checkUsersSavedAlbums(ids).build().execute();
        for (Boolean b : response){
            if(b){
                //regex removes square brackets with any content between them
                return "You already have the " + Arrays.toString(ids).replaceAll("\\[(.*?)\\]", "$1" + " albums saved.");
            }
        }
        return "You didn't save the " + Arrays.toString(ids).replaceAll("\\[(.*?)\\]", "$1" + " albums.");
    }

    //http://localhost:8080/api/user/check-saved-shows/?ids=0yGFanYUflGtxAN23HQLY2
    @GetMapping("/check-saved-shows")
    public String checkSavedShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        Boolean[] response = spotifyConnect.getSpotifyApi().checkUsersSavedShows(ids).build().execute();
        for (Boolean b : response){
            if(b){
                //regex removes square brackets with any content between them
                return "You already have the " + Arrays.toString(ids).replaceAll("\\[(.*?)\\]", "$1" + " shows saved.");
            }
        }
        return "You didn't save the " + Arrays.toString(ids).replaceAll("\\[(.*?)\\]", "$1" + " shows.");

    }

    //http://localhost:8080/api/user/check-saved-tracks/?ids=66CXWjxzNUsdJxJ2JdwvnR
    @GetMapping("/check-saved-tracks")
    public String checkSavedTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        Boolean[] response = spotifyConnect.getSpotifyApi().checkUsersSavedTracks(ids).build().execute();
        for (Boolean b : response){
            if(b){
                //regex removes square brackets with any content between them
                return "You already have the " + Arrays.toString(ids).replaceAll("\\[(.*?)\\]", "$1" + " tracks saved.");
            }
        }
        return "You didn't save the " + Arrays.toString(ids).replaceAll("\\[(.*?)\\]", "$1" + " tracks.");
    }

    @GetMapping("/top-artists")
    public Map<String, Object> getTopArtists() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersTopArtists().build().execute();

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
        map.put("Top artists ", list);
        return map;
    }


    @GetMapping("/top-tracks")
    public Map<String, Object> getTopTracks() throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersTopTracks().build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(Track track : response.getItems()){
            String name = track.getName();
            ExternalUrl externalUrls = track.getExternalUrls();

            List<String> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getArtists();
            for(ArtistSimplified artistSimplified : artists){
                artistsList.add(artistSimplified.getName());
            }

            List<AlbumModel> albumsList = new ArrayList<>();
            AlbumSimplified albums = track.getAlbum();
            String nameAlbum = albums.getName();
            Image[] imageAlbum = albums.getImages();
            ExternalUrl externalUrlAlbum = albums.getExternalUrls();
            List<String> artistsOfAlbumList = new ArrayList<>();
            ArtistSimplified[] artistsOfAlbum = albums.getArtists();
            for(ArtistSimplified artistSimplified : artistsOfAlbum){
                artistsOfAlbumList.add(artistSimplified.getName());
            }
            albumsList.add(new AlbumModel(nameAlbum, artistsOfAlbumList, imageAlbum, externalUrlAlbum));

            list.add(new TrackModel(name, externalUrls, artistsList, albumsList));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Top tracks ", list);
        return map;
    }

    //TODO fix so it works with getUsersTopArtistsAndTracks method
    @GetMapping("/top-artists-and-tracks")
    public Map<String, Object> getTopArtistsAndTracks() throws ParseException, SpotifyWebApiException, IOException {
        var responseArtist = spotifyConnect.getSpotifyApi().getUsersTopArtists().build().execute();
        List<ArtistModel> artistModels = new ArrayList<>();
        for(Artist artist : responseArtist.getItems()){
            ExternalUrl externalUrl = artist.getExternalUrls();
            Followers followers = artist.getFollowers();
            String[] genres = artist.getGenres();
            Image[] images = artist.getImages();
            String artistName = artist.getName();

            artistModels.add(new ArtistModel(externalUrl, followers, genres, images, artistName));
        }

        var responseTracks = spotifyConnect.getSpotifyApi().getUsersTopTracks().build().execute();
        List<TrackModel> trackModels = new ArrayList<>();
        for(Track track : responseTracks.getItems()){
            String name = track.getName();
            ExternalUrl externalUrls = track.getExternalUrls();

            List<String> artistsList = new ArrayList<>();
            ArtistSimplified[] artistsTracks = track.getArtists();
            for(ArtistSimplified artistSimplified : artistsTracks){
                artistsList.add(artistSimplified.getName());
            }

            List<AlbumModel> albumsList = new ArrayList<>();
            AlbumSimplified albums = track.getAlbum();
            String nameAlbum = albums.getName();
            Image[] imageAlbum = albums.getImages();
            ExternalUrl externalUrlAlbum = albums.getExternalUrls();
            List<String> artistsOfAlbumList = new ArrayList<>();
            ArtistSimplified[] artistsOfAlbum = albums.getArtists();
            for(ArtistSimplified artistSimplified : artistsOfAlbum){
                artistsOfAlbumList.add(artistSimplified.getName());
            }
            albumsList.add(new AlbumModel(nameAlbum, artistsOfAlbumList, imageAlbum, externalUrlAlbum));

            trackModels.add(new TrackModel(name, externalUrls, artistsList, albumsList));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Top artists", artistModels);
        map.put("Top tracks", trackModels);
        return map;
    }

    //http://localhost:8080/api/user/get-list-of-playlists-from?user_id=hrn1isdy2ia8q7wfb1ew2fah6
    @GetMapping("/get-list-of-playlists-from")
    public Map<String, Object> getListOfAnotherUserPlaylists(@RequestParam String user_id) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getListOfUsersPlaylists(user_id).build().execute();

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
        map.put("Playlists ", list);
        return map;
    }
}


