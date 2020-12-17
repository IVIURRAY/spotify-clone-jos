package com.jos.spotifyclone.controller;

import com.google.gson.JsonArray;
import com.jos.spotifyclone.model.PlaylistModel;
import com.jos.spotifyclone.services.SpotifyConnect;
import com.neovisionaries.i18n.CountryCode;
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
import java.util.*;

@RestController
@RequestMapping("api/playlists")
public class PlaylistsController {

    @Autowired
    SpotifyConnect spotifyConnect;

    //http://localhost:8080/api/playlists/name?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&name=<FUTURE NAME>
    @GetMapping("/name")
    public String changePlaylistName(@RequestParam String playlist_id,@RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).name(name).build().execute();
        return "The name of the " + playlist_id  + " playlist is now " + name + ".";
    }

    //http://localhost:8080/api/playlists/visibility?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&public_=<TRUE/FALSE>
    @GetMapping("/visibility")
    public String changePlaylistVisibility(@RequestParam String playlist_id,@RequestParam boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).public_(public_).build().execute();
        if(public_){
            return "Your " + playlist_id + " playlist is now public.";
        }
        return "Your " + playlist_id + " playlist is not public.";
    }

    //http://localhost:8080/api/playlists/collaborative?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&collaborative=<TRUE/FALSE>
    @GetMapping("/collaborative")
    public String changePlaylistCollab(@RequestParam String playlist_id,@RequestParam boolean collaborative) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).collaborative(collaborative).build().execute();
        if(collaborative){
            return "Your " + playlist_id + " playlist is now collaborative.";
        }
        return "Your " + playlist_id + " playlist is not collaborative.";
    }

    //http://localhost:8080/api/playlists/description?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&description=<FUTURE DESCRIPTION>
    @GetMapping("/description")
    public String changePlaylistDescription(@RequestParam String playlist_id,@RequestParam String description) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).description(description).build().execute();
        return "The description of your " + playlist_id + " has changed.";
    }

    //http://localhost:8080/api/playlists/details?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&name=<FUTURE NAME>&description=<FUTURE DESCRIPTION>&collab=<TRUE/FALSE>&public_=<TRUE/FALSE>
    @GetMapping("/details")
    public String changePlaylist(@RequestParam String playlist_id,@RequestParam String name, @RequestParam String description, @RequestParam boolean collab, @RequestParam boolean public_) throws ParseException, SpotifyWebApiException, IOException {
        String response = spotifyConnect.getSpotifyApi().changePlaylistsDetails(playlist_id).name(name).description(description).collaborative(collab).public_(public_).build().execute();
        return "The playlist with the id " + playlist_id + " has been modified.";
    }

    //http://localhost:8080/api/playlists/new?playlist_id=<THE ID OF YOUR OWN ACCOUNT>&name=<FUTURE NAME>
    @GetMapping("/new")
    public String createPlaylist(@RequestParam String user_id, @RequestParam String name) throws ParseException, SpotifyWebApiException, IOException {
        return "You created a new playlist with the name " + name + ".";
    }

    //https://png-pixel.com/   a small base64 image that wont crash due to too long header
    //http://localhost:8080/api/playlists/custom-cover?playlist_id=<THE ID OF YOUR OWN PLAYLIST>&location=iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8zsDwHwAE5QH4waZ7uQAAAABJRU5ErkJggg==
    @GetMapping("/custom-cover")
    public String uploadCustomPlaylistCover(@RequestParam String playlist_id, @RequestParam String data) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().uploadCustomPlaylistCoverImage(playlist_id).image_data(data).build().execute();
    }

    @GetMapping("/add")
    public SnapshotResult addItemsTo(@RequestParam String playlist_id, @RequestParam String[] uris) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().addItemsToPlaylist(playlist_id,uris).build().execute();
    }

    @GetMapping("/remove")
    public SnapshotResult removeItemsFrom(@RequestParam String playlist_id, @RequestParam JsonArray tracks) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().removeItemsFromPlaylist(playlist_id,tracks).build().execute();
    }

    @GetMapping("/reorder")
    public SnapshotResult reorderItemsFrom(@RequestParam String playlist_id, @RequestParam int range_start, @RequestParam int range_length, @RequestParam int insertBefore) throws ParseException, SpotifyWebApiException, IOException {
        return  spotifyConnect.getSpotifyApi().reorderPlaylistsItems(playlist_id,range_start,insertBefore).build().execute();
    }

    @GetMapping("/replace")
    public String replaceItemsFrom(@RequestParam String playlist_id, @RequestParam String[] uris) throws ParseException, SpotifyWebApiException, IOException {
        return  spotifyConnect.getSpotifyApi().replacePlaylistsItems(playlist_id, uris).build().execute();
    }

    /**
     *
     * @param locale
     * Optional.
     * The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore.
     * For example: es_MX, meaning “Spanish (Mexico)”.
     * Provide this parameter if you want the results returned in a particular language (where available).
     * Note that, if locale is not supplied, or if the specified language is not available,
     * all strings will be returned in the Spotify default language (American English).
     * The locale parameter, combined with the country parameter, may give odd results if not carefully matched.
     * For example country=SE&locale=de_DE will return a list of categories relevant to Sweden but as German language strings.
     *
     * @param country
     * Optional.
     * A country: an ISO 3166-1 alpha-2 country code.
     * Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @param timestamp
     * Optional.
     * A timestamp in ISO 8601 format: yyyy-MM-ddTHH:mm:ss.
     * Use this parameter to specify the user’s local time to get results tailored for that specific date and time in the day.
     * If not provided, the response defaults to the current UTC time.
     * Example: “2014-10-23T09:00:00” for a user whose local time is 9AM.
     * If there were no featured playlists (or there is no data) at the specified time, the response will revert to the current UTC time.
     *
     * @param limit
     * Optional.
     * The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     *
     * @param offset
     * Optional.
     * The index of the first item to return. Default: 0 (the first object).
     * Use with limit to get the next set of items.
     */
    @GetMapping("/featured")
    public Map<String,Object> getListOfFeaturedPlaylists(@RequestParam(required = false, defaultValue = "en_US") String locale,
                                                         @RequestParam(required = false, defaultValue = "US") CountryCode country,
                                                         @RequestParam(required = false) String timestamp,
                                                         @RequestParam(required = false, defaultValue = "20") Integer limit,
                                                         @RequestParam(required = false, defaultValue = "0") Integer offset) throws ParseException, SpotifyWebApiException, IOException {
        FeaturedPlaylists response = spotifyConnect.getSpotifyApi().getListOfFeaturedPlaylists()
                .setQueryParameter("locale", locale)
                .setQueryParameter("country", country)
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .build().execute();

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

    /**
     * http://localhost:8080/api/playlists/playlist?playlist_id=3AGOiaoRXMSjswCLtuNqv5
     *
     * @param playlist_id
     * The Spotify ID for the playlist.
     *
     * @param market
     * Optional. An ISO 3166-1 alpha-2 country code or the string from_token.
     * Provide this parameter if you want to apply Track Relinking.
     * For episodes, if a valid user access token is specified in the request header, the country associated with
     * the user account will take priority over this parameter.
     *
     * @param fields
     * Optional.
     * Filters for the query: a comma-separated list of the fields to return.
     * If omitted, all fields are returned. For example, to get just the total number of items and the request limit:
     * fields=total,limit
     * A dot separator can be used to specify non-reoccurring fields, while parentheses can be used to specify reoccurring fields within objects.
     * For example, to get just the added date and user ID of the adder:
     * fields=items(added_at,added_by.id)
     * Use multiple parentheses to drill down into nested objects, for example:
     * fields=items(track(name,href,album(name,href)))
     * Fields can be excluded by prefixing them with an exclamation mark, for example:
     * fields=items.track.album(!external_urls,images)
     */
    @GetMapping("/playlist")
    public Map<String, Object> getPlaylist(@RequestParam String playlist_id,
                                           @RequestParam(required = false, defaultValue = "US") String market,
                                           @RequestParam(required = false) String fields) throws ParseException, SpotifyWebApiException, IOException {
        Playlist response = spotifyConnect.getSpotifyApi().getPlaylist(playlist_id)
                .setQueryParameter("market", market)
                .build().execute();

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

    /**
     * http://localhost:8080/api/playlists/tracks?playlist_id=37i9dQZF1DX4fpCWaHOned
     *
     * @param playlist_id
     * The Spotify ID for the playlist.
     *
     * @param limit
     * Optional.
     * The maximum number of items to return. Default: 100. Minimum: 1. Maximum: 100.
     *
     * @param offset
     * Optional.
     * The index of the first item to return. Default: 0 (the first object).
     *
     * @param market
     * Optional. An ISO 3166-1 alpha-2 country code or the string from_token.
     * Provide this parameter if you want to apply Track Relinking.
     * For episodes, if a valid user access token is specified in the request header,
     * the country associated with the user account will take priority over this parameter.
     *
     * @param fields
     * Optional.
     * Filters for the query: a comma-separated list of the fields to return.
     * If omitted, all fields are returned. For example, to get just the total number of items and the request limit:
     * fields=total,limit
     * A dot separator can be used to specify non-reoccurring fields, while parentheses can be used to specify reoccurring fields within objects.
     * For example, to get just the added date and user ID of the adder:
     * fields=items(added_at,added_by.id)
     * Use multiple parentheses to drill down into nested objects, for example:
     * fields=items(track(name,href,album(name,href)))
     * Fields can be excluded by prefixing them with an exclamation mark, for example:
     * fields=items.track.album(!external_urls,images)
     */
    @GetMapping("/tracks")
    public Map<String, Object> getPlaylistItems(@RequestParam String playlist_id,
                                                @RequestParam(required = false, defaultValue = "20") Integer limit,
                                                @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                @RequestParam(required = false, defaultValue = "US") String market,
                                                @RequestParam(required = false) String fields) throws ParseException, SpotifyWebApiException, IOException {
        var response = spotifyConnect.getSpotifyApi().getPlaylistsItems(playlist_id)
                .setQueryParameter("limit", limit)
                .setQueryParameter("offset", offset)
                .setQueryParameter("market", market)
                .build().execute();

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

    /**
     * http://localhost:8080/api/playlists/image?playlist_id=3AGOiaoRXMSjswCLtuNqv5
     *
     * @param playlist_id
     * The Spotify ID for the playlist.
     */
    @GetMapping("/image")
    public Image[] getPlaylistImage(@RequestParam String playlist_id) throws ParseException, SpotifyWebApiException, IOException {
        return spotifyConnect.getSpotifyApi().getPlaylistCoverImage(playlist_id).build().execute();
    }
}
