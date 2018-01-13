package ua.nure.dzhafarov.dineit.screen.main.fragments.orders.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.toolbar.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import ua.nure.dzhafarov.dineit.data.model.Order
import ua.nure.dzhafarov.dineit.screen.main.fragments.orders.FoodsOrderActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {
    
    private lateinit var order: Order
    private val timeFormatter: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        fillToolbar()
        
        val orderId = intent.getIntExtra(ORDER_ID, 0)
        
        btnShowFoods.setOnClickListener { 
            startActivity(FoodsOrderActivity.getLauncherIntent(this, orderId))
        }
        
        if (orderId != 0) {
            loadOrder(orderId)   
        } else {
            finish()
        }
    }
    
    private fun loadOrder(id: Int) {
        ApiManager.loadOrderById(accessToken(), id, object : Callback<Order> {
            override fun success(obj: Order) {
                this@OrderDetailsActivity.order = obj
                fillData()
            }

            override fun error(obj: String) {
                Toast.makeText(this@OrderDetailsActivity, obj, Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun fillData() {
        tvOrderNumber.text = order.id.toString()
        tvOrderStatus.text = order.status
        tvOrderedTime.text = timeFormatter.format(order.orderedTime)
        tvOrderedDate.text = dateFormatter.format(order.orderedTime)
        tvTotalPrice.text = getString(R.string.uan_price, order.price)

        val path = Environment.getExternalStorageDirectory().toString()
        val file = File(path, "QRCode${order.qrCode?.id}.png")

        ivQRCode.setOnClickListener {
            showInGallery(file.absolutePath)
        }
        
        Picasso
                .with(this)
                .load(file)
                .fit()
                .centerCrop()
                .into(ivQRCode)
    }

    private fun showInGallery(path: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse("file://$path"), "image/*")
        startActivity(intent)
    }
    
    private fun fillToolbar() {
        toolbar.title = getString(R.string.order_details)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
        toolbar.setNavigationOnClickListener({ onBackPressed() })
    }
    
    companion object {
        const val ORDER_ID: String = "order-id"
        
        fun getLauncherIntent(context: Context, orderId: Int): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent
        }
    }
}