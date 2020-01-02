
package com.kotlin.mvvm.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import com.recep.hunt.R
/**
 * Created by khurram
 * Email : khurram.shahzad094@gmail.com
 */
internal inline fun <T, R> T?.runIfNotNull(block: T.() -> R): R? = this?.block()

//endregion

//region VIEW

@Suppress("DEPRECATION")
internal fun View.setBackgroundCompat(drawable: Drawable) = when {
    hasMinimumSdk(Build.VERSION_CODES.JELLY_BEAN) -> background = drawable
    else -> setBackgroundDrawable(drawable)
}

//endregion

//region CONTEXT
internal fun Context?.isInPortrait() = this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

internal fun Context.getAttrId(attrId: Int): Int {
    TypedValue().run {
        return when {
            !theme.resolveAttribute(attrId, this, true) -> INVALID_RESOURCE_ID
            else -> resourceId
        }
    }
}

//endregion