package ua.nure.dzhafarov.dineit.data.model

data class Customer(
        var id: Int,
        var firstName: String,
        var lastName: String,
        var email: String,
        var phoneNumber: String,
        var username: String,
        var password: String
)