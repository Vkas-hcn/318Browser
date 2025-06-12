package bigo.ad.zhf;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.annotation.Keep;

public class Xvfbg extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);
        if (i10 == 100) {
            Log.e("TAG", "onProgressChanged:onPageStarted=url="+i10);
            NJbg.bgmkh(i10);
        }
    }
}
