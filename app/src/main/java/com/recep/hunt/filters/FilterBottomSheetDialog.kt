package com.recep.hunt.filters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.recep.hunt.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.recep.hunt.filters.adapter.FilterAdapter
import com.recep.hunt.constants.Constants
import com.recep.hunt.filters.model.LookingForMainModel
import com.recep.hunt.utilis.NonSwipeableViewPager
import com.recep.hunt.utilis.SharedPrefrenceManager
import kotlinx.android.synthetic.main.filter_bottom_sheet_layout.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.support.v4.toast
import kotlin.math.abs




/**
 * Created by Rishabh Shukla
 * on 2019-08-27
 * Email : rishabh1450@gmail.com
 */

class FilterBottomSheetDialog(val ctx: Context) : BottomSheetDialogFragment() {
    
    private var mBottomSheetListener: FilterBottomSheetListener?=null
    private lateinit var ageRangeSeekBar: RangeSeekBar
    private lateinit var viewPager : NonSwipeableViewPager
    private lateinit var filterAgeTextView :TextView
    private lateinit var filterTabLayout: TabLayout
    private lateinit var lookingForModel: LookingForMainModel
    private lateinit var moveLeftBtn : ImageButton
    private lateinit var moveRightBtn : ImageButton

    private lateinit var maleImageView: ImageView
    private lateinit var feMaleImageView: ImageView
    private lateinit var bothImageView: ImageView
    private lateinit var applyButton: Button
    private lateinit var lookingForDate : String
    private lateinit var lookingForBusiness : String
    private lateinit var lookingForFriendship : String
    private var interestedIn: String = ""
    private var leftAge : Int = 18
    private var rightAge : Int = 50

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.filter_bottom_sheet_layout, container, false)
        viewPager = v.find(R.id.filter_viewPager)
        ageRangeSeekBar = v.find(R.id.filter_age_range_seekbar)
        filterAgeTextView = v.find(R.id.filter_age_textView)
        filterTabLayout = v.find(R.id.filter_tab_layout)
        moveLeftBtn = v.find(R.id.filter_move_left_arrow_image)
        moveRightBtn = v.find(R.id.filter_move_right_arrow_image)
        maleImageView = v.find(R.id.filter_male_imageView)
        feMaleImageView = v.find(R.id.filter_female_imageView)
        bothImageView = v.find(R.id.filter_both_imageView)
        applyButton = v.find(R.id.filter_appyly_button)

        val leftAgeTemp = SharedPrefrenceManager.getUserInterestedAgeFrom(v.context)
        val rightAgeTemp = SharedPrefrenceManager.getUserInterestedAgeTo(v.context)
        if (leftAgeTemp.isNotEmpty())
            leftAge = leftAgeTemp.toInt()
        if (rightAgeTemp.isNotEmpty())
            rightAge = rightAgeTemp.toInt()

        lookingForDate = SharedPrefrenceManager.getUserLookingFor(v.context, "Date")
        lookingForBusiness = SharedPrefrenceManager.getUserLookingFor(v.context, "Business")
        lookingForFriendship = SharedPrefrenceManager.getUserLookingFor(v.context, "Friendship")

        lookingForModel = LookingForMainModel.getInstance()
        val adapter = FilterAdapter(ctx, lookingForModel.getData(lookingForDate, lookingForFriendship, lookingForBusiness))
        viewPager.adapter = adapter
        filterTabLayout.setupWithViewPager(viewPager)
        viewPager.setPageTransformer(false,FadePageTransformer())

        filterAgeTextView.text =
            ctx.resources.getString(R.string.fromYears_toYears, leftAge.toString(), rightAge.toString())

        ageRangeSeekBar.setProgress(leftAge.toFloat(),rightAge.toFloat())
        v.filter_dismiss_btn.setOnClickListener {
            dismiss()
        }

        setupInterstedInSelecters(0)



        moveLeftBtn.setOnClickListener {

            val position = getItem(-1)
            Log.e("Move Left Btn","Value : $position")
            viewPager.setCurrentItem(position,true)

            if(position > -1 )
            setupInterstedInSelecters(position)


        }
        moveRightBtn.setOnClickListener {
            val position = getItem(+1)
            Log.e("Move Right Btn","Value : $position")
            viewPager.setCurrentItem(position,true)
            if(position < 3)
                setupInterstedInSelecters(position)



        }
        maleImageView.setOnClickListener {
            interestedIn = "Male"
            changeGenderBackgrounds(Constants.MALE)

            when (viewPager.currentItem) {
                0 -> {
                    lookingForDate = Constants.MALE
                }

                1 -> {
                    lookingForFriendship = Constants.MALE
                }
                2 -> {
                    lookingForBusiness = Constants.MALE
                }
            }
        }
        feMaleImageView.setOnClickListener {
            interestedIn = "Female"
            changeGenderBackgrounds(Constants.FEMALE)
            when (viewPager.currentItem) {
                0 -> {
                    lookingForDate = Constants.FEMALE
                }

                1 -> {
                    lookingForFriendship = Constants.FEMALE
                }
                2 -> {
                    lookingForBusiness = Constants.FEMALE
                }
            }
        }
        bothImageView.setOnClickListener {
            interestedIn = "Both"
            changeGenderBackgrounds(Constants.BOTH)
            when (viewPager.currentItem) {
                0 -> {
                    lookingForDate = Constants.BOTH
                }

                1 -> {
                    lookingForFriendship = Constants.BOTH
                }
                2 -> {
                    lookingForBusiness = Constants.BOTH
                }
            }
        }



        ageRangeSeekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(rangeSeekBar: RangeSeekBar, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                filterAgeTextView.text =
                    ctx.resources.getString(R.string.fromYears_toYears,leftValue.toInt().toString(),rightValue.toInt().toString())
                leftAge = leftValue.toInt()
                rightAge = rightValue.toInt()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

        })

        applyButton.setOnClickListener {
            var selectedLookingFor = ""

            SharedPrefrenceManager.setUserInterestedIn(v.context, lookingForDate, "Date")
            SharedPrefrenceManager.setUserInterestedIn(v.context, lookingForFriendship, "Friendship")
            SharedPrefrenceManager.setUserInterestedIn(v.context, lookingForBusiness, "Business")
            SharedPrefrenceManager.setUserInterestedAge(v.context, leftAge.toString(), rightAge.toString())
            dismiss()

        }

        return v
    }

    private fun setupInterstedInSelecters(position:Int){
        val data = lookingForModel.getData(lookingForDate, lookingForFriendship, lookingForBusiness)
        val interstedIn = data[position].interestedIn
        changeGenderBackgrounds(interstedIn.toString())
    }
    private fun changeGenderBackgrounds(interstedIn:String){
        if (interstedIn.isEmpty()) {
            maleImageView.image = resources.getDrawable(R.drawable.ic_man)
            feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
            bothImageView.image = resources.getDrawable(R.drawable.ic_others_gender)
            maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            feMaleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
            bothImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
        }
        else {
            when(interstedIn){
                Constants.MALE ->{
                    maleImageView.image = resources.getDrawable(R.drawable.ic_man_white)
                    feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
                    bothImageView.image = resources.getDrawable(R.drawable.ic_others_gender)

                    maleImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
                    feMaleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                    bothImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                }
                Constants.FEMALE->{
                    maleImageView.image = resources.getDrawable(R.drawable.ic_man)
                    feMaleImageView.image = resources.getDrawable(R.drawable.ic_female_white)
                    bothImageView.image = resources.getDrawable(R.drawable.ic_others_gender)

                    maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                    feMaleImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
                    bothImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                }
                Constants.BOTH->{
                    maleImageView.image = resources.getDrawable(R.drawable.ic_man)
                    feMaleImageView.image = resources.getDrawable(R.drawable.ic_female)
                    bothImageView.image = resources.getDrawable(R.drawable.ic_other_white)
                    maleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                    feMaleImageView.background = resources.getDrawable(R.drawable.unselected_circular_btn)
                    bothImageView.background = resources.getDrawable(R.drawable.selected_cirular_btn)
                }
            }
        }

    }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    interface FilterBottomSheetListener{
        fun onOptionClick(text: String)
    }


}

private class FadePageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        if (position <= -1.0f || position >= 1.0f) {
            view.translationX = view.width * position
            view.alpha = 0.0f
        } else if (position == 0.0f) {
            view.translationX = view.width * position
            view.alpha = 1.0f
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.translationX = view.width * -position
            view.alpha = 1.0f - abs(position)
        }
    }
}