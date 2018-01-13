package ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_restaurant.view.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.data.model.FoodCompany
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick

class FoodCompanyAdapter(private val foodCompanies: MutableList<FoodCompany>,
                         private val context: Context,
                         private val listener: OnUserClick<FoodCompany>
): RecyclerView.Adapter<FoodCompanyHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodCompanyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_restaurant, parent, false)

        return FoodCompanyHolder(view)
    }

    override fun onBindViewHolder(holder: FoodCompanyHolder, position: Int) {
        val restaurant = foodCompanies[position]
        
        holder.itemView.setOnClickListener { listener.onClick(restaurant) }
        holder.itemView.tvRestaurantName.text = restaurant.name
        holder.itemView.tvRestaurantPhoneNumber.text = restaurant.phoneNumber
        
        Picasso
                .with(context)
                .load(restaurant.imageURL)
                .fit()
                .centerCrop()
                .into(holder.itemView.ivRestaurantAvatar)

    }

    override fun getItemCount(): Int {
        return foodCompanies.size
    }
}