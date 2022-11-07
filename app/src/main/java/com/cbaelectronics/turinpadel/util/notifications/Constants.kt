/**
 * Created by ITDev by Eduardo Sanchez on 31/3/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.util.notifications

class Constants {

    companion object{
        const val BASE_URL = "https://fcm.googleapis.com/"
        const val SERVER_KEY = "AAAANir19uY:APA91bHGmaayhPXrcJeW4uXKZuq2HjX8zsHgsTgpSgpRuh3nkNzEBGFdKP8L-rsMY1ozibIESgfJypII1iaufR8QXz5b_6gyy5TH2q41D9OfIF7qSzq7O3JRL9-38c0LLM2BVVdsHV1F"
        const val CONTENT_TYPE = "application/json"

        const val TITLE = "title"
        const val BODY = "body"
        const val TYPE = "type"
        const val USER = "user"

        const val TYPE_MATCH = "match"
        const val TYPE_TURN = "turn"
        const val TYPE_FIXED_TURN = "fixedTurn"
        const val TYPE_POST = "post"
        const val TYPE_COMMENT = "comment"
        const val TYPE_REQUEST = "request"

        const val FRAGMENT_ID = "fragment"
        const val FRAGMENT_MATCH = "fragmentMatch"
        const val FRAGMENT_TURN = "fragmentTurn"
        const val FRAGMENT_POST = "fragmentPost"
    }

}