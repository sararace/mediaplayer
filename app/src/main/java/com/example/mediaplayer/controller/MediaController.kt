package com.example.mediaplayer.controller

import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.mediaplayer.repository.SongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MediaController(
    songRepository: SongRepository,
    private val assets: AssetManager
) {
    private var mediaPlayer: MediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    // song order
    private val playlist = songRepository.songs
    private val currentSongIndex = MutableStateFlow(0)
    val isPlaying = MutableStateFlow(false) //playing if true, paused if false
    val currentSong = combine(playlist, currentSongIndex) { songList, index ->
        val song = songList[index]
        playSong(song.filename)
        song
    }

    private fun playSong(filename: String) {
        Log.d("MediaController", "playing song")
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            if (!mediaPlayer.isPlaying && filename.isNotEmpty()) {
                val fd = assets.openFd(filename)
                mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                fd.close()
                mediaPlayer.prepare()
                mediaPlayer.start()
                isPlaying.emit(true)
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
}