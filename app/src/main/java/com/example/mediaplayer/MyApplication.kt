package com.example.mediaplayer

import android.app.Application
import com.example.mediaplayer.controller.MediaController
import com.example.mediaplayer.util.SongRetriever

class MyApplication : Application() {

    lateinit var songRetriever: SongRetriever
    lateinit var mediaController: MediaController

    override fun onCreate() {
        super.onCreate()

        songRetriever = SongRetriever(assets)
        mediaController = MediaController(songRetriever, assets)
    }
}