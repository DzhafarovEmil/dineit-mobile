package ua.nure.dzhafarov.dineit.screen.main.fragments.restaurants.foods

import ua.nure.dzhafarov.dineit.data.model.Food

interface OnFoodChoose {
    fun onChoose(food: Food)
    
    fun onUnChoose(food: Food)
}