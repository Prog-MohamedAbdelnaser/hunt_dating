package com.recep.hunt.swipe.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.FeaturesConstants
import com.recep.hunt.R
import com.recep.hunt.api.ApiClient
import com.recep.hunt.base.activity.BaseActivity
import com.recep.hunt.base.extentions.handleApiErrorWithSnackBar
import com.recep.hunt.domain.entities.PushNotificationSingleUserParams
import com.recep.hunt.features.common.CommonState
import com.recep.hunt.filters.FilterBottomSheetDialog
import com.recep.hunt.matchs.MatchQuestionnaireActivity
import com.recep.hunt.model.MakeUserOnline
import com.recep.hunt.model.UserSwipe
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.model.swipeUser.SwipeUserResponse
import com.recep.hunt.notifications.NotificationsActivity
import com.recep.hunt.profile.ProfileActivity
import com.recep.hunt.profile.UserProfileActivity
import com.recep.hunt.swipe.adapter.SwipeProfiles
import com.recep.hunt.swipe.model.SwipeUserModel
import com.recep.hunt.swipe.vm.SwipeViewModel
import com.recep.hunt.userDetail.UserDetalBottomSheetFragment
import com.recep.hunt.utilis.SharedPrefrenceManager
import com.recep.hunt.utilis.Utils
import com.recep.hunt.utilis.launchActivity

import kotlinx.android.synthetic.main.activity_swip_profiles.*
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import swipeable.com.layoutmanager.OnItemSwiped
import swipeable.com.layoutmanager.SwipeableLayoutManager
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper

class SwipProfilesActivity : BaseActivity() , SwipeProfiles.ClickToolbarControllerListener,
    FilterBottomSheetDialog.FilterBottomSheetListener {

    var bottomSheet: UserDetalBottomSheetFragment? = null

    private var usersProfileList = ArrayList<SwipeUserModel>()

    private var locationName=""

    val swipeProfilesAdapter:SwipeProfiles by lazy { SwipeProfiles() }

    private val swipViewModel: SwipeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swip_profiles)

        getUsersProfilesFromArgs()
        initRecyclerSwiping()
        initModelObservers()
    }

    private fun initModelObservers() {
        swipViewModel.apply {
            sendPushNotificationSingleUserLiveData.observe(this@SwipProfilesActivity, Observer {
                handlePushNotificationSingleUserState(it)
            })
        }
    }

    private fun handlePushNotificationSingleUserState(state: CommonState<Any>?) {
        when(state){
            is CommonState.Success->{
                //todo when like sent successfully
               gotoMatchQuestionScreen(swipeProfilesAdapter.getCurrentProfile())
            }
            is CommonState.Error->{
                handleApiErrorWithSnackBar(state.exception)
            }
        }
    }

    private fun setupUserDetailBottomSheet(swipeUserModel: SwipeUserModel) {
        Log.e("TAG", " called ")

        if (bottomSheet != null) {
            try {
                if (!bottomSheet?.dialog?.isShowing!!) {
                    supportFragmentManager.beginTransaction().remove(bottomSheet!!).commit();
                }
            } catch (e: Exception) {
                try {
                    supportFragmentManager.beginTransaction().remove(bottomSheet!!).commit();
                } catch (e: Exception) {
                }
            } catch (e: Exception) {
            }
            try {
                if (!bottomSheet?.dialog?.isShowing!!) {
                    bottomSheet = UserDetalBottomSheetFragment(this, swipeUserModel)
                    bottomSheet?.show(supportFragmentManager, "UserDetalBottomSheetFragment")
                }
            } catch (e: Exception) {
                bottomSheet = UserDetalBottomSheetFragment(this, swipeUserModel)
                bottomSheet?.show(supportFragmentManager, "UserDetalBottomSheetFragment")
            }
        } else {
            bottomSheet = UserDetalBottomSheetFragment(this, swipeUserModel)
            bottomSheet?.show(supportFragmentManager, "UserDetalBottomSheetFragment")

        }

    }

    private fun initRecyclerSwiping() {
        recyclerViewSwiping.layoutManager = SwipeableLayoutManager().setAngle(10)
            .setAnimationDuratuion(450)
            .setMaxShowCount(usersProfileList.size)
            .setScaleGap(0.1f)
            .setTransYGap(0)

        recyclerViewSwiping.adapter=swipeProfilesAdapter
        swipeProfilesAdapter.updateItems(usersProfileList)



        val swipeableTouchHelperCallback = object : SwipeableTouchHelperCallback(object : OnItemSwiped {

                override fun onItemSwiped() {

                if (swipeProfilesAdapter.isLastPosition()){ finish()
                }
                    swipeProfilesAdapter.removeTop()
                }

                override fun onItemSwipedLeft() {
                    callSwipeUserApi(swipeProfilesAdapter.getCurrentProfile(),1)
                }

                override fun onItemSwipedRight() {
                    sendLikeToUser(swipeProfilesAdapter.getCurrentProfile())
                    callSwipeUserApi(swipeProfilesAdapter.getCurrentProfile(),2)
                }

                override fun onItemSwipedUp() {
                    Log.e("SWIPE", "UP")
                    setupUserDetailBottomSheet(swipeProfilesAdapter.getCurrentProfile())
                }

                override fun onItemSwipedDown() {
                    Log.e("SWIPE", "DOWN")
                }
            }) {

            override fun getAllowedDirectionsMovementFlags(holder: RecyclerView.ViewHolder?): Int {
                return ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            }

            override fun onMoveMotionEventLeft(x:Float,xCord:Float) {

                swipeProfilesAdapter.updateUserReactAction(false)

                if (x - xCord >= 255 || (x - xCord) % 255 >= 153)
                    swipeProfilesAdapter.userStoriesImageView?.setColorFilter(
                        Color.argb(153, 255, 42, 78))
                else if ((x - xCord) % 255 < 153)
                    swipeProfilesAdapter.userStoriesImageView?.setColorFilter(
                        Color.argb(((x - xCord) % 255).toInt(), 255, 42, 78))

            }

            override fun onMoveMotionEventRight(x:Float,xCord:Float) {

                swipeProfilesAdapter.updateUserReactAction(true)
                //  swipeProfilesAdapter.updateUserReactAction(true)
                if (xCord - x >= 255 || (xCord - x) % 255 >= 179)
                    swipeProfilesAdapter.userStoriesImageView?.setColorFilter(
                        Color.argb(179, 58, 204, 225))
                else if ((xCord - x) % 255 < 179)
                    swipeProfilesAdapter.userStoriesImageView?.setColorFilter(
                        Color.argb(((xCord - x) % 255).toInt(), 58, 204, 225))

            }

            override fun onMoveMotionEventCanceld() {
                swipeProfilesAdapter.updateUserReactAction(null)
                swipeProfilesAdapter.userStoriesImageView?.colorFilter = null
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeableTouchHelperCallback)

        itemTouchHelper.attachToRecyclerView(recyclerViewSwiping)


    }

    private fun getUsersProfilesFromArgs(){
        usersProfileList = intent.getParcelableArrayListExtra("swipeUsers")
        locationName = intent.getStringExtra(FeaturesConstants.LOCATION_OBJECT_KEY)
    }

    private fun navigateToProfileActivity() {
        launchActivity<ProfileActivity>()
    }

    private fun showIncognitoBtn() {
        val isIncognito = SharedPrefrenceManager.getisIncognito(this)

        if (isIncognito) {
            SharedPrefrenceManager.setisIncognito(this, false)
            val ll = LayoutInflater.from(this).inflate(R.layout.incoginito_dialog_layout, null)
            val dialog = Dialog(this)
            dialog.setContentView(ll)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val gotItBtn: Button = dialog.find(R.id.got_it_btn)
            gotItBtn.setOnClickListener {
                makeUserOnOffline(false)
                dialog.dismiss()
            }
            dialog.show()
        } else {
            makeUserOnOffline(true)
            SharedPrefrenceManager.setisIncognito(this, true)
        }

    }

    private fun makeUserOnOffline(is_online: Boolean) {
        val makeUserOnline = MakeUserOnline(is_online)

        val call = ApiClient.getClient.makeUserOnline(
            makeUserOnline,
            SharedPrefrenceManager.getUserToken(this)
        )

        call.enqueue(object : Callback<MakeUserOnlineResponse> {
            override fun onFailure(call: Call<MakeUserOnlineResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MakeUserOnlineResponse>, response: Response<MakeUserOnlineResponse>) {

                if (!response.isSuccessful && !isFinishing) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@SwipProfilesActivity, strErrorJson)) {
                        return
                    }
                }

                if (is_online == false)
                    Toast.makeText(this@SwipProfilesActivity, "You're offline", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@SwipProfilesActivity, "You're online", Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun navigateToNotificationActivity(){
        launchActivity<NotificationsActivity>()
    }

    private fun openFilterBottomFragment(){
        val bottomSheet = FilterBottomSheetDialog(this)
        bottomSheet.show(supportFragmentManager, "FilterBottomSheetDialog")
    }

    override fun onFilterBottomSheetClickApplyListener() {

    }

    override fun onClickFilter() {
        openFilterBottomFragment()
    }

    override fun onClickShowProfile() {
        navigateToProfileActivity()
    }

    override fun onClickShowIncognito() {
        showIncognitoBtn()
    }

    override fun onClickShowNotification() {
        navigateToNotificationActivity()
    }

    private fun sendLikeToUser(swipeUserModel: SwipeUserModel) {
        swipViewModel.sendPushNotificationSingleUser(PushNotificationSingleUserParams(swipeUserModel.id,"Like"))
    }

    private fun callSwipeUserApi(mSwipeUserModel: SwipeUserModel, like: Int) {
        var likes: String = ""
        if (like == 1) //Dislike this user
        {
            likes = "dislike"
        }
        if (like == 2) //Like this user
        {
            likes = "like"
        }
        showProgressDialog()
        val swipe = UserSwipe(mSwipeUserModel.id, likes)
        val call = ApiClient.getClient.swipeUser(swipe, SharedPrefrenceManager.getUserToken(this))
        call.enqueue(object : Callback<SwipeUserResponse> {
            override fun onFailure(call: Call<SwipeUserResponse>, t: Throwable) {
                Log.d("Api call failure -> ", "" + call)
                hideProgressDialog()
            }

            override fun onResponse(call: Call<SwipeUserResponse>, response: Response<SwipeUserResponse>) {
                hideProgressDialog()
                if (!response.isSuccessful) {
                    val strErrorJson = response.errorBody()?.string()
                    if (Utils.isSessionExpire(this@SwipProfilesActivity, strErrorJson)) {
                        return
                    }
                }

                val status = response.body()?.status

                if (status == 2) {
                    gotoMatchQuestionScreen(mSwipeUserModel)

                    Toast.makeText(
                        this@SwipProfilesActivity,
                        "Congratulation, You matched",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        })
    }

    private fun gotoMatchQuestionScreen(mSwipeUserModel: SwipeUserModel) {
        val intent = Intent(this, MatchQuestionnaireActivity::class.java)
        intent.putExtra("swipeUsers", mSwipeUserModel)
        intent.putExtra(FeaturesConstants.LOCATION_OBJECT_KEY, locationName)

//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}
