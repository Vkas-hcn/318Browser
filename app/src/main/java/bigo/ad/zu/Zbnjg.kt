package bigo.ad.zu

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class Zbnjg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "Zbnjg-onCreate")

    }

    override fun onDestroy() {
        (window.decorView as? ViewGroup)?.removeAllViews()
        super.onDestroy()
    }
}
