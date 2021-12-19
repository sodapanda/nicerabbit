package fit.soda.nicerabbit.playvideo

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import fit.soda.nicerabbit.R
import fit.soda.nicerabbit.httpclient.HttpApi

class PlayVideoAct : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var videoUrl: String
    private lateinit var subtitleUrl: String
    private val httpApi: HttpApi = HttpApi(this, Handler())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        exoPlayer = ExoPlayer.Builder(this).build()
        setupVideoView()

        videoUrl = intent.getStringExtra("url") ?: ""
        subtitleUrl = intent.getStringExtra("subtitleUrl") ?: ""

        startPlay()
        downloadSubtitle();
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

    private fun downloadSubtitle() {
        httpApi.subtitle(subtitleUrl) {
            findViewById<TextView>(R.id.subtitle_view).text = it
        }
    }
}