package ua.nure.dzhafarov.dineit.data.model

data class Food(
        val id: Int,
        var name: String,
        var type: String,
        var price: Double,
        var imageURL: String,
        var description: String) {
    
    var isSelected: Boolean = false 
}