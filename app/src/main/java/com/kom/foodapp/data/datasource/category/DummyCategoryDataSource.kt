package com.kom.foodapp.data.datasource.category

import com.kom.foodapp.data.model.Category

class DummyCategoryDataSource : CategoryDataSource {
    override fun getCategoryData(): List<Category> {
        return mutableListOf(
            Category(
                image = "https://github.com/YudaSaputraa/FoodApp-assets/blob/main/category_img/img_rice.webp?raw=true",
                name = "Nasi"
            ),
            Category(
                image = "https://github.com/YudaSaputraa/FoodApp-assets/blob/main/category_img/img_noodle.webp?raw=true",
                name = "Mie"
            ),
            Category(
                image = "https://github.com/YudaSaputraa/FoodApp-assets/blob/main/category_img/img_snack.webp?raw=true",
                name = "Snack"
            ),
            Category(
                image = "https://github.com/YudaSaputraa/FoodApp-assets/blob/main/category_img/img_drink.webp?raw=true",
                name = "Minuman"
            ),
            Category(
                image = "https://github.com/YudaSaputraa/FoodApp-assets/blob/main/category_img/img_vegetables.webp?raw=true",
                name = "Sayuran"
            ),
        )
    }
}