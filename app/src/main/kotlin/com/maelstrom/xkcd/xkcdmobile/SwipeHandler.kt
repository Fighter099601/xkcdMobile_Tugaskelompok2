package com.maelstrom.xkcd.xkcdmobile

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class SwipeHandler : View.OnTouchListener, GestureDetector.OnGestureListener {

    private var gestureDetector: GestureDetector? = null
    private var mainActivity: MainActivity?= null
    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100


    constructor(c: Context, activity: MainActivity) {
        gestureDetector = GestureDetector(c, this)
        this.mainActivity = activity
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        val diffY = e2!!.y - e1!!.y
        val diffX = e2.x - e1.x
        if (Math.abs(diffX) > Math.abs(diffY))
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0)
                    onSwipeRight()
                else
                    onSwipeLeft()

                return true
            } else
                return false
        else
            return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        throw UnsupportedOperationException()
    }

    fun onSwipeRight() {
        mainActivity!!.prevComic(mainActivity!!.findViewById(R.id.prevButton)!!)
    }

    fun onSwipeLeft() {
        mainActivity!!.nextComic(mainActivity!!.findViewById(R.id.nextButton)!!)
    }
}