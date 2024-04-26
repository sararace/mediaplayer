package com.example.mediaplayer

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.mediaplayer.repository.SongRepository

class MyApplication : Application() {

    lateinit var mediaPlayer: MediaPlayer
    lateinit var songRepository: SongRepository

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        songRepository = SongRepository(assets)
    }
}