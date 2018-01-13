package ua.nure.dzhafarov.dineit.screen.food

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_food_details.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.data.model.Food
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import android.support.v4.view.ViewCompat

class FoodDetailsActivity : AppCompatActivity() {
    
    private lateinit var food: Food
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        appBar.addOnOffsetChangedListener { _, verticalOffset ->
            if (collapsingToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_primary_24dp) 
            } else {
                toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_white_24dp)
            }
        }

        val foodId = intent.getIntExtra(FOOD_ID, 0)
        
        if (foodId != 0) {
            loadFood(foodId)   
        } else {
            finish()
        }
    }

    private fun loadFood(id: Int) {
        ApiManager.loadFoodById(accessToken(), id, object : Callback<Food> {
            override fun success(obj: Food) {
                this@FoodDetailsActivity.food = obj
                fillFoodData()
            }

            override fun error(obj: String) {
                Toast.makeText(this@FoodDetailsActivity, obj, Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun fillFoodData() {
        fillToolbar()
        
        tvFoodType.text = food.type
        tvFoodPrice.text = getString(R.string.uan_price, food.price)
        tvFoodDescription.text = food.description
        
        Picasso
                .with(this)
                .load(food.imageURL)
                .fit()
                .centerCrop()
                .into(ivFoodAvatar)
    }
    
    private fun fillToolbar() {
        toolbar.title = food.name
        toolbar.setNavigationOnClickListener({ onBackPressed() })
    }
    
    companion object {
        const val FOOD_ID: String = "food-id"
        
        fun getLauncherIntent(context: Context, foodId: Int): Intent {
            val intent = Intent(context, FoodDetailsActivity::class.java)
            intent.putExtra(FOOD_ID, foodId)
            return intent
        }
    }
}