package com.ketanolab.simidic.anim

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ViewSwitcher

class ImageLogo(private val context: Context) : ViewSwitcher.ViewFactory {
    override fun makeView(): View {
        return ImageView(context)
    }
}