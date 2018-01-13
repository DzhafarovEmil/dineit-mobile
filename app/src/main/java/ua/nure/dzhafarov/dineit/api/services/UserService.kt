package ua.nure.dzhafarov.dineit.api.services

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ua.nure.dzhafarov.dineit.data.model.Customer
import ua.nure.dzhafarov.dineit.data.model.TokenData

interface UserService {

    @GET("oauth/token")
    fun login(@Query("username") username: String,
              @Query("password") password: String,
              @Query("grant_type") grantType: String): Call<TokenData>

    @POST("register-customer/")
    fun register(@Body customer: Customer): Call<Int>

    @PUT("api/customer/{id}")
    fun updateCustomer(@Path("id") id: Int,
                       @Query("access_token") accessToken: String,
                       @Body customer: Customer): Call<Customer>

    @GET("api/customer/{username}")
    fun findByUsername(@Path("username") username: String,
                       @Query("access_token") accessToken: String): Call<Customer>

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

        fun create(): UserService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://dineit.herokuapp.com/")
                    .client(httpClient).build()

            return retrofit.create(UserService::class.java)
        }
    }
}