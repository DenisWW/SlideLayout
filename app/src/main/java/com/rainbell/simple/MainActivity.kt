package com.rainbell.simple

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.rainbell.simple.activity.TitleActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
        sample_text1.text = "我爱你";
        sample_text1.setOnClickListener {
//            var  number =10;
//            number = number shl 1
//            number = number shr 1;
//            Log.e("number","=="+Integer.toBinaryString(number));
            Toast.makeText(this, sample_text1!!.text, Toast.LENGTH_SHORT).show();
        };
        sample_text.setOnClickListener {
            val intent = Intent();
            intent.setClass(this, TitleActivity().javaClass)
//            intent.setClass(this, TitleActivity::class.java)
            startActivity(intent)

        }

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
