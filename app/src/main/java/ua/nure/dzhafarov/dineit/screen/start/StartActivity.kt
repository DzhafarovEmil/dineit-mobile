package ua.nure.dzhafarov.dineit.screen.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.screen.auth.LoginActivity
import ua.nure.dzhafarov.dineit.screen.auth.RegisterActivity
import ua.nure.dzhafarov.dineit.screen.main.MainActivity
import ua.nure.dzhafarov.dineit.data.security.AuthManager.isAccessTokenExist
import ua.nure.dzhafarov.dineit.data.security.AuthManager.isAccessTokenExpired

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        if (isAccessTokenExist() && !isAccessTokenExpired()) {
            startActivity(MainActivity.getLauncherIntent(this))
            finish()
        }

        btnRegister.setOnClickListener {
            startActivity(RegisterActivity.getLauncherIntent(this))
            finish()
        }

        btnLogin.setOnClickListener {
            startActivity(LoginActivity.getLauncherIntent(this))
            finish()
        }
    }

    companion object {
        fun getLauncherIntent(context: Context): Intent {
            return Intent(context, StartActivity::class.java)
        }
    }
}