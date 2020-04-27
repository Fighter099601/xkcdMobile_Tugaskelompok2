package com.maelstrom.xkcd.xkcdmobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private var baseUrl: String? = null
    private var currentUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        baseUrl = "https://xkcd.com/2273/"
        currentUrl = "https://imgs.xkcd.com/comics/truck_proximity.png"

        findViewById(R.id.parent)!!.setOnTouchListener(SwipeHandler(applicationContext, this))
        NewComicLoader(this).execute(baseUrl, currentUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_share) {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/plain"
            currentUrl = findViewById(R.id.comicImage)!!.contentDescription.toString()
            sendIntent.putExtra(Intent.EXTRA_TEXT, baseUrl!! + currentUrl!!)
            startActivity(Intent.createChooser(sendIntent, "Share comic through"))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun prevComic(v: View) {
        val tmp = v as Button
        if (tmp.contentDescription != null) {
            val prevUrl = tmp.contentDescription.toString()
            NewComicLoader(this).execute(baseUrl, prevUrl)
        }
    }

    fun nextComic(v: View) {
        val tmp = v as Button
        if (tmp.contentDescription != null) {
            val nextUrl = tmp.contentDescription.toString()
            NewComicLoader(this).execute(baseUrl, nextUrl)
        }
    }

    fun goFullscreen(v: View) {
        val fullscreenIntent = Intent(this, FullscreenActivity::class.java)
        currentUrl = findViewById(R.id.comicTitle)!!.contentDescription.toString()
        val title = (findViewById(R.id.comicTitle) as TextView).text.toString()
        val alt = findViewById(R.id.progressBar)!!.contentDescription.toString()
        Toast.makeText(this, title, Toast.LENGTH_LONG).show()
        fullscreenIntent.putExtra("imageURL", currentUrl)
        fullscreenIntent.putExtra("imageTitle", title)
        fullscreenIntent.putExtra("imageAlt", alt)

        startActivity(fullscreenIntent)
    }


    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {

        Toast.makeText(this, "Fling", Toast.LENGTH_SHORT).show()
        val xDiff = e1.x - e2.x
        val yDiff = e1.y - e2.y

        if (Math.abs(xDiff) > Math.abs(yDiff) && Math.abs(xDiff) > 100)
            if (xDiff < 0)
                prevComic(findViewById(R.id.prevButton)!!)
            else
                nextComic(findViewById(R.id.nextButton)!!)

        return true
    }
}