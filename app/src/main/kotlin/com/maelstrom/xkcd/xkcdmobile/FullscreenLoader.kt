package com.maelstrom.xkcd.xkcdmobile

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import uk.co.senab.photoview.PhotoViewAttacher
import java.net.URL

class FullscreenLoader(private val mActivity: Activity) : AsyncTask<String, Int, Bitmap>() {

    private var photoViewAttacher: PhotoViewAttacher? = null
    private var progressBar: ProgressBar? = null

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar = mActivity.findViewById(R.id.imageLoader) as ProgressBar
        progressBar!!.max = 3
        progressBar!!.progress = 0
        progressBar!!.visibility = View.VISIBLE
    }

    override fun onPostExecute(bitmap: Bitmap) {
        super.onPostExecute(bitmap)

        progressBar!!.visibility = View.INVISIBLE

        val imageView = mActivity.findViewById(R.id.fullscreen_content) as ImageView
        imageView.setImageBitmap(bitmap)

        if (photoViewAttacher != null)
            photoViewAttacher!!.update()
        else
            photoViewAttacher = PhotoViewAttacher(imageView, true)
    }

    protected fun onProgressUpdate(vararg values: Int) {
        super.onProgressUpdate(values[0])
        progressBar!!.progress = values[0]
    }

    override fun doInBackground(vararg params: String): Bitmap {

        val url = params[0]
        publishProgress(1)
        var bmp: Bitmap? = null
        try {
            val displayURL = URL(url)
            bmp = BitmapFactory.decodeStream(displayURL.openConnection().inputStream)
            publishProgress(2)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bmp!!
    }
}