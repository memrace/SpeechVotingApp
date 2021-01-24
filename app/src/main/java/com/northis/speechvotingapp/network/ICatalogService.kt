package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.Speech
import com.northis.speechvotingapp.model.Speeches
import retrofit2.Response
import retrofit2.http.*
import java.util.*

private const val SERVICE = "speechcatalog/"
private const val SPEECH = "speeches/"
private const val CALENDAR = "calendar/"
private const val ARRAY = "array/"

interface ICatalogService {


    @GET("$SERVICE$SPEECH{uuid}")
    suspend fun getSpeech(@Path("uuid") uuid: String): Response<Speech>

    @GET("$SERVICE$SPEECH")
    suspend fun getSpeeches(
        @Query("sort") sort: String,
        @Query("skip") skip: Int? = null,
        @Query("status") status: String? = null,
        @Query("count") count: Int? = null,
        @Query("minCreateDate") minCreateDate: Date? = null,
        @Query("maxCreateDate") maxCreateDate: Date? = null,
        @Query("minEndDate") minEndDate: Date? = null,
        @Query("maxEndDate") maxEndDate: Date? = null,
        @Query("executor") executorId: String? = null,
        @Query("creator") creatorId: String? = null,
        @Query("theme") theme: String? = null
    ): Response<Speeches>

    // Под вопросом.
    @POST("$SERVICE$SPEECH$ARRAY")
    suspend fun getSpeechesArray(@Body arrayId: Array<String>): Response<ArrayList<Speech>>

    @Multipart
    @PATCH("$SERVICE$SPEECH{uuid}/executor")
    suspend fun changeSpeechExecutor(
        @Path("uuid") uuid: String,
        @Part("ExecutorId") executorId: String,
        @Part("SpeechDate") speechDate: Date
    ): Response<Unit>

    @POST("$SERVICE$SPEECH")
    suspend fun createSpeech(@Body speech: Speech): Response<Speech>

    @POST("$SERVICE$SPEECH$CALENDAR")
    suspend fun createSpeechInCalendar(@Body speech: Speech): Response<Speech>

    @DELETE("$SERVICE$SPEECH{uuid}")
    suspend fun deleteSpeech(@Path("uuid") speechId: String): Response<Unit>

    @GET("$SERVICE$CALENDAR")
    suspend fun getCalendarSpeeches(
        @Query("minEndDate") minEndDate: Date,
        @Query("maxEndDate") maxEndDate: Date
    ): Response<ArrayList<Speech>>

    @Multipart
    @PATCH("$SERVICE$SPEECH/{uuid}")
    suspend fun updateSpeech(
        @Path("uuid") speechId: String,
        @Part("Description") description: String,
        @Part("Theme") theme: String
    ): Response<Unit>

}