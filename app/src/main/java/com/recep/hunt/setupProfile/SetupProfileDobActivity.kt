package com.recep.hunt.setupProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import com.recep.hunt.R
import com.recep.hunt.utilis.launchActivity
import kotlinx.android.synthetic.main.activity_setup_profile_dob.*
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


class SetupProfileDobActivity : AppCompatActivity() {

    private lateinit var dobEditText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_dob)
        init()
    }
    private fun init(){

        dobEditText = find(R.id.dob_et)
        setSupportActionBar(setupProfiledob__toolbar)

        dobEditText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                showDatePicker()
            }
        }

        setup_profile_dob_next_btn.setOnClickListener {
            launchActivity<SetupProfileGenderActivity>()
        }
    }

    private fun showDatePicker(){
        alert {

            isCancelable = false

            lateinit var datePicker: DatePicker

            customView {
                verticalLayout {
                    datePicker = datePicker {
                    }
                }
            }

            yesButton {
                //dd/M/yyyy parsed date format

                val parsedDate = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                val formatedDate = getFormattedDate(parsedDate)
                val age = getAge(parsedDate)
                dobEditText.setText(formatedDate)
                years_old_textView.text = resources.getString(R.string.years_old,age.toString())
            }

            noButton { }

        }.show()
    }
    private fun getFormattedDate(input:String,inoutDateFormat:String = "dd/M/yyyy"):String{
        val inputFormat = SimpleDateFormat(inoutDateFormat, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MM/dd/YYYY", Locale.ENGLISH)
        val date = inputFormat.parse(input)
        val formattedDate = outputFormat.format(date)
        Log.e("else Format  ",": $formattedDate")
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
