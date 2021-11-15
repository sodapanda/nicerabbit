package fit.soda.nicerabbit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fit.soda.nicerabbit.download.Download

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.test_btn).setOnClickListener {
            val download = Download()
            download.get()
        }
    }
}