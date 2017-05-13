# Movie Buddy
A simple app for movie recommendations. It will fetch popular movies from [The Movie DB](https://www.themoviedb.org/) and recommands u a random movie.

## Getting started

A personal movie db API key needs to be applied in [strings.xml](app/src/main/res/values/strings.xml).
The entry should look like this: 
```xml
<string name="the_movie_db_api_key">YOUR_KEY</string>
```

## How it works
The app will start with an movie recommendation already in place. If u want to get another movie recommendation, u have three ways to achieve that.

![Usage](./doc/get-next.gif)

### Swipe
Swipe on the screen to get the next video.

### Shake the phone


### Speech recognition
Press the yellow FAB-Button on the right bottom corner and use one of the following commands:
+ "next"
+ "next film"

## License

The contents of this repository are covered under the [MIT License](LICENSE)