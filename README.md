# mediaplayer
A simple prototype of a media player app

# Architecture Choices
Compose:
This app uses Jetpack Compose for the UI.

MVVM:
This app uses MVVM architecture to separate the UI logic from the business logic. The view is
represented by the "Screen" compose files, and the ViewModel extends the ViewModel class.
Navigation is implemented using the NavHost and NavController classes from the Jetpack navigation
library for Compose.

Dependency Injection:
MediaController and SongRetriever are initialized in the Application class and dependency injection
is used to pass them as parameters to the viewModels. I could have used a library like Dagger or
Koin which would make more sense on a larger scale project, but for this small project, I used a
Factory inside the viewModel to be able to inject the repository directly.

# Spec Assumptions
Some behaviors were not specified in the specifications so I made my own assumptions. 
1. For the mode buttons, I made these toggle buttons. I wanted the user to be able to undo and get back to the original state in case they clicked accidentally.
2. Logically, repeat one and repeat all cannot be enabled at the same time.
3. The shuffle button restarts the playlist due to simplicity. Production-level media players might move the current song to the start of the shuffled list instead so as not to interrupt playback.
4. With repeat all enabled, the next button will go from the end of the playlist to the start, but the previous button will not wrap from the beginning of the playlist to the end.
5. When next is pressed on the last song or previous is pressed on the first song, nothing happens (unless next is pressed on the last song while repeat all is enabled, then it goes to the first song)