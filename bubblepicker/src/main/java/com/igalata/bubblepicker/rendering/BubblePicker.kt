package com.igalata.bubblepicker.rendering

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.MotionEvent
import com.igalata.bubblepicker.BubblePickerListener
import com.igalata.bubblepicker.R
import com.igalata.bubblepicker.adapter.BubblePickerAdapter
import com.igalata.bubblepicker.exception.ModePickerException
import com.igalata.bubblepicker.exception.UnSupportedActionPickerException
import com.igalata.bubblepicker.model.Color
import com.igalata.bubblepicker.model.PickerItem

/**
 * Created by simone on 1/1/18.
 */

class BubblePicker @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, mode: Int = 1) : GLSurfaceView(context, attrs) {
    companion object {
        val SPACE_FINITE: Int = 1
        val SPACE_INFINITE: Int = 2
    }

    @ColorInt
    var background: Int = 0
        set(value) {
            field = value
            renderer.backgroundColor = Color(value)
        }

    var adapter: BubblePickerAdapter? = null
        set(value) {
            field = value
            if (value != null) {
                renderer.items = ArrayList((0 until value.totalCount)
                        .map { value.getItem(it) }.toList())
            }
        }
    var maxSelectedCount: Int? = null
        set(value) {
            renderer.maxSelectedCount = value
        }
    var listener: BubblePickerListener? = null
        set(value) {
            renderer.listener = value
        }
    var bubbleSize = 50
        set(value) {
            if (value in 1..100) {
                renderer.bubbleSize = value
            }
        }

    val selectedItems: List<PickerItem?>
        get() = renderer.selectedItems

    var centerImmediately = false
        set(value) {
            if (mode == SPACE_INFINITE) {
                field = value
                renderer.centerImmediately = value
            } else throw UnSupportedActionPickerException()
        }
    private var renderer: PickerRenderer
    private var startX = 0f
    private var startY = 0f
    private var previousX = 0f
    private var previousY = 0f

    private var previousUpTime = 0L
    private var modeAssigned = false

    var mode: Int = 0
        set(value) {
            if (modeAssigned) throw UnSupportedActionPickerException()
            if (value in 1..2) field = value
            else throw ModePickerException()
        }

    init {
        this.mode = mode
        attrs?.let { retrieveAttributesBefore(attrs) }
        if (this.mode in 1..2) {
            modeAssigned = true
        } else {
            throw ModePickerException()
        }
        renderer = if (this.mode == SPACE_FINITE) PickerRendererFinite(this) else if (this.mode == SPACE_INFINITE) PickerRendererInfinite(this) else throw ModePickerException()

        this.setZOrderOnTop(true)
        this.setEGLContextClientVersion(2)
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder.setFormat(PixelFormat.RGBA_8888)
        this.setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
        attrs?.let { retrieveAttributesAfter(attrs) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                previousX = event.x
                previousY = event.y
            }
            MotionEvent.ACTION_UP -> {
                if (mode == SPACE_INFINITE) {
                    if (isClick(event)) renderer.resize(event.x, event.y)
                    renderer.release()
                } else if (mode == SPACE_FINITE) {
                    if (isClick(event) && !isTooFast()) renderer.resize(event.x, event.y)
                    previousUpTime = System.currentTimeMillis()
                    renderer.release()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if ((isSwipe(event)) && (mode == SPACE_INFINITE)) {
                    renderer.swipe(previousX - event.x, previousY - event.y)
                    previousX = event.x
                    previousY = event.y
                } else {
                    release()
                }
            }
            else -> release()
        }

        return true
    }

    private fun release() = postDelayed({ renderer.release() }, 0)

    private fun isClick(event: MotionEvent) = Math.abs(event.x - startX) < 20 && Math.abs(event.y - startY) < 20

    private fun isTooFast() = System.currentTimeMillis() - previousUpTime < 250

    private fun isSwipe(event: MotionEvent) = Math.abs(event.x - previousX) > 20 && Math.abs(event.y - previousY) > 20

    private fun retrieveAttributesAfter(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BubblePicker)

        if (array.hasValue(R.styleable.BubblePicker_maxSelectedCount)) {
            maxSelectedCount = array.getInt(R.styleable.BubblePicker_maxSelectedCount, -1)
        }

        if (array.hasValue(R.styleable.BubblePicker_backgroundColor)) {
            background = array.getColor(R.styleable.BubblePicker_backgroundColor, -1)
        }
        array.recycle()
    }

    private fun retrieveAttributesBefore(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BubblePicker)

        if (array.hasValue(R.styleable.BubblePicker_mode)) {
            mode = array.getInt(R.styleable.BubblePicker_mode, -1)
        }

        array.recycle()
    }

}
