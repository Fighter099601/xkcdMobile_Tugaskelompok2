package com.maelstrom.xkcd.xkcdmobile

import android.graphics.Bitmap

class XKCDComic {
    var title: String? = null
    var altText: String? = null

    var id: Int? = 0
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

    /*
    {
    "month": "6",
    "num": 112,
    "link": "",
    "year": "2006",
    "news": "",
    "safe_title": "Baring My Heart",
    "transcript": "[[A venn diagram with three sets]]\nDescription of set 1: People who can always make me smile\nDescription of set 2: People who constantly show me new things about the world\nDescription of set 3: People I want to spend the rest of my life with\nIntersection point: YOU.\nIntersection of sets 2 and 3: Vanilla Ice\n{{title text: I'm just trying to explain, please don't be jealous! Man, why are all my relationships ruined by early 90's rappers?}}", "alt": "I'm just trying to explain, please don't be jealous!  Man, why are all my relationships ruined by early 90's rappers?",
    "img": "http:\/\/imgs.xkcd.com\/comics\/baring_my_heart.png",
    "title": "Baring My Heart",
    "day": "7"
    }
     */
}
