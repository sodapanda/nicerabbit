package fit.soda.nicerabbit.download

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.github.kevinsawicki.http.HttpRequest
import com.permissionx.guolindev.PermissionX
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.stream.StreamInfo
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import kotlin.text.*

class Downloader {
    fun extractVideoUrl(
        pageUrl: String,
        callback: (success: Boolean, info: DownloadableVideoInfo?) -> Unit
    ) {
        Observable.just("start")
            .flatMap {
                val streamInfo = StreamInfo.getInfo(NewPipe.getService(0), pageUrl)
                // subtitle
                var subtitleUrl = ""
                for (subtitlesStream in streamInfo.subtitles) {
                    subtitleUrl = subtitlesStream.getUrl()
                }

                // video
                val videoItem = streamInfo.videoStreams.firstOrNull { it.quality == "medium" }
                val downloadableVideoInfo = DownloadableVideoInfo(
                    videoItem?.url ?: "",
                    subtitleUrl,
                    videoItem?.width ?: 0,
                    videoItem?.height ?: 0
                )

                Observable.just(downloadableVideoInfo)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.videoUrl.isEmpty()) {
                    callback(false, null)
                } else {
                    callback(true, it)
                }
            }, { err ->
                err.printStackTrace()
                callback(false, null)
            })
    }

    fun downloadFile(
        context: FragmentActivity,
        url: String,
        path: String,
        callback: (event: String, revSize: Int) -> Unit
    ) {
        Log.i("nicerabbit", "url:" + url)
        PermissionX.init(context)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Observable.just("start")
                        .flatMap {
                            val outPutFile = File(path)
                            HttpRequest.get(url).receive(outPutFile)
                            Observable.just("done")
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            callback(it, 0)
                        }
                }
            }
    }
}

class DownloadableVideoInfo(
    val videoUrl: String,
    val subtitleUrl: String,
    val width: Int,
    val height: Int
)