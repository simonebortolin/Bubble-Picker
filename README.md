# Bubble-Picker
[![](https://jitpack.io/v/simonebortolin/Bubble-Picker.svg)](https://jitpack.io/#simonebortolin/Bubble-Picker)

Android 3rd party library to make a bubble picker whit an easy-to-use animation

Forked from [Bubble Picker](https://github.com/igalata/Bubble-Picker)

## Screenshots
<a href="https://github.com/simonebortolin/Bubble-Picker/blob/master/image_1.png"><img src="https://github.com/simonebortolin/Bubble-Picker/blob/master/image_1.png" alt="" width="200px"></a>
<a href="https://github.com/simonebortolin/Bubble-Picker/blob/master/image_2.png"><img src="https://github.com/simonebortolin/Bubble-Picker/blob/master/image_2.png" alt="" width="200px"></a>
<a href="https://github.com/simonebortolin/Bubble-Picker/blob/master/image_3.png"><img src="https://github.com/simonebortolin/Bubble-Picker/blob/master/image_3.png" alt="" width="200px"></a>


## Installation

Step 1: Add this to your **root** build.gradle file (not your module build.gradle file):

    allprojects {
      repositories {
        ...
        maven { url "https://jitpack.io" }
      }
    }


Step 2: Add this to your module `build.gradle` file:

    dependencies {
      ...
        implementation 'com.github.simonebortolin:Bubble-Picker:0.3.1'
    }


## How to use this library

    <?xml version="1.0" encoding="utf-8"?>
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    
        <com.igalata.bubblepicker.rendering.BubblePicker
            android:id="@+id/picker"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:backgroundColor="@android:color/white" />

    </FrameLayout>

Override onResume() and onPause() methods to call the same methods from the BubblePicker

    Kotlin
    override fun onResume() {
          super.onResume()
          picker.onResume()
    }
    
    override fun onPause() {
          super.onPause()
          picker.onPause()
    }
    Java
    
    @Override
    protected void onResume() {
          super.onResume();
          picker.onResume();
    }
    
    @Override
    protected void onPause() {
          super.onPause();
          picker.onPause();
    }


Specify the BubblePickerAdapter

Kotlin

    val titles = resources.getStringArray(R.array.countries)
    val colors = resources.obtainTypedArray(R.array.colors)
    
    pickerSimple.adapter = object : BubblePickerAdapter {

        override val totalCount = titles.size
        
        override fun getItem(position: Int): PickerItem {
            return PickerItem().apply {
                title = titles[position]
        
        
                // edged
                color = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                borderColor = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length(), 0))
                textColor = ContextCompat.getColor(this@MainActivity, android.R.color.black)
                selectedColor = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length() + 1, 0))
                selectedTextColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
        
        
                // or inverse
                selectedColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                selectedBorderColor = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length(), 0))
                selectedTextColor = ContextCompat.getColor(this@MainActivity, android.R.color.black)
                color = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length() + 1, 0))
                textColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                
                // or with shades
                
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
                
                // or with images
                
                bubbleStyle = BubbleStyle(
                        textColor = ContextCompat.getColor(this@MainActivity, android.R.color.white),
                        backgroundColor = ContextCompat.getColor(this@MainActivity, colors.getResourceId((position * 2) % colors.length(), 0)),
                        icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_flag_white_24dp),
                        iconPosition = if (position % 2 == 0) IconPosition.TOP else IconPosition.BOTTOM
                )
    
                // or whit icon
    
                bubbleSelectedStyle = BubbleStyle(
                        textColor = ContextCompat.getColor(this@MainActivity, android.R.color.white),
                        image = ContextCompat.getDrawable(this@MainActivity, images.getResourceId(position, 0))
                )
    
            }
        }
    }
    
    colors.recycle()

Specify the BubblePickerListener to get notified about events

    Kotlin
    
    picker.listener = object : BubblePickerListener {
        override fun onBubbleSelected(item: PickerItem) {

        }

        override fun onBubbleDeselected(item: PickerItem) {

        }
    }
Java

    picker.setListener(new BubblePickerListener() {
        @Override
        public void onBubbleSelected(@NotNull PickerItem item) {
            
        }

        @Override
        public void onBubbleDeselected(@NotNull PickerItem item) {

        }
    });
To get all selected items use picker.selectedItems variable in Kotlin or picker.getSelectedItems() method in Java.

For more usage examples please review the sample app

## Known iOS versions of the animation

* https://github.com/Ronnel/BubblePicker
* https://github.com/efremidze/Magnetic

## Credits


I thank all the authors of the various commits that I have included in my fork


## License

MIT License

Copyright (c) 2017 Irina Galata

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
