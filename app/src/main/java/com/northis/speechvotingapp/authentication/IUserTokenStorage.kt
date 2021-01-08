package com.northis.speechvotingapp.authentication

import android.content.Context
import java.util.*

interface IUserTokenStorage {
    fun saveToken(context: Context, accessToken: String, idToken: String?, refreshToken: String?)
    fun getAccessToken(context: Context): String?
    fun getRefreshToken(context: Context): String?
    fun setExpirationDate(context: Context, expiresIn: Int)
    fun isExpired(context: Context): Boolean


    companion object {
        val instance: IUserTokenStorage by lazy {
            object : IUserTokenStorage {
                override fun saveToken(
                    context: Context,
                    accessToken: String,
                    idToken: String?,
                    refreshToken: String?
                ) {
                    val sharedPreferences = context.getSharedPreferences(
                        StorageValuesEnum.USER_META_DATA.toString(),
                        Context.MODE_PRIVATE
                    )
                    with(sharedPreferences.edit()) {
                        putString(StorageValuesEnum.ACCESS_TOKEN.toString(), accessToken)
                        putString(StorageValuesEnum.ID_TOKEN.toString(), idToken)
                        putString(StorageValuesEnum.REFRESH_TOKEN.toString(), refreshToken)
                        apply()
                    }
                }

                override fun getAccessToken(context: Context): String? {
                    val sharedPreferences = context.getSharedPreferences(
                        StorageValuesEnum.USER_META_DATA.toString(),
                        Context.MODE_PRIVATE
                    )
                    return sharedPreferences.getString(
                        StorageValuesEnum.ACCESS_TOKEN.toString(),
                        ""
                    )
                }

                override fun getRefreshToken(context: Context): String? {
                    val sharedPreferences = context.getSharedPreferences(
                        StorageValuesEnum.USER_META_DATA.toString(),
                        Context.MODE_PRIVATE
                    )
                    return sharedPreferences.getString(
                        StorageValuesEnum.REFRESH_TOKEN.toString(),
                        ""
                    )
                }

                override fun setExpirationDate(context: Context, expiresIn: Int) {
                    val sharedPreferences = context.getSharedPreferences(
                        StorageValuesEnum.USER_META_DATA.toString(),
                        Context.MODE_PRIVATE
                    )
                    val dateNow = Date().time
                    val expiresDate = dateNow + expiresIn.toLong()
                    sharedPreferences.edit()
                        .putLong(StorageValuesEnum.EXPIRATION_DATE.toString(), expiresDate)
                }

                override fun isExpired(context: Context): Boolean {
                    val sharedPreferences = context.getSharedPreferences(
                        StorageValuesEnum.USER_META_DATA.toString(),
                        Context.MODE_PRIVATE
                    )
                    val dateNow = Date().time
                    return dateNow <= sharedPreferences.getLong(
                        StorageValuesEnum.EXPIRATION_DATE.toString(),
                        0
                    )
                }
            }
        }
    }
}

private enum class StorageValuesEnum {
    USER_META_DATA,
    ACCESS_TOKEN,
    ID_TOKEN,
    REFRESH_TOKEN,
    EXPIRATION_DATE
}