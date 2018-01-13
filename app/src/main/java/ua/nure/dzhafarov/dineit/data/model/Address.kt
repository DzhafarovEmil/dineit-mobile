package ua.nure.dzhafarov.dineit.data.model

data class Address(
        val id: Int,
        var country: String,
        var city: String,
        var district: String,
        var street: String,
        var building: String
)