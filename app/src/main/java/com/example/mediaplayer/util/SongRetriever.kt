package com.example.mediaplayer.util

import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.util.Log
import com.example.mediaplayer.model.Song

class SongRetriever(private val assets: AssetManager) {

    private val retriever = MediaMetadataRetriever()

    fun getSongs() : List<Song> {
        Log.d("SongRetriever", "populating songs")
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
                        formatDuration(duration),
                        filename
                    )
                )
            }
        }
        return songList
    }

    private fun formatDuration(duration: Long?): String {
        if (duration == null) return ""
        val totalSeconds = duration/1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return if (seconds < 10) {
            "$minutes:0$seconds"
        } else {
            "$minutes:$seconds"
        }
    }
}