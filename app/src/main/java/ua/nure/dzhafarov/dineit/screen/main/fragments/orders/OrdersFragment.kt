package ua.nure.dzhafarov.dineit.screen.main.fragments.orders

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.toolbar_main.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.data.model.Order
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import ua.nure.dzhafarov.dineit.screen.main.BaseMenuFragment
import ua.nure.dzhafarov.dineit.screen.main.fragments.orders.details.OrderDetailsActivity
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick

class OrdersFragment : BaseMenuFragment() {

    private var orders: MutableList<Order> = mutableListOf()
    private var adapter: OrderAdapter? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ibExit.visibility = GONE
        
        refresher.setOnRefreshListener { loadOrders() }
        refresher.setColorSchemeColors(activity.getColor(R.color.colorPrimary))

        rvOrders.layoutManager = LinearLayoutManager(context)
        adapter = OrderAdapter(orders, context, object: OnUserClick<Order> {
            override fun onClick(obj: Order) {
                startActivity(
                        OrderDetailsActivity.getLauncherIntent(this@OrdersFragment.context, obj.id)
                )
            }
        })
        rvOrders.adapter = adapter

        loadOrders()
    }

    private fun loadOrders() {
        refresher?.let { it.isRefreshing = true }

        ApiManager.allUserOrders(accessToken(), object : Callback<List<Order>> {
            override fun success(obj: List<Order>) {
                orders.clear()
                orders.addAll(obj)
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

    override fun getLayoutResId() = R.layout.fragment_orders

    private fun checkForNoElements() {
        if (orders.isEmpty()) {
            tvNothingToShow?.visibility = View.VISIBLE
            rvOrders?.visibility = View.GONE
        } else {
            tvNothingToShow?.visibility = View.GONE
            rvOrders?.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = OrdersFragment()
    }
}