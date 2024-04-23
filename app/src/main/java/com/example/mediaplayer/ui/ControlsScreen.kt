package com.example.mediaplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ControlsScreen(
    controlsViewModel: ControlsViewModel = viewModel()
) {
    val controlsUiState by controlsViewModel.uiState.collectAsState()
}

@Composable
fun CurrentlyPlayingLayout(
    title: String,
    artist: String,
    modifier: Modifier = Modifier
) {

}