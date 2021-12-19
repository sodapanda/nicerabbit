package fit.soda.nicerabbit.httpclient

import android.content.Context
import android.os.Handler
import com.github.kevinsawicki.http.HttpRequest
import java.lang.Exception

class HttpApi(private val context: Context, private val mainHandler: Handler) {
    fun subtitle(url: String, callback: (content: String) -> Unit) {
        Thread {
            val result = request(url)
            mainHandler.post {
                callback(result)
            }
        }.start()
    }

    private fun request(url: String): String {
        var result = ""
        try {
            result = HttpRequest.get(url).body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result;
    }
}