package fit.soda.nicerabbit.playvideo

import android.annotation.SuppressLint
import android.net.Uri
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
import com.google.android.exoplayer2.C.SELECTION_FLAG_DEFAULT
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
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

        videoUrl = intent.getStringExtra("url") ?: ""
        subtitleUrl = intent.getStringExtra("subtitleUrl") ?: ""
        videoSizeRatio = intent.getStringExtra("videoSizeRatio") ?: "16:9"

        setupVideoView()
        setupSubtitleView()
        downloadSubtitle {
            startPlay()
        }
    }

    private fun setupVideoView() {
        exoPlayer = ExoPlayer.Builder(this).build()
        val playerView = findViewById<StyledPlayerView>(R.id.player_view)
        (playerView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = videoSizeRatio

        playerView.setShowSubtitleButton(true)
        playerView.player = exoPlayer
    }

    private fun setupSubtitleView() {
        subRecView = findViewById(R.id.subtitle_rec_view)
        subRecView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        subAdapter = MAdapter()
        subRecView.adapter = subAdapter
    }

    private fun startPlay() {
        val subtitle = MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitleUrl))
            .setMimeType(MimeTypes.APPLICATION_TTML)
            .setLanguage("eng")
            .setLabel("eng sub")
            .setSelectionFlags(SELECTION_FLAG_DEFAULT)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .setSubtitleConfigurations(listOf(subtitle))
            .build()

        exoPlayer.addListener(object : Player.Listener {
            override fun onCues(cues: MutableList<Cue>) {
                super.onCues(cues)

                var subtitleText = ""
                cues.forEach {
                    subtitleText = "${subtitleText}${it.text.toString()}"
                }

                val breakLine = subtitleText.split("\n")
                val adjusted = breakLine.lastOrNull()

                subAdapter.subtitleTimeEvent(adjusted ?: "")
            }
        })
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    private fun downloadSubtitle(callback: () -> Unit) {
        httpApi.subtitle(subtitleUrl) {
            val subUtils = SubtitleUtils()
            val subtitle = subUtils.convert(it)
            subAdapter.setData(subtitle)
            callback()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.stop()
        exoPlayer.release()
    }
}

class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val subtitleTv: TextView = itemView.findViewById(R.id.subtitle_tv)
    val bgHighLight: View = itemView.findViewById(R.id.bg_high_light)
}

class MAdapter : RecyclerView.Adapter<MViewHolder>() {
    private var lastHighLightIndex = -1
    private var subtitle: List<Subtitle>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.subtitle_item_lauyout, parent, false)

        return MViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        val thisSub = subtitle?.get(position)
        holder.subtitleTv.text = thisSub?.text
        if (subtitle?.get(position)?.highLight == true) {
            holder.bgHighLight.visibility = View.VISIBLE
        } else {
            holder.bgHighLight.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return subtitle?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(subtitle: List<Subtitle>) {
        this.subtitle = subtitle
        notifyDataSetChanged()
    }

    fun subtitleTimeEvent(thisSubTitle: String) {
        var thisIndex = -1
        subtitle?.forEachIndexed { index, subtitleItem ->
            if (thisSubTitle.startsWith(subtitleItem.text ?: "")) {
                thisIndex = index
            }
        }

        if (thisIndex != -1) {
            subtitle?.get(thisIndex)?.highLight = true
            notifyItemChanged(thisIndex)

            if (lastHighLightIndex != -1) {
                subtitle?.get(lastHighLightIndex)?.highLight = false
                notifyItemChanged(lastHighLightIndex)
            }

            lastHighLightIndex = thisIndex
        }
    }
}