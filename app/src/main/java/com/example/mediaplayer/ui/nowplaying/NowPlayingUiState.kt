package com.example.mediaplayer.ui.nowplaying

data class NowPlayingUiState(
    val currentSongTitle: String = "",
    val currentSongArtist: String = "",
    val isPlaying: Boolean = false
)