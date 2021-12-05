package fit.soda.nicerabbit

import android.app.Application
import androidx.preference.PreferenceManager
import fit.soda.nicerabbit.newpipe.DownloaderImpl
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.downloader.Downloader

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        NewPipe.init(getDownloader())
    }

    protected fun getDownloader(): Downloader? {
        val downloader: DownloaderImpl = DownloaderImpl.init(null)
        setCookiesToDownloader(downloader)
        return downloader
    }

    protected fun setCookiesToDownloader(downloader: DownloaderImpl) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )
        val key = "recaptcha_cookies_key"
        downloader.setCookie("recaptcha_cookies", prefs.getString(key, null))
        downloader.updateYoutubeRestrictedModeCookies(applicationContext)
    }
}