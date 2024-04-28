package com.example.mediaplayer.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediaplayer.R
import com.example.mediaplayer.model.Song
import com.example.mediaplayer.ui.theme.MediaPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onNavigateToNowPlaying: () -> Unit,
    libraryViewModel: LibraryViewModel
) {
    val songs by libraryViewModel.songs.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigateToNowPlaying() }) {
                        Icon(Icons.Filled.ArrowBack, "")
                    }
                },
                title = { Text(stringResource(id = R.string.library)) }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    HeaderItem()
                }
                items(songs) { song ->
                    SongItem(song = song)
                }
            }
        }
    }
}

@Composable
fun HeaderItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.weight(5f),
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.song_title)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(5f),
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.artist)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(4f),
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.track_length)
        )
    }
}

@Composable
fun SongItem(song: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.weight(5f),
            text = song.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(5f),
            text = song.artist,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(4f),
            text = song.trackLength
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderItemPreview() {
    MediaPlayerTheme {
        HeaderItem()
    }
}

@Preview(showBackground = true)
@Composable
fun SongItemPreview() {
    MediaPlayerTheme {
        SongItem(Song("Life Is a Highway", "Rascal Flatts", "4:37", ""))
    }
}