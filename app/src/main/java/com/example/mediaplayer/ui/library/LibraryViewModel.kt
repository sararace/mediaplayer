package com.example.mediaplayer.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mediaplayer.MyApplication
import com.example.mediaplayer.model.Song
import com.example.mediaplayer.util.SongRetriever
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    songRetriever: SongRetriever
) : ViewModel() {
    val songs = MutableStateFlow<List<Song>>(emptyList())

    init {
        viewModelScope.launch {
            songs.emit(songRetriever.getSongs())
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
                return LibraryViewModel(
                    (application as MyApplication).songRetriever
                ) as T
            }
        }
    }
}