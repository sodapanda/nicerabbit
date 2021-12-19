package fit.soda.nicerabbit.playvideo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import fit.soda.nicerabbit.R

class PlayVideoAct : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var videoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        exoPlayer = ExoPlayer.Builder(this).build()
        setupVideoView()

        videoUrl = intent.getStringExtra("url") ?: ""

        startPlay()
    }

    private fun setupVideoView() {
        val playerView = findViewById<PlayerView>(R.id.player_view)

        playerView.player = exoPlayer
    }

    private fun startPlay() {
        val mediaItem = MediaItem.fromUri(videoUrl)

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }
}