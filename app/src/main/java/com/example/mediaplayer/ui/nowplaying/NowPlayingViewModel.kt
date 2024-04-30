package com.example.mediaplayer.ui.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mediaplayer.MyApplication
import com.example.mediaplayer.controller.MediaController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NowPlayingViewModel(
    private val mediaController: MediaController
) : ViewModel() {

    private val _uiState = MutableStateFlow(NowPlayingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            mediaController.currentSong.collect { song ->
                _uiState.update { currentState ->
                    currentState.copy(
                        currentSongTitle = song.title,
                        currentSongArtist = song.artist
                    )
                }
            }
        }
    }

    fun pause() {
        mediaController.pause()
        _uiState.update { currentState ->
            currentState.copy(
                isPlaying = false
            )
        }
    }

    fun resume() {
        mediaController.resume()
        _uiState.update { currentState ->
            currentState.copy(
                isPlaying = true
            )
        }
    }

    fun previous() {
        mediaController.previous()
    }

    fun next() {
        mediaController.next()
    }

    fun toggleShuffle() {
        mediaController.toggleShuffle(!_uiState.value.isShuffled)
        _uiState.update { currentState ->
            currentState.copy(
                isShuffled = !currentState.isShuffled
            )
        }
    }

    fun toggleRepeatAll() {
        val state = _uiState.value
        val repeatAll = !state.isRepeatAll
        val repeatOne = if (!state.isRepeatAll) false else state.isRepeatOne
        mediaController.isRepeatAll = repeatAll
        mediaController.isRepeatOne = repeatOne
        _uiState.update { currentState ->
            currentState.copy(
                isRepeatAll = repeatAll,
                isRepeatOne = repeatOne
            )
        }
    }

    fun repeatOne() {
        val state = _uiState.value
        val repeatOne = !state.isRepeatOne
        val repeatAll = if (!state.isRepeatOne) false else state.isRepeatAll
        mediaController.isRepeatOne = repeatOne
        mediaController.isRepeatAll = repeatAll
        _uiState.update { currentState ->
            currentState.copy(
                isRepeatOne = repeatOne,
                isRepeatAll = repeatAll
            )
        }
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