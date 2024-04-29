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
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class MediaController(
    songRetriever: SongRetriever,
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

    val isPlaying = MutableStateFlow(false) //playing if true, paused if false
    val currentSong = combine(playlist, currentSongIndex) { songList, index ->
        songList[index]
    }

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
                isPlaying.emit(true)
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
        if (currentSongIndex.value <= playlist.value.size) {
            currentSongIndex.updateAndGet { it + 1 }.also {
                playSong(it)
            }
        }
    }

    fun pause() {
        mediaPlayer.pause()
        isPlaying.value = false
    }

    fun resume() {
        mediaPlayer.start()
        isPlaying.value = true
    }

    fun previous() {
        mediaPlayer.reset()
        if (currentSongIndex.value >= 1) {
            val newSongIndex = currentSongIndex.updateAndGet { it - 1 }
            if (isPlaying.value) {
                playSong(newSongIndex)
            }
        }
    }

    fun next() {
        mediaPlayer.reset()
        if (currentSongIndex.value <= playlist.value.size) {
            val newSongIndex = currentSongIndex.updateAndGet { it + 1 }
            if (isPlaying.value) {
                playSong(newSongIndex)
            } else {
                coroutineScope.launch {
                    prepareSong(newSongIndex)
                }
            }
        }
    }
}