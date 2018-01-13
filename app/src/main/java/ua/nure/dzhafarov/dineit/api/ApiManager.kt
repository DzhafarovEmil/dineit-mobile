package ua.nure.dzhafarov.dineit.api

import android.util.Log
import retrofit2.Call
import ua.nure.dzhafarov.dineit.api.services.UserService
import ua.nure.dzhafarov.dineit.api.services.OrderService
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import ua.nure.dzhafarov.dineit.api.services.FoodCompanyService
import ua.nure.dzhafarov.dineit.api.services.FoodService
import ua.nure.dzhafarov.dineit.data.model.*

object ApiManager {
    
    fun allFoodCompanies(accessToken: String, callback: Callback<List<FoodCompany>>) {
        return FoodCompanyService.create()
                .foodCompanies(accessToken)
                .enqueue(
                        object : retrofit2.Callback<List<FoodCompany>> {
                    override fun onFailure(call: Call<List<FoodCompany>>?, t: Throwable?) {
                        callback.error(t?.message.toString())
                    }

                    override fun onResponse(call: Call<List<FoodCompany>>?, response: Response<List<FoodCompany>>?) {
                        if (response?.errorBody() != null) {
                            if (response.code() == 400) {
                                val message = errorDescription(response)
                                callback.error(message)
                            }
                        } else {
                            val foodCompanies = response?.body()!!
                            callback.success(foodCompanies)
                        }
                    }
                })
    }

    fun allUserOrders(accessToken: String, callback: Callback<List<Order>>) {
        return OrderService.create()
                .orders(accessToken)
                .enqueue(
                        object : retrofit2.Callback<List<Order>> {
                            override fun onFailure(call: Call<List<Order>>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<List<Order>>?, response: Response<List<Order>>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val userOrders = response?.body()!!
                                    callback.success(userOrders)
                                }
                            }
                        })
    }
    
    fun allFoodsInFoodCompany(accessToken: String, foodCompanyId: Int, callback: Callback<List<Food>>) {
        return FoodService.create()
                .foodsInFoodCompany(accessToken, foodCompanyId)
                .enqueue(
                        object : retrofit2.Callback<List<Food>> {
                            override fun onFailure(call: Call<List<Food>>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<List<Food>>?, response: Response<List<Food>>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val foods = response?.body()!!
                                    callback.success(foods)
                                }
                            }
                        })
    }
    
    fun createOrder(accessToken: String, order: Order, foodCompanyId: Int, fridgeId: Int, callback: Callback<QRCode>) {
        return OrderService.create()
                .createNewOrder(accessToken, foodCompanyId, fridgeId,order)
                .enqueue(
                        object : retrofit2.Callback<QRCode> {
                            override fun onFailure(call: Call<QRCode>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<QRCode>?, response: Response<QRCode>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val responseOrder = response?.body()!!
                                    callback.success(responseOrder)
                                }
                            }
                        }
                )
    }
    
    fun login(username: String, password: String, callback: Callback<TokenData>) {
        return UserService.create()
                .login(username, password, "password")
                .enqueue(
                        object : retrofit2.Callback<TokenData> {
                            override fun onFailure(call: Call<TokenData>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<TokenData>?, response: Response<TokenData>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)  
                                    }
                                } else {
                                    val tokenData = response?.body()!!
                                    callback.success(tokenData)
                                }
                            }
                        }
                )
    }

    fun register(customer: Customer, callback: Callback<Customer>) {
        return UserService.create()
                .register(customer)
                .enqueue(
                        object : retrofit2.Callback<Int> {
                            override fun onFailure(call: Call<Int>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<Int>?, response: Response<Int>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val id = response?.body()!!
                                    customer.id = id
                                    callback.success(customer)
                                }
                            }
                        }
                )
    }

    fun allFoodsInOrder(accessToken: String, orderId: Int, callback: Callback<List<Food>>) {
        return OrderService.create()
                .foodsInOrder(orderId, accessToken)
                .enqueue(
                        object : retrofit2.Callback<List<Food>> {
                            override fun onFailure(call: Call<List<Food>>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<List<Food>>?, response: Response<List<Food>>?) {
                                if (response?.errorBody() != null) {
                                    val message = errorDescription(response)
                                    callback.error(message)
                                } else {
                                    val foods = response?.body()!!
                                    callback.success(foods)
                                }
                            }
                        })
    }

    fun loadFoodById(accessToken: String, foodId: Int, callback: Callback<Food>) {
        return FoodService.create()
                .foodById(foodId, accessToken)
                .enqueue(
                        object : retrofit2.Callback<Food> {
                            override fun onFailure(call: Call<Food>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<Food>?, response: Response<Food>?) {
                                if (response?.errorBody() != null) {
                                    val message = errorDescription(response)
                                    callback.error(message)
                                } else {
                                    val food = response?.body()!!
                                    callback.success(food)
                                }
                            }
                        })
    }

    fun loadFoodCompanyById(accessToken: String, foodCompanyId: Int, callback: Callback<FoodCompany>) {
        return FoodCompanyService.create()
                .foodCompanyById(foodCompanyId, accessToken)
                .enqueue(
                        object : retrofit2.Callback<FoodCompany> {
                            override fun onFailure(call: Call<FoodCompany>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<FoodCompany>?, response: Response<FoodCompany>?) {
                               
                                if (response?.errorBody() != null) {
                                    val message = errorDescription(response)
                                    callback.error(message)
                                } else {
                                    val foodCompany = response?.body()!!
                                    callback.success(foodCompany)
                                }
                            }
                        })
    }
    
    fun loadCustomerByUsername(accessToken: String, username: String, callback: Callback<Customer>) {
        return UserService.create()
                .findByUsername(username, accessToken)
                .enqueue(
                        object : retrofit2.Callback<Customer> {
                            override fun onFailure(call: Call<Customer>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<Customer>?, response: Response<Customer>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val customer = response?.body()!!
                                    callback.success(customer)
                                }
                            }
                        }
                )
    }

    fun updateCustomer(accessToken: String, customer: Customer, callback: Callback<Customer>) {
        return UserService.create()
                .updateCustomer(customer.id, accessToken, customer)
                .enqueue(
                        object : retrofit2.Callback<Customer> {
                            override fun onFailure(call: Call<Customer>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<Customer>?, response: Response<Customer>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val newCustomer = response?.body()!!
                                    callback.success(newCustomer)
                                }
                            }
                        }
                )
    }

    fun loadOrderById(accessToken: String, id: Int, callback: Callback<Order>) {
        return OrderService.create()
                .findById(id, accessToken)
                .enqueue(
                        object : retrofit2.Callback<Order> {
                            override fun onFailure(call: Call<Order>?, t: Throwable?) {
                                callback.error(t?.message.toString())
                            }

                            override fun onResponse(call: Call<Order>?, response: Response<Order>?) {
                                if (response?.errorBody() != null) {
                                    if (response.code() == 400) {
                                        val message = errorDescription(response)
                                        callback.error(message)
                                    }
                                } else {
                                    val responseOrder = response?.body()!!
                                    callback.success(responseOrder)
                                }
                            }
                        }
                )
    }
    
    private fun <T> errorDescription(response: Response<T>?): String {
        val json: JsonObject = Gson().fromJson(response?.errorBody()?.string(), JsonObject::class.java)
        return json.get("error_description").asString
    }
}