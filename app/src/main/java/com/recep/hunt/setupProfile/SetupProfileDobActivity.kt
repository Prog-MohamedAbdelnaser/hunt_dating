package com.recep.hunt.setupProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.profile.model.User
import com.recep.hunt.profile.viewmodel.UserViewModel
import com.recep.hunt.utilis.*
import kotlinx.android.synthetic.main.activity_setup_profile_dob.*
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


class SetupProfileDobActivity : BaseActivity() {

    private var apiDate = ""
    private val TAG = SetupProfileDobActivity::class.java.simpleName
    private lateinit var dobEditText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_dob)
        setScreenTitle(R.string.setup_profile)
        getBackButton().setOnClickListener {
            finish()
        }
        init()
    }
    private fun init(){

        dobEditText = find(R.id.dob_et)
        this.hideKeyboard()
        dobEditText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                showDatePicker()
            }
        }

        setup_profile_dob_next_btn.setOnClickListener {
            moveToUploadPicture()
        }
    }
    private fun moveToUploadPicture(){
        val dob = dobEditText.text.toString()
        if(dob.isNotEmpty()){
            Log.e(TAG,"apiDate : $apiDate")
            launchActivity<SetupProfileUploadPhotoActivity>()
        }else{
            Helpers.showErrorSnackBar(this,resources.getString(R.string.complete_form),resources.getString(R.string.you_have_complete_form))
            return
        }

    }

    private fun showDatePicker(){
        alert {

            isCancelable = false

            lateinit var datePicker: DatePicker

            customView {
                verticalLayout {
                    datePicker = datePicker {
                        maxDate = System.currentTimeMillis()

                    }
                }
            }

            yesButton {
                //dd/M/yyyy parsed date format

                val parsedDate = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                val formatedDate = getFormattedDate(parsedDate)
                apiDate = getFormattedDate(input = parsedDate,outputDateFormat = Constants.apiDateFormat)
                val age = getAge(parsedDate)
                dobEditText.setText(formatedDate)
                dobEditText.clearFocus()
                years_old_textView.text = resources.getString(R.string.years_old,age.toString())
                dobEditText.clearFocus()
            }

            noButton { }

        }.show()
    }
    private fun getFormattedDate(input:String,inputDateFormat:String = "dd/M/yyyy",outputDateFormat:String = "MM/dd/YYYY"):String{
        val inputFormat = SimpleDateFormat(inputDateFormat, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputDateFormat,Locale.ENGLISH)
        val date = inputFormat.parse(input)
        val formattedDate = outputFormat.format(date)
        Log.e(TAG,": Input date Format : $inputDateFormat")
        Log.e(TAG,": Output date Format : $outputDateFormat")
        Log.e(TAG,": Formatted date : $formattedDate")
        return formattedDate
    }

    private fun getAge(dobString: String): Int {

        var date: Date? = null
        val sdf = SimpleDateFormat("dd/M/yyyy")
        try {
            date = sdf.parse(dobString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (date == null) return 0

        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.time = date

        val year = dob.get(Calendar.YEAR)
        val month = dob.get(Calendar.MONTH)
        val day = dob.get(Calendar.DAY_OF_MONTH)

        dob.set(year, month + 1, day)

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }



        return age
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
