# Spotify Clone JOS
A Spotify clone built by Java Open Source Group


# How to get involved
1) Join our discord [channel](https://discord.gg/NqTfYW) and introduce yourself to get involved!
2) Find a task you want to work on from the [project board](https://github.com/IVIURRAY/spotify-clone-jos/projects)
  - [Backend Board](https://github.com/IVIURRAY/spotify-clone-jos/projects/1)
  - [Frontend Board](https://github.com/IVIURRAY/spotify-clone-jos/projects/3)

# How to Contribute?

First off, thank you for wanting to contribute! :thumbsup:

1) Fork this repo
2) Make your changes on a branch, example `feature/<my-cool-feature>`
3) Push your changes to your forked repo
4) Raise a pull request!

*  Follow [this](https://www.dataschool.io/how-to-contribute-on-github/amp/) guide if the above does not make sense!

# How to setup Spotify Auth
In order for this to work you need to have a Spotify [Applicaiton](https://developer.spotify.com/documentation/general/guides/app-settings/#register-your-app) registered.

1) [Go here](https://developer.spotify.com/documentation/general/guides/app-settings/#register-your-app) and register an application
2) This will allow you to get a `client id` and `client secret` which you need to authenticate to Spotify.
3) Replace your `client id` and `secret id` in the [`application.properties`](https://github.com/IVIURRAY/spotify-clone-jos/blob/master/backend/src/main/resources/application.properties) file.
4) Make sure you add your Redirect URIs to `http://localhost:8080/api/spotify-auth` in the Spotify Dashboar also, otherwise it will not work!
