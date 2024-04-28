package com.example.mediaplayer.ui.nowplaying

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayer.R
import com.example.mediaplayer.ui.theme.MediaPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    onNavigateToLibrary: () -> Unit,
    nowPlayingViewModel: NowPlayingViewModel
) {
    val nowPlayingUiState by nowPlayingViewModel.uiState.collectAsState(NowPlayingUiState())

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
            NowPlaying(
                title = nowPlayingUiState.currentSongTitle,
                artist = nowPlayingUiState.currentSongArtist
            )
            Spacer(modifier = Modifier.height(24.dp))
            Controls(
                isPlaying = nowPlayingUiState.isPlaying,
                nowPlayingViewModel::pause,
                nowPlayingViewModel::resume,
                nowPlayingViewModel::previous,
                nowPlayingViewModel::next
            )
        }
    }
}

@Composable
fun NowPlaying(
    title: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = artist,
            fontSize = 14.sp
        )
    }
}

@Composable
fun Controls(
    isPlaying: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        IconButton(onClick = { onPrevious() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.skip_previous),
                contentDescription = ""
            )
        }
        if (isPlaying) {
            IconButton(onClick = { onPause() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.pause),
                    contentDescription = ""
                )
            }
        } else {
            IconButton(onClick = { onResume() }) {
                Icon(Icons.Filled.PlayArrow, "")
            }
        }
        IconButton(onClick = { onNext() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.skip_next),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun Modes(
    modifier: Modifier = Modifier
) {

}

@Preview(showBackground = true)
@Composable
fun CurrentlyPlayingPreview() {
    MediaPlayerTheme {
        NowPlaying("Life Is a Highway", "Rascal Flatts")
    }
}