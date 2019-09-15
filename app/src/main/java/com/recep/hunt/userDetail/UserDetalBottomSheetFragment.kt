package com.recep.hunt.userDetail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.recep.hunt.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog






/**
 * Created by Rishabh Shukla
 * on 2019-09-13
 * Email : rishabh1450@gmail.com
 */

class UserDetalBottomSheetFragment(private val ctx:Context) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_detail_bottom_sheet_layout,container,false)
        init(view)
        return view
    }

    private fun init(view:View?){
        if(view != null){

        }
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
//        val view = View.inflate(context, R.layout.user_detail_bottom_sheet_layout, null)
//
//
//        dialog.setContentView(view)
//        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
//        return dialog
//
//    }

}