package com.recep.hunt.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Scope
import com.google.android.gms.plus.People
import com.google.android.gms.plus.Plus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.GsonBuilder
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.R
import com.recep.hunt.login.adapter.SocialLoginChatReceivedAdapter
import com.recep.hunt.login.adapter.SocialLoginChatSentAdapter
import com.recep.hunt.constants.Constants
import com.recep.hunt.login.instagramClassesJava.InstagramApp
import com.recep.hunt.login.instagramClassesJava.InstagramApp.WHAT_FINALIZE
import com.recep.hunt.login.model.LoginChatMessageModel
import com.recep.hunt.login.model.UserSocialModel
import com.recep.hunt.profile.viewmodel.UserViewModel
import com.recep.hunt.setupProfile.SetupProfileActivity
import com.recep.hunt.utilis.Helpers
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.hideKeyboard
import com.recep.hunt.utilis.launchActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList


class SocialLoginActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    companion object {
        const val socialTypeKey = "social_type_key"
        const val userSocialModel = "user_social_key"
        //  private val HTTP_TRANSPORT: HttpTransport = AndroidHttp.newCompatibleTransport()
    }

    private val TAG = SocialLoginActivity::class.java.simpleName
    //Google Login Request Code
    private val RC_SIGN_IN = 7
    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleApiClient: GoogleApiClient
    //Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userDetailsModel: UserSocialModel
    private lateinit var dialog: KProgressHUD

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText

    private lateinit var userViewModel: UserViewModel
    private var mApp: InstagramApp? = null
    private lateinit var gso: GoogleSignInOptions

    private var userInfoHashmap = HashMap<String, String>()
    private val handler = Handler(Handler.Callback { msg ->
        if (msg.what == InstagramApp.WHAT_FINALIZE) {
            userInfoHashmap = mApp!!.userInfo
        } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
            Toast.makeText(this@SocialLoginActivity, "Check your network.", Toast.LENGTH_SHORT).show()
        }
        false
    })

    private val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        getDeviceToken()
        mAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        setupGoogleAuth()
        setupInstaAuth()
        init()


//        userViewModel.logUsers()

        //printHashKey(this)
    }

    private fun init() {
        recyclerView = find(R.id.social_login_recyclerView)
        messageEditText = find(R.id.type_msg_et)
        setupRecyclerView()

    }

    private fun setupInstaAuth() {
        mApp = InstagramApp(this@SocialLoginActivity, Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.CALLBACK_URL)
        mApp!!.setListener(object : InstagramApp.OAuthAuthenticationListener {
            override fun onSuccess() {
                // userInfoHashmap = mApp.
                mApp!!.fetchUserName(handler)
                // mApp!!.getAllMediaImages()
            }

            override fun onFail(error: String) {
                Toast.makeText(this@SocialLoginActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
        if (mApp!!.hasAccessToken()) {
            mApp!!.fetchUserName(handler)
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.send_msg_btn -> {
                    if (messageEditText.text.isNotEmpty()) {
                        addMessage()
                    }
                }
                R.id.connect_with_fb_btn -> {
                    setupFbLoginAuth()
//                    launchActivity<HomeActivity>()
                }
                R.id.connect_with_google_btn -> {
                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }
                R.id.connect_with_insta_btn ->// toast("Coming soon.. !")
                    //setupInstaAuth()
                    launchingInst()

                R.id.social_login_skip_btn ->{
                    SharedPrefrenceManager.setIsFromSocial(this,true)

                    launchActivity<SetupProfileActivity>()
                }
            }
        }
    }


    private fun setupGoogleAuth() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestScopes(Scope(Scopes.PLUS_LOGIN))
            .requestEmail()
            .requestProfile()
            .requestScopes(Scope(Scopes.PLUS_ME), Scope(Scopes.PROFILE))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .addApi(Plus.API)
            .addScope(Scope(Scopes.PLUS_ME))
            .addScope(Scope(Scopes.PROFILE))
            .build()
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            if (token != null) {
                SharedPrefrenceManager.setDeviceToken(this@SocialLoginActivity, token)
                Log.e(TAG, "Device Token : $token")
            }
        }
    }

    private fun setupFbLoginAuth() {
        // Initialize Facebook Login button
        LoginManager.getInstance().logOut()
        LoginManager.getInstance()
            .logInWithReadPermissions(this@SocialLoginActivity, Arrays.asList("public_profile", "email","user_photos"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.e("loginResult ", loginResult.toString())
//                    SharedPrefrenceManager.clearAllSharePreference(this@SocialLoginActivity)
                    getUserDetails(loginResult)
                }

                override fun onCancel() {
                    Log.e("loginResult ", "cancel")
                }

                override fun onError(exception: FacebookException) {
                    Log.e("loginResult ", "error  $exception")
                }
            })
        6
    }

    private fun setupRecyclerView() {
        val linearLayout = LinearLayoutManager(this@SocialLoginActivity)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = adapter
        val chatData = dummyChatdata()
        for (data in chatData) {
            //msg type case
            if (data.type == Constants.messageSentType) {
                adapter.add(
                    SocialLoginChatSentAdapter(
                        this@SocialLoginActivity,
                        data
                    )
                )
            } else {
                adapter.add(
                    SocialLoginChatReceivedAdapter(
                        this@SocialLoginActivity,
                        data
                    )
                )
            }
        }

        Helpers.runAnimation(recyclerView)
    }

    private fun addMessage() {
        val message = messageEditText.text.toString()
        val model = LoginChatMessageModel(message, Constants.messageSentType)
        adapter.add(SocialLoginChatSentAdapter(this@SocialLoginActivity, model))
        adapter.notifyDataSetChanged()
        messageEditText.setText("")
        this@SocialLoginActivity.hideKeyboard()
    }

    //Dummy Chat Data
    private fun dummyChatdata(): ArrayList<LoginChatMessageModel> {
        val data = ArrayList<LoginChatMessageModel>()
        if (data.size == 0) {
            data.add(
                LoginChatMessageModel(
                    "You might like to become a member of our loyalty program and accumulate points.",
                    Constants.messageSentType
                )
            )
            data.add(
                LoginChatMessageModel(
                    "Interesting", Constants.messageReceivedType
                )
            )
            data.add(
                LoginChatMessageModel(
                    "i am looking forward to it ",
                    Constants.messageReceivedType
                )
            )
            data.add(
                LoginChatMessageModel(
                    "Sure i will call you asap!",
                    Constants.messageSentType
                )
            )
        }
        return data
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(Exception::class.java)
                val acct = result.getSignInAccount()
                if (account != null)
                    firebaseAuthWithGoogle(account)



                Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                val person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)
                Log.i(TAG, "--------------------------------")
                Log.i(TAG, "Display Name: " + person.displayName)
                Log.i(TAG, "Gender: " + person.gender)
                Log.i(TAG, "AboutMe: " + person.aboutMe)
                Log.i(TAG, "Birthday: " + person.birthday)
                Log.i(TAG, "Current Location: " + person.currentLocation)
                Log.i(TAG, "Language: " + person.language)


            } catch (e: Exception) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e)
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
                    Log.e(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
//                    SharedPrefrenceManager.clearAllSharePreference(this@SocialLoginActivity)
                    updateGoogleUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }


            }
    }

    private fun updateGoogleUI(user: FirebaseUser?) {
        if (user != null) {
            val gson = GsonBuilder().setPrettyPrinting().create()
            userDetailsModel = UserSocialModel(
                user.uid,
                user.photoUrl.toString(),
                user.displayName!!,
                user.email!!
            )

            //  var userInf  = user.providerData

            val json: String = gson.toJson(userDetailsModel)
            Log.e("Full name : ${user.displayName}", "")
            val fullname = user.displayName!!.split(" ").toTypedArray()

            val firstName: String = fullname[0]
            val lastName: String = fullname[1]

            SharedPrefrenceManager.setUserFirstName(this, firstName)
            SharedPrefrenceManager.setUserLastName(this, lastName)
            SharedPrefrenceManager.setUserEmail(this, user.email!!)
            SharedPrefrenceManager.setUserDetailModel(this@SocialLoginActivity, json)
            SharedPrefrenceManager.setProfileImg(this, user.photoUrl.toString())
            SharedPrefrenceManager.setsocialType(this, "social")
            SharedPrefrenceManager.setGoogleLoginToken(this,user.uid)
            SharedPrefrenceManager.setGoogleId(this,user.uid)

            launchActivity<ContinueAsSocialActivity> {
                putExtra(socialTypeKey, Constants.socialGoogleType)
                putExtra(userSocialModel, json)
            }
            var googleSignInClient = GoogleSignIn.getClient(this@SocialLoginActivity, gso);
            googleSignInClient.signOut();


        } else {
            toast("userNotFound")
        }
    }

    private fun getUserDetails(loginResult: LoginResult) {
        val data_request = GraphRequest.newMeRequest(loginResult.accessToken) { json_object, response ->
            try {
                Log.e(TAG, "facebook respone : $json_object")
                val picture = json_object.getJSONObject("picture")
                val data = picture.getJSONObject("data")
                //Fetch the data from the response
                val facebook_pic = data.optString("url", "")
                val social_name = json_object.optString("name", "")
                val social_email = json_object.optString("email", "")
                val id = json_object.getString("id")
                // val gender = json_object.getString("gender").
                 userDetailsModel = UserSocialModel(id, facebook_pic, social_name, social_email)

                val gson = GsonBuilder().setPrettyPrinting().create()
                val json: String = gson.toJson(userDetailsModel)

                val fullname = social_name.split(" ").toTypedArray()
                val firstName: String = fullname[0]
                val lastName: String = fullname[1]

                SharedPrefrenceManager.setUserFirstName(this, firstName)
                SharedPrefrenceManager.setUserLastName(this, lastName)
                SharedPrefrenceManager.setUserDetailModel(this@SocialLoginActivity, json)
                SharedPrefrenceManager.setUserEmail(this, social_email)

                SharedPrefrenceManager.setFacebookId(this@SocialLoginActivity,id)
                SharedPrefrenceManager.setFacebookLoginToken(this@SocialLoginActivity,loginResult.accessToken.token.toString())

                try {
                    SharedPrefrenceManager.setProfileImg(this,facebook_pic)
                } catch(e: IOException) {
                    e.printStackTrace()
                }
                SharedPrefrenceManager.setsocialType(this, "social")

                // SharedPrefrenceManager.setUserGender(this, gender)
                fbUserImages(id)
                launchActivity<ContinueAsSocialActivity> {
                    putExtra(socialTypeKey, Constants.socialFBType)
                    putExtra(userSocialModel, json)
                }
            } catch (e: JSONException) {
                Log.d("JSONException: ", e.message, e)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        val permission_param = Bundle()
        permission_param.putString("fields", "id,name,email,picture.width(480).height(480),gender,birthday")
        data_request.parameters = permission_param
        data_request.executeAsync()

    }


    private fun fbUserImages(id: String) {
        GraphRequest(AccessToken.getCurrentAccessToken(),
            //"/{user-id}/photos",
            "http://graph.facebook.com/{$id}/photos",
            null,
            HttpMethod.GET,
            GraphRequest.Callback() {
                fun onCompleted(response: GraphResponse) {
                    val a=response
                    Log.d(TAG,response.toString())
                }

            }
        ).executeAsync();
    }


    private fun showInstaAuthDialog() {
        val ll = LayoutInflater.from(this).inflate(R.layout.instagram_auth_dialog, null)
        val dialog = Dialog(this@SocialLoginActivity)
        dialog.setContentView(ll)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val webViewClient = WebViewClient()

        val webview = dialog.find<WebView>(R.id.webView)

        dialog.show()
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
                Log.i(TAG, "PACKAGE_NAME: $packageName")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }

    }


    private fun launchingInst() {
        mApp!!.authorize()
        /* if (mApp!!.hasAccessToken()) {
             val builder = AlertDialog.Builder(this@SocialLoginActivity)
             builder.setMessage("Disconnect from Instagram?")
                 .setCancelable(false)
                 .setPositiveButton("Yes") { dialog, id ->
                     mApp!!.resetAccessToken()
                 }
                 .setNegativeButton("No") { dialog, id -> dialog.cancel() }
             val alert = builder.create()
             alert.show()
         } else {
             mApp!!.authorize()
         }*/
    }


    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


}
