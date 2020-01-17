package com.recep.hunt.constants


/**
 * Created by Rishabh Shukla
 * on 2019-08-18
 * Email : rishabh1450@gmail.com
 */

class Constants {
    companion object {

        const val CLIENT_ID = "50861e5c85324044a5c2b888735d7ca8"
        const val CLIENT_SECRET = "ad97c323aaa94ffdb5bdf5a6a25435e1"
        // public static final String CALLBACK_URL = "instagram://connect";
        const val CALLBACK_URL = "https://www.gethunt.app/"

        //BASEURl =
        const val baseURL = "http://165.22.18.129/dev/api"
        const val instaGramUrl = "https://www.instagram.com/oauth/authorize/?"
        const val prefsName = "app_prefs_file"

        const val messageSentType = "messageSentType"
        const val messageReceivedType = "messageReceivedType"

        const val IMGURI = "image_uri"
        const val socialFBType = "socialFBType"
        const val socialInstaType = "socialInstaType "
        const val socialGoogleType = "socialGoogleType"

        const val apiDateFormat = "yyyy-MM-dd"
        const val isOTPVerified = true

        const val oneMonthValue = "1"
        const val sixMonthValue = "6"
        const val twelveMonthValue = "12"

        //Info you provide keys
        const val friends = "friends"
        const val birthday = "birthday"
        const val hometown = "homeTown"
        const val photos = "photos"
        const val pageLikes = "pageLikes"
        const val gender = "gender"
        const val emailAddress = "emailAddress"

        //Male,Female,Others,Both
        const val male = "male"
        const val female = "female"
        const val others = "others"
        const val both = "male,female"


        const val MALE = "Male"
        const val FEMALE = "Female"
        const val OTHERS = "Other"
        const val BOTH = "Both"
        const val isUserGenderChanged = false

        var defaultWidth = 600

        const val NULL = "null"

        const val RELATIONSHIP = 0
        const val HEIGHT = 1
        const val GYM = 2
        const val EDUCATIONLEVEL = 3
        const val DRINK = 4
        const val SMOKE = 5
        const val PETS = 6
        const val LOOKINGFOR = 7
        const val KIDS = 8
        const val ZODIAC = 9
        const val RELIGION = 10

        //Mouse click duration
        const val CLICK_ACTION_THRESHOLD = 200


//        val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoRefrence&key=${context.resources.getString(R.string.google_api_key)}"


    }
}