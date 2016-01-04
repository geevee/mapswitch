# mapswitch
## What is it?
**mapswitch** is an extension for Google Chrome for switching between various map providers, including
 Google Maps, Yandex Maps, OpenStreetMap, Wikimapia

## How to install?

[From Chrome Web Store](https://chrome.google.com/webstore/detail/mapswitch/ineobcbceekmckhjifhdmglkhgngnhmd)

## How to hack and build?

To build this project, you will need:

  * [IntelliJ IDEA](https://www.jetbrains.com/idea/), (15.0 or newer, Community or Ultimate Edition)
  * [Kotlin plugin for IntelliJ IDEA](http://kotlinlang.org/) (1.0 Beta 4 or newer, can be installed using "Plugins" page in IntelliJ IDEA preferences)
  
To build, just invoke "Make Project" action in IntelliJ IDEA. After that, you can load unpacked extension into Chrome
from `out/artifacts/extension` directory.

## Why Kotlin?

This extension is written in [http://kotlinlang.org/](Kotlin), modern programming language for JVM, Android and the browser.
I have spent 3.5 years+ working on this language at JetBrains, and I wanted to play with its compilation to JS.
The latter may have a lot of glitches (perfect JS support is not a goal for Kotlin 1.0), but it is fun anyway!
If you find any bugs in Kotlin, please don't hesitate to [http://kotlinlang.org/community.html](contact Kotlin team).
     
## Ideas?
If you have any ideas, feel free to create issues on Github.