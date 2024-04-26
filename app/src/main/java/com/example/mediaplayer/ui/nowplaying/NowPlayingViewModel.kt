package com.example.mediaplayer.ui.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mediaplayer.MyApplication
import com.example.mediaplayer.controller.MediaController
import kotlinx.coroutines.flow.combine

class NowPlayingViewModel(
    private val mediaController: MediaController
) : ViewModel() {

    val uiState = combine(mediaController.currentSong, mediaController.isPlaying) { song, isPlaying ->
        NowPlayingUiState(song.title, song.artist, isPlaying)
    }

    fun pause() {
        mediaController.pause()
    }

    fun resume() {
        mediaController.resume()
    }

    fun previous() {
        mediaController.previous()
    }

    fun next() {
        mediaController.next()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return NowPlayingViewModel(
                    (application as MyApplication).mediaController
                ) as T
            }
        }
    }
}