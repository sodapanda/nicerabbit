package fit.soda.nicerabbit.newpipe;

import static org.schabi.newpipe.extractor.ServiceList.SoundCloud;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.StreamingService;

import java.util.concurrent.TimeUnit;

public final class ServiceHelper {
    private static final StreamingService DEFAULT_FALLBACK_SERVICE = ServiceList.YouTube;

    private ServiceHelper() {
    }

    @DrawableRes
    public static int getIcon(final int serviceId) {
//        switch (serviceId) {
//            case 0:
//                return R.drawable.place_holder_youtube;
//            case 1:
//                return R.drawable.place_holder_cloud;
//            case 2:
//                return R.drawable.place_holder_gadse;
//            case 3:
//                return R.drawable.place_holder_peertube;
//            case 4:
//                return R.drawable.place_holder_bandcamp;
//            default:
//                return R.drawable.place_holder_circle;
//        }
        return 0;
    }

    public static String getTranslatedFilterString(final String filter, final Context c) {
//        switch (filter) {
//            case "all":
//                return c.getString(R.string.all);
//            case "videos":
//            case "sepia_videos":
//            case "music_videos":
//                return c.getString(R.string.videos_string);
//            case "channels":
//                return c.getString(R.string.channels);
//            case "playlists":
//            case "music_playlists":
//                return c.getString(R.string.playlists);
//            case "tracks":
//                return c.getString(R.string.tracks);
//            case "users":
//                return c.getString(R.string.users);
//            case "conferences":
//                return c.getString(R.string.conferences);
//            case "events":
//                return c.getString(R.string.events);
//            case "music_songs":
//                return c.getString(R.string.songs);
//            case "music_albums":
//                return c.getString(R.string.albums);
//            case "music_artists":
//                return c.getString(R.string.artists);
//            default:
//                return filter;
//        }
        return "";
    }

    /**
     * Get a resource string with instructions for importing subscriptions for each service.
     *
     * @param serviceId service to get the instructions for
     * @return the string resource containing the instructions or -1 if the service don't support it
     */
    @StringRes
    public static int getImportInstructions(final int serviceId) {
//        switch (serviceId) {
//            case 0:
//                return R.string.import_youtube_instructions;
//            case 1:
//                return R.string.import_soundcloud_instructions;
//            default:
//                return -1;
//        }
        return 0;
    }


    public static long getCacheExpirationMillis(final int serviceId) {
        if (serviceId == SoundCloud.getServiceId()) {
            return TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
        } else {
            return TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
        }
    }

    public static boolean isBeta(final StreamingService s) {
        switch (s.getServiceInfo().getName()) {
            case "YouTube":
                return false;
            default:
                return true;
        }
    }
}
