package com.jos.spotifyclone.com.jos.spotifyclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.jos.spotifyclone")
public class SpotifyCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotifyCloneApplication.class, args);
	}

}
