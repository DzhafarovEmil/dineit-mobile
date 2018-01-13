package ua.nure.dzhafarov.dineit.data.security

import android.content.Context
import android.content.SharedPreferences
import ua.nure.dzhafarov.dineit.data.model.TokenData


object AuthManager {

    private lateinit var prefs: SharedPreferences

    fun initPrefsWithContext(context: Context) {
        prefs = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }
    
    fun accessToken(): String = prefs.getString(ACCESS_TOKEN, "")
    
    fun refreshToken(): String = prefs.getString(REFRESH_TOKEN, "")
    
    fun username(): String = prefs.getString(USERNAME, "")
    
    fun saveTokenData(token: TokenData) {
        prefs
                .edit()
                .putString(ACCESS_TOKEN, token.accessToken)
                .putString(REFRESH_TOKEN, token.refreshToken)
                .putLong(EXPIRES_IN, System.currentTimeMillis() / 1000L + token.expiresIn)
                .apply()
    }

    fun saveUsername(username: String) {
        prefs
                .edit()
                .putString(USERNAME, username)
                .apply()
    }
    
    fun updateAccessToken() {
        
    }
    
    fun clearTokenStorage() {
        prefs
                .edit()
                .clear()
                .apply()
    }

    fun isAccessTokenExist() = prefs.contains(ACCESS_TOKEN)
    
    fun isAccessTokenExpired(): Boolean {
        val currSeconds = System.currentTimeMillis() / 1000L
        val tokenSeconds =  prefs.getLong(EXPIRES_IN, 0L)
        
        return currSeconds > tokenSeconds
    }
    
    private val SHARED_NAME = "dineItPrefs"

    private val ACCESS_TOKEN = "access_token"

    private val REFRESH_TOKEN = "refresh_token"

    private val EXPIRES_IN = "expires_in"
    
    private val USERNAME = "username"
}