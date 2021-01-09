package com.northis.speechvotingapp.authentication

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.*

interface IUserTokenStorage {
    fun getStorage(context: Context): SharedPreferences {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPrefsFile: String = StorageValuesEnum.USER_META_DATA.toString()
        return EncryptedSharedPreferences.create(
            context,
            sharedPrefsFile,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

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
                    val sharedPreferences = getStorage(context)
                    with(sharedPreferences.edit()) {
                        putString(StorageValuesEnum.ACCESS_TOKEN.toString(), accessToken)
                        putString(StorageValuesEnum.ID_TOKEN.toString(), idToken)
                        putString(StorageValuesEnum.REFRESH_TOKEN.toString(), refreshToken)
                        apply()
                    }
                }

                override fun getAccessToken(context: Context): String? {
                    val sharedPreferences = getStorage(context)
                    Log.d("Token request", "Токен получен!")
                    return sharedPreferences.getString(
                        StorageValuesEnum.ACCESS_TOKEN.toString(),
                        ""
                    )
                }

                override fun getRefreshToken(context: Context): String? {
                    val sharedPreferences = getStorage(context)
                    return sharedPreferences.getString(
                        StorageValuesEnum.REFRESH_TOKEN.toString(),
                        ""
                    )
                }

                override fun setExpirationDate(context: Context, expiresIn: Int) {
                    val sharedPreferences = getStorage(context)
                    val dateNow = Date().time
                    val expiresDate = dateNow + expiresIn.toLong()
                    sharedPreferences.edit()
                        .putLong(StorageValuesEnum.EXPIRATION_DATE.toString(), expiresDate)
                }

                override fun isExpired(context: Context): Boolean {
                    val sharedPreferences = getStorage(context)
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