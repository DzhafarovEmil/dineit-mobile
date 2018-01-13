package ua.nure.dzhafarov.dineit.screen.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.data.model.Customer
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.api.ApiManager

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        fillToolbar(showBackButton = true)
        
        val textWatcher = OnTextChanged()
        tilFirstName.editText?.addTextChangedListener(textWatcher)
        tilLastName.editText?.addTextChangedListener(textWatcher)
        tilEmail.editText?.addTextChangedListener(textWatcher)
        tilUserName.editText?.addTextChangedListener(textWatcher)
        tilPassword.editText?.addTextChangedListener(textWatcher)
        
        btnRegister.setOnClickListener { 
            val customer = Customer(
                    0,
                    tilFirstName.editText?.text.toString().trim(),
                    tilLastName.editText?.text.toString().trim(),
                    tilEmail.editText?.text.toString(),
                    tilPhoneNumber.editText?.text.toString(),
                    tilUserName.editText?.text.toString(),
                    tilPassword.editText?.text.toString()
                    
            )
         
            register(customer)
        }
    }

    private fun validate() {
        btnRegister.isEnabled = (
                !tilFirstName.editText?.text?.isEmpty()!! &&
                        !tilLastName.editText?.text?.isEmpty()!! && 
                        !tilEmail.editText?.text?.isEmpty()!! && 
                        !tilUserName.editText?.text?.isEmpty()!! && 
                        !tilPassword.editText?.text?.isEmpty()!!
                )
    }

    private fun fillToolbar(@StringRes string: Int = R.string.registration, showBackButton: Boolean = false) {
        toolbar.title = getString(string)
        if (showBackButton) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener({ onBackPressed() })
        }
    }

    private fun register(customer: Customer) {
        ApiManager.register(customer, object : Callback<Customer> {
            override fun success(obj: Customer) {
                startActivity(LoginActivity.getLauncherIntent(this@RegisterActivity))
            }

            override fun error(obj: String) {
                Toast.makeText(this@RegisterActivity, obj, Toast.LENGTH_SHORT).show()
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
        fun getLauncherIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}