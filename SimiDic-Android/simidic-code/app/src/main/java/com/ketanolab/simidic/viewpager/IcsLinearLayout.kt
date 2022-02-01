package com.ketanolab.simidic.viewpager

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout

/**
 * A simple extension of a regular linear layout that supports the divider API
 * of Android 4.0+. The dividers are added adjacent to the children by changing
 * their layout params. If you need to rely on the margins which fall in the
 * same orientation as the layout you should wrap the child in a simple
 * [android.widget.FrameLayout] so it can receive the margin.
 */
internal class IcsLinearLayout(context: Context, themeAttr: Int) : LinearLayout(context) {
    private var mDivider: Drawable? = null
    private var mDividerWidth = 0
    private var mDividerHeight = 0
    private val mShowDividers: Int
    private val mDividerPadding: Int
    override fun setDividerDrawable(divider: Drawable) {
        if (divider === mDivider) {
            return
        }
        mDivider = divider
        if (divider != null) {
            mDividerWidth = divider.intrinsicWidth
            mDividerHeight = divider.intrinsicHeight
        } else {
            mDividerWidth = 0
            mDividerHeight = 0
        }
        setWillNotDraw(divider == null)
        requestLayout()
    }

    override fun measureChildWithMargins(
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
        val index = indexOfChild(child)
        val orientation = orientation
        val params = child.layoutParams as LayoutParams
        if (hasDividerBeforeChildAt(index)) {
            if (orientation == VERTICAL) {
                //Account for the divider by pushing everything up
                params.topMargin = mDividerHeight
            } else {
                //Account for the divider by pushing everything left
                params.leftMargin = mDividerWidth
            }
        }
        val count = childCount
        if (index == count - 1) {
            if (hasDividerBeforeChildAt(count)) {
                if (orientation == VERTICAL) {
                    params.bottomMargin = mDividerHeight
                } else {
                    params.rightMargin = mDividerWidth
                }
            }
        }
        super.measureChildWithMargins(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (mDivider != null) {
            if (orientation == VERTICAL) {
                drawDividersVertical(canvas)
            } else {
                drawDividersHorizontal(canvas)
            }
        }
        super.onDraw(canvas)
    }

    private fun drawDividersVertical(canvas: Canvas) {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child != null && child.visibility != GONE) {
                if (hasDividerBeforeChildAt(i)) {
                    val lp = child.layoutParams as LayoutParams
                    val top = child.top - lp.topMargin /* - mDividerHeight*/
                    drawHorizontalDivider(canvas, top)
                }
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            val child = getChildAt(count - 1)
            var bottom = 0
            bottom = //final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child?.bottom /* + lp.bottomMargin*/ ?: height - paddingBottom - mDividerHeight
            drawHorizontalDivider(canvas, bottom)
        }
    }

    private fun drawDividersHorizontal(canvas: Canvas) {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child != null && child.visibility != GONE) {
                if (hasDividerBeforeChildAt(i)) {
                    val lp = child.layoutParams as LayoutParams
                    val left = child.left - lp.leftMargin /* - mDividerWidth*/
                    drawVerticalDivider(canvas, left)
                }
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            val child = getChildAt(count - 1)
            var right = 0
            right = //final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child?.right /* + lp.rightMargin*/ ?: width - paddingRight - mDividerWidth
            drawVerticalDivider(canvas, right)
        }
    }

    private fun drawHorizontalDivider(canvas: Canvas, top: Int) {
        mDivider!!.setBounds(
            paddingLeft + mDividerPadding, top,
            width - paddingRight - mDividerPadding, top + mDividerHeight
        )
        mDivider!!.draw(canvas)
    }

    private fun drawVerticalDivider(canvas: Canvas, left: Int) {
        mDivider!!.setBounds(
            left, paddingTop + mDividerPadding,
            left + mDividerWidth, height - paddingBottom - mDividerPadding
        )
        mDivider!!.draw(canvas)
    }

    private fun hasDividerBeforeChildAt(childIndex: Int): Boolean {
        if (childIndex == 0 || childIndex == childCount) {
            return false
        }
        if (mShowDividers and SHOW_DIVIDER_MIDDLE != 0) {
            var hasVisibleViewBefore = false
            for (i in childIndex - 1 downTo 0) {
                if (getChildAt(i).visibility != GONE) {
                    hasVisibleViewBefore = true
                    break
                }
            }
            return hasVisibleViewBefore
        }
        return false
    }

    companion object {
        private val LL = intArrayOf( /* 0 */
            android.R.attr.divider,  /* 1 */
            android.R.attr.showDividers,  /* 2 */
            android.R.attr.dividerPadding
        )
        private const val LL_DIVIDER = 0
        private const val LL_SHOW_DIVIDER = 1
        private const val LL_DIVIDER_PADDING = 2
    }

    init {
        val a = context.obtainStyledAttributes(null, LL, themeAttr, 0)
        dividerDrawable = a.getDrawable(LL_DIVIDER)!!
        mDividerPadding = a.getDimensionPixelSize(LL_DIVIDER_PADDING, 0)
        mShowDividers = a.getInteger(LL_SHOW_DIVIDER, SHOW_DIVIDER_NONE)
        a.recycle()
    }
}