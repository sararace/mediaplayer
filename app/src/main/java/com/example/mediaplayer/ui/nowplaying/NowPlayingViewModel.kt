package com.example.mediaplayer.ui.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mediaplayer.MyApplication
import com.example.mediaplayer.repository.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class NowPlayingViewModel(
    songRepository: SongRepository
) : ViewModel() {
    // song order
    private var playlist = songRepository.songs

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

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return NowPlayingViewModel((application as MyApplication).songRepository) as T
            }
        }
    }
}