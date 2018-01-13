package ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_food.view.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.data.model.Food

class FoodsAdapter(private val foods: MutableList<Food>,
                   private val context: Context,
                   private val listener: OnUserClick<Food>
): RecyclerView.Adapter<FoodHolder>() {
    
    constructor(foods: MutableList<Food>,
                context: Context,
                listener: OnUserClick<Food>,
                foodChoose: OnFoodChoose) : this(foods, context, listener) {
        this.foodChoose = foodChoose
    }
    
    var select: Boolean = false
    private lateinit var foodChoose: OnFoodChoose
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_food, parent, false)

        return FoodHolder(view)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val food = foods[position]
        
        holder.itemView.setOnClickListener { listener.onClick(food) }
        
        holder.itemView.cbOrder.setOnCheckedChangeListener {_, isChecked -> 
            if (isChecked) {
                foodChoose.onChoose(food)
            } else {
                foodChoose.onUnChoose(food)
            }
        }
        
        if (select) {
            holder.itemView.cbOrder.visibility = VISIBLE
            holder.itemView.cbOrder.isChecked = food.isSelected
        } else {
            holder.itemView.cbOrder.isChecked = false
            holder.itemView.cbOrder.visibility = GONE
        }
        
        holder.itemView.tvFoodName.text = food.name
        holder.itemView.tvFoodType.text = food.type
        holder.itemView.tvFoodPrice.text = context.getString(R.string.uan_price, food.price)

        Picasso
                .with(context)
                .load(food.imageURL)
                .fit()
                .centerCrop()
                .into(holder.itemView.ivFoodAvatar)
    }

    override fun getItemCount(): Int {
        return foods.size
    }
}