package ua.nure.dzhafarov.dineit.screen.main

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import ua.nure.dzhafarov.dineit.screen.main.fragments.orders.OrdersFragment
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.FoodCompaniesFragment
import ua.nure.dzhafarov.dineit.screen.main.fragments.settings.SettingsFragment

class MainMenuAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragments = listOf(
            FoodCompaniesFragment.newInstance(),
            OrdersFragment.newInstance(),
            SettingsFragment.newInstance()
    )
    
    private var currentFragment: BaseMenuFragment? = null
    
    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any?) {
        if (getCurrentFragment() != `object`) {
            currentFragment = `object` as BaseMenuFragment
        }
        super.setPrimaryItem(container, position, `object`)
    }

    fun getCurrentFragment() = currentFragment
}