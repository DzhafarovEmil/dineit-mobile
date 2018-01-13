package ua.nure.dzhafarov.dineit.screen.main.fragments.orders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_order_foods.*
import kotlinx.android.synthetic.main.toolbar_main.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.data.model.Food
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import ua.nure.dzhafarov.dineit.screen.food.FoodDetailsActivity
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.FoodsAdapter
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick

class FoodsOrderActivity : AppCompatActivity() {

    private var foods: MutableList<Food> = mutableListOf()
    private var adapter: FoodsAdapter? = null
    private var orderId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_foods)
        orderId = intent.getIntExtra(ORDER_ID, 0)
        
        fillToolbar()

        refresher.setOnRefreshListener { loadFoods() }
        refresher.setColorSchemeColors(getColor(R.color.colorPrimary))
        
        rvFoods.layoutManager = LinearLayoutManager(this)
        adapter = FoodsAdapter(foods, this, object : OnUserClick<Food> {
            override fun onClick(obj: Food) {
                startActivity(FoodDetailsActivity.getLauncherIntent(this@FoodsOrderActivity, obj.id))
            }
        })
        rvFoods.adapter = adapter
        
        if (orderId != 0) {
            loadFoods()
        }
    }

    private fun loadFoods() {
        refresher?.let { it.isRefreshing = true }

        ApiManager.allFoodsInOrder(accessToken(), orderId, object : Callback<List<Food>> {
            override fun success(obj: List<Food>) {
                foods.clear()
                foods.addAll(obj)
                adapter?.notifyDataSetChanged()
                refresher?.let { it.isRefreshing = false }
                checkForNoElements()
            }

            override fun error(obj: String) {
                Toast.makeText(this@FoodsOrderActivity, obj, Toast.LENGTH_SHORT).show()
                refresher?.let { it.isRefreshing = false }
            }
        })
    }

    private fun checkForNoElements() {
        if (foods.isEmpty()) {
            tvNothingToShow?.visibility = View.VISIBLE
            rvFoods?.visibility = View.GONE
        } else {
            tvNothingToShow?.visibility = View.GONE
            rvFoods?.visibility = View.VISIBLE
        }
    }

    private fun fillToolbar() {
        toolbar.title = getString(R.string.order_number, orderId)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
        toolbar.setNavigationOnClickListener({ onBackPressed() })
    }

    companion object {
        const val ORDER_ID = "order-id"

        fun getLauncherIntent(context: Context, orderId: Int): Intent {
            val intent = Intent(context, FoodsOrderActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent
        }
    }
}
