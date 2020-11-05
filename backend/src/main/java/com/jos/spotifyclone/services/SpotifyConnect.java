package com.jos.spotifyclone.services;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest.Builder;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;

@Component
public class SpotifyConnect {
    private final SpotifyApi spotifyApi;
    private AuthorizationCodeUriRequest.Builder authorizationCodeUriRequestBuilder;
    private AuthorizationCodeUriRequest auth;

    public SpotifyConnect(
            @Value("${spotify.api.clientId}") String clientId,
            @Value("${spotify.api.secretId}") String secretId,
            @Value("${spotify.api.redirectUri}") String redirectUri
    ) {
   
    	this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(secretId)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectUri))
                .build();
        
        this.authorizationCodeUriRequestBuilder = spotifyApi.authorizationCodeUri();
    }


    @PostConstruct
    public void openAuthWindow() {
    	 String redirecturi = "http://localhost:8080/api/spotify-auth";
    	final URI uri = authorizationCodeUriRequestBuilder.scope("user-read-recently-played")
        		.redirect_uri(SpotifyHttpManager.makeUri(redirecturi))
        		.client_id("751456ead9dc4a1190f99ebe2a5cc694")
        		.build().execute();
    
        Runtime runtime = Runtime.getRuntime();
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
