package fit.soda.nicerabbit

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fit.soda.nicerabbit.download.Download

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val handler = Handler()
        findViewById<View>(R.id.test_btn).setOnClickListener {
            val download = Download()
//            download.get(this@MainActivity, handler, "3VGsiayxVRA")
            download.testNewPipe(
                this@MainActivity,
                handler,
                "https://www.youtube.com/watch?v=w4FpRaczI5U"
            )
        }
    }
}