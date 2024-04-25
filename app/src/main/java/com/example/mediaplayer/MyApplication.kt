package com.example.mediaplayer

import android.app.Application
import com.example.mediaplayer.repository.SongRepository

class MyApplication : Application() {

    lateinit var songRepository: SongRepository

    override fun onCreate() {
        super.onCreate()

        songRepository = SongRepository(assets)
    }
}