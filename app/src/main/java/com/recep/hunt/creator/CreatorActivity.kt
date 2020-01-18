package com.recep.hunt.creator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.creator.adapter.CreatorQuestionAdapter
import com.recep.hunt.creator.model.CreatorQustion
import com.recep.hunt.creator.model.OptionModel
import com.recep.hunt.payment.adapter.FaqPaymentAdapter
import org.jetbrains.anko.find
import android.view.WindowManager
import android.view.Window
import org.aviran.cookiebar2.CookieBar.dismiss
import android.R.id.text2
import android.R.attr.text
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Html
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.goodiebag.pinview.Pinview
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.recep.hunt.api.ApiClient
import com.recep.hunt.model.createTicket.CreateTicketResponse
import com.recep.hunt.profile.model.IceBreakerModel
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_creator.*
import kotlinx.android.synthetic.main.activity_creator.otp_progrss_txt
import kotlinx.android.synthetic.main.activity_otp_verification.*
import kotlinx.android.synthetic.main.activity_user_profile_edit.*
import kotlinx.android.synthetic.main.add_new_ice_breaker_question_dialog.*
import kotlinx.android.synthetic.main.ask_question_dailog.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class CreatorActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var remanigTime: TextView
    private lateinit var btnHaveAnIde: Button
    private var imgLivedate = MutableLiveData<Bitmap>()
    private lateinit var progressBar: ProgressBar
    private val handler = Handler()
    private lateinit var cl_progressbar: ConstraintLayout
    private var pStatus = 4
    private var pStatusVisible = 5
    var imageFileTobeUploaded = MutableLiveData<File>()

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var dialog: Dialog
    private lateinit var timer: CountDownTimer
    private lateinit var voiceFile :String

    private lateinit var otpPinView: Pinview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)
        init()

    }

    private fun init() {
        recyclerView = find(R.id.question_recyclerView)

        btnHaveAnIde = find(R.id.btnDontHaveAnIdea)

        btnHaveAnIde.setOnClickListener {
            askQuestion()
        }

        login_nxt_btn.setOnClickListener {
            askQuestion()
        }

        progressBar = find(R.id.otp_progressBar)
        cl_progressbar = find(R.id.cl_progress_bar)


        val res = resources
        val drawable = res.getDrawable(R.drawable.circuler_color_progressbaar)
        progressBar.progressDrawable = drawable
        progressBar.progress = 0
        progressBar.max = 4


        Thread(Runnable {
            while (pStatus < 4) {
                pStatus += 1
                pStatusVisible -= 1

                handler.post {
                    progressBar.progress = pStatus
                    otp_progrss_txt.text = pStatusVisible.toString()

                    if (pStatusVisible == 0) {
                        runOnUiThread {
                            progressBar.visibility = View.GONE
                            remanigTime.visibility = View.GONE
                            btnHaveAnIde.visibility = View.VISIBLE
                        }
                    }
                }
                try {
                    Thread.sleep(1000)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }


        }).start()



        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = CreatorQuestionAdapter(this, getQustion())
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)
    }


    private fun getQustion(): ArrayList<CreatorQustion>? {

        val list = ArrayList<CreatorQustion>()
        val option = ArrayList<OptionModel>()

        option.add(OptionModel(1, "More People"))

        option.add(OptionModel(2, "Better Performance "))

        option.add(OptionModel(3, "New Features"))


        list.add(
            CreatorQustion(
                1, "Be a creator of this app!\n" +
                        "Tell us your suggestions!", R.drawable.ic_claps, option
            )
        )


        return list

    }

    private fun askQuestion() {
        val cancelButton: Button
        val askButton: Button
        val uploadImage: ConstraintLayout
        val image: ImageView
        val option2Et: EditText
        val option3Et: EditText

        val optionText: EditText
        val ll = LayoutInflater.from(this).inflate(R.layout.ask_question_dailog, null)


        dialog = Dialog(this)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        cancelButton = dialog.find(R.id.ic_breaker_cancel_btn)
        askButton = dialog.find(R.id.ic_breaker_add_btn)
        uploadImage = dialog.find(R.id.camera_layout)
        image = dialog.find(R.id.ivImage)
        optionText = dialog.find(R.id.add_ice_breaker_question_et)
        var stopRecord : ImageView=dialog.find(R.id.stopRecord)

        stopRecord.onClick {
            stopAudio()
        }
        dialog.startRecordingCard.onClick {
            recordAudio()
        }
        uploadImage.setOnClickListener {
            ImagePicker.with(this).setShowCamera(true).setMultipleMode(false).start()
        }


        imgLivedate.observe(this, androidx.lifecycle.Observer {
            image.visibility = View.VISIBLE
            image.setImageBitmap(it)
        })

        cancelButton.setOnClickListener {
            launchActivity<TicketGenrated> { }

            dialog.dismiss()
        }
        askButton.setOnClickListener {
            giveUsRate()
            launchActivity<TicketGenrated> { }

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart(
                "message",
                optionText.text.toString()
            )

            val imageFile = imageFileTobeUploaded.value
//            if (imageFile != null && imageFile.exists()) {
//                builder.addFormDataPart(
//                    "image",
//                    imageFile.name,
//                    RequestBody.create(MediaType.parse("multipart/form-questionData"), imageFile)
//                )
//                builder.addFormDataPart(
//                    "audio",
//                    File(voiceFile).name,
//                    RequestBody.create(MediaType.parse("multipart/form-questionData"), File(voiceFile))
//                )
//            }
            val call = ApiClient.getClient.createTicket(
                builder.build(),
                SharedPrefrenceManager.getUserToken(this@CreatorActivity)
            )

            call.enqueue(object : Callback<CreateTicketResponse> {
                override fun onFailure(call: Call<CreateTicketResponse>, t: Throwable) {
                    Log.d("Creator" , "Faliure" + t.message.toString())
                    Toast.makeText(this@CreatorActivity, "Please try again", Toast.LENGTH_SHORT)
                }

                override fun onResponse(
                    call: Call<CreateTicketResponse>,
                    response: Response<CreateTicketResponse>
                ) {
                    Log.d("Creator" , "Success" + response.code())
//                    Log.d("Creator" , "Success" + response.body()!!.questionData.ticket_id)

                    dialog.dismiss()

                    if (!response.isSuccessful) {
                        val strErrorJson = response.errorBody()?.string()
                        if (Utils.isSessionExpire(this@CreatorActivity, strErrorJson)) {
                            return
                        }
                    }

                }
            })
        }
        dialog.show()
    }

    private fun stopAudio() {
        mediaRecorder.stop()
        timer.cancel()

      showAudioRecordedView()
    }

    private fun recordAudio() {
        mediaRecorder = MediaRecorder()

        var path = "/hunt"
        var f = File(Environment.getExternalStorageDirectory().toString(), path)
        f.mkdir()

        voiceFile = f.absolutePath +"/audio.mp3"

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(voiceFile);
        mediaRecorder.prepare();
        mediaRecorder.start();

       var startRecordingCard =  dialog.find<ConstraintLayout>(R.id.startRecordingCard)
        startRecordingCard.visibility = View.GONE
        var recordingStarted =  dialog.find<ConstraintLayout>(R.id.recordingStarted)
        recordingStarted.visibility = View.VISIBLE

        startTimer()
    }

    private fun showAudioRecordedView(){
        var recordingStarted =  dialog.find<ConstraintLayout>(R.id.recordingStarted)
        recordingStarted.visibility = View.GONE
        var recordingDone =  dialog.find<ConstraintLayout>(R.id.recordingDone)
        recordingDone.visibility = View.VISIBLE
    }

    private fun startTimer() {
        timer = object: CountDownTimer(60000 , 1000){
            override fun onFinish() {
                Log.d("CreatorA " , "record donw" )

                mediaRecorder.stop();
                mediaRecorder.release();
                showAudioRecordedView()
            }

            override fun onTick(millisUntilFinished: Long) {
                dialog.find<TextView>(R.id.recordTimer).setText((millisUntilFinished/1000).toString()+" sec remaining")
            }
        }

        timer.start()
    }

    private fun giveUsRate() {
        val submit: LinearLayout
        val ll = LayoutInflater.from(this).inflate(R.layout.rate_us_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        submit = dialog.find(R.id.lytGiveRate)



        submit.setOnClickListener {
            launchActivity<TicketGenrated> { }

            dialog.dismiss()
        }


        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageFile: File
        if (requestCode === Config.RC_PICK_IMAGES && resultCode === Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            if (images.size == 1) {
                imageFile = File(images[0].path)
                MediaScannerConnection.scanFile(
                    this, arrayOf(imageFile.getAbsolutePath()), null
                ) { path, uri ->
                    CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL).start(this)
                }

            }
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                imageFileTobeUploaded.value = File(resultUri.path)
                var bitmap =
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                imgLivedate.value = bitmap
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }


}
