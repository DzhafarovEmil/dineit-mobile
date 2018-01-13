package ua.nure.dzhafarov.dineit.screen.main.fragments.orders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_order.view.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.data.model.Order
import ua.nure.dzhafarov.dineit.data.model.OrderStatus
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(private val orders: MutableList<Order>,
                   private val context: Context,
                   private val listener: OnUserClick<Order>
) : RecyclerView.Adapter<OrderHolder>() {
    
    private val timeFormatter: SimpleDateFormat = 
            SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormatter: SimpleDateFormat =
            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_order, parent, false)

        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val order = orders[position]
        val date = Date(order.orderedTime)
        
        holder.itemView.setOnClickListener { listener.onClick(order) }
        holder.itemView.tvPrice.text = context.getString(R.string.uan_price, order.price)
        holder.itemView.tvStatus.text = OrderStatus.valueOf(order.status).toString()
        holder.itemView.tvOrderedTime.text = timeFormatter.format(date)
        holder.itemView.tvOrderedDate.text = dateFormatter.format(date)
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}