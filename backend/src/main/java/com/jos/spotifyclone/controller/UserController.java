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

@RequestMapping("api/users")
@RestController
public class UserController {

    @Autowired
    SpotifyConnect spotifyConnect;

    @GetMapping("/me")
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

    //http://localhost:8080/api/users/profile?user_ids=provalio
    @GetMapping("/profile")
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

    @GetMapping("/playlists")
    public Map<String,Object> playlistsOfCurrentUser(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                                     @RequestParam(required = false, defaultValue = "0") Integer offset) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getListOfCurrentUsersPlaylists()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .build().execute();
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

    /**
     * type:
     * Required. The ID type: currently only artist is supported.
     *
     * @param limit
     * Optional.
     * The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     *
     * @param after
     * Optional. The last artist ID retrieved from the previous request.
     */
    @GetMapping("/following")
    public Map<String,Object> followedArtists(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                              @RequestParam(required = false) String after) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersFollowedArtists(ModelObjectType.ARTIST)
                .setQueryParameter("limit", limit)
                .build().execute();

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

    /**
     * http://localhost:8080/api/users/check?user_ids=1l0mKo96Jh9HVYONcRl3Yp
     *
     * @param user_ids
     * Required. A comma-separated list of the artist or the user Spotify IDs to check.
     * For example: ids=74ASZWbe4lXaubB36ztrGX,08td7MxkoHQkXnWAYD8d6Q.
     * A maximum of 50 IDs can be sent in one request.
     *
     * @param type
     * Required.
     * The ID type: either artist or user.
     */
    @GetMapping("/check")
    public String checkFollowArtistOrUser(@RequestParam String[] user_ids, @RequestParam String type) throws ParseException, SpotifyWebApiException, IOException {
        if("artist".equalsIgnoreCase(type)){
            Boolean[] response = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(ModelObjectType.ARTIST,user_ids).build().execute();
            for (Boolean b : response){
                if(b){
                    //regex removes square brackets with any content between them
                    return "You are already following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
                }
            }
        }

        return "You are not following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
    }

    /**
     * http://localhost:8080/api/users/follow?user_ids=1l0mKo96Jh9HVYONcRl3Yp
     *
     * @param user_ids
     * Optional. A comma-separated list of the artist or the user Spotify IDs.
     * For example: ids=74ASZWbe4lXaubB36ztrGX,08td7MxkoHQkXnWAYD8d6Q.
     * A maximum of 50 IDs can be sent in one request.
     *
     * @param type
     * Required.
     * The ID type: either artist or user.
     */
    @GetMapping("/follow")
    public String followArtistOrUser(@RequestParam String[] user_ids, @RequestParam String type) throws ParseException, SpotifyWebApiException, IOException {
        if("artist".equalsIgnoreCase(type)){
            String response = spotifyConnect.getSpotifyApi().followArtistsOrUsers(ModelObjectType.ARTIST,user_ids).build().execute();
            Boolean[] responseCheck = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(ModelObjectType.ARTIST,user_ids).build().execute();
            for (Boolean b : responseCheck){
                if(b){
                    //regex removes square brackets with any content between them
                    return "Success! You are now following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
                }
            }
        }

        if("user".equalsIgnoreCase(type)){
            String response = spotifyConnect.getSpotifyApi().followArtistsOrUsers(ModelObjectType.USER,user_ids).build().execute();
            Boolean[] responseCheck = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(ModelObjectType.USER,user_ids).build().execute();
            for (Boolean b : responseCheck){
                if(b){
                    //regex removes square brackets with any content between them
                    return "Success! You are now following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
                }
            }
        }

        return "You are not following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
    }

    /**
     * http://localhost:8080/api/users/unfollow?user_ids=1l0mKo96Jh9HVYONcRl3Yp
     *
     * @param user_ids
     * Optional. A comma-separated list of the artist or the user Spotify IDs.
     * For example: ids=74ASZWbe4lXaubB36ztrGX,08td7MxkoHQkXnWAYD8d6Q.
     * A maximum of 50 IDs can be sent in one request.
     *
     * @param type
     * Required.
     * The ID type: either artist or user.
     */
    @GetMapping("/unfollow")
    public String unfollowArtistOrUser(@RequestParam String[] user_ids, @RequestParam String type) throws ParseException, SpotifyWebApiException, IOException {
        if("artist".equalsIgnoreCase(type)){
            String response = spotifyConnect.getSpotifyApi().unfollowArtistsOrUsers(ModelObjectType.ARTIST,user_ids).build().execute();
            Boolean[] responseCheck = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(ModelObjectType.ARTIST,user_ids).build().execute();
            for (Boolean b : responseCheck){
                if(b){
                    //regex removes square brackets with any content between them
                    return "You are following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
                }
            }
        }

        if("user".equalsIgnoreCase(type)){
            String response = spotifyConnect.getSpotifyApi().unfollowArtistsOrUsers(ModelObjectType.USER,user_ids).build().execute();
            Boolean[] responseCheck = spotifyConnect.getSpotifyApi().checkCurrentUserFollowsArtistsOrUsers(ModelObjectType.USER,user_ids).build().execute();
            for (Boolean b : responseCheck){
                if(b){
                    //regex removes square brackets with any content between them
                    return "You are following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1");
                }
            }
        }
        return "You are not following " + Arrays.toString(user_ids).replaceAll("\\[(.*?)\\]", "$1") + " anymore.";
    }

    /**
     * http://localhost:8080/api/users/check/playlist?ownerId=abbaspotify&playlistId=3AGOiaoRXMSjswCLtuNqv5&user_ids=abbaspotify
     *
     * @param ownerId
     * The id of the owner of the playlist.
     *
     * @param playlistId
     * The Spotify ID of the playlist.
     *
     * @param user_ids
     * Required. A comma-separated list of Spotify User IDs ;
     * the ids of the users that you want to check to see if they follow the playlist.
     * Maximum: 5 ids.
     */
    @GetMapping("/check/playlist")
    public String checkUsersFollowPlaylist(@RequestParam String ownerId,
                                           @RequestParam String playlistId,
                                           @RequestParam String[] user_ids) throws ParseException, SpotifyWebApiException, IOException {
        Boolean[] response = spotifyConnect.getSpotifyApi().checkUsersFollowPlaylist(ownerId, playlistId, user_ids).build().execute();
        for (Boolean b : response){
            if(b){
                return "The users are following " + playlistId.replaceAll("\\[(.*?)\\]", "$1");
            }
        }
        return "The users are not following " + playlistId.replaceAll("\\[(.*?)\\]", "$1");
    }

    /**
     * http://localhost:8080/api/users/follow/playlist?playlistId=3AGOiaoRXMSjswCLtuNqv5
     *
     * @param playlistId
     * The Spotify ID of the playlist.
     * Any playlist can be followed, regardless of its public/private status, as long as you know its playlist ID.
     *
     * @param public_
     * Optional.
     * Defaults to true.
     * If true the playlist will be included in user’s public playlists, if false it will remain private.
     * To be able to follow playlists privately, the user must have granted the playlist-modify-private scope.
     */
    @GetMapping("/follow/playlist")
    public String followPlaylist(@RequestParam String playlistId, @RequestParam(required = false, defaultValue = "true") boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().followPlaylist(playlistId, public_).build().execute();
        return "Success! You are now following the " + playlistId.replaceAll("\\[(.*?)\\]", "$1") + " playlist.";
    }

    /**
     * http://localhost:8080/api/users/unfollow/playlist?ownerId=abbaspotify&playlistId=3AGOiaoRXMSjswCLtuNqv5
     *
     * @param ownerId
     * The id of the owner of the playlist.
     * @param playlistId
     * The Spotify ID of the playlist that is to be no longer followed.
     */
    @GetMapping("/unfollow/playlist")
    public String unfollowPlaylist(String ownerId, @RequestParam String playlistId) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().unfollowPlaylist(ownerId, playlistId).build().execute();
        return "You are not following " + playlistId.replaceAll("\\[(.*?)\\]", "$1")+ " anymore.";
    }

    /**
     *
     * @param limit
     * Optional.
     * The maximum number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     *
     * @param offset
     * Optional.
     * The index of the first object to return.
     * Default: 0 (i.e., the first object). Use with limit to get the next set of objects.
     *
     * @param market
     * Optional.
     * An ISO 3166-1 alpha-2 country code or the string from_token.
     */
    @GetMapping("/saved/albums")
    public Map<String, Object> savedAlbums(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                           @RequestParam(required = false, defaultValue = "0") Integer offset,
                                           @RequestParam(required = false, defaultValue = "US") String market) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getCurrentUsersSavedAlbums()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .setQueryParameter("market", market)
                .build().execute();

        List<AlbumModel> list = new ArrayList<>();
        for(SavedAlbum album : response.getItems()){
            String name = album.getAlbum().getName();
            Image[] image = album.getAlbum().getImages();
            ExternalUrl externalUrls = album.getAlbum().getExternalUrls();

            List<Object> artists = new ArrayList<>();
            ArtistSimplified[] artistArray = album.getAlbum().getArtists();
            for(ArtistSimplified artistSimplified : artistArray){
                artists.add(artistSimplified.getName());
                artists.add(artistSimplified.getExternalUrls());
            }
            list.add(new AlbumModel(name, artists, image, externalUrls));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Saved albums ", list);
        return map;
    }

    /**
     *
     * @param limit
     * Optional.
     * The maximum number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     *
     * @param offset
     * Optional.
     * The index of the first object to return.
     * Default: 0 (i.e., the first object). Use with limit to get the next set of objects.
     */
    @GetMapping("/saved/shows")
    public Map<String, Object> savedShows(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                          @RequestParam(required = false, defaultValue = "0") Integer offset) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersSavedShows()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .build().execute();

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

    /**
     *
     * @param limit
     * Optional.
     * The maximum number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     *
     * @param offset
     * Optional.
     * The index of the first object to return.
     * Default: 0 (i.e., the first object). Use with limit to get the next set of objects.
     *
     * @param market
     * Optional.
     * An ISO 3166-1 alpha-2 country code or the string from_token.
     */
    @GetMapping("/saved/tracks")
    public Map<String, Object> savedTracks(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                           @RequestParam(required = false, defaultValue = "0") Integer offset,
                                           @RequestParam(required = false, defaultValue = "US") String market) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersSavedTracks()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .setQueryParameter("market", market)
                .build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(SavedTrack track : response.getItems()){
            String name = track.getTrack().getName();
            ExternalUrl externalUrls = track.getTrack().getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getTrack().getArtists();
            for(ArtistSimplified artistSimplified : artists){
                artistsList.add(artistSimplified.getName());
                artistsList.add(artistSimplified.getExternalUrls());
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

    /**
     * run http://localhost:8080/api/users/saved/albums/ first to find id's
     * http://localhost:8080/api/users/remove/albums?ids=<replace with albums id>
     *
     * @param ids
     * Optional.
     * A comma-separated list of the Spotify IDs. For example: ids=4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M.
     * Maximum: 50 IDs.
     */
    @GetMapping("/remove/albums")
    public String removeAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().removeAlbumsForCurrentUser(ids).build().execute();
        return "Success! Album/s with id/s " + Arrays.toString(ids) + " was/were deleted.";
    }

    /**
     * run http://localhost:8080/api/users/saved/shows/ first to find id's
     * http://localhost:8080/api/users/remove/shows?ids=<replace with shows id>
     *
     * @param ids
     * Required.
     * A comma-separated list of Spotify IDs for the shows to be deleted from the user’s library.
     *
     * @param market
     * Optional.
     * An ISO 3166-1 alpha-2 country code. If a country code is specified, only shows that are available in that market will be removed.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     */
    @GetMapping("/remove/shows")
    public String removeShows(@RequestParam String[] ids, @RequestParam(required = false, defaultValue = "US") String market) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().removeUsersSavedShows(ids)
                .setQueryParameter("market", market)
                .build().execute();
        return "Success! Show/s with id/s " + Arrays.toString(ids) + " was/were deleted.";
    }

    /**
     * run http://localhost:8080/api/users/saved/tracks/ first to find id's
     * http://localhost:8080/api/users/remove/tracks?ids=<replace with tracks id>
     *
     * @param ids
     * Optional.
     * A comma-separated list of the Spotify IDs.
     * For example: ids=4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M.
     * Maximum: 50 IDs.
     */
    @GetMapping("/remove/tracks")
    public String removeTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().removeUsersSavedTracks(ids).build().execute();
        return "Success! Track/s with id/s " + ids + "was/were deleted.";
    }

    /**
     * http://localhost:8080/api/search/album?id=arianagrande
     * http://localhost:8080/api/users/save/albums?ids=<replace with albums id>
     *
     * @param ids
     * Optional.
     * A comma-separated list of the Spotify IDs.
     * For example: ids=4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M.
     * Maximum: 50 IDs.
     */
    @GetMapping("/save/albums")
    public String saveAlbums(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().saveAlbumsForCurrentUser(ids).build().execute();
        return "Success! Album/s with id/s " + ids + "was/were saved.";
    }

    /**
     * http://localhost:8080/api/search/show?id=bieber
     * http://localhost:8080/api/users/save/shows?ids=<replace with shows id>
     *
     * @param ids
     * Required.
     * A comma-separated list of the Spotify IDs.
     * Maximum: 50 IDs.
     */
    @GetMapping("/save/shows")
    public String saveShows(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().saveShowsForCurrentUser(ids).build().execute();
        return "Success! Show/s with id/s " + ids + "was/were saved.";
    }

    /**
     * http://localhost:8080/api/search/track?id=positions
     * http://localhost:8080/api/users/save/tracks?ids=<replace with tracks id>
     * @param ids
     * Optional.
     * A comma-separated list of the Spotify IDs.
     * For example: ids=4iV5W9uYEdYUVa79Axb7Rh,1301WleyT98MSxVHPZCA6M.
     * Maximum: 50 IDs.
     */
    @GetMapping("/save/tracks")
    public String saveTracks(@RequestParam String[] ids) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().saveTracksForUser(ids).build().execute();
        return "Success! Track/s with id/s " + ids + "was/were saved.";
    }

    /**
     * http://localhost:8080/api/users/check/albums/?ids=66CXWjxzNUsdJxJ2JdwvnR
     * @param ids
     * Required.
     * A comma-separated list of the Spotify IDs for the albums.
     * Maximum: 50 IDs.
     */
    @GetMapping("/check/albums")
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

    /**
     * http://localhost:8080/api/users/check/shows/?ids=0yGFanYUflGtxAN23HQLY2
     *
     * @param ids
     * Required.
     * A comma-separated list of the Spotify IDs for the shows.
     * Maximum: 50 IDs.
     */
    @GetMapping("/check/shows")
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

    /**
     * http://localhost:8080/api/users/check/tracks/?ids=66CXWjxzNUsdJxJ2JdwvnR
     *
     * @param ids
     * Required.
     * A comma-separated list of the Spotify IDs for the tracks.
     * Maximum: 50 IDs.
     */
    @GetMapping("/check/tracks")
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

    /**
     *
     * @param limit
     * Optional.
     * The number of entities to return. Default: 20. Minimum: 1. Maximum: 50. For example: limit=2
     *
     * @param offset
     * Optional.
     * The index of the first entity to return. Default: 0 (i.e., the first track). Use with limit to get the next set of entities.
     *
     * @param time_range
     * Optional.
     * Over what time frame the affinities are computed.
     * Valid values: long_term (calculated from several years of data and including all new data as it becomes available),
     * medium_term (approximately last 6 months), short_term (approximately last 4 weeks).
     * Default: medium_term.
     */
    @GetMapping("/top/artists")
    public Map<String, Object> getTopArtists(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                             @RequestParam(required = false, defaultValue = "0") Integer offset,
                                             @RequestParam(required = false, defaultValue = "medium_term") String time_range) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersTopArtists()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .setQueryParameter("time_range", time_range)
                .build().execute();

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

    /**
     *
     * @param limit
     * Optional.
     * The number of entities to return. Default: 20. Minimum: 1. Maximum: 50. For example: limit=2
     *
     * @param offset
     * Optional.
     * The index of the first entity to return. Default: 0 (i.e., the first track). Use with limit to get the next set of entities.
     *
     * @param time_range
     * Optional.
     * Over what time frame the affinities are computed.
     * Valid values: long_term (calculated from several years of data and including all new data as it becomes available),
     * medium_term (approximately last 6 months), short_term (approximately last 4 weeks).
     * Default: medium_term.
     */
    @GetMapping("/top/tracks")
    public Map<String, Object> getTopTracks(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                            @RequestParam(required = false, defaultValue = "0") Integer offset,
                                            @RequestParam(required = false, defaultValue = "medium_term") String time_range) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getUsersTopTracks()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .setQueryParameter("time_range", time_range)
                .build().execute();

        List<TrackModel> list = new ArrayList<>();
        for(Track track : response.getItems()){
            String name = track.getName();
            ExternalUrl externalUrls = track.getExternalUrls();

            List<Object> artistsList = new ArrayList<>();
            ArtistSimplified[] artists = track.getArtists();
            for(ArtistSimplified artistSimplified : artists){
                artistsList.add(artistSimplified.getName());
                artistsList.add(artistSimplified.getExternalUrls());
            }

            List<AlbumModel> albumsList = new ArrayList<>();
            AlbumSimplified albums = track.getAlbum();
            String nameAlbum = albums.getName();
            Image[] imageAlbum = albums.getImages();
            ExternalUrl externalUrlAlbum = albums.getExternalUrls();
            List<Object> artistsOfAlbumList = new ArrayList<>();
            ArtistSimplified[] artistsOfAlbum = albums.getArtists();
            for(ArtistSimplified artistSimplified : artistsOfAlbum){
                artistsOfAlbumList.add(artistSimplified.getName());
                artistsOfAlbumList.add(artistSimplified.getExternalUrls());
            }
            albumsList.add(new AlbumModel(nameAlbum, artistsOfAlbumList, imageAlbum, externalUrlAlbum));

            list.add(new TrackModel(name, externalUrls, artistsList, albumsList));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Top tracks ", list);
        return map;
    }

    /**
     * TODO wait for update so you can get the items from getItems since its available now
     *
     * @param limit
     * Optional.
     * The number of entities to return. Default: 20. Minimum: 1. Maximum: 50. For example: limit=2
     *
     * @param offset
     * Optional.
     * The index of the first entity to return. Default: 0 (i.e., the first track). Use with limit to get the next set of entities.
     *
     * @param time_range
     * Optional.
     * Over what time frame the affinities are computed.
     * Valid values: long_term (calculated from several years of data and including all new data as it becomes available),
     * medium_term (approximately last 6 months), short_term (approximately last 4 weeks).
     * Default: medium_term.
     */
    @GetMapping("/top")
    public Map<String, Object> getTopArtistsAndTracks(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                                      @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                      @RequestParam(required = false, defaultValue = "medium_term") String time_range) throws ParseException, SpotifyWebApiException, IOException {
        var responseArtist = spotifyConnect.getSpotifyApi().getUsersTopArtists()
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .setQueryParameter("time_range", time_range)
                .build().execute();
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

            List<Object> artistsList = new ArrayList<>();
            ArtistSimplified[] artistsTracks = track.getArtists();
            for(ArtistSimplified artistSimplified : artistsTracks){
                artistsList.add(artistSimplified.getName());
                artistsList.add(artistSimplified.getExternalUrls());
            }

            List<AlbumModel> albumsList = new ArrayList<>();
            AlbumSimplified albums = track.getAlbum();
            String nameAlbum = albums.getName();
            Image[] imageAlbum = albums.getImages();
            ExternalUrl externalUrlAlbum = albums.getExternalUrls();
            List<Object> artistsOfAlbumList = new ArrayList<>();
            ArtistSimplified[] artistsOfAlbum = albums.getArtists();
            for(ArtistSimplified artistSimplified : artistsOfAlbum){
                artistsOfAlbumList.add(artistSimplified.getName());
                artistsOfAlbumList.add(artistSimplified.getExternalUrls());
            }
            albumsList.add(new AlbumModel(nameAlbum, artistsOfAlbumList, imageAlbum, externalUrlAlbum));

            trackModels.add(new TrackModel(name, externalUrls, artistsList, albumsList));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Top artists", artistModels);
        map.put("Top tracks", trackModels);
        return map;
    }

    /**
     * http://localhost:8080/api/user/playlists/users?user_id=hrn1isdy2ia8q7wfb1ew2fah6
     *
     * @param user_id
     * Required.
     * The user’s Spotify user ID.
     *
     * @param limit
     * Optional.
     * The maximum number of playlists to return. Default: 20. Minimum: 1. Maximum: 50.
     *
     * @param offset
     * Optional.
     * The index of the first playlist to return. Default: 0 (the first object).
     * Maximum offset: 100.000. Use with limit to get the next set of playlists.
     */
    @GetMapping("/playlists/user")
    public Map<String, Object> getListOfAnotherUserPlaylists(@RequestParam String user_id,
                                                             @RequestParam(required = false, defaultValue = "20") Integer limit,
                                                             @RequestParam(required = false, defaultValue = "0") Integer offset) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getListOfUsersPlaylists(user_id)
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .build().execute();

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


