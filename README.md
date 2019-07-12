it is just a vision, how pins could be organized with help of MVP and Dagger

Pinterest gallery - images and json cached on the device, in case of no internet connection.

MVP provide clean structure, Activity and Fragemts make source code short and readable.

Instead of Glide image library... I implemented the LruCache for image cache storage in memory only, not on SD card.

Cached library created as separated module.

Dagger2 used for injection of dependencies

Retrofit2 connect to API

RxJava2 operate with threads

ConstraintLayout for better layout render

Also implemented usual test cases: unit, mockk of kotlin and Espresso
