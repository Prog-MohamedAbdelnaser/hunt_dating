package com.recep.hunt.utilis

import android.content.Context
import com.recep.hunt.constants.Constants


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */
enum class PersistenceKeys {
    UserModel,
    IsOtpVerified,
    IsFromSocial,
    UserMobileNumber,
    DeviceToken,
    UserFirstName,
    UserLastName,
    UserGender,
    UserEmail,
    IsUserLoggedIn,
    UserDob,
    UserImage,
    UserLookingFor,
    UserInterestedIn,
    UserLatitude,
    UserLongitude,
    UserCountryCode,
    UserCountry,
    IsLoggedIn,
    AboutYou,
    JobTitle,
    CompanyName,
    HomeTown,
    SchoolUniversity,
    ProfileFirstImg,
    ProfileSecImg,
    ProfileThirdImg,
    ProfileFourImg,
    ProfileFiveImg,
    ProfileSixImg,
    ProfileImg,
    UserGenderChanged,
    RelationShipStatus,
    Height,
    Gym,
    EducationLevel,
    Drink,
    Smoke,
    Pets,
    LookingFor,
    Kids,
    Zodiac,
    Religion,
    IsIncognito,
    UserAge,
    socialType
}

class SharedPrefrenceManager {
    companion object {
        //ISOTpVerified
        fun setIsOtpVerified(context: Context, value: Boolean) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.IsOtpVerified.toString(), value)
            editor.apply()
        }

        fun getIsOtpVerified(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getBoolean(PersistenceKeys.IsOtpVerified.toString(), false)
        }

        //IsLoggedIn
        fun setIsLoggedIn(context: Context, value: Boolean) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.IsLoggedIn.toString(), value)
            editor.apply()
        }

        fun getIsLoggedIn(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getBoolean(PersistenceKeys.IsLoggedIn.toString(), false)
        }
        //isIncognito
        fun setisIncognito(context: Context, value: Boolean) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.IsIncognito.toString(), value)
            editor.apply()
        }

        fun getisIncognito(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getBoolean(PersistenceKeys.IsIncognito.toString(), false)
        }

        //IsUserLoggedIn
        fun setIsUserLoggedIn(context: Context, value: Boolean) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.IsUserLoggedIn.toString(), value)
            editor.apply()
        }

        fun geIsUserLoggedIn(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getBoolean(PersistenceKeys.IsUserLoggedIn.toString(), false)
        }

        //UserLoginType
        fun setsocialType(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.socialType.toString(), value)
        }

        fun getsocialType(context: Context) = getSharePrefs(context, PersistenceKeys.socialType.toString())


        //User model
        fun setUserDetailModel(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserModel.toString(), value)
        }

        fun getUserDetailModel(context: Context) = getSharePrefs(context, PersistenceKeys.UserModel.toString())


        //IsFromSocial
        fun setIsFromSocial(context: Context, value: Boolean) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.IsFromSocial.toString(), value)
            editor.apply()
        }

        fun getIsFromSocial(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getBoolean(PersistenceKeys.IsFromSocial.toString(), false)
        }


        //DeviceToken
        fun setDeviceToken(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.DeviceToken.toString(), value)
        }

        fun getDeviceToken(context: Context) = getSharePrefs(context, PersistenceKeys.DeviceToken.toString())

        //UserAge
        fun setUserage(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.UserAge.toString(), value)
        }
        fun getUserAge(context: Context) = getSharePrefs(context,PersistenceKeys.UserAge.toString())

        //UserFirstName
        fun setUserFirstName(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserFirstName.toString(), value)
        }

        fun getUserFirstName(context: Context) = getSharePrefs(context, PersistenceKeys.UserFirstName.toString())

        //UserLastName
        fun setUserLastName(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserLastName.toString(), value)
        }

        fun getUserLastName(context: Context) = getSharePrefs(context, PersistenceKeys.UserLastName.toString())

        //UserGender
        fun setUserGender(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserGender.toString(), value)
        }

        fun getUserGender(context: Context) = getSharePrefs(context, PersistenceKeys.UserGender.toString())

        //UserEmail
        fun setUserEmail(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserEmail.toString(), value)
        }

        fun getUserEmail(context: Context) = getSharePrefs(context, PersistenceKeys.UserEmail.toString())


        //UserDob
        fun setUserDob(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserDob.toString(), value)
        }

        fun getUserDob(context: Context) = getSharePrefs(context, PersistenceKeys.UserDob.toString())

        //UserImage
        fun setUserImage(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserImage.toString(), value)
        }

        fun getUserImage(context: Context) = getSharePrefs(context, PersistenceKeys.UserImage.toString())


        //UserLookingFor
        fun setUserLookingFor(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserLookingFor.toString(), value)
        }

        fun getUserLookingFor(context: Context) = getSharePrefs(context, PersistenceKeys.UserLookingFor.toString())

        //UserInterestedIn - value for userLooking for
        fun setUserInterestedIn(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserInterestedIn.toString(), value)
        }

        fun getUserInterestedIn(context: Context) = getSharePrefs(context, PersistenceKeys.UserInterestedIn.toString())

        //UserLatitude - value for userLooking for
        fun setUserLatitude(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserLatitude.toString(), value)
        }

        fun getUserLatitude(context: Context) = getSharePrefs(context, PersistenceKeys.UserLatitude.toString())

        //UserLongitude - value for userLooking for
        fun setUserLongitude(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserLongitude.toString(), value)
        }

        fun getUserLongitude(context: Context) = getSharePrefs(context, PersistenceKeys.UserLongitude.toString())


        //UserMobileNumber
        fun setUserMobileNumber(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserMobileNumber.toString(), value)
        }

        fun getUserMobileNumber(context: Context) = getSharePrefs(context, PersistenceKeys.UserMobileNumber.toString())

        //UserCountryCode
        fun setUserCountryCode(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserCountryCode.toString(), value)
        }

        fun getUserCountryCode(context: Context) = getSharePrefs(context, PersistenceKeys.UserCountryCode.toString())

        //UserCountry
        fun setUserCountry(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.UserCountry.toString(), value)
        }

        fun getUserCountry(context: Context) = getSharePrefs(context, PersistenceKeys.UserCountry.toString())

        //AboutYou
        fun setAboutYou(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.AboutYou.toString(), value)
        }

        fun getAboutYou(context: Context) = getSharePrefs(context, PersistenceKeys.AboutYou.toString())

        //JobTitle
        fun setJobTitle(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.JobTitle.toString(), value)
        }

        fun getJobTitle(context: Context) = getSharePrefs(context, PersistenceKeys.JobTitle.toString())

        //CompanyName
        fun setCompanyName(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.CompanyName.toString(), value)
        }

        fun getCompanyName(context: Context) = getSharePrefs(context, PersistenceKeys.CompanyName.toString())

        //HomeTown
        fun setHomeTown(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.HomeTown.toString(), value)
        }

        fun getHomeTown(context: Context) = getSharePrefs(context, PersistenceKeys.HomeTown.toString())

        //SchoolUniversity
        fun setSchoolUniversity(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.SchoolUniversity.toString(), value)
        }

        fun getSchoolUniversity(context: Context) = getSharePrefs(context, PersistenceKeys.SchoolUniversity.toString())


        fun setFirstImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileFirstImg.toString(), value)
        }

        fun getFirstImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileFirstImg.toString())

        fun setSecImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileSecImg.toString(), value)
        }

        fun getSecImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileSecImg.toString())


        fun setThirdImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileThirdImg.toString(), value)
        }

        fun getThirdImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileThirdImg.toString())


        fun setFourthImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileFourImg.toString(), value)
        }

        fun getFourthImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileFourImg.toString())


        fun setFiveImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileFiveImg.toString(), value)
        }

        fun getFiveImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileFiveImg.toString())


        fun setSixImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileSixImg.toString(), value)
        }

        fun getSixImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileSixImg.toString())


        fun setProfileImg(context: Context, value: String) {
            setSharedPrefs(context, PersistenceKeys.ProfileImg.toString(), value)
        }

        fun getProfileImg(context: Context) = getSharePrefs(context, PersistenceKeys.ProfileImg.toString())


        fun setUserGenderChanged(context: Context,value: Boolean){
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putBoolean(PersistenceKeys.UserGenderChanged.toString(), value)
            editor.apply()
        }

        fun getUserGenderChanged(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getBoolean(PersistenceKeys.UserGenderChanged.toString(), false)
        }

        fun setRelationshipStatus(context: Context,value: String){
            setSharedPrefs(context, PersistenceKeys.RelationShipStatus.toString(), value)
        }
        fun getRelationShipStatus(context: Context) = getSharePrefs(context,PersistenceKeys.RelationShipStatus.toString())

        fun setUserHeight(context: Context,value:String){
            setSharedPrefs(context,PersistenceKeys.Height.toString(),value)
        }
        fun getUserHeight(context: Context) = getSharePrefs(context,PersistenceKeys.Height.toString())

        fun setUserGym(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Gym.toString(),value)
        }
        fun getUserGym(context: Context) = getSharePrefs(context,PersistenceKeys.Gym.toString())

        fun setUserEducationLevel(context: Context,value:String){
            setSharedPrefs(context,PersistenceKeys.EducationLevel.toString(),value)
        }
        fun getUserEducationLevel(context: Context) = getSharePrefs(context,PersistenceKeys.EducationLevel.toString())

        fun setUserDrink(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Drink.toString(),value)
        }
        fun getUserDrink(context: Context) = getSharePrefs(context,PersistenceKeys.Drink.toString())

        fun setSmoke(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Smoke.toString(),value)
        }
        fun getSomke(context: Context) = getSharePrefs(context,PersistenceKeys.Smoke.toString())

        fun setPets(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Pets.toString(),value)
        }
        fun getPets(context: Context) = getSharePrefs(context,PersistenceKeys.Pets.toString())

        fun setLookingFor(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.LookingFor.toString(),value)
        }
        fun getLookingFor(context: Context) = getSharePrefs(context,PersistenceKeys.LookingFor.toString())

        fun setKids(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Kids.toString(),value)
        }
        fun getKids(context: Context) = getSharePrefs(context,PersistenceKeys.Kids.toString())

        fun setZodiac(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Zodiac.toString(),value)
        }
        fun getZodiac(context: Context) = getSharePrefs(context,PersistenceKeys.Zodiac.toString())

        fun setReligion(context: Context,value: String){
            setSharedPrefs(context,PersistenceKeys.Religion.toString(),value)
        }
        fun getReligion(context: Context) = getSharePrefs(context,PersistenceKeys.Religion.toString())









//        RelationShipStatus,
//        Height,
//        Gym,
//        EducationLevel,
//        Drink,
//        Smoke,
//        Pets,
//        LookingFor,
//        Kids,
//        Zodiac,
//        Religion

        private fun setSharedPrefs(context: Context, key: String, value: String) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        private fun getSharePrefs(context: Context, key: String): String {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0)
            return sharedPreferences.getString(key, "null") ?: ""
        }

        fun clearAllSharePreference(context: Context) {
            val sharedPreferences = context.getSharedPreferences(Constants.prefsName, 0).edit()
            sharedPreferences.clear()
        }


    }
}