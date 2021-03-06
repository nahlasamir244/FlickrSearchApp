package com.nahlasamir244.flickrsearchapp.utils.helpers

import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.domain.LoadPhoto

@InverseBindingAdapter(attribute = "bindText")
fun EditText.getTextValue(): String {
    return text.toString()
}

/**
 * Binding the edit text to the a live data
 *
 * @param text
 */
@BindingAdapter("bindText")
fun EditText.setTextValue(text: String?) {
    val checkedText = text ?: ""

    if (text.toString() != checkedText) {
        setText(checkedText)
    }

}

@BindingAdapter("bindTextAttrChanged")
fun EditText.setListener(listener: InverseBindingListener?) {
    if (listener != null) {
        doAfterTextChanged {
            listener.onChange()
        }
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) {
        val loadPhoto = LoadPhoto()
        loadPhoto(view, url, R.drawable.ic_image_default_grey)
    }
}

