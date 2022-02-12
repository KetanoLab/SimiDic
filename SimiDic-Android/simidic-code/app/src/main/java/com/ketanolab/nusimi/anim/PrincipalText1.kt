package com.ketanolab.nusimi.anim

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.ViewSwitcher
import com.ketanolab.nusimi.R

class PrincipalText(private val context: Context) : ViewSwitcher.ViewFactory {
    override fun makeView(): View {
        val t = TextView(context)
        t.gravity = Gravity.CENTER
        t.textSize = 20f
        t.setTextColor(context.resources.getColor(R.color.black1))
        return t
    }
}