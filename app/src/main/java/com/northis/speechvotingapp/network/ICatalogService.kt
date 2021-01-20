package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.Speech
import kotlinx.coroutines.Deferred
import retrofit2.http.*
import java.util.*

private const val SERVICE = "speechcatalog/"
private const val SPEECH = "speeches/"
private const val CALENDAR = "calendar/"
private const val ARRAY = "array/"

interface ICatalogService {


    @GET("$SERVICE$SPEECH{uuid}")
    suspend fun getSpeech(@Path("uuid") uuid: String): Speech

    @GET("$SERVICE$SPEECH")
    suspend fun getSpeeches(
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
    ): ArrayList<Speech>

    @POST("$SERVICE$SPEECH$ARRAY")
    suspend fun getSpeechesArray(@Body arrayId: Array<String>): ArrayList<Speech>

    @Multipart
    @PATCH("$SERVICE$SPEECH{uuid}/executor")
    suspend fun changeSpeechExecutor(
        @Path("uuid") uuid: String,
        @Part("ExecutorId") executorId: String,
        @Part("SpeechDate") speechDate: Date
    )

    @POST("$SERVICE$SPEECH")
    suspend fun createSpeech(@Body speech: Speech): Speech

    @POST("$SERVICE$SPEECH$CALENDAR")
    suspend fun createSpeechInCalendar(@Body speech: Speech): Speech

    @DELETE("$SERVICE$SPEECH{uuid}")
    suspend fun deleteSpeech(@Path("uuid") speechId: String)

    @GET("$SERVICE$CALENDAR")
    suspend fun getCalendarSpeeches(
        @Query("minEndDate") minEndDate: Date,
        @Query("maxEndDate") maxEndDate: Date
    ): ArrayList<Speech>

    @Multipart
    @PATCH("$SERVICE$SPEECH/{uuid}")
    suspend fun updateSpeech(
        @Path("uuid") speechId: String,
        @Part("Description") description: String,
        @Part("Theme") theme: String
    )

}