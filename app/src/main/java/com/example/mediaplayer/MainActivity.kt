package com.example.mediaplayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayer.ui.library.LibraryScreen
import com.example.mediaplayer.ui.nowplaying.NowPlayingScreen
import com.example.mediaplayer.ui.theme.MediaPlayerTheme

class MainActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }

        setContent {
            MediaPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "nowplaying") {
                        composable("nowplaying") {
                            NowPlayingScreen(onNavigateToLibrary = {
                                navController.navigate("library")
                            }, mediaPlayer = mediaPlayer)
                        }
                        composable("library") {
                            LibraryScreen(onNavigateToNowPlaying = {
                                navController.navigate("nowplaying")
                            }) }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
