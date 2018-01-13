package ua.nure.dzhafarov.dineit.core

import android.app.Application
import ua.nure.dzhafarov.dineit.data.security.AuthManager

class DineItApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AuthManager.initPrefsWithContext(this)
    }
}