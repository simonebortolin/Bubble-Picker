# Bubble-Picker

Forked from [Bubble Picker](https://github.com/igalata/Bubble-Picker)

[![License](http://img.shields.io/badge/license-MIT-green.svg?style=flat)]()


<img src="shot.gif"/>

## Requirements
- Android SDK 16+

## Usage

Add to your root build.gradle:
```Groovy
allprojects {
	repositories {
	...
	maven { url "https://jitpack.io" }
	}
}
```

Add the dependency:
```Groovy
dependencies {
	compile 'com.github.simonebortolin:Bubble-Picker:0.2.11'
}
```

## How to use this library

Add `BubblePicker` to your xml layout

```xml
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
```

Override onResume() and onPause() methods to call the same methods from the `BubblePicker`

Kotlin
```kotlin
override fun onResume() {
      super.onResume()
      picker.onResume()
}

override fun onPause() {
      super.onPause()
      picker.onPause()
}
```

Java
```java
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
```

Pass the `PickerItem` list to the `BubblePicker`

Kotlin
```kotlin
val titles = resources.getStringArray(R.array.countries)
val colors = resources.obtainTypedArray(R.array.colors)
val images = resources.obtainTypedArray(R.array.images)

picker.adapter = object : BubblePickerAdapter {

            override val totalCount = titles.size

            override fun getItem(position: Int): PickerItem {
                return PickerItem().apply {
                    title = titles[position]
                    color = ContextCompat.getColor(this@DemoActivity, android.R.color.black)
                    textColor = ContextCompat.getColor(this@DemoActivity, android.R.color.white)
                    selectedColor = ContextCompat.getColor(this@DemoActivity, android.R.color.holo_blue_light)
                    selectedTextColor = ContextCompat.getColor(this@DemoActivity, android.R.color.white)
                }
            }
}
```

Specify the `BubblePickerListener` to get notified about events

Kotlin
```kotlin
picker.listener = object : BubblePickerListener {
            override fun onBubbleSelected(item: PickerItem) {

            }

            override fun onBubbleDeselected(item: PickerItem) {

            }
}
```

Java
```java
picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
});
```

To get all selected items use `picker.selectedItems` variable in Kotlin or `picker.getSelectedItems()` method in Java.

For more usage examples please review the sample app

## Known iOS versions of the animation

* https://github.com/Ronnel/BubblePicker
* https://github.com/efremidze/Magnetic

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
