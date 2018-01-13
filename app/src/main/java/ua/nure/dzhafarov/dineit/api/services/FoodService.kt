package ua.nure.dzhafarov.dineit.api.services

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ua.nure.dzhafarov.dineit.data.model.Food

interface FoodService {
    @GET("food")
    fun foodsInFoodCompany(@Query("access_token") accessToken: String,
                           @Query("food_company_id") foodCompanyId: Int) : Call<List<Food>>

    @GET("food/{id}")
    fun foodById(@Path("id") foodId: Int, 
                 @Query("access_token") accessToken: String): Call<Food>
    
    companion object Factory {
        private val basic: String = "Basic " + String(
                Base64.encode(("dine-it-client" + ":" + "dine-it-client-pass").toByteArray(), Base64.DEFAULT)
        ).trim()

        private val httpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original?.newBuilder()?.header("Authorization", basic)
            requestBuilder?.header("Accept", "application/json")
            requestBuilder?.header("Content-Type", "application/x-www-form-urlencoded")
            requestBuilder?.method(original.method(), original.body())
            val request = requestBuilder?.build()
            chain.proceed(request!!)
        }.build()
        
        fun create() : FoodService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://dineit.herokuapp.com/api/")
                    .client(httpClient)
                    .build()

            return retrofit.create(FoodService::class.java)
        }
    }
}