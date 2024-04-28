package com.example.mediaplayer

import android.app.Application
import com.example.mediaplayer.controller.MediaController
import com.example.mediaplayer.repository.SongRepository

class MyApplication : Application() {

    lateinit var songRepository: SongRepository
    lateinit var mediaController: MediaController

    override fun onCreate() {
        super.onCreate()

        songRepository = SongRepository(assets)
        mediaController = MediaController(songRepository, assets)
    }
}