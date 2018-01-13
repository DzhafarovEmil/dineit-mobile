package ua.nure.dzhafarov.dineit.api

interface Callback<T> {
    
    fun success(obj: T)
    
    fun error(obj: String)
}