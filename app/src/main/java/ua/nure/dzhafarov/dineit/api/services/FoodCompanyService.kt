package ua.nure.dzhafarov.dineit.api.services

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.nure.dzhafarov.dineit.data.model.Food
import ua.nure.dzhafarov.dineit.data.model.FoodCompany

interface FoodCompanyService {
    
    @GET("food-company")
    fun foodCompanies(@Query("access_token") accessToken: String): Call<List<FoodCompany>>

    @GET("food-company/")
    fun foodCompanyById(@Query("id") foodCompanyId: Int,
                        @Query("access_token") accessToken: String): Call<FoodCompany>
    
    companion object Factory {
        fun create() : FoodCompanyService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://dineit.herokuapp.com/api/")
                    .build()

            return retrofit.create(FoodCompanyService::class.java)
        }
    }
}