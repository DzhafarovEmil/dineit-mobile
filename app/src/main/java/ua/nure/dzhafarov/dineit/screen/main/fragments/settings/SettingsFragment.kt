package ua.nure.dzhafarov.dineit.screen.main.fragments.settings

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar_main.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.data.model.Customer
import ua.nure.dzhafarov.dineit.data.security.AuthManager
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import ua.nure.dzhafarov.dineit.data.security.AuthManager.username
import ua.nure.dzhafarov.dineit.screen.main.BaseMenuFragment
import ua.nure.dzhafarov.dineit.screen.start.StartActivity

class SettingsFragment : BaseMenuFragment() {

    private lateinit var currentCustomer: Customer

    override fun getLayoutResId() = R.layout.fragment_settings

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ibExit.visibility = VISIBLE

        ibExit.setOnClickListener {
            AuthManager.clearTokenStorage()
            startActivity(StartActivity.getLauncherIntent(context))
            activity.finish()
        }

        btnChange.setOnClickListener {
            activateInputs(true)
        }

        btnApply.setOnClickListener {
            updateCustomer()
        }

        loadCustomerInfo()
    }

    private fun loadCustomerInfo() {
        ApiManager.loadCustomerByUsername(accessToken(), username(), object : Callback<Customer> {
            override fun success(obj: Customer) {
                initializeInputsWithCustomer(obj)
                currentCustomer = obj
                activateInputs(false)
            }

            override fun error(obj: String) {
                Toast.makeText(context, obj, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCustomer() {
        currentCustomer.firstName = etFirstName.text.toString()
        currentCustomer.lastName = etLastName.text.toString()
        currentCustomer.email = etEmail.text.toString()
        currentCustomer.phoneNumber = etPhoneNumber.text.toString()
        currentCustomer.password = etPassword.text.toString()

        ApiManager.updateCustomer(accessToken(), currentCustomer, object : Callback<Customer> {
            override fun success(obj: Customer) {
                activateInputs(false)
                Toast.makeText(
                        this@SettingsFragment.context,
                        "Your profile has been successfully changed!",
                        Toast.LENGTH_LONG
                ).show()
            }

            override fun error(obj: String) {
                Toast.makeText(context, obj, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeInputsWithCustomer(customer: Customer) {
        etFirstName.setText(customer.firstName)
        etLastName.setText(customer.lastName)
        etEmail.setText(customer.email)
        etPhoneNumber.setText(customer.phoneNumber)
        etPassword.setText(customer.password)
    }

    private fun activateInputs(isEnabled: Boolean) {
        etFirstName.isEnabled = isEnabled
        etLastName.isEnabled = isEnabled
        etEmail.isEnabled = isEnabled
        etPhoneNumber.isEnabled = isEnabled
        etPassword.isEnabled = isEnabled
        btnApply.isEnabled = isEnabled
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}