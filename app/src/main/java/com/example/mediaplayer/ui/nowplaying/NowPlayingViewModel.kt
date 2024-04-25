package com.example.mediaplayer.ui.nowplaying

import androidx.lifecycle.ViewModel
import com.example.mediaplayer.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class NowPlayingViewModel : ViewModel() {
    // song order
    private var playlist = MutableStateFlow<List<Song>>(
        listOf(
            Song(
                "Clair de Lune",
                "Hotel Commodore Ensemble",
                240000,
                "Clair_de_Lune.mp3"
            )
        )
    )

    private var currentSongIndex = MutableStateFlow(0)
    private val currentSong = combine(currentSongIndex, playlist) { index, songList ->
        songList[index]
    }

    val uiState = currentSong.map { song ->
        NowPlayingUiState(song.title, song.artist)
    }

    val currentSongFile = currentSong.map { song ->
        song.filename
    }
}