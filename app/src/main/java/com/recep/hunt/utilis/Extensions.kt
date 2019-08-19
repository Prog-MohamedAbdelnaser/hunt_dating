package com.recep.hunt.utilis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle


/**
 * Created by Rishabh Shukla
 * on 2019-08-18
 * Email : rishabh1450@gmail.com
 */


//Intent+Extenions
inline fun <reified T : Any> Activity.launchActivity (
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {})
{
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}
inline fun <reified T : Any> Context.launchActivity (
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {})
{
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}
inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

// --- Intent+Extenions --