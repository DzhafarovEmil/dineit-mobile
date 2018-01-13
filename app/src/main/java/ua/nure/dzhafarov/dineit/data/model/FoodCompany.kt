package ua.nure.dzhafarov.dineit.data.model

data class FoodCompany(
        val id: Int,
        var name: String,
        var phoneNumber: String,
        var address: Address,
        var imageURL: String,
        var socialNetworkRefs: Map<String, String> = linkedMapOf()
)