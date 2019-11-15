package com.recep.hunt.login.instagramClassesJava;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import com.google.gson.GsonBuilder;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.recep.hunt.api.ApiClient;
import com.recep.hunt.constants.Constants;
import com.recep.hunt.login.ContinueAsSocialActivity;
import com.recep.hunt.login.model.UserSocialModel;
import com.recep.hunt.model.CheckUserEmail;
import com.recep.hunt.model.isEmailRegister.isEmailRegisterResponse;
import com.recep.hunt.utilis.FileUtils;
import com.recep.hunt.utilis.SharedPrefrenceManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.recep.hunt.login.SocialLoginActivity.socialTypeKey;
import static com.recep.hunt.login.SocialLoginActivity.userSocialModel;


public class InstagramApp {
    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private HashMap<String, String> userInfo = new HashMap<String, String>();
    private ArrayList<String> imageThumbList = new ArrayList<String>();
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private Context mCtx;
    private String json;
    private StringBuilder stringBuilder = new StringBuilder();

    private String mClientId;
    private String mClientSecret;
    private String firstName, lastName;
    private UserSocialModel userDetailsModel;

    public static int WHAT_FINALIZE = 0;
    public static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;
    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_URL = "url";

    /**
     * Callback url, as set in 'Manage OAuth Costumers' page
     * (https://developer.github.com/)
     */

    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String TAG = "InstagramAPI";
    public static final String TAG_ID = "id";
    public static final String TAG_PROFILE_PICTURE = "profile_picture";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_BIO = "bio";
    public static final String TAG_WEBSITE = "website";
    public static final String TAG_COUNTS = "counts";
    public static final String TAG_FOLLOWS = "follows";
    public static final String TAG_FOLLOWED_BY = "followed_by";
    public static final String TAG_MEDIA = "media";
    public static final String TAG_FULL_NAME = "full_name";
    public static final String TAG_META = "meta";
    public static final String TAG_CODE = "code";

    public InstagramApp(Context context, String clientId, String clientSecret, String callbackUrl) {
        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
        mTokenUrl = TOKEN_URL + "?client_id=" + clientId +
                "&client_secret=" + clientSecret + "&redirect_uri=" + mCallbackUrl +
                "&grant_type=authorization_code";
        mAuthUrl = AUTH_URL
                + "?client_id="
                + clientId
                + "&redirect_uri="
                + mCallbackUrl
                + "&response_type=code&display=touch&scope=basic";
        // + "&response_type=code&display=touch&scope=likes+comments+relationships";

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    // URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    // urlConnection.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId + "&client_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code);

                    writer.flush();
                    String response = Utils.streamToString(urlConnection
                            .getInputStream());
                    Log.i(TAG, "response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();

                    mAccessToken = jsonObj.getString("access_token");
                    Log.i(TAG, "Got access token: " + mAccessToken);

                    String id = jsonObj.getJSONObject("user").getString("id");
                    String user = jsonObj.getJSONObject("user").getString(
                            "username");
                    String name = jsonObj.getJSONObject("user").getString(
                            "full_name");

                    mSession.storeAccessToken(mAccessToken, id, user, name);
                    // getAllMediaImages();

                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    public void fetchUserName(final Handler handler) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.prefsName, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        mProgress = new ProgressDialog(mCtx);
        mProgress.setMessage("Loading ...");
        mProgress.show();
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = WHAT_FINALIZE;
                try {
                    URL url = new URL(API_URL + "/users/" + mSession.getId() + "/?access_token=" + mAccessToken);
                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    String response = Utils.streamToString(urlConnection.getInputStream());
                    System.out.println(response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject data_obj = jsonObj.getJSONObject(TAG_DATA);
                    userInfo.put(TAG_ID, data_obj.getString(TAG_ID));

                    userInfo.put(TAG_PROFILE_PICTURE, data_obj.getString(TAG_PROFILE_PICTURE));


                    userInfo.put(TAG_USERNAME, data_obj.getString(TAG_USERNAME));

                    userInfo.put(TAG_BIO, data_obj.getString(TAG_BIO));

                    userInfo.put(TAG_WEBSITE, data_obj.getString(TAG_WEBSITE));

                    JSONObject counts_obj = data_obj.getJSONObject(TAG_COUNTS);

                    userInfo.put(TAG_FOLLOWS, counts_obj.getString(TAG_FOLLOWS));

                    userInfo.put(TAG_FOLLOWED_BY, counts_obj.getString(TAG_FOLLOWED_BY));

                    userInfo.put(TAG_MEDIA, counts_obj.getString(TAG_MEDIA));

                    userInfo.put(TAG_FULL_NAME, data_obj.getString(TAG_FULL_NAME));

                    JSONObject meta_obj = jsonObj.getJSONObject(TAG_META);

                    userInfo.put(TAG_CODE, meta_obj.getString(TAG_CODE));


                    String[] fullname = data_obj.getString(TAG_FULL_NAME).split(" ");
                    if (fullname.length > 1) {
                        firstName = fullname[0];
                        lastName = fullname[1];
                    } else {

                        firstName = data_obj.getString(TAG_FULL_NAME);
                        lastName = "";
                    }

                    userDetailsModel = new UserSocialModel(data_obj.getString(TAG_ID), data_obj.getString(TAG_PROFILE_PICTURE),
                            data_obj.getString(TAG_FULL_NAME), data_obj.getString(TAG_USERNAME));

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.prefsName, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();




                    if(!checkIfUserLogin(data_obj.getString(TAG_USERNAME)))
                    {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        json = gson.toJson(userDetailsModel);
                        editor.putString("UserModel", String.valueOf(json)); // Storing string
                        editor.putString("UserFirstName", firstName); // Storing string
                        editor.putString("UserLastName", lastName); // Storing string
                        editor.putString("UserEmail", data_obj.getString(TAG_USERNAME));// Storing string
                        editor.putString("ProfileImg", getBitmapFromURL(data_obj.getString(TAG_PROFILE_PICTURE)));
                        editor.putString("InstagramToken",TAG_ID);
                        editor.putString("InstagramId",TAG_ID);
                        editor.putString("socialType", "social"); //
                        editor.commit();
                        mProgress.dismiss();
                        getAllMediaImages();
                    }







                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }
                mProgress.dismiss();
                handler.sendMessage(handler.obtainMessage(what, 2, 0));

            }
        }.start();



    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                // fetchUserName();
                mProgress.dismiss();
                mListener.onSuccess();
            }
        }
    };


    private Handler mHandlerForImages = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                // fetchUserName();
                mProgress.dismiss();
                mListener.onSuccess();
            }
        }
    };


    public HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getUserName() {
        return mSession.getUsername();
    }

    public String getId() {
        return mSession.getId();
    }

    public String getName() {
        return mSession.getName();
    }

    public String getTOken() {
        return mSession.getAccessToken();
    }

    public void authorize() {
        // Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
        // webAuthIntent.setData(Uri.parse(AUTH_URL));
        // mCtx.startActivity(webAuthIntent);
        mDialog.show();
    }


    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }

    public void getAllMediaImages() {

        mProgress = ProgressDialog.show(mCtx, "", "Loading images...");
        mProgress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int what = WHAT_FINALIZE;
                try {
                    URL url = new URL("https://api.instagram.com/v1/users/self/media/recent/?access_token=" + mAccessToken);
                    //Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    String response = Utils.streamToString(urlConnection.getInputStream());
                    System.out.println(response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                    JSONArray data = jsonObj.getJSONArray(TAG_DATA);
                    stringBuilder.setLength(0);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        JSONObject images_obj = data_obj.getJSONObject(TAG_IMAGES);
                        JSONObject thumbnail_obj = images_obj.getJSONObject(TAG_THUMBNAIL);
                        String str_url = thumbnail_obj.getString(TAG_URL);
                        imageThumbList.add(str_url);
                        stringBuilder.append(str_url + ",");
                    }

                    String allImages = removeLastChar(stringBuilder.toString());
                    Log.v("images", allImages);

                    mProgress.dismiss();
                    Intent intent = new Intent(mCtx, ContinueAsSocialActivity.class);
                    intent.putExtra("social_type_key", Constants.socialInstaType);
                    intent.putExtra("user_social_key", String.valueOf(json));
                    mCtx.startActivity(intent);
                    mSession.resetAccessToken();
                    // System.out.println("jsonObject::" + jsonObject);

                } catch (Exception exception) {
                    exception.printStackTrace();
                    what = WHAT_ERROR;
                    mProgress.dismiss();
                }
                // mProgress.dismiss();
                mHandlerForImages.sendEmptyMessage(what);
            }
        }).

                start();

    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }


    public String  getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return convertString(myBitmap);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    String convertString(Bitmap bitmap)
    {

        final int lnth=bitmap.getByteCount();
        ByteBuffer dst= ByteBuffer.allocate(lnth);
        bitmap.copyPixelsToBuffer( dst);
        byte[] barray=dst.array();
        String ans=Base64.encodeToString(barray,Base64.DEFAULT);
        return ans;
    }
    Boolean result=false;


    Boolean checkIfUserLogin(String email)
    {


        CheckUserEmail emailModel=new CheckUserEmail(email)

        Call<isEmailRegisterResponse> call = ApiClient.INSTANCE.getGetClient().checkIsEmailRegister(SharedPrefrenceManager.Companion.getUserToken(getApplicationContext()),emailModel);


        call.enqueue(new Callback<isEmailRegisterResponse>(){

            @Override
            public void onResponse(Call<isEmailRegisterResponse> call, Response<isEmailRegisterResponse> response) {
                if(!response.body().getStatus())
                {
                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                }
                 result=response.body().getStatus();
            }

            @Override
            public void onFailure(Call<isEmailRegisterResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something want wrong",Toast.LENGTH_LONG).show();

                result=false;


            }
        });
        return result;
    }

}
