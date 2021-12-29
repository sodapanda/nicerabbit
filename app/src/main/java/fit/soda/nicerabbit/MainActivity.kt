package fit.soda.nicerabbit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import fit.soda.nicerabbit.download.DownloadableVideoInfo
import fit.soda.nicerabbit.download.Downloader
import fit.soda.nicerabbit.playvideo.PlayVideoAct

class MainActivity : FragmentActivity() {
    lateinit var urlTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlTextView = findViewById(R.id.url_text)
        val sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
            ?: "https://www.youtube.com/watch?v=1uDfnHoPq3w"

        urlTextView.text = sharedUrl

        findViewById<View>(R.id.test_btn).setOnClickListener {
            val downloader = Downloader()
            downloader.extractVideoUrl(sharedUrl) { success, info ->
                if (success && info != null) {
                    startPlayerAct(info)
                } else {
                    Toast.makeText(this@MainActivity, "实际下载地址获取错误", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.download_btn).setOnClickListener {
            val downloader = Downloader()
            downloader.extractVideoUrl("https://www.youtube.com/watch?v=1uDfnHoPq3w") { success, info ->
                if (success && info != null && info.videoUrl.isNotEmpty()) {
                    downloader.downloadFile(
                        this@MainActivity,
                        info.videoUrl,
                        getExternalFilesDir(null)?.absolutePath + "/ytb.mp4" ?: ""
                    ) { _, _ ->
                        Toast.makeText(this@MainActivity, "download done", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun startPlayerAct(info: DownloadableVideoInfo) {
        val intent = Intent(this@MainActivity, PlayVideoAct::class.java)
        intent.putExtra("url", info.videoUrl)
        intent.putExtra("subtitleUrl", info.subtitleUrl)
        intent.putExtra("videoSizeRatio", "${info.width}:${info.height}")
        startActivity(intent)
    }
}