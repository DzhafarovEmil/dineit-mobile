package ua.nure.dzhafarov.dineit.screen.main

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import kotlinx.android.synthetic.main.activity_main.*
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.toolbar_main.*
import ua.nure.dzhafarov.dineit.R

class MainActivity : AppCompatActivity() {

    private var currentFragment: BaseMenuFragment? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
    }

    private fun initViews() {
        val tabColors = resources.getIntArray(R.array.tab_colors)
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_menu)
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors)
        bottomNavigation.inactiveColor = ContextCompat.getColor(this, R.color.inactive_bottom_menu)
        bottomNavigation.accentColor = ContextCompat.getColor(this, R.color.active_bottom_menu)
        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        
        val adapter = MainMenuAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 0

        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            if (currentFragment == null) {
                currentFragment = adapter.getCurrentFragment()
            }

            if (wasSelected) {
                currentFragment?.refresh()
                return@setOnTabSelectedListener true
            }

            if (currentFragment != null) {
                currentFragment!!.willBeHidden()
            }

            viewPager.setCurrentItem(position, false)
            currentFragment = adapter.getCurrentFragment()
            currentFragment?.willBeDisplayed()
            currentFragment?.refresh()

            return@setOnTabSelectedListener true
        }
        
        bottomNavigation.currentItem = 0
        currentFragment = adapter.getCurrentFragment()
    }

    companion object {
        fun getLauncherIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}