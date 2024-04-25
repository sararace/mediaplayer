# mediaplayer
A simple prototype of a media player app

# Choices
Dependency Injection:
By declaring MediaPlayer as a variable in MediaPlayer, it is created once and can
be accessed by both screens. This is a simple form of dependency injection.

For SongRepository, it needed to be a parameter to the viewModel, not the view. So
instead a Factory was used inside the viewModel to be able to inject the
repository directly.