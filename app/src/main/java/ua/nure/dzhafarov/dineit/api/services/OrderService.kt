package ua.nure.dzhafarov.dineit.api.services

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ua.nure.dzhafarov.dineit.data.model.Food
import ua.nure.dzhafarov.dineit.data.model.Order
import ua.nure.dzhafarov.dineit.data.model.QRCode

interface OrderService {
    @GET("order")
    fun orders(@Query("access_token") accessToken: String,
               @Query("sort") sortType: Int = 1,
               @Query("user") userType: String = "customer"): Call<List<Order>>

    @POST("create-order/")
    fun createNewOrder(@Query("access_token") accessToken: String,
                       @Query("food_company_id") foodCompanyId: Int,
                       @Query("fridge_id") fridgeId: Int,
                       @Body order: Order): Call<QRCode>

    @GET("order/{order_id}/foods")
    fun foodsInOrder(@Path("order_id") orderId: Int,
                     @Query("access_token") accessToken: String): Call<List<Food>>

    @GET("order/{id}")
    fun findById(@Path("id") id: Int,
                 @Query("access_token") accessToken: String): Call<Order>
    
    companion object Factory {
        fun create(): OrderService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://dineit.herokuapp.com/api/")
                    .build()

            return retrofit.create(OrderService::class.java)
        }
    }
}