package com.recep.hunt.api

import com.recep.hunt.domain.entities.*
import com.recep.hunt.model.*
import com.recep.hunt.model.GetRandomQuestions.GetRandomQuestionReponse
import com.recep.hunt.model.RegistrationModule.RegistrationResponse
import com.recep.hunt.model.RegistrationModule.User
import com.recep.hunt.model.SubscriptinPlans.SubscriptionsPlansResponse
import com.recep.hunt.model.TicketList.TicketListResponse
import com.recep.hunt.model.UpdateUserInfoResponse.UpdateUserInfoResponseModel
import com.recep.hunt.model.UserProfile.UserProfileResponse
import com.recep.hunt.model.createTicket.CreateTicketResponse
import com.recep.hunt.model.isEmailRegister.isEmailRegisterResponse
import com.recep.hunt.model.login.LoginResponse
import com.recep.hunt.model.logout.LogoutReponse
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.model.nearestLocation.NearestLocationResponse
import com.recep.hunt.model.notification.NotificationResponse
import com.recep.hunt.model.randomQuestion.RandomQuestionResponse
import com.recep.hunt.model.reportUser.ReportUserResponse
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.swipeUser.SwipeUserResponse
import com.recep.hunt.model.usersList.UsersListResponse
import com.recep.hunt.model.viewTicket.ViewTicketResponse
import io.reactivex.Single

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    @POST("/api/login")
    fun loginUser(@Body login: LoginModel): Call<LoginResponse>


    @POST("/api/register")
    fun createUser(@Body file : RequestBody): Call<RegistrationResponse>
//    @POST("/api/register")
//    fun createUser(@Body login: Registration): Call<RegistrationResponse>


    @POST("/api/is-user-online")
    fun makeUserOnline(@Body isUserOnline:MakeUserOnline,@Header("Authorization")  authorization:String):Call<MakeUserOnlineResponse>

//    @POST("/api/update-user-profile")
//    fun saveUserDetails(@Body userDetails:UpdateUserInfoModel):Call<UpdateUserInfoResponseModel>


    @POST("/api/update-user-profile")
    fun saveUserDetails(@Body file: RequestBody,@Header("Authorization")  authorization:String):Call<UpdateUserInfoResponseModel>

    @POST("/api/update-user-profile-images")
    fun saveImages(@Body file: RequestBody,@Header("Authorization")  authorization:String):Call<UpdateUserInfoResponseModel>

    @POST("/api/add-more-images")
    fun addImageInAlbum(@Body file: RequestBody,@Header("Authorization")  authorization:String):Call<UpdateUserInfoResponseModel>

    @GET("api/logout")
    fun  logoutUser(@Header("Authorization")  authorization:String):Call<LogoutReponse>


    @POST("/api/report-user")
    fun reportUser(@Body reportUser:ReportUser):Call<ReportUserResponse>



    @GET("/api/user-profile")
    fun getUserProfile(@Header("Authorization")  authorization:String) : Call<UserProfileResponse>

    @POST("/api/user-swipe-data")
    fun userSwipes(@Body userSwipe: UserSwipe,@Header("Authorization")  authorization:String):Call<User>

    @POST("/api/create-ticket")
    fun createTicket(@Body file : RequestBody,@Header("Authorization")  authorization:String):Call<CreateTicketResponse>



    @GET("/api/ticket-list")
    fun getTickets(@Header("Authorization")  authorization:String):Call<TicketListResponse>



    @POST("/api/view-ticket")
    fun viewTicket(@Body viewTicket: ViewTicket,@Header("Authorization")  authorization:String):Call<ViewTicketResponse>



    @POST("/api/delete-user")
    fun deleteUser(@Body deleteUser: DeleteUser,@Header("Authorization")  authorization:String):Call<ViewTicketResponse>



    @GET("/api/subscription-plan")
    fun getSubscriptionPlan(@Header("Authorization")  authorization:String):Call<SubscriptionsPlansResponse>



    @GET("/api/random-question")
    fun getRandomQuestion():Call<GetRandomQuestionReponse>

    @POST("/api/answer-random-question")
    fun answerRandomQuestion(@Header("Authorization")  authorization:String,@Body answerRandomQuestions: AnswerRandomQuestions):Call<AnswerRandomQuestions>

    @POST("/api/nearest-place")
    fun getNearestPlace(@Body nearestLocation: NearestLocation,@Header("Authorization")  authorization:String):Call<NearestLocationResponse>

    @POST("/api/select-loation")
    fun selectLocation(@Body selectLocation: SelectLocation,@Header("Authorization")  authorization:String):Call<SelectLocationResponse>

    @POST("/api/users-list")
    fun usersList(@Body usersListFilter: UsersListFilter,@Header("Authorization")  authorization:String):Call<UsersListResponse>

    @POST("/api/user-swipe-data")
    fun swipeUser(@Body userSwipe: UserSwipe,@Header("Authorization")  authorization:String):Call<SwipeUserResponse>



    @GET("/api/notification")
    fun getNotification(@Header("Authorization")  authorization:String):Call<NotificationResponse>



    @GET("/api/random-question")
    fun getRandomQuestion(@Header("Authorization")  authorization:String):Call<RandomQuestionResponse>


    @POST("/api/email-check")
    fun checkIsEmailRegister(@Header("Authorization")  authorization:String,@Body email:CheckUserEmail):Call<isEmailRegisterResponse>


    /*@POST("/api/user-hunt-begin")
    fun sendUserHuntBeginLocation(@Header("Authorization")  authorization:String,@Body beginHuntLocationParams: BeginHuntLocationParams): Single<Any>
    */
    @POST("/api/user-hunt-begin")
    fun sendUserHuntBeginLocation(@Body beginHuntLocationParams: BeginHuntLocationParams): Single<APIResponse<BeginHuntLocation>>

    @POST("/api/push-notification-single-user")
    fun pushNotificationSingleUser(@Body pushNotificationSingleUserParams: PushNotificationSingleUserParams): Single<APIResponse<Any>>

    @POST("/api/update-hunt-begin")
    fun updateHuntBegin(@Body updateHuntBeginParams: UpdateHuntBeginParams): Single<APIResponse<Any>>

}