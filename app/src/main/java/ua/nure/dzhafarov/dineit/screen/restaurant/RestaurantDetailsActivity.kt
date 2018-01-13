package ua.nure.dzhafarov.dineit.screen.restaurant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.squareup.picasso.Picasso
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_food_company_details.*
import ua.nure.dzhafarov.dineit.data.model.FoodCompany
import ua.nure.dzhafarov.dineit.data.model.SocialNetwork
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.FoodsActivity
import ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods.OnUserClick
import ua.nure.dzhafarov.dineit.screen.restaurant.social.SocialNetworkAdapter

class RestaurantDetailsActivity : AppCompatActivity() {
    
    private lateinit var foodCompany: FoodCompany
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_company_details)

        val foodCompanyId = intent.getIntExtra(FOOD_COMPANY_ID, 0)
        
        fabFoods.setOnClickListener { 
            startActivity(FoodsActivity.getLauncherIntent(this, foodCompanyId)) 
        }
        
        appBar.addOnOffsetChangedListener { _, verticalOffset ->
            if (collapsingToolbar.height + verticalOffset < 
                    2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_primary_24dp) 
            } else {
                toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_white_24dp)
            }
        }
        
        if (foodCompanyId != 0) {
            loadFoodCompany(foodCompanyId)   
        } else {
            finish()
        }
    }

    private fun loadFoodCompany(id: Int) {
        ApiManager.loadFoodCompanyById(accessToken(), id, object : Callback<FoodCompany> {
            override fun success(obj: FoodCompany) {
                this@RestaurantDetailsActivity.foodCompany = obj
                fillData()
            }

            override fun error(obj: String) {
                Toast.makeText(
                        this@RestaurantDetailsActivity, 
                        obj, 
                        Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    private fun fillData() {
        fillToolbar()

        tvAddressDetails.text = getString(
                R.string.address_details,
                foodCompany.address.country,
                foodCompany.address.city,
                foodCompany.address.street,
                foodCompany.address.building)
        tvPhoneNumber.text = foodCompany.phoneNumber
        tvPhoneNumber.setOnClickListener { 
            openInKeyBoard(foodCompany.phoneNumber) 
        }
        
        val links = mutableListOf<SocialNetwork>()
        rvSocialNetworks.layoutManager = LinearLayoutManager(this)
        foodCompany.socialNetworkRefs.forEach { 
            links.add(
                    SocialNetwork(it.key, it.value)
            ) 
        }
        
        rvSocialNetworks.adapter = SocialNetworkAdapter(links, object: OnUserClick<SocialNetwork> {
            override fun onClick(obj: SocialNetwork) {
                openInBrowser(obj.url)
            }
        })
        
        Picasso
                .with(this)
                .load(foodCompany.imageURL)
                .fit()
                .centerCrop()
                .into(ivFoodCompanyAvatar)
    }
    
    private fun fillToolbar() {
        toolbar.title = foodCompany.name
        toolbar.setNavigationOnClickListener({ onBackPressed() })
    }
    
    private fun openInBrowser(url: String) {
        var copyUrl = url
        
        if (copyUrl != "") {

            if (!copyUrl.contains("http://")) {
                copyUrl = "http://" + url
            }

            val web = Uri.parse(copyUrl)
            val intent = Intent(Intent.ACTION_VIEW, web)
            startActivity(intent)
        }
    }
    
    private fun openInKeyBoard(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }
    
    companion object {
        const val FOOD_COMPANY_ID: String = "food-company-id"
        
        fun getLauncherIntent(context: Context, foodCompanyId: Int): Intent {
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra(FOOD_COMPANY_ID, foodCompanyId)
            return intent
        }
    }
}