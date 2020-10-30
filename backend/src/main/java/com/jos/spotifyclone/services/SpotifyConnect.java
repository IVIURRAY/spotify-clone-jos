package com.jos.spotifyclone.services;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;

@Service
public class SpotifyConnect {
    private static final String CLIENT_ID  = "";
    private static final String SECRET_ID = "";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/spotify-auth");
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(SECRET_ID)
            .setRedirectUri(redirectUri)
            .build();
    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .build();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *") //how often to run seconds minutes hours days month year
    public static void clientCredentials_Sync() {
        final URI uri = authorizationCodeUriRequest.execute();
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addAuthCode(String code) throws ParseException, SpotifyWebApiException, IOException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Yey! - I can print out user data!
        User user = spotifyApi.getCurrentUsersProfile().build().execute();
        System.out.println(user.toString());
    }
}
