package com.kom.foodapp.di

import android.content.SharedPreferences
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.datasource.cart.CartDatabaseDataSource
import com.kom.foodapp.data.datasource.category.CategoryApiDataSource
import com.kom.foodapp.data.datasource.category.CategoryDataSource
import com.kom.foodapp.data.datasource.menu.MenuApiDataSource
import com.kom.foodapp.data.datasource.menu.MenuDataSource
import com.kom.foodapp.data.datasource.profile.ProfileDataSource
import com.kom.foodapp.data.datasource.profile.ProfileDataSourceImpl
import com.kom.foodapp.data.datasource.user.UserPrefDataSource
import com.kom.foodapp.data.datasource.user.UserPrefDataSourceImpl
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CartRepositoryImpl
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.CategoryRepositoryImpl
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.data.repository.MenuRepositoryImpl
import com.kom.foodapp.data.repository.UserPrefRepository
import com.kom.foodapp.data.repository.UserPrefRepositoryImpl
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseService
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import com.kom.foodapp.data.source.local.database.AppDatabase
import com.kom.foodapp.data.source.local.database.dao.CartDao
import com.kom.foodapp.data.source.local.pref.UserPreference
import com.kom.foodapp.data.source.local.pref.UserPreferenceImpl
import com.kom.foodapp.data.source.network.services.FoodAppApiService
import com.kom.foodapp.presentation.cart.CartViewModel
import com.kom.foodapp.presentation.checkout.CheckoutViewModel
import com.kom.foodapp.presentation.detailmenu.DetailMenuViewModel
import com.kom.foodapp.presentation.home.HomeViewModel
import com.kom.foodapp.presentation.login.LoginViewModel
import com.kom.foodapp.presentation.main.MainViewModel
import com.kom.foodapp.presentation.profile.ProfileViewModel
import com.kom.foodapp.presentation.register.RegisterViewModel
import com.kom.foodapp.presentation.splashscreen.SplashViewModel
import com.kom.foodapp.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
object AppModules {

    private val networkModule = module {
        single<FoodAppApiService> { FoodAppApiService.invoke() }
    }
    private val firebaseModule = module {
        single<FirebaseService> { FirebaseServiceImpl() }
    }
    private val localModule = module {
        single<AppDatabase> { AppDatabase.createInstance(androidContext()) }
        single<CartDao> { get<AppDatabase>().cartDao() }
        single<SharedPreferences> {
            SharedPreferenceUtils.createPreference(
                androidContext(),
                UserPreferenceImpl.PREF_NAME
            )
        }
        single<UserPreference> { UserPreferenceImpl(get()) }
    }

    private val datasource = module {
        single<AuthDataSource> { FirebaseAuthDataSource(get()) }
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<CategoryDataSource> { CategoryApiDataSource(get()) }
        single<MenuDataSource> { MenuApiDataSource(get()) }
        single<ProfileDataSource> { ProfileDataSourceImpl() }
        single<UserPrefDataSource> { UserPrefDataSourceImpl(get()) }
    }

    private val repository = module {
        single<CartRepository> { CartRepositoryImpl(get()) }
        single<CategoryRepository> { CategoryRepositoryImpl(get()) }
        single<MenuRepository> { MenuRepositoryImpl(get(), get()) }
        single<UserPrefRepository> { UserPrefRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::CartViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModel { params ->
            DetailMenuViewModel(
                extras = params.get(),
                cartRepository = get()
            )
        }
        viewModelOf(::HomeViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::MainViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::SplashViewModel)
        viewModelOf(::ProfileViewModel)

    }

    val modules = listOf(
        networkModule,
        firebaseModule,
        localModule,
        datasource,
        repository,
        viewModelModule

    )
}