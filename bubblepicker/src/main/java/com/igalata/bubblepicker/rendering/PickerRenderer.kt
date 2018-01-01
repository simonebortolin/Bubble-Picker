package com.igalata.bubblepicker.rendering

import android.opengl.GLSurfaceView
import com.igalata.bubblepicker.BubblePickerListener
import com.igalata.bubblepicker.model.Color
import com.igalata.bubblepicker.model.PickerItem
import java.util.*

/**
 * Created by simone on 1/1/18.
 */

interface PickerRenderer : GLSurfaceView.Renderer {
    var backgroundColor: Color?
    var maxSelectedCount: Int?
    var bubbleSize: Int
    var listener: BubblePickerListener?
    var items: ArrayList<PickerItem>
    val selectedItems: List<PickerItem?>
    var centerImmediately: Boolean

    fun release()

    fun swipe(x: Float, y: Float)

    fun resize(x: Float, y: Float): Item?
}