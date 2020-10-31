package com.jos.spotifyclone.services;

import java.io.IOException;
import java.net.URI;

import javax.annotation.PostConstruct;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@Service
public class SpotifyConnect {
    private static final String CLIENT_ID = "751456ead9dc4a1190f99ebe2a5cc694";
    private static final String SECRET_ID = "47f3ba9cd3f546d98711b7b3b26a0ac4";
    private static final String REDIRECT_URI = "https://github.com/talentedasian/spotify-clone-jos";
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(SECRET_ID)
            .setRedirectUri(SpotifyHttpManager.makeUri(REDIRECT_URI))
            .build();
    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .build();;

    @PostConstruct
    public void openAuthWindow() {
        final URI uri = authorizationCodeUriRequest.execute();
        Runtime runtime = Runtime.getRuntime();
        System.out.println(uri.toString());
        try {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + uri);
        } catch (IOException e) {
            System.out.println("If you're running on Windows and read this it looks like we can't open your browser...");
        }
        try {
            runtime.exec("open " + uri);
        } catch (IOException e) {
            System.out.println("If you're running on MacOS and read this it looks like we can't open your browser...");
        }
    }

    public void addAuthCode(String code) throws ParseException, SpotifyWebApiException, IOException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        // Set access and refresh token for further "spotifyApi" object usage
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
    }

    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }
}
