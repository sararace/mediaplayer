package com.example.mediaplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayer.ui.library.LibraryScreen
import com.example.mediaplayer.ui.library.LibraryViewModel
import com.example.mediaplayer.ui.nowplaying.NowPlayingScreen
import com.example.mediaplayer.ui.nowplaying.NowPlayingViewModel
import com.example.mediaplayer.ui.theme.MediaPlayerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            val viewModel: NowPlayingViewModel by viewModels { NowPlayingViewModel.Factory }
                            NowPlayingScreen(
                                onNavigateToLibrary = {
                                    navController.navigate("library")
                                },
                                nowPlayingViewModel = viewModel
                            )
                        }
                        composable("library") {
                            val viewModel: LibraryViewModel by viewModels { LibraryViewModel.Factory }
                            LibraryScreen(
                                onNavigateToNowPlaying = {
                                    navController.navigate("nowplaying")
                                },
                                libraryViewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
