package fit.soda.nicerabbit.playvideo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import fit.soda.nicerabbit.R
import fit.soda.nicerabbit.httpclient.HttpApi
import fit.soda.nicerabbit.subtitle.Subtitle
import fit.soda.nicerabbit.subtitle.SubtitleUtils

class PlayVideoAct : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer

    // intent
    private lateinit var videoUrl: String
    private lateinit var subtitleUrl: String
    private lateinit var videoSizeRatio: String

    // view
    private lateinit var subRecView: RecyclerView
    private lateinit var subAdapter: MAdapter

    private val httpApi: HttpApi = HttpApi(this, Handler())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        exoPlayer = ExoPlayer.Builder(this).build()

        videoUrl = intent.getStringExtra("url") ?: ""
        subtitleUrl = intent.getStringExtra("subtitleUrl") ?: ""
        videoSizeRatio = intent.getStringExtra("videoSizeRatio") ?: "16:9"

        setupVideoView()
        startPlay()
        setupSubtitleView()
        downloadSubtitle();
    }

    private fun setupVideoView() {
        val playerView = findViewById<PlayerView>(R.id.player_view)
        (playerView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = videoSizeRatio

        playerView.player = exoPlayer
    }

    private fun setupSubtitleView() {
        subRecView = findViewById(R.id.subtitle_rec_view)
        subRecView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        subAdapter = MAdapter()
        subRecView.adapter = subAdapter
    }

    private fun startPlay() {
        val mediaItem = MediaItem.fromUri(videoUrl)

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()


    }

    private fun downloadSubtitle() {
        httpApi.subtitle(subtitleUrl) {
            val subUtils = SubtitleUtils()
            val subtitle = subUtils.convert(it)
            subAdapter.setData(subtitle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.stop()
    }
}

class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val subtitleTv: TextView = itemView.findViewById(R.id.subtitle_tv)
}

class MAdapter : RecyclerView.Adapter<MViewHolder>() {
    var subtitle: Subtitle? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subtitle_item_lauyout, parent, false)

        return MViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        val thisSub = subtitle?.content?.get(position)
        holder.subtitleTv.text = thisSub?.second
    }

    override fun getItemCount(): Int {
        return subtitle?.content?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(subtitle: Subtitle) {
        this.subtitle = subtitle
        notifyDataSetChanged()
    }
}