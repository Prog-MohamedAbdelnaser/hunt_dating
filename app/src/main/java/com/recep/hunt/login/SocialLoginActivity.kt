package com.recep.hunt.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.GsonBuilder
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.adapters.SocialLoginChatReceivedAdapter
import com.recep.hunt.adapters.SocialLoginChatSentAdapter
import com.recep.hunt.constants.Constants
import com.recep.hunt.firebaseHelper.FirebaseHelpers
import com.recep.hunt.models.LoginChatMessageModel
import com.recep.hunt.models.UserSocialModel
import com.recep.hunt.setupProfile.SetupProfileActivity
import com.recep.hunt.setupProfile.SetupProfileCompletedActivity
import com.recep.hunt.setupProfile.SetupProfileGalleryActivity
import com.recep.hunt.setupProfile.SetupProfileUploadPhotoActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.hideKeyboard
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_social_login.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.json.JSONObject

class SocialLoginActivity : AppCompatActivity(),View.OnClickListener {


    companion object{
        const val socialTypeKey = "social_type_key"
        const val userSocialModel = "user_social_key"
    }

    //Google Login Request Code
    private val RC_SIGN_IN = 7
    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    //Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userDetailsModel : UserSocialModel
    private lateinit var dialog : KProgressHUD

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText :EditText
    private val adapter = GroupAdapter<ViewHolder>() // using groupie to render recyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        mAuth = FirebaseAuth.getInstance()
        setupGoogleAuth()
        init()
    }
    private fun init(){
        recyclerView = find(R.id.social_login_recyclerView)
        messageEditText = find(R.id.type_msg_et)
        setupRecyclerView()
    }
    private fun setupGoogleAuth(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.send_msg_btn -> {
                    if(messageEditText.text.isNotEmpty()){
                        addMessage()
                    }
                }
                R.id.connect_with_fb_btn -> launchActivity<ContinueAsSocialActivity>{
                    putExtra(socialTypeKey,Constants.socialFBType)

                }
                R.id.connect_with_google_btn -> {

                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }
                R.id.connect_with_insta_btn -> launchActivity<ContinueAsSocialActivity>{
                    putExtra(socialTypeKey,Constants.socialInstaType)
                }
                R.id.social_login_skip_btn -> launchActivity<SetupProfileActivity>()
            }
        }
    }


    private fun setupRecyclerView(){
        val linearLayout = LinearLayoutManager(this@SocialLoginActivity)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = adapter
        val chatData = dummyChatdata()
        for(data in chatData){
            //msg type case
            if(data.type == Constants.messageSentType){
                adapter.add(SocialLoginChatSentAdapter(this@SocialLoginActivity,data))
            }else{
                adapter.add(SocialLoginChatReceivedAdapter(this@SocialLoginActivity,data))
            }
        }

        Helpers.runAnimation(recyclerView)
    }

    private fun addMessage(){
        val message = messageEditText.text.toString()
        val model = LoginChatMessageModel(message,Constants.messageSentType)
        adapter.add(SocialLoginChatSentAdapter(this@SocialLoginActivity,model))
        adapter.notifyDataSetChanged()
        messageEditText.setText("")
        this@SocialLoginActivity.hideKeyboard()
    }

    //Dummy Chat Data
    private fun dummyChatdata():ArrayList<LoginChatMessageModel>{
        val data = ArrayList<LoginChatMessageModel>()
        if(data.size == 0){
            data.add(LoginChatMessageModel("You might like to become a member of our loyalty program and accumulate points.",Constants.messageSentType))
            data.add(LoginChatMessageModel("Interesting",Constants.messageReceivedType))
            data.add(LoginChatMessageModel("i am looking forward to it ",Constants.messageReceivedType))
            data.add(LoginChatMessageModel("Sure i will call you asap!",Constants.messageSentType))
        }
        return data
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(Exception::class.java)
                if(account != null)
                firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)

            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateGoogleUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                }


            }
    }

    private fun updateGoogleUI(user: FirebaseUser?){
        if(user != null){

            val gson = GsonBuilder().setPrettyPrinting().create()
            userDetailsModel = UserSocialModel(user.uid,user.photoUrl.toString(), user.displayName!!,user.email!!)
            val json: String = gson.toJson(userDetailsModel)

            launchActivity<ContinueAsSocialActivity> {
                putExtra(socialTypeKey,Constants.socialGoogleType)
                putExtra(userSocialModel,json)
            }

        }
        else{
            toast("userNotFound")
        }
    }


}
