# mediaplayer
A simple prototype of a media player app

# Choices
Dependency Injection:
MediaController and SongRetriever are initialized in the Application class and dependency injection
is used to pass them as parameters to the viewModels. I could have used a library like Dagger or
Koin which would make more sense on a larger scale project, but for this small project, I used a
Factory inside the viewModel to be able to inject the repository directly.