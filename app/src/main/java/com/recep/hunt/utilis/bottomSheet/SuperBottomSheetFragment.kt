package com.kotlin.mvvm.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.recep.hunt.R


/**
 * Created by khurram
 * Email : khurram.shahzad094@gmail.com
 */
abstract class SuperBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var sheetTouchOutsideContainer: View
    private lateinit var sheetContainer: CornerRadiusFrameLayout
    private lateinit var behavior: BottomSheetBehavior<*>

    // Customizable properties
    private var propertyDim = 0f
    private var propertyCornerRadius = 0f
    private var propertyStatusBarColor = 0
    private var propertyIsAlwaysExpanded = false
    private var propertyIsSheetCancelableOnTouchOutside = true
    private var propertyIsSheetCancelable = true
    private var propertyAnimateCornerRadius = true

    // Bottom sheet properties
    private var canSetStatusBarColor = false

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (animateStatusBar()) {
            return SuperBottomSheetDialog(this.context!!, R.style.superBottomSheetDialog)
        }


        return SuperBottomSheetDialog(this.context!!)
    }

    @CallSuper
    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change status bar on the condition: API >= 21
        val supportsStatusBarColor = hasMinimumSdk(Build.VERSION_CODES.LOLLIPOP)

        // Init properties
        propertyDim = getDim()
        propertyCornerRadius = getCornerRadius()
        propertyStatusBarColor = getStatusBarColor()
        propertyIsAlwaysExpanded = isSheetAlwaysExpanded()
        propertyIsSheetCancelable = isSheetCancelable()
        propertyIsSheetCancelableOnTouchOutside = isSheetCancelableOnTouchOutside()
        propertyAnimateCornerRadius = animateCornerRadius()

        // Set dialog properties
        dialog.runIfNotNull {
            setCancelable(propertyIsSheetCancelable)

            val isCancelableOnTouchOutside =
                propertyIsSheetCancelable && propertyIsSheetCancelableOnTouchOutside
            setCanceledOnTouchOutside(isCancelableOnTouchOutside)

            window.runIfNotNull {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(propertyDim)

                if (supportsStatusBarColor) {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    setStatusBarColor(1f)
                }
            }
        }

        return null
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        // Init UI components
        iniBottomSheetUiComponents()
    }

    //region UI METHODS

    @UiThread
    private fun iniBottomSheetUiComponents() {
        // Store views references
        sheetContainer = dialog?.findViewById(R.id.super_bottom_sheet)!!
        sheetTouchOutsideContainer = dialog?.findViewById(R.id.touch_outside)!!

        // Load bottom sheet behaviour
        behavior = BottomSheetBehavior.from(sheetContainer)

        Resources.getSystem().displayMetrics.heightPixels
        // If is always expanded, there is no need to set the peek height
        if (!propertyIsAlwaysExpanded) {
            behavior.peekHeight =Resources.getSystem().displayMetrics.heightPixels


            sheetContainer.run {
                minimumHeight = behavior.peekHeight
            }
        } else {
            val layoutParams = sheetContainer.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            sheetContainer.layoutParams = layoutParams
        }

        // Override sheet callback events
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    setStatusBarColor(1f)
                    dialog?.cancel()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setRoundedCornersOnScroll(bottomSheet, slideOffset)
                setStatusBarColorOnScroll(bottomSheet, slideOffset)
            }
        })
    }

    @UiThread
    private fun setStatusBarColorOnScroll(bottomSheet: View, slideOffset: Float) {
        if (!canSetStatusBarColor) {
            return
        }

        if (bottomSheet.height != sheetTouchOutsideContainer.height) {
            canSetStatusBarColor = false
            return
        }

        if (slideOffset.isNaN() || slideOffset <= 0) {
            setStatusBarColor(1f)
            return
        }

        val invertOffset = 1 - (1 * slideOffset)
        setStatusBarColor(invertOffset)
    }

    @SuppressLint("NewApi")
    @UiThread
    private fun setStatusBarColor(dim: Float) {
        if (!canSetStatusBarColor) {
            return
        }

        val color = calculateColor(propertyStatusBarColor, dim)
        dialog?.window!!.statusBarColor = color
    }

    //endregion

    //region CORNERS

    @UiThread
    private fun setRoundedCornersOnScroll(bottomSheet: View, slideOffset: Float) {
        if (!propertyAnimateCornerRadius) {
            return
        }

        if (bottomSheet.height != sheetTouchOutsideContainer.height) {
            propertyAnimateCornerRadius = false
            return
        }

        if (slideOffset.isNaN() || slideOffset <= 0) {
            sheetContainer.setCornerRadius(propertyCornerRadius)
            return
        }

        if (propertyAnimateCornerRadius) {
            val radius = propertyCornerRadius - (propertyCornerRadius * slideOffset)
            sheetContainer.setCornerRadius(radius)
        }
    }

    //endregion

    //region PUBLIC

    @Dimension
    open fun getPeekHeight(): Int = with(context!!.getAttrId(R.attr.superBottomSheet_peekHeight)) {
        val peekHeightMin = when (this) {
            INVALID_RESOURCE_ID -> resources.getDimensionPixelSize(R.dimen.super_bottom_sheet_peek_height)
            else -> resources.getDimensionPixelSize(this)
        }

        // 16:9 ratio
        return with(resources.displayMetrics) {
            Math.max(peekHeightMin, heightPixels - heightPixels * 9 / 16)
        }
    }

    @Dimension
    open fun getDim(): Float = with(context!!.getAttrId(R.attr.superBottomSheet_dim)) {
        return when (this) {
            INVALID_RESOURCE_ID -> TypedValue().run {
                resources.getValue(R.dimen.super_bottom_sheet_dim, this, true)
                float
            }

            else -> TypedValue().let {
                resources.getValue(this, it, true)
                it.float
            }
        }
    }

    @ColorInt
    open fun getBackgroundColor(): Int =
        with(context!!.getAttrId(R.attr.superBottomSheet_backgroundColor)) {
            return when (this) {
                INVALID_RESOURCE_ID -> Color.parseColor("#393e46")
                else -> ContextCompat.getColor(context!!, this)
            }
        }


    @ColorInt
    open fun getStatusBarColor(): Int =
        with(context!!.getAttrId(R.attr.superBottomSheet_statusBarColor)) {
            return when (this) {
                INVALID_RESOURCE_ID -> ContextCompat.getColor(
                    context!!,
                    context!!.getAttrId(R.attr.colorPrimaryDark)
                )
                else -> ContextCompat.getColor(context!!, this)
            }
        }

    @Dimension
    open fun getCornerRadius(): Float =
        with(context!!.getAttrId(R.attr.superBottomSheet_cornerRadius)) {
            return when (this) {
                INVALID_RESOURCE_ID -> context!!.resources.getDimension(R.dimen.super_bottom_sheet_radius)
                else -> resources.getDimension(this)
            }
        }

    open fun isSheetAlwaysExpanded(): Boolean =
        with(context!!.getAttrId(R.attr.superBottomSheet_alwaysExpanded)) {
            return when (this) {
                INVALID_RESOURCE_ID -> context!!.resources.getBoolean(R.bool.super_bottom_sheet_isAlwaysExpanded)
                else -> resources.getBoolean(this)
            }
        }

    open fun isSheetCancelableOnTouchOutside(): Boolean =
        with(context!!.getAttrId(R.attr.superBottomSheet_cancelableOnTouchOutside)) {
            return when (this) {
                INVALID_RESOURCE_ID -> context!!.resources.getBoolean(R.bool.super_bottom_sheet_cancelableOnTouchOutside)
                else -> resources.getBoolean(this)
            }
        }

    open fun isSheetCancelable(): Boolean =
        with(context!!.getAttrId(R.attr.superBottomSheet_cancelable)) {
            return when (this) {
                INVALID_RESOURCE_ID -> context!!.resources.getBoolean(R.bool.super_bottom_sheet_cancelable)
                else -> resources.getBoolean(this)
            }
        }

    open fun animateCornerRadius(): Boolean =
        with(context!!.getAttrId(R.attr.superBottomSheet_animateCornerRadius)) {
            return when (this) {
                INVALID_RESOURCE_ID -> context!!.resources.getBoolean(R.bool.super_bottom_sheet_animate_corner_radius)
                else -> resources.getBoolean(this)
            }
        }

    open fun animateStatusBar(): Boolean =
        with(context!!.getAttrId(R.attr.superBottomSheet_animateStatusBar)) {
            return when (this) {
                INVALID_RESOURCE_ID -> context!!.resources.getBoolean(R.bool.super_bottom_sheet_animate_status_bar)
                else -> resources.getBoolean(this)
            }
        }

    //endregion
}