package bigo.ad.zhf;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.Keep;


public class Qbjfr extends Handler {
    public Qbjfr() {

    }
    @Override
    public void handleMessage(Message message) {
        int r0 = message.what;
        NJbg.bgmkh(r0);
    }
}

