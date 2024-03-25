package com.kom.foodapp.data.datasource.menu

import com.example.foodapp.model.Menu

interface MenuDataSource {
    fun getMenuData(): List<Menu>
}

class MenuDataSourceImpl() : MenuDataSource {
    override fun getMenuData(): List<Menu> {
        return mutableListOf(
            Menu(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/menu_img/img_frenchfries_menu.webp",
                name = "Kentang Goreng",
                price = 15000.0,
                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77",
                locationAddress = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                desc = "Kentang goreng yang renyah di luar dan lembut di dalam.",

                ),
            Menu(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/menu_img/img_friedrice_menu.webp",
                name = "Nasi Goreng",
                price = 30000.0,
                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77",
                locationAddress = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                desc = "Nasi goreng spesial dengan bumbu yang khas dan cita rasa yang lezat."
            ),
            Menu(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/menu_img/img_noodle_menu.webp",
                name = "Mie Kuah Spesial",
                price = 25000.0,
                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77",
                locationAddress = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                desc = "Mie kuah dengan campuran bahan-bahan pilihan dan kuah yang gurih."
            ),
            Menu(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/menu_img/img_lime_mojito_menu.webp",
                name = "Lime Mojito",
                price = 20000.0,
                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77",
                locationAddress = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                desc = "Minuman segar dengan perpaduan jeruk nipis, mint, dan soda."
            ),
            Menu(
                image = "https://raw.githubusercontent.com/YudaSaputraa/FoodApp-assets/main/menu_img/img_salad_menu.webp",
                name = "Salad",
                price = 23000.0,
                locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77",
                locationAddress = "Jl. BSD Green Office Park, Jl. BSD Grand Boulevard, Sampora, BSD, Kabupaten Tanggerang, Banten 15345",
                desc = "Salad segar dengan berbagai macam sayuran dan dressing yang lezat."
            ),
        )
    }

}