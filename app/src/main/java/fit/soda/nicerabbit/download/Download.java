package fit.soda.nicerabbit.download;

import android.content.Context;
import android.os.Handler;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import android.widget.Toast;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.util.List;

import goandroid.FfmpegCmd;
import goandroid.Goandroid;

public class Download {
    public void get(Context context, Handler handler, String id) {
        try {
            Os.setenv("HTTPS_PROXY", "https://192.168.22.254:8080", true);
        } catch (ErrnoException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = context.getFilesDir().getAbsolutePath();
                Goandroid.downloadYoutube("https://www.youtube.com/watch?v=kZOIL4CC2sM", path + "/", "youtube", new FfmpegCmd() {
                    @Override
                    public String runCmd(String cmd) {
                        android.util.Log.i("ffmpeg", "cmd is " + cmd);
                        FFmpegSession session = FFmpegKit.execute(cmd);
                        if (ReturnCode.isSuccess(session.getReturnCode())) {
                            String log = session.getAllLogsAsString();
                            android.util.Log.i("ffmpeg", log);
                        } else {
                            android.util.Log.i("ffmpeg", session.getAllLogsAsString());
                        }
                        return "ok";
                    }
                });

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public static void testNewPipe(String url, StreamListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StreamInfo streamInfo = StreamInfo.getInfo(NewPipe.getService(0), url);
                    List<VideoStream> streams = streamInfo.getVideoStreams();
                    for (VideoStream stream : streams) {
                        if (stream.getQuality().equals("medium")) {
                            Log.i("nicerabbit", stream.getUrl());
                            listener.onResponse(stream.getUrl());
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface StreamListener {
        void onResponse(String url);
    }
}
