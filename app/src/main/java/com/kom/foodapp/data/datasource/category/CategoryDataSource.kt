package com.kom.foodapp.data.datasource.category

import com.kom.foodapp.data.model.Category

interface CategoryDataSource {
    fun getCategoryData(): List<Category>
}

class CategoryDataSourceImpl() : CategoryDataSource {
    override fun getCategoryData(): List<Category> {
        return mutableListOf(
            Category(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/category_img/img_rice.webp",
                name = "Nasi"
            ),
            Category(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/category_img/img_noodle.webp",
                name = "Mie"
            ),
            Category(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/category_img/img_snack.webp",
                name = "Snack"
            ),
            Category(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/category_img/img_drink.webp",
                name = "Minuman"
            ),
            Category(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/category_img/img_vegetables.webp",
                name = "Sayuran"
            ),
        )
    }

}