package com.igalata.bubblepicker.rendering

import android.opengl.GLES20
import android.view.View
import com.igalata.bubblepicker.*
import com.igalata.bubblepicker.exception.ModePickerException
import com.igalata.bubblepicker.exception.UnSupportedActionPickerException
import com.igalata.bubblepicker.model.Color
import com.igalata.bubblepicker.model.PickerItem
import com.igalata.bubblepicker.physics.EngineFinite
import com.igalata.bubblepicker.physics.EngineInfinite
import org.jbox2d.common.Vec2
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by irinagalata on 1/19/17.
 */
class PickerRendererFinite(val glView: View) : PickerRenderer {

    override var backgroundColor: Color? = null
    override var maxSelectedCount: Int? = null
        set(value) {
            EngineFinite.maxSelectedCount = value
        }
    override var bubbleSize = 50
        set(value) {
            EngineFinite.radius = value
        }
    override var listener: BubblePickerListener? = null
    override lateinit var items: ArrayList<PickerItem>
    override val selectedItems: List<PickerItem?>
        get() = EngineInfinite.selectedBodies.map { circles.firstOrNull { circle -> circle.circleBody == it }?.pickerItem }
    override var centerImmediately: Boolean
        get() = throw UnSupportedActionPickerException()
        set(value) = throw UnSupportedActionPickerException()

    private var programId = 0
    private var verticesBuffer: FloatBuffer? = null
    private var uvBuffer: FloatBuffer? = null
    private var vertices: FloatArray? = null
    private var textureVertices: FloatArray? = null
    private var textureIds: IntArray? = null

    private val scaleX: Float
        get() = if (glView.width < glView.height) glView.height.toFloat() / glView.width.toFloat() else 1f
    private val scaleY: Float
        get() = if (glView.width < glView.height) 1f else glView.width.toFloat() / glView.height.toFloat()
    private val circles = ArrayList<Item>()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(backgroundColor?.red ?: 1f, backgroundColor?.green ?: 1f,
                backgroundColor?.blue ?: 1f, backgroundColor?.alpha ?: 1f)
        enableTransparency()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        initialize()
    }

    override fun onDrawFrame(gl: GL10?) {
        calculateVertices()
        EngineFinite.move()
        drawFrame()
    }

    private fun initialize() {
        clear()
        EngineFinite.build(items.size, scaleX, scaleY).forEachIndexed { index, body ->
                    circles.add(Item(glView.context, items[index], body))
                }
        items.forEach { if (it.isSelected) EngineFinite.resize(circles.first { circle -> circle.pickerItem == it }) }
        if (textureIds == null) textureIds = IntArray(circles.size * 2)
        initializeArrays()
    }

    private fun initializeArrays() {
        vertices = FloatArray(circles.size * 8)
        textureVertices = FloatArray(circles.size * 8)
        circles.forEachIndexed { i, item -> initializeItem(item, i) }
        verticesBuffer = vertices?.toFloatBuffer()
        uvBuffer = textureVertices?.toFloatBuffer()
    }

    private fun initializeItem(item: Item, index: Int) {
        initializeVertices(item, index)
        textureVertices?.passTextureVertices(index)
        item.bindTextures(textureIds ?: IntArray(0), index)
    }

    private fun calculateVertices() {
        circles.forEachIndexed { i, item -> initializeVertices(item, i) }
        vertices?.forEachIndexed { i, float -> verticesBuffer?.put(i, float) }
    }

    private fun initializeVertices(body: Item, index: Int) {
        val radius = body.radius
        val radiusX = radius * scaleX
        val radiusY = radius * scaleY

        body.initialPosition.apply {
            vertices?.put(8 * index, floatArrayOf(x - radiusX, y + radiusY, x - radiusX, y - radiusY,
                    x + radiusX, y + radiusY, x + radiusX, y - radiusY))
        }
    }

    private fun drawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniform4f(GLES20.glGetUniformLocation(programId, BubbleShader.U_BACKGROUND), 1f, 1f, 1f, 0f)
        verticesBuffer?.passToShader(programId, BubbleShader.A_POSITION)
        uvBuffer?.passToShader(programId, BubbleShader.A_UV)
        circles.forEachIndexed { i, circle -> circle.drawItself(programId, i, scaleX, scaleY) }
    }

    private fun enableTransparency() {
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        attachShaders()
    }

    private fun attachShaders() {
        programId = createProgram(createShader(GLES20.GL_VERTEX_SHADER, BubbleShader.vertexShader),
                createShader(GLES20.GL_FRAGMENT_SHADER, BubbleShader.fragmentShader))
        GLES20.glUseProgram(programId)
    }

    private fun createProgram(vertexShader: Int, fragmentShader: Int) = GLES20.glCreateProgram().apply {
                GLES20.glAttachShader(this, vertexShader)
                GLES20.glAttachShader(this, fragmentShader)
                GLES20.glLinkProgram(this)
            }

    private fun createShader(type: Int, shader: String) = GLES20.glCreateShader(type).apply {
                GLES20.glShaderSource(this, shader)
                GLES20.glCompileShader(this)
            }

    override fun release() = EngineFinite.release()

    private fun getItem(position: Vec2) = position.let {
        val x = it.x.convertPoint(glView.width, scaleX)
        val y = it.y.convertPoint(glView.height, scaleY)
        circles.find { Math.sqrt(((x - it.x).sqr() + (y - it.y).sqr()).toDouble()) <= it.radius }
    }

    override fun swipe(x: Float, y: Float) = throw ModePickerException()

    override fun resize(x: Float, y: Float) = getItem(Vec2(x, glView.height - y))?.apply {
        if (EngineFinite.resize(this)) {
            listener?.let {
                if (circleBody.increased) it.onBubbleDeselected(pickerItem) else it.onBubbleSelected(pickerItem)
            }
        }
    }

    private fun clear() {
        circles.clear()
        EngineFinite.clear()
    }

}