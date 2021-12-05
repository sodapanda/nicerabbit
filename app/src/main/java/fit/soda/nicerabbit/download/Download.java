package fit.soda.nicerabbit.download;

import android.content.Context;
import android.os.Handler;
import android.system.ErrnoException;
import android.system.Os;
import android.widget.Toast;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;

import goandroid.FfmpegCmd;
import goandroid.Goandroid;

public class Download {
    public void get(Context context, Handler handler, String id) {
        try {
            Os.setenv("HTTPS_PROXY","https://192.168.22.254:8080",true);
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
}
