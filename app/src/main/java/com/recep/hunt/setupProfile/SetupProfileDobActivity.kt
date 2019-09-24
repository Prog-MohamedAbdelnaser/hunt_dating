package com.recep.hunt.setupProfile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.recep.hunt.R
import com.recep.hunt.constants.Constants
import com.recep.hunt.datePicker.datePickerView.DatePickerPopUpWindow
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
    private var dob = ""
    private lateinit var dobTextView: TextView
    private lateinit var calendar: Calendar;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_dob)
        setScreenTitle(R.string.setup_profile)
        getBaseCancelBtn().setOnClickListener { Helpers.segueToSocialLoginScreen(this) }
        getBackButton().setOnClickListener { finish() }
        init()
    }

    private fun init() {
        dobTextView = find(R.id.user_dob)
        this.hideKeyboard()
        show_dob_dialog_btn.setOnClickListener {
            showDatePicker()
//            datepicker()
        }

        setup_profile_dob_next_btn.setOnClickListener {
            moveToUploadPicture()
        }
    }

    private fun moveToUploadPicture() {
        if (dob != "") {
            SharedPrefrenceManager.setUserDob(this, dob)
            launchActivity<SetupProfileUploadPhotoStep2Activity>()
        } else {
            Helpers.showErrorSnackBar(
                this,
                resources.getString(R.string.complete_form),
                resources.getString(R.string.you_have_complete_form)
            )
            return
        }

    }

    private fun showDatePicker() {
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
                apiDate = getFormattedDate(input = parsedDate, outputDateFormat = Constants.apiDateFormat)
                val age = getAge(parsedDate)
                dobTextView.text = formatedDate
                dobTextView.textColor = resources.getColor(R.color.app_text_black)
                years_old_textView.text = resources.getString(R.string.years_old, age.toString())
                dob = formatedDate
                dob_layout.clearFocus()
                it.dismiss()
                it.cancel()
                hideKeyboard()
            }

            noButton { }

        }.show()
    }

    private fun getFormattedDate(
        input: String,
        inputDateFormat: String = "dd/M/yyyy",
        outputDateFormat: String = "MM/dd/yyyy"
    ): String {
        val inputFormat = SimpleDateFormat(inputDateFormat, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputDateFormat, Locale.ENGLISH)
        val date = inputFormat.parse(input)
        val formattedDate = outputFormat.format(date)
        Log.e(TAG, ": Input date Format : $inputDateFormat")
        Log.e(TAG, ": Output date Format : $outputDateFormat")
        Log.e(TAG, ": Formatted date : $formattedDate")
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

    private fun datepicker() {
        val datePicker = DatePickerPopUpWindow.Builder(applicationContext)
            .setMinYear(1900)
            .setMaxYear(currentyear() + 1)
            .setSelectedDate(currentDate())
            .setLocale(Locale.getDefault())
            .setConfirmButtonText("Done")
            .setCancelButtonText("Cancel")
            .setOnDateSelectedListener(this::onDateSelected)
            .setConfirmButtonTextColor(Color.parseColor("#999999"))
            .setCancelButtonTextColor(Color.parseColor("#009900"))
            .setButtonTextSize(18)
            .setViewTextSize(20)
            .setShowShortMonths(true)
            .setShowDayMonthYear(true)
            .build()
        datePicker.show(this@SetupProfileDobActivity)
    }

    private fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val parsedDate = "${dayOfMonth}/${month + 1}/${year}"
        val formatedDate = getFormattedDate(parsedDate)
        dob = formatedDate
        apiDate = getFormattedDate(input = parsedDate, outputDateFormat = Constants.apiDateFormat)
        val age = getAge(parsedDate)
        dobTextView.text = formatedDate
        dobTextView.textColor = resources.getColor(R.color.app_text_black)
        years_old_textView.text = resources.getString(R.string.years_old, age.toString())
    }

    private fun currentyear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    private fun currentDate(): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMMM-yyyy")
        val cDate = df.format(c)
        return cDate
    }
}
