package com.maelstrom.xkcd.xkcdmobile

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.URL

class NewComicLoader(private val mActivity: Activity) : AsyncTask<String, Int, XKCDComic>() {
    override fun onPreExecute() {
        super.onPreExecute()
        val progressBar = mActivity.findViewById(R.id.progressBar) as ProgressBar
        progressBar.progress = 0
        progressBar.visibility = View.VISIBLE
        progressBar.max = 5
    }

    override fun onPostExecute(xkcdComic: XKCDComic?) {
        super.onPostExecute(xkcdComic)

        val mProgressBar = mActivity.findViewById(R.id.progressBar) as ProgressBar
        mProgressBar.visibility = View.INVISIBLE

        val textView = mActivity.findViewById(R.id.comicTitle) as TextView
        val imageView = mActivity.findViewById(R.id.comicImage) as ImageView

        if (xkcdComic != null) {
            textView.text = xkcdComic.title
            textView.setTextColor(Color.WHITE)
            textView.contentDescription = xkcdComic.imageURL

            imageView.setImageBitmap(xkcdComic.image)
            imageView.contentDescription = xkcdComic.currentURL

            mActivity.findViewById(R.id.prevButton).contentDescription = xkcdComic.prevURL
            mActivity.findViewById(R.id.nextButton).contentDescription = xkcdComic.nextURL

            mProgressBar.contentDescription = xkcdComic.altText
        }
    }

    protected fun onProgressUpdate(vararg values: Int) {
        super.onProgressUpdate(values[0])
        val mProgressBar = mActivity.findViewById(R.id.progressBar) as ProgressBar
        mProgressBar.progress = values[0]
    }

    override fun doInBackground(vararg params: String): XKCDComic {
        val doc: Document
        val imgURL: String
        val xkcdComic = XKCDComic()
        try {
            doc = Jsoup.connect(params[0] + params[1]).get()

            val mainDiv = doc.getElementById("comic")
            val mainImg = mainDiv.getElementsByTag("img").first()
            imgURL = mainImg.absUrl("src")
            xkcdComic.title = mainImg.attr("alt")
            xkcdComic.altText = mainImg.attr("title")
            publishProgress(1)

            val url = URL(imgURL)
            xkcdComic.imageURL = imgURL
            val comicImg = BitmapFactory.decodeStream(url.openConnection().inputStream)
            xkcdComic.image = comicImg
            publishProgress(2)

            val navDiv = doc.getElementsByClass("comicNav").first()
            val prevUrl = navDiv.child(1).getElementsByTag("a").first().attr("href")
            xkcdComic.prevURL = prevUrl
            publishProgress(3)
            val nextUrl = navDiv.child(3).getElementsByTag("a").first().attr("href")
            xkcdComic.nextURL = nextUrl
            publishProgress(4)

            xkcdComic.currentURL = params[1]

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return xkcdComic
    }
}