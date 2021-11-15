package fit.soda.nicerabbit.download;

import goandroid.Goandroid;

public class Download {
    public void get() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Goandroid.exampleClient("/sdcard/good.mp4", "RsKfMkHT1EU");
            }
        }).start();
    }
}
