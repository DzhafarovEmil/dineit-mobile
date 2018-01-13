package ua.nure.dzhafarov.dineit.data.model

enum class OrderStatus {
    CREATED, DONE, DOING, RECEIVED;

    override fun toString(): String {
        return this.name[0].toUpperCase() + this.name.substring(1).toLowerCase()
    }
}