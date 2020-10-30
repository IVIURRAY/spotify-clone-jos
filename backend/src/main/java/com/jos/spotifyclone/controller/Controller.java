package com.jos.spotifyclone.controller;

import com.wrapper.spotify.SpotifyApi;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class Controller {
  public abstract void run();
}
