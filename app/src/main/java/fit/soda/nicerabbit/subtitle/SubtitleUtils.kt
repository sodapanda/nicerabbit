package fit.soda.nicerabbit.subtitle

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

class SubtitleUtils {
    fun convert(ttml: String): Subtitle {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(StringReader(ttml))
        val subtitle = Subtitle()
        var eventType = parser.eventType
        var readingContent = false
        var subItem: Pair<String, String> = Pair("", "")
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
                        subItem = Pair("${begin}:${end}", "")
                    }
                }
                XmlPullParser.TEXT -> {
                    if (readingContent) {
                        Log.i("nicerabbit", "text content:${parser.text}")
                        subItem = subItem.copy(second = parser.text)
                    }
                }
                XmlPullParser.END_TAG -> {
                    Log.i("nicerabbit", "end tag:${parser.name} ")
                    if (parser.name == "p") {
                        readingContent = false
                        subtitle.content.add(subItem)
                    }
                }
            }

            eventType = parser.next()
        }
        return subtitle
    }
}

class Subtitle {
    val content: MutableList<Pair<String, String>> = ArrayList()
}