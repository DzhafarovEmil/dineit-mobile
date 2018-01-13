package ua.nure.dzhafarov.dineit.data.model

data class Order(
        var id: Int,
        var customer: Customer?,
        var foodCompany: FoodCompany?,
        var price: Double,
        var orderedTime: Long,
        var status: String,
        var foods: MutableList<Food>
) {
    var qrCode: QRCode? = null
}