# Popular Movies App

[![License Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://github.com/fjoglar/popular-movies-app/blob/master/LICENSE.txt)
[![fjoglar twitter](https://img.shields.io/badge/twitter-@felipejoglar-blue.svg)](http://twitter.com/felipejoglar)
[![Platform Android](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)


## Api Key use

First you need to get an API key from [The Movie Database](https://www.themoviedb.org/) website.

Then inside the `gradle.properties` file substitute `your_tmdb_api_key_here` with your actual API key and add this file to your .gitignore to avoid pushing your key to your public repository.

``` groovy
# The Movie Database Api Key
tmdbApiKey = "your_tmdb_api_key_here"
```


## Languages, libraries and tools used

* [Java](https://docs.oracle.com/javase/8/)
* Android Support Libraries
* [RxJava2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
* [Retrofit](https://github.com/square/retrofit)
* [Gson](https://github.com/google/gson)


## Requirements

* JDK 1.8
* [Android SDK](https://developer.android.com/studio/index.html)
* Android O ([API 27](https://developer.android.com/preview/api-overview.html))
* Latest Android SDK Tools and build tools.


## License

```
This project was submitted by Felipe Joglar as part of the Android Developer
Nanodegree At Udacity.

As part of Udacity Honor code, your submissions must be your own work, hence
submitting this project as yours will cause you to break the Udacity Honor Code
and the suspension of your account.

Me, the author of the project, allow you to check the code as a reference, but if
you submit it, it's your own responsibility if you get expelled.

Besides the above notice, the following license applies and this license notice
must be included in all works derived from this project.

Copyright 2018 Felipe Joglar Santos

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
