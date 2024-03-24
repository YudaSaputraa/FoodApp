package com.kom.foodapp.data.datasource

import com.kom.foodapp.data.model.Profile

interface ProfileDataSource {
    fun getProfileData(): List<Profile>
}

class ProfileDataSourceImpl() : ProfileDataSource {
    override fun getProfileData(): List<Profile> {
        return mutableListOf(
            Profile(
                name = "Yuda Saputraa",
                email = "yudasaputra082@gmail.com",
                phoneNumber = "089767653",
                image = "https://static.wikia.nocookie.net/cartoons/images/e/ed/Profile_-_SpongeBob_SquarePants.png/revision/latest?cb=20230305115632",
                password = "1234",
                locationAddress = "https://maps.app.goo.gl/QLChXJcYJUuQWPQh8"
            )
        )
    }

}