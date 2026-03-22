package com.jarvis.movietracker.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadImage(url: String?, placeholder: Int = android.R.color.darker_gray) {
    Glide.with(this)
        .load(if (url != null) Constants.TMDB_IMAGE_BASE_URL + url else null)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}

fun String.formatReleaseYear(): String {
    return if (this.isNotEmpty()) {
        try {
            this.substring(0, 4)
        } catch (e: Exception) {
            this
        }
    } else {
        "N/A"
    }
}

