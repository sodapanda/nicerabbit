package fit.soda.nicerabbit.subtitle

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

class SubtitleUtils {
    fun convert(ttml: String): List<Subtitle> {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(StringReader(ttml))
        val subtitleList = ArrayList<Subtitle>()
        var currentSubtitle = Subtitle()
        var eventType = parser.eventType
        var readingContent = false
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val begin = parser.getAttributeValue(null, "begin")
                    val end = parser.getAttributeValue(null, "end")
                    Log.i(
                        "nicerabbit",
                        "start tag name:${parser.name} text:${parser.text} begin:${begin} end${end}"
                    )
                    if (parser.name == "p") {
                        readingContent = true
                        currentSubtitle = Subtitle()
                        currentSubtitle.begin = begin
                        currentSubtitle.end = end
                    }
                }
                XmlPullParser.TEXT -> {
                    if (readingContent) {
                        Log.i("nicerabbit", "text content:${parser.text}")
                        currentSubtitle.text = parser.text
                    }
                }
                XmlPullParser.END_TAG -> {
                    Log.i("nicerabbit", "end tag:${parser.name} ")
                    if (parser.name == "p") {
                        readingContent = false
                        subtitleList.add(currentSubtitle)
                    }
                }
            }

            eventType = parser.next()
        }
        return subtitleList
    }
}

class Subtitle {
    var text: String? = ""
    var highLight: Boolean = false
    var begin: String? = ""
    var end: String? = ""
}