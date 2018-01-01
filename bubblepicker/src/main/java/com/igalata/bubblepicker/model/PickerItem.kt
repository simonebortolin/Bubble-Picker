package com.igalata.bubblepicker.model

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.util.TypedValue

/**
 * Created by irinagalata on 1/19/17.
 */
data class PickerItem @JvmOverloads constructor(var title: String? = null,
                                                var titleBroken: String? = null,
                                                var bubbleStyle: BubbleStyle,
                                                var bubbleSelectedStyle: BubbleStyle,
                                                var isSelected: Boolean = false,
                                                var minTextSize: Int = 8,
                                                var maxTextSize: Int = 12,
                                                var textSizeUnit: Int = TypedValue.COMPLEX_UNIT_SP) {
    @JvmOverloads constructor(title: String? = null,
                              titleBroken: String? = null,
                              icon: Drawable? = null,
                              showIconInSelectedBubble: Boolean = false,
                              showIconInBubble: Boolean = false,
                              @ColorInt color: Int? = null,
                              @ColorInt selectedColor: Int? = null,
                              @ColorInt textColor: Int? = null,
                              @ColorInt selectedTextColor: Int? = null,
                              @ColorInt borderColor: Int? = null,
                              @ColorInt borderSelectedColor: Int? = null,
                              isSelected: Boolean = false,
                              minTextSize: Int = 8,
                              maxTextSize: Int = 12,
                              textSizeUnit: Int = TypedValue.COMPLEX_UNIT_SP) : this(title, titleBroken, BubbleStyle(color, textColor, borderColor, if (showIconInBubble) icon else null), BubbleStyle(selectedColor, selectedTextColor, borderSelectedColor, if (showIconInSelectedBubble) icon else null), isSelected, minTextSize, maxTextSize, textSizeUnit)

    var color: Int?
        @ColorInt
        get() = bubbleStyle.backgroundColor
        set(value) {
            bubbleStyle.backgroundColor = value
        }

    var selectedColor: Int?
        @ColorInt
        get() = bubbleSelectedStyle.backgroundColor
        set(value) {
            bubbleSelectedStyle.backgroundColor = value
        }

    var textColor: Int?
        @ColorInt
        get() = bubbleStyle.textColor
        set(value) {
            bubbleStyle.textColor = value
        }

    var selectedTextColor: Int?
        @ColorInt
        get() = bubbleSelectedStyle.textColor
        set(value) {
            bubbleSelectedStyle.textColor = value
        }

    var borderColor: Int?
        @ColorInt
        get() = bubbleStyle.borderColor
        set(value) {
            bubbleStyle.borderColor = value
        }

    var selectedBorderColor: Int?
        @ColorInt
        get() = bubbleSelectedStyle.borderColor
        set(value) {
            bubbleSelectedStyle.borderColor = value
        }

    var overlayAlpha: Float
        get() = if (isSelected) bubbleSelectedStyle.overlayAlpha else bubbleStyle.overlayAlpha
        set(value) {
            if (isSelected)
                bubbleSelectedStyle.overlayAlpha = value
            else bubbleStyle.overlayAlpha = value
        }
}


data class BubbleStyle(@ColorInt var backgroundColor: Int? = null,
                       @ColorInt var textColor: Int? = null,
                       @ColorInt var borderColor: Int? = null,
                       var icon: Drawable? = null,
                       var iconPosition: IconPosition? = null,
                       var image: Drawable? = null,
                       var gradient: BubbleGradient? = null,
                       var overlayAlpha: Float = 1f)


enum class IconPosition { Top, Bottom }


data class BubbleGradient @JvmOverloads constructor(val startColor: Int,
                                                    val endColor: Int,
                                                    val direction: Int = HORIZONTAL) {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    private val bitmapSize = 256f

    internal val gradient: LinearGradient?
        get() {
            val horizontal = this.direction == BubbleGradient.HORIZONTAL
            return LinearGradient(if (horizontal) 0f else bitmapSize / 2f,
                    if (horizontal) bitmapSize / 2f else 0f,
                    if (horizontal) bitmapSize else bitmapSize / 2f,
                    if (horizontal) bitmapSize / 2f else bitmapSize,
                    this.startColor, this.endColor, Shader.TileMode.CLAMP)
        }

}