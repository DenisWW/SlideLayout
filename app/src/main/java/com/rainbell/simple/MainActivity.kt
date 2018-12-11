package com.rainbell.simple

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.rainbell.simple.activity.TitleActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    internal var windowManager: WindowManager? = null
    var textView: TextView? = null
    var mParams: WindowManager.LayoutParams? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
        sample_text1.text = "我爱你";
        textView = TextView(this);
        textView!!.text = "他爱你";
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mParams = WindowManager.LayoutParams();
        mParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        mParams!!.format = PixelFormat.TRANSLUCENT

        sample_text1.setOnClickListener {
            //            var  number =10;
//            number = number shl 1
//            number = number shr 1;
//            Log.e("number","=="+Integer.toBinaryString(number));
//            if (textView!!.parent != null) {
//                var parent: ViewParent
//                parent = textView!!.parent
//                parent as ViewGroup
//                parent.removeView(textView)
//            }
//            windowManager!!.addView(textView, mParams)
            Toast.makeText(this, sample_text1!!.text, Toast.LENGTH_SHORT).show();

        }
        sample_text.setOnClickListener {
            val intent = Intent();
            intent.setClass(this, TitleActivity().javaClass)
//            intent.setClass(this, TitleActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_UP) {
//            Log.e("KEYCODE_BACK", "======")
//            return true
//        }
        Log.e("KEYCODE_BACK", "======")
        when (keyCode or event!!.action) {
            KeyEvent.KEYCODE_BACK or KeyEvent.ACTION_DOWN -> {
                Log.e("KEYCODE_BACK1", "======")
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
