package fit.soda.nicerabbit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fit.soda.nicerabbit.download.Download
import fit.soda.nicerabbit.playvideo.PlayVideoAct

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.test_btn).setOnClickListener {
            Download.testNewPipe("https://www.youtube.com/watch?v=1uDfnHoPq3w") { videoUrl, subtitleUrl ->
                this@MainActivity.runOnUiThread {
                    val intent = Intent(this@MainActivity, PlayVideoAct::class.java)
                    intent.putExtra("url", videoUrl)
                    intent.putExtra("subtitleUrl", subtitleUrl)
                    startActivity(intent)
                }
            }
        }
    }
}