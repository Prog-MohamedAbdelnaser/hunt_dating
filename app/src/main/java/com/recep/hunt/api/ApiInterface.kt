package com.recep.hunt.api

import com.recep.hunt.model.*
import com.recep.hunt.model.GetRandomQuestions.GetRandomQuestionReponse
import com.recep.hunt.model.RegistrationModule.RegistrationResponse
import com.recep.hunt.model.SubscriptinPlans.SubscriptionsPlansResponse
import com.recep.hunt.model.TicketList.TicketListResponse
import com.recep.hunt.model.UpdateUserInfoResponse.UpdateUserInfoResponseModel
import com.recep.hunt.model.UserProfile.UserProfileResponse
import com.recep.hunt.model.createTicket.CreateTicketResponse
import com.recep.hunt.model.login.LoginResponse
import com.recep.hunt.model.login.User
import com.recep.hunt.model.logout.LogoutReponse
import com.recep.hunt.model.makeUserOnline.MakeUserOnlineResponse
import com.recep.hunt.model.nearestLocation.NearestLocationResponse
import com.recep.hunt.model.notification.NotificationResponse
import com.recep.hunt.model.reportUser.ReportUserResponse
import com.recep.hunt.model.selectLocation.SelectLocationResponse
import com.recep.hunt.model.viewTicket.ViewTicketResponse
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
    fun makeUserOnline(@Body isUserOnline:MakeUserOnline):Call<MakeUserOnlineResponse>

    @POST("/api/update-user-profile")
    fun saveUserDetails(@Body userDetails:UpdateUserInfoModel):Call<UpdateUserInfoResponseModel>

    @GET("api/logout")
    fun  logoutUser(@Header("Authorization")  authorization:String):Call<LogoutReponse>

    @POST("/api/report-user")
    fun reportUser(@Body reportUser:ReportUser):Call<ReportUserResponse>

    @GET("/api/user-profile")
    fun getUserProfile(@Header("Authorization")  authorization:String) : Call<UserProfileResponse>

    @POST("/api/user-swipe-data")
    fun userSwipes(@Body userSwipe: UserSwipe):Call<User>

    @POST("/api/create-ticket")
    fun createTicket(@Body createTicket: CreateTicket):Call<CreateTicketResponse>

    @GET("/api/ticket-list")
    fun getTickets():Call<TicketListResponse>

    @POST("/api/view-ticket")
    fun viewTicket(@Body viewTicket: ViewTicket):Call<ViewTicketResponse>

    @POST("/api/delete-user")
    fun deleteUser(@Body deleteUser: DeleteUser):Call<ViewTicketResponse>

    @GET("/api/subscription-plan")
    fun getSubscriptionPlan():Call<SubscriptionsPlansResponse>

    @GET("/api/random-question")
    fun getRandomQuestion():Call<GetRandomQuestionReponse>

    @POST("/api/answer-random-question")
    fun answerRandomQuestion(@Body answerRandomQuestions: AnswerRandomQuestions):Call<AnswerRandomQuestions>

    @POST("/api/nearest-place")
    fun getNearestPlace(@Body nearestLocation: NearestLocation):Call<NearestLocationResponse>

    @POST("/api/select-loation")
    fun selectLocation(@Body selectLocation: SelectLocation):Call<SelectLocationResponse>

//    @POST("/api/users-list")
//    fun usersList(@Body usersListFilter: UsersListFilter):Call<UsersListResponse>

    @GET("/api/notification")
    fun getNotification(@Header("Authorization")  authorization:String):Call<NotificationResponse>
}