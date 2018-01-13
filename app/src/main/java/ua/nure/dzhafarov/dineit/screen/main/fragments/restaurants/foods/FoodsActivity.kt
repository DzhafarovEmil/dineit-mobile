package ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_foods.*
import kotlinx.android.synthetic.main.toolbar_main.*
import ua.nure.dzhafarov.dineit.R
import ua.nure.dzhafarov.dineit.api.ApiManager
import ua.nure.dzhafarov.dineit.api.Callback
import ua.nure.dzhafarov.dineit.data.model.Food
import ua.nure.dzhafarov.dineit.data.model.Order
import ua.nure.dzhafarov.dineit.data.model.OrderStatus
import ua.nure.dzhafarov.dineit.data.security.AuthManager
import ua.nure.dzhafarov.dineit.data.security.AuthManager.accessToken
import android.support.v7.widget.RecyclerView
import android.util.Base64
import ua.nure.dzhafarov.dineit.data.model.QRCode
import ua.nure.dzhafarov.dineit.screen.food.FoodDetailsActivity
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class FoodsActivity : AppCompatActivity() {

    private var foods: MutableList<Food> = mutableListOf()
    private var selectedFoods: MutableSet<Food> = mutableSetOf()
    private var adapter: FoodsAdapter? = null
    private var foodCompanyId: Int = 0

    private var progressDialog: ProgressDialog? = null

    private var selecting: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foods)
        fillToolbar(showBackButton = true)

        progressDialog = createProgressDialog()
        refresher.setOnRefreshListener { loadFoods() }
        refresher.setColorSchemeColors(getColor(R.color.colorPrimary))

        ibDoneOrder.setOnClickListener {
            showConfirmationDialog(selectedFoods.sumByDouble { it.price })
        }

        fabCreateOrder.setOnClickListener {
            if (selecting) {
                if (foods.isEmpty()) {
                    Toast.makeText(this, "There are not available foods!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                showCheckBoxes()
                fabCreateOrder.setImageDrawable(getDrawable(R.drawable.ic_close_white_24dp))
                selecting = false
            } else {
                clearCreatingOrder()
                fabCreateOrder.setImageDrawable(getDrawable(R.drawable.ic_create_white_24dp))
            }
        }

        rvFoods.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    fabCreateOrder.hide()
                } else {
                    fabCreateOrder.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        rvFoods.layoutManager = LinearLayoutManager(this)
        adapter = FoodsAdapter(foods, this, object : OnUserClick<Food> {
            override fun onClick(obj: Food) {
                startActivity(FoodDetailsActivity.getLauncherIntent(this@FoodsActivity, obj.id))
            }
        }, object : OnFoodChoose {
            override fun onUnChoose(food: Food) {
                selectedFoods.remove(food)
                food.isSelected = false
                checkSelectedFoods()
            }

            override fun onChoose(food: Food) {
                food.isSelected = true
                selectedFoods.add(food)
                checkSelectedFoods()
            }
        })
        rvFoods.adapter = adapter
        foodCompanyId = intent.getIntExtra(FOOD_COMPANY_ID, 0)
        if (foodCompanyId != 0) {
            loadFoods()
        }
    }

    private fun showCheckBoxes() {
        adapter?.select = true
        adapter?.notifyDataSetChanged()
    }

    private fun hideCheckBoxes() {
        adapter?.select = false
        
        selectedFoods.forEach { it.isSelected = false }
        
        adapter?.notifyDataSetChanged()
    }

    private fun loadFoods() {
        refresher?.let { it.isRefreshing = true }

        ApiManager.allFoodsInFoodCompany(AuthManager.accessToken(), foodCompanyId, object : Callback<List<Food>> {
            override fun success(obj: List<Food>) {
                foods.clear()
                foods.addAll(obj)
                adapter?.notifyDataSetChanged()
                refresher?.let { it.isRefreshing = false }
                checkForNoElements()
            }

            override fun error(obj: String) {
                Toast.makeText(this@FoodsActivity, obj, Toast.LENGTH_SHORT).show()
                refresher?.let { it.isRefreshing = false }
            }
        })
    }

    private fun showConfirmationDialog(price: Double) {
        val alert = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alert.setTitle("Confirmation")
        alert.setMessage("Price to pay: ${getString(R.string.uan_price, price)}")
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Pay", { _, _ ->
            val order = Order(
                    0,
                    null,
                    null,
                    price,
                    System.currentTimeMillis(),
                    OrderStatus.CREATED.name,
                    selectedFoods.toMutableList()
            )

            sendOrder(order)
        })
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", { _, _ -> clearCreatingOrder() })
        alert.show()
    }

    private fun sendOrder(order: Order) {
        progressDialog?.show()

        ApiManager.createOrder(accessToken(), order, foodCompanyId, 1, object : Callback<QRCode> {
            override fun success(obj: QRCode) {
                clearCreatingOrder()
                progressDialog?.hide()
                qrCode = obj
                askPermission()
            }

            override fun error(obj: String) {
                Toast.makeText(this@FoodsActivity, obj, Toast.LENGTH_SHORT).show()
                progressDialog?.hide()
            }
        })
    }

    private lateinit var qrCode: QRCode
    
    private fun askPermission() {
        val perms = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")

        val permsRequestCode = 200

        requestPermissions(perms, permsRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            200 -> {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    createImageFromBytes(qrCode)           
                }
            } 
        }
    }

    private fun createImageFromBytes(qrCode: QRCode) {
        val bytes = Base64.decode(qrCode.data, 0)
        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val path = Environment.getExternalStorageDirectory().toString()
        val file = File(path, "QRCode${qrCode.id}.png")
        val fOut = FileOutputStream(file)

        bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()

        MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, file.name)
        Toast.makeText(this, "Your order has been gotten!", Toast.LENGTH_LONG).show()
    }

    private fun clearCreatingOrder() {
        ibDoneOrder?.let { it.visibility = GONE }
        fabCreateOrder?.setImageDrawable(getDrawable(R.drawable.ic_create_white_24dp))
        selecting = true
        hideCheckBoxes()
        selectedFoods.clear()
    }

    private fun createProgressDialog(): ProgressDialog {
        val pd = ProgressDialog(this, R.style.DialogTheme)
        pd.setMessage("It will take some moments")
        pd.setCancelable(false)
        pd.isIndeterminate = true
        return pd
    }

    private fun checkSelectedFoods() {
        if (selectedFoods.isEmpty()) {
            ibDoneOrder.visibility = GONE
        } else {
            ibDoneOrder.visibility = VISIBLE
        }
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

    private fun fillToolbar(@StringRes string: Int = R.string.foods, showBackButton: Boolean = false) {
        toolbar.title = getString(string)
        if (showBackButton) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp)
            toolbar.setNavigationOnClickListener({ onBackPressed() })
        }
    }

    companion object {
        const val FOOD_COMPANY_ID = "food-company-id"

        fun getLauncherIntent(context: Context, foodCompanyId: Int): Intent {
            val intent = Intent(context, FoodsActivity::class.java)
            intent.putExtra(FOOD_COMPANY_ID, foodCompanyId)
            return intent
        }
    }
}