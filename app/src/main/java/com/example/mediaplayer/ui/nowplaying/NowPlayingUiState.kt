package com.example.mediaplayer.ui.nowplaying

data class NowPlayingUiState(
    val currentSongTitle: String = "",
    val currentSongArtist: String = "",
    val isPlaying: Boolean = false,
    val isShuffled: Boolean = false,
    val isRepeatOne: Boolean = false,
    val isRepeatAll: Boolean = false
)