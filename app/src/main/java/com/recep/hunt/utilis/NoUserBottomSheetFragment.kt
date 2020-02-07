package com.recep.hunt.utilis

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlin.mvvm.utils.SuperBottomSheetFragment
import com.recep.hunt.R
import kotlinx.android.synthetic.main.no_user_bottom_sheet.*

/**
 * Created by khurram
 * Email : khurram.shahzad094@gmail.com
 */
class NoUserBottomSheetFragment : SuperBottomSheetFragment(), View.OnClickListener{

    companion object {
        fun newInstance() = NoUserBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.no_user_bottom_sheet, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_view_another_location.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.button_view_another_location) {
            dismiss()
        }
    }

    override fun getPeekHeight(): Int {
        // Your code goes here
        // 16:9 ratio
        return with(resources.displayMetrics) {
            (heightPixels - heightPixels * 9 / 16) + 380
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
