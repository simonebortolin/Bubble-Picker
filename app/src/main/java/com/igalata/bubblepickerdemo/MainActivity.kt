package com.igalata.bubblepickerdemo

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.igalata.bubblepicker.BubblePickerListener
import com.igalata.bubblepicker.adapter.BubblePickerAdapter
import com.igalata.bubblepicker.model.BubbleGradient
import com.igalata.bubblepicker.model.BubbleStyle
import com.igalata.bubblepicker.model.PickerItem
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val boldTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_BOLD) }
    private val mediumTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_MEDIUM) }
    private val regularTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_REGULAR) }

    companion object {
        private const val ROBOTO_BOLD = "roboto_bold.ttf"
        private const val ROBOTO_MEDIUM = "roboto_medium.ttf"
        private const val ROBOTO_REGULAR = "roboto_regular.ttf"
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_simple -> {
                pickerSimple.visibility = View.VISIBLE
                pickerGradient.visibility = View.GONE
                pickerImage.visibility = View.GONE
                true
            }
            R.id.navigation_gradient -> {
                pickerSimple.visibility = View.GONE
                pickerGradient.visibility = View.VISIBLE
                pickerImage.visibility = View.GONE
                true
            }
            R.id.navigation_imgae -> {
                pickerSimple.visibility = View.GONE
                pickerGradient.visibility = View.GONE
                pickerImage.visibility = View.VISIBLE
                true
            }
            else -> true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)



        simple()
        gradeient()
        image()

        pickerSimple.bubbleSize = 10
        pickerGradient.bubbleSize = 10
        pickerImage.bubbleSize = 10

        val bubblePickerListener = object : BubblePickerListener {
            override fun onBubbleSelected(item: PickerItem) = toast("${item.title} selected")

            override fun onBubbleDeselected(item: PickerItem) = toast("${item.title} deselected")
        }

        pickerSimple.listener = bubblePickerListener
        pickerGradient.listener = bubblePickerListener
        pickerImage.listener = bubblePickerListener

    }

    override fun onResume() {
        super.onResume()
        pickerSimple.onResume()
        pickerGradient.onResume()
        pickerImage.onResume()

    }

    override fun onPause() {
        super.onPause()
        pickerSimple.onPause()
        pickerGradient.onPause()
        pickerImage.onPause()

    }

    private fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private fun simple() {
        val titles = resources.getStringArray(R.array.countries)
        val colors = resources.obtainTypedArray(R.array.colors)

        pickerSimple.adapter = object : BubblePickerAdapter {

            override val totalCount = titles.size + 1

            override fun getItem(position: Int): PickerItem {
                return PickerItem().apply {
                    if (position >= titles.size) {
                        title = "ReallyLongCountryName"
                        titleBroken = "ReallyLongCountry-\nName"
                    } else {
                        title = titles[position]
                    }

                    color = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length(), 0))
                    textColor = ContextCompat.getColor(this@MainActivity, android.R.color.black)
                    selectedColor = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length() + 1, 0))
                    selectedTextColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                }
            }
        }

        colors.recycle()
    }

    private fun image() {
        val titles = resources.getStringArray(R.array.countries)
        val colors = resources.obtainTypedArray(R.array.colors)

        pickerImage.adapter = object : BubblePickerAdapter {

            override val totalCount = titles.size + 1

            override fun getItem(position: Int): PickerItem {
                return PickerItem().apply {
                    if (position >= titles.size) {
                        title = "ReallyLongCountryName"
                        titleBroken = "ReallyLongCountry-\nName"
                    } else {
                        title = titles[position]
                    }

                    color = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length(), 0))
                    textColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                    selectedColor = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length() + 1, 0))
                    selectedTextColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                }
            }
        }

        colors.recycle()
    }

    private fun gradeient() {
        val titles = resources.getStringArray(R.array.countries)
        val colors = resources.obtainTypedArray(R.array.colors_gradient)

        pickerGradient.adapter = object : BubblePickerAdapter {

            override val totalCount = titles.size + 1

            override fun getItem(position: Int): PickerItem {
                return PickerItem().apply {
                    if (position >= titles.size) {
                        title = "ReallyLongCountryName"
                        titleBroken = "ReallyLongCountry-\nName"
                    } else {
                        title = titles[position]
                    }

                    bubbleStyle = BubbleStyle(
                            textColor = ContextCompat.getColor(this@MainActivity, android.R.color.white),
                            gradient = BubbleGradient(colors.getColor((position * 2) % 8, 0),
                                    colors.getColor((position * 2) % 8 + 1, 0), if (position % 2 == 0) BubbleGradient.VERTICAL else BubbleGradient.HORIZONTAL)
                    )

                    bubbleSelectedStyle = BubbleStyle(
                            textColor = ContextCompat.getColor(this@MainActivity, android.R.color.white),
                            gradient = BubbleGradient(colors.getColor((position * 2) % 8, 0),
                                    colors.getColor((position * 2) % 8 + 1, 0), if (position % 2 == 1) BubbleGradient.VERTICAL else BubbleGradient.HORIZONTAL)
                    )
                }
            }
        }

        colors.recycle()
    }

}
