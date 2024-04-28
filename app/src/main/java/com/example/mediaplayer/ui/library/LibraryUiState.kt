package com.example.mediaplayer.ui.library

import com.example.mediaplayer.model.Song

data class LibraryUiState(
    val items: List<Song> = emptyList()
)