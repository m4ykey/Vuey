# Vuey

<img src="logo.png" align="left" width="150" />

Vuey is open source Album, Movies and TV Shows Tracker.

## Screenshots


<div>
  <img src="screenshots/album_detail.jpg" width="150" alt="Album Detail" />
  <img src="screenshots/movie_detail.jpg" width="150" alt="Movie Detail" />
  <img src="screenshots/tv_show_detail.jpg" width="150" alt="Tv Show Detail" />
  <img src="screenshots/episode_list.jpg" width="150" alt="Episode List" />
</div>

## Project Setup

1. Clone repository and open project in the latest version of Android Studio.
2. Generate and import your `google-services.json` file and put it in the `/app`
3. Create `local.properties` and import it to `/app`
4. Add your [Spotify](https://developer.spotify.com/dashboard) SPOTIFY_CLIENT_ID and SPOTIFY_CLIENT_SECRET and [TMDB](https://developer.themoviedb.org/docs) key in `local.properties`
```
TMDB_API_KEY="YOUR_TMDB_API_KEY"
SPOTIFY_CLIENT_ID="YOUR_SPOTIFY_CLIENT_ID"
SPOTIFY_CLIENT_SECRET="YOUR_SPOTIFY_CLIENT_SECRET"
```
5. Clean and rebuild project

## TODO
- [ ] Save TV Shows seasons to database
- [ ] Save TV Shows episodes to database
- [ ] Improve UI
- [ ] Dark/Light Theme
- [ ] Settings Screen
- [ ] Statistic Screen

## Tech
- [Retrofit](https://square.github.io/retrofit/)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [OkHttpClient](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/)
- [Firebase](https://firebase.google.com/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Gson](https://github.com/google/gson)
- [Coil](https://coil-kt.github.io/coil/)
