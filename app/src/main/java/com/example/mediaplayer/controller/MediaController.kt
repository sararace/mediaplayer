package com.example.mediaplayer.controller

import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.mediaplayer.model.Song
import com.example.mediaplayer.util.SongRetriever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class MediaController(
    private val songRetriever: SongRetriever,
    private val assets: AssetManager
) {
    private var mediaPlayer: MediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        setOnCompletionListener { onSongComplete() }
    }
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // song order
    private val playlist = MutableStateFlow<List<Song>>(emptyList())
    private val currentSongIndex = MutableStateFlow(0)

    val currentSong = combine(playlist, currentSongIndex) { songList, index ->
        songList[index]
    }

    var isRepeatAll = false
    var isRepeatOne = false

    init {
        coroutineScope.launch {
            val defaultPlaylist = songRetriever.getSongs()
            playlist.emit(defaultPlaylist)
            prepareSong(defaultPlaylist.first().filename)
        }
    }

    private fun playSong(index: Int) {
        val song = playlist.value[index]
        playSong(song.filename)
    }

    private fun playSong(filename: String) {
        Log.d("MediaController", "playing song")
        coroutineScope.launch {
            if (!mediaPlayer.isPlaying && filename.isNotEmpty()) {
                prepareSong(filename)
                mediaPlayer.start()
            }
        }
    }

    private suspend fun prepareSong(index: Int) {
        val song = playlist.value[index]
        prepareSong(song.filename)
    }

    private suspend fun prepareSong(filename: String) {
        if (!mediaPlayer.isPlaying && filename.isNotEmpty()) {
            val fd = assets.openFd(filename)
            mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            fd.close()
            mediaPlayer.prepare()
        }
    }

    private fun onSongComplete() {
        mediaPlayer.reset()
        if (isRepeatOne) {
            playSong(currentSongIndex.value)
        } else if (currentSongIndex.value <= playlist.value.size) {
            currentSongIndex.updateAndGet { it + 1 }.also {
                playSong(it)
            }
        }
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun resume() {
        mediaPlayer.start()
    }

    fun previous() {
        val isPlaying = mediaPlayer.isPlaying
        val newSongIndex = if (isRepeatOne) {
            currentSongIndex.value
        } else if (currentSongIndex.value >= 1) {
            currentSongIndex.value - 1
        } else {
            null
        }
        skipToSong(isPlaying, newSongIndex)
    }

    fun next() {
        val isPlaying = mediaPlayer.isPlaying
        val newSongIndex = if (isRepeatOne) {
            currentSongIndex.value
        } else if (currentSongIndex.value < playlist.value.size - 1) {
            currentSongIndex.value + 1
        } else if (isRepeatAll) {
            0
        } else {
            null
        }
        skipToSong(isPlaying, newSongIndex)
    }

    private fun skipToSong(isPlaying: Boolean, index: Int?) {
        if (index != null) {
            currentSongIndex.value = index
            mediaPlayer.reset()
            if (isPlaying) {
                playSong(index)
            } else {
                coroutineScope.launch {
                    prepareSong(index)
                }
            }
        }
    }

    fun toggleShuffle(shuffle: Boolean) {
        val isPlaying = mediaPlayer.isPlaying
        mediaPlayer.reset()
        coroutineScope.launch {
            playlist.update {
                if (shuffle) {
                    it.shuffled()
                } else {
                    songRetriever.getSongs()
                }
            }
            currentSongIndex.emit(0)
            if (isPlaying) {
                playSong(0)
            } else {
                prepareSong(0)
            }
        }
    }
}