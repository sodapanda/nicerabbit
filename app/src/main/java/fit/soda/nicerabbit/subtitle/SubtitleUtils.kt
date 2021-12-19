package fit.soda.nicerabbit.subtitle

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

class SubtitleUtils {
    fun convert(ttml: String): Subtitle {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(StringReader(ttml))
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {


            eventType = parser.next()
        }
        return Subtitle()
    }
}

class Subtitle {
    val content: List<Map<String, String>>? = null
}