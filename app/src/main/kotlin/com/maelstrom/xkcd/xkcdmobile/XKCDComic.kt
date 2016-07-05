package com.maelstrom.xkcd.xkcdmobile

import android.graphics.Bitmap

class XKCDComic {
    var title: String? = null
    var altText: String? = null

    var image: Bitmap? = null
    var imageURL: String? = null
    var currentURL: String? = null
    var nextURL: String? = null
    var prevURL: String? = null

    constructor() {
    }

    constructor(title: String, image: Bitmap) {
        this.title = title
        this.image = image
    }
}
