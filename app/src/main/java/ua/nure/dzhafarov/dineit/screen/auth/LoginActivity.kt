package ua.nure.dzhafarov.dineit.screen.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.screen.main.MainActivity
import ua.nure.dzhafarov.dineit.data.model.TokenData
import ua.nure.dzhafarov.dineit.data.security.AuthManager
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.api.ApiManager

class LoginActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textWatcher = OnTextChanged()
        tilUserName.editText?.addTextChangedListener(textWatcher)
        tilPassword.editText?.addTextChangedListener(textWatcher)

        fillToolbar(showBackButton = true)

        btnLogin.setOnClickListener {
            login(tilUserName.editText?.text.toString().trim(),
                    tilPassword.editText?.text.toString().trim()
            )
        }
    }

    private fun validate() {
        btnLogin.isEnabled = (
                !tilUserName.editText?.text?.isEmpty()!! &&
                        !tilPassword.editText?.text?.isEmpty()!!
                )
    }

    private fun fillToolbar(@StringRes string: Int = R.string.log_in, showBackButton: Boolean = false) {
        toolbar.title = getString(string)
        if (showBackButton) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener({ onBackPressed() })
        }
    }

    private fun login(username: String, password: String) {
        clpLogin?.let { it.visibility = VISIBLE }
        
        ApiManager.login(username, password, object : Callback<TokenData> {
            override fun success(obj: TokenData) {
                AuthManager.saveTokenData(obj)
                AuthManager.saveUsername(username)
                startActivity(MainActivity.getLauncherIntent(this@LoginActivity))
                clpLogin?.let { it.visibility = GONE }
                this@LoginActivity.finish()
            }

            override fun error(obj: String) {
                Toast.makeText(this@LoginActivity, obj, Toast.LENGTH_SHORT).show()
                clpLogin?.let { it.visibility = GONE }
            }
        })
    }

    inner class OnTextChanged : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validate()
        }
    }

    companion object {
        fun getLauncherIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}