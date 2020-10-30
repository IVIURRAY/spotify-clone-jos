package com.jos.spotifyclone;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.browse.GetListOfNewReleasesRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.Arrays;

public class NewReleases {
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(SpotifyConnect.token)
            .build();
    private static final GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
//          .country(CountryCode.SE)
//          .limit(10)
//          .offset(0)
            .build();

    public static void getListOfNewReleases_Sync() {
        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = getListOfNewReleasesRequest.execute();

            System.out.println("Total: " + albumSimplifiedPaging.getTotal());
            System.out.println(Arrays.toString(albumSimplifiedPaging.getItems()));
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
