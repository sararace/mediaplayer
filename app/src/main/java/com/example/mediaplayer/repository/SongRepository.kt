package com.example.mediaplayer.repository

import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import com.example.mediaplayer.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SongRepository(assets: AssetManager) {

    val songs: MutableStateFlow<List<Song>> = MutableStateFlow(listOf())
    private val retriever = MediaMetadataRetriever()

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch { populateSongs(assets) }
    }

    private suspend fun populateSongs(assets: AssetManager) {
        val assetList = assets.list("")
        val songList = mutableListOf<Song>()
        if (assetList != null) {
            for (filename in assetList.filter { it.endsWith(".mp3") }) {
                val fd = assets.openFd(filename)
                retriever.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                fd.close()
                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                songList.add(
                    Song(
                        title ?: "",
                        artist ?: "",
                        duration ?: 0,
                        filename
                    )
                )
            }
        }
        songs.emit(songList)
    }
}