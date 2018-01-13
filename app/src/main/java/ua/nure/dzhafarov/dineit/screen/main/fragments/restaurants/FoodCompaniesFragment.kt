package ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_restaurants.*
import kotlinx.android.synthetic.main.toolbar_main.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.data.model.FoodCompany
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import ua.nure.dzhafarov.dineit.screen.main.BaseMenuFragment
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick
import ua.nure.dzhafarov.dineit.screen.restaurant.RestaurantDetailsActivity

class FoodCompaniesFragment : BaseMenuFragment() {
    
    private var foodCompanies: MutableList<FoodCompany> = mutableListOf()
    private var adapter: FoodCompanyAdapter? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ibExit.visibility = GONE
        
        refresher.setOnRefreshListener { loadFoodCompanies() }
        refresher.setColorSchemeColors(activity.getColor(R.color.colorPrimary))
        
        rvFoodCompanies.layoutManager = LinearLayoutManager(context)
        adapter = FoodCompanyAdapter(foodCompanies, context, object: OnUserClick<FoodCompany> {
            override fun onClick(obj: FoodCompany) {
                startActivity(RestaurantDetailsActivity.getLauncherIntent(context, obj.id))
            }
        } )
        rvFoodCompanies.adapter = adapter

        loadFoodCompanies()
    }

    private fun loadFoodCompanies() {
        refresher?.let { it.isRefreshing = true }
        
        ApiManager.allFoodCompanies(accessToken(), object : Callback<List<FoodCompany>> {
            override fun success(obj: List<FoodCompany>) {
                foodCompanies.clear()
                foodCompanies.addAll(obj)
                adapter?.notifyDataSetChanged()
                refresher?.let { it.isRefreshing = false }
                checkForNoElements()
            }

            override fun error(obj: String) {
                Toast.makeText(context, obj, Toast.LENGTH_SHORT).show()
                refresher?.let { it.isRefreshing = false }
            }
        })
    }
    
    override fun getLayoutResId() = R.layout.fragment_restaurants

    private fun checkForNoElements() {
        if (foodCompanies.isEmpty()) {
            tvNothingToShow?.visibility = VISIBLE
            rvFoodCompanies?.visibility = GONE
        } else {
            tvNothingToShow?.visibility = GONE
            rvFoodCompanies?.visibility = VISIBLE
        }
    }
    
    companion object {
        fun newInstance() = FoodCompaniesFragment()
    }
}