package com.hexadecimal.snapchatclone

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL


class ViewSnapActivity : AppCompatActivity() {

    var messageTextView: TextView? = null
    var snapImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)

        messageTextView?.text = intent.getStringExtra("message")

        val task =  ImageDownloader()
        val myImage : Bitmap
        try {                                                   // indirilecek resmin adresini verdik
            myImage = task.execute(intent.getStringExtra("imageURL")).get()
            snapImageView?.setImageBitmap(myImage)           // gelen resmi snapImageView ogesine bastırdık

        } catch (e : Exception) {
            e.printStackTrace()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {

            try {
                val url = URL(urls[0])

                val connection = url.openConnection() as HttpURLConnection
                connection.connect()         // baglantiyi kurduk

                val `in` = connection.inputStream                     // gelen verileri almak icin kullandik
                return BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
