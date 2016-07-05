package com.maelstrom.xkcd.xkcdmobile

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

class FullscreenActivity : AppCompatActivity() {

    private var viewTitle: String? = null
    private var altTitle: String? = null
    private var imageURL: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        val callIntent = intent

        imageURL = callIntent.getStringExtra("imageURL")
        viewTitle = callIntent.getStringExtra("imageTitle")
        altTitle = callIntent.getStringExtra("imageAlt")

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = viewTitle
            actionBar.setIcon(R.drawable.cueball)
        }

        FullscreenLoader(this).execute(imageURL)

        findViewById(R.id.altText)!!.setOnClickListener {
            val textView = TextView(this@FullscreenActivity)
            textView.setBackgroundColor(Color.YELLOW)
            textView.text = altTitle
            textView.setPadding(5, 10, 5, 10)
            textView.ellipsize = TextUtils.TruncateAt.END
            textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val toast = Toast(applicationContext)
            toast.view = textView
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun downloadComic(v: View) {
        val comicDownloader = ComicDownloader()
        if (comicDownloader.isReadyForDownload(this)) {
            if (!comicDownloader.isFilePresent("/xkcdComics/", viewTitle!! + ".png"))
                comicDownloader.DownloadFile(applicationContext, imageURL!!, "xkcdComics", viewTitle!!, ".png")
            else
                Toast.makeText(this, "Comic already downloaded...", Toast.LENGTH_SHORT).show()
        }
    }
}