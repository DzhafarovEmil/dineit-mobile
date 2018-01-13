package ua.nure.dzhafarov.dineit.screen.main

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.toolbar.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.utils.inflate

abstract class BaseMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return container?.inflate(getLayoutResId())
    }
    
    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }
    
    abstract fun getLayoutResId(): Int
    
    fun refresh() {

    }

    fun willBeHidden() {

    }

    fun willBeDisplayed() {

    }
    
    private fun fillToolbar(@StringRes string: Int = R.string.app_name, showBackButton: Boolean = false) {
        toolbar.title = getString(string)
        if (showBackButton) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener({ activity.onBackPressed() })
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_main_24dp)
        }
    }
}