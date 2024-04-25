package com.example.mediaplayer.ui.nowplaying

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediaplayer.R
import com.example.mediaplayer.ui.theme.MediaPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    onNavigateToLibrary: () -> Unit,
    mediaPlayer: MediaPlayer?,
    nowPlayingViewModel: NowPlayingViewModel = viewModel()
) {
    val nowPlayingUiState by nowPlayingViewModel.uiState.collectAsState(NowPlayingUiState())
    val nowPlayingFilename by nowPlayingViewModel.currentSongFile.collectAsState(initial = "")

    val context = LocalContext.current

    if (mediaPlayer?.isPlaying == false && nowPlayingFilename.isNotEmpty()) {
        val fd = context.assets.openFd(nowPlayingFilename)
        mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        fd.close()
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = { onNavigateToLibrary() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.queue_music),
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CurrentlyPlaying(
                title = nowPlayingUiState.currentSongTitle,
                artist = nowPlayingUiState.currentSongArtist
            )
        }
    }
}

@Composable
fun CurrentlyPlaying(
    title: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = artist,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentlyPlayingPreview() {
    MediaPlayerTheme {
        CurrentlyPlaying("Life Is a Highway", "Rascal Flatts")
    }
}