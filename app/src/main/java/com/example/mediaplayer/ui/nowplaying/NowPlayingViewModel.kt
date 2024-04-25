package com.example.mediaplayer.ui.nowplaying

import androidx.lifecycle.ViewModel
import com.example.mediaplayer.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class NowPlayingViewModel : ViewModel() {
    // song order
    private var playlist = MutableStateFlow<List<Song>>(listOf(
        Song(
        "Clair de Lune",
        "Hotel Commodore Ensemble",
        240000,
        "Clair_de_Lune.mp3"
    )
    ))

    private var currentSongIndex = MutableStateFlow(0)

    val uiState = combine(currentSongIndex, playlist) { index, songList ->
        val song = songList[index]
        NowPlayingUiState(song.title, song.artist)
    }
}