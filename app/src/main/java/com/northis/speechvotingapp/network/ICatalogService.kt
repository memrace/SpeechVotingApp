package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.Speech
import retrofit2.Call
import retrofit2.http.*
import java.util.*

private const val SERVICE = "speechcatalog/"
private const val SPEECH = "speeches/"
private const val CALENDAR = "calendar/"
private const val ARRAY = "array/"

interface ICatalogService {


    @GET("$SERVICE$SPEECH{uuid}")
    fun getSpeech(@Path("uuid") uuid: String): Call<Speech>

    @GET("$SERVICE$SPEECH")
    fun getSpeeches(
        @Query("sort") sort: String,
        @Query("skip") skip: Int?,
        @Query("count") count: Int?,
        @Query("minCreateDate") minCreateDate: Date?,
        @Query("maxCreateDate") maxCreateDate: Date?,
        @Query("minEndDate") minEndDate: Date?,
        @Query("maxEndDate") maxEndDate: Date?,
        @Query("executor") executorId: String?,
        @Query("creator") creatorId: String?,
        @Query("theme") theme: String?
    ): Call<Array<Speech>>

    @POST("$SERVICE$SPEECH$ARRAY")
    fun getSpeechesArray(@Body arrayId: Array<String>): Call<Array<Speech>>

    @Multipart
    @PATCH("$SERVICE$SPEECH{uuid}/executor")
    fun changeSpeechExecutor(
        @Path("uuid") uuid: String,
        @Part("ExecutorId") executorId: String,
        @Part("SpeechDate") speechDate: Date
    )

    @POST("$SERVICE$SPEECH")
    fun createSpeech(@Body speech: Speech): Call<Speech>

    @POST("$SERVICE$SPEECH$CALENDAR")
    fun createSpeechInCalendar(@Body speech: Speech): Call<Speech>

    @DELETE("$SERVICE$SPEECH{uuid}")
    fun deleteSpeech(@Path("uuid") speechId: String)

    @GET("$SERVICE$CALENDAR")
    fun getCalendarSpeeches(
        @Query("minEndDate") minEndDate: Date,
        @Query("maxEndDate") maxEndDate: Date
    ): Call<Array<Speech>>

    @Multipart
    @PATCH("$SERVICE$SPEECH/{uuid}")
    fun updateSpeech(
        @Path("uuid") speechId: String,
        @Part("Description") description: String,
        @Part("Theme") theme: String
    )

}