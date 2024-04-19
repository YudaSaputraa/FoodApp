package com.kom.foodapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.datasource.cart.CartDatabaseDataSource
import com.kom.foodapp.data.datasource.category.CategoryApiDataSource
import com.kom.foodapp.data.datasource.category.CategoryDataSource
import com.kom.foodapp.data.datasource.menu.MenuApiDataSource
import com.kom.foodapp.data.datasource.menu.MenuDataSource
import com.kom.foodapp.data.model.Category
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.repository.CartRepositoryImpl
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.CategoryRepositoryImpl
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.data.repository.MenuRepositoryImpl
import com.kom.foodapp.data.source.local.database.AppDatabase
import com.kom.foodapp.data.source.local.pref.UserPreference
import com.kom.foodapp.data.source.local.pref.UserPreferenceImpl
import com.kom.foodapp.data.source.network.services.FoodAppApiService
import com.kom.foodapp.databinding.FragmentHomeBinding
import com.kom.foodapp.presentation.detailmenu.DetailActivity
import com.kom.foodapp.presentation.home.adapter.CategoryAdapter
import com.kom.foodapp.presentation.home.adapter.MenuAdapter
import com.kom.foodapp.utils.GenericViewModelFactory
import com.kom.foodapp.utils.proceedWhen

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userPreference: UserPreference

    private val viewModel: HomeViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val apiService = FoodAppApiService.invoke()
        val menuDataSource: MenuDataSource = MenuApiDataSource(apiService)
        val cartDataSource: CartDataSource = CartDatabaseDataSource(database.cartDao())
        val menuRepository: MenuRepository = MenuRepositoryImpl(menuDataSource)
        val categoryDataSource: CategoryDataSource = CategoryApiDataSource(apiService)
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)
        val cartRepository = CartRepositoryImpl(cartDataSource)

        GenericViewModelFactory.create(
            HomeViewModel(
                categoryRepository,
                menuRepository,
                cartRepository
            )
        )
    }

    private var isDarkMode: Boolean = false
    private var menuAdapter: MenuAdapter? = null

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
            getMenuData(it.name)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreferenceImpl(requireContext())
        val isUsingGridMode = userPreference.isUsingGridMode()
        bindModeList(isUsingGridMode)
        setClickAction(isUsingGridMode)
        setIconFromPref(isUsingGridMode)
        getCategoryData()
        setCategoryData()
        setThemeMode()
    }

    private fun setCategoryData() {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
    }

    private fun getMenuData(categoryName: String? = null) {
        viewModel.getMenu(categoryName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.rvMenu.isVisible = true
                    it.payload?.let { data -> bindMenu(data) }
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        it.exception?.message.orEmpty()
                    binding.rvMenu.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        getString(R.string.text_on_menu_data_empty)
                    binding.rvMenu.isVisible = false
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.rvMenu.isVisible = false
                }
            )
        }
    }

    private fun setMenuTitleConstraint(isLoading: Boolean) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clContent)

        if (isLoading) {
            constraintSet.connect(
                R.id.tv_menu_title,
                ConstraintSet.TOP,
                R.id.layout_on_empty_data_state_category,
                ConstraintSet.BOTTOM
            )

        } else {
            constraintSet.connect(
                R.id.tv_menu_title,
                ConstraintSet.TOP,
                R.id.rv_category,
                ConstraintSet.BOTTOM
            )
        }

        constraintSet.applyTo(binding.clContent)
    }


    private fun getCategoryData() {
        viewModel.getCategories().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateCategory.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataStateCategory.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataStateCategory.tvOnEmptyData.isVisible = false
                    binding.rvCategory.isVisible = true
                    it.payload?.let { data -> bindCategory(data) }
                    setMenuTitleConstraint(false)
                },
                doOnError = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutOnEmptyDataStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataStateCategory.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataStateCategory.tvOnEmptyData.text =
                        it.exception?.message.orEmpty()
                    binding.rvCategory.isVisible = false
                    setMenuTitleConstraint(true)
                },
                doOnEmpty = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutOnEmptyDataStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataStateCategory.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataStateCategory.tvOnEmptyData.text =
                        getString(R.string.text_category_on_empty)
                    binding.rvCategory.isVisible = false
                    setMenuTitleConstraint(true)
                },
                doOnLoading = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutOnEmptyDataStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataStateCategory.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataStateCategory.tvOnEmptyData.isVisible = false
                    binding.rvCategory.isVisible = false
                    setMenuTitleConstraint(true)
                }
            )
        }
    }


    private fun setThemeMode() {
        binding.layoutHeader.ivThemeMode.setOnClickListener {
            isDarkMode = !isDarkMode
            if (isDarkMode) {
                binding.layoutHeader.ivThemeMode.setImageResource(R.drawable.ic_dark_mode)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_toast_theme_mode), Toast.LENGTH_SHORT
                ).show()

            } else {
                binding.layoutHeader.ivThemeMode.setImageResource(R.drawable.ic_light_mode)
                Toast.makeText(requireContext(), R.string.text_toast_theme_mode, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun bindModeList(isGridMode: Boolean) {
        val listMode =
            if (isGridMode)
                MenuAdapter.MODE_GRID
            else
                MenuAdapter.MODE_LIST
        menuAdapter = MenuAdapter(
            listMode = listMode,
            listener = object : MenuAdapter.OnItemClickedListener<Menu> {
                override fun onItemSelected(item: Menu) {
                    pushToDetail(item)
                }

                override fun onItemAddedToCart(item: Menu) {
                    viewModel.addItemToCart(item)
                }

            }
        )
        binding.rvMenu.apply {
            adapter = this@HomeFragment.menuAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (isGridMode)
                    2
                else
                    1
            )
        }
        getMenuData(null)
    }

    private fun bindCategory(categories: List<Category>) {
        categoryAdapter.submitData(categories)
    }

    private fun bindMenu(menu: List<Menu>) {
        menuAdapter?.submitData(menu)
    }

    private fun pushToDetail(item: Menu) {
        DetailActivity.startActivity(
            requireContext(), item
        )
    }

    private fun setClickAction(usingGrid: Boolean) {
        var isGridMode = usingGrid
        binding.ivMenuList.setOnClickListener {
            isGridMode = !isGridMode
            if (isGridMode)
                binding.ivMenuList.setImageResource(R.drawable.ic_menu_list)
            else
                binding.ivMenuList.setImageResource(R.drawable.ic_menu_grid)
            bindModeList(isGridMode)
            userPreference.setUsingGridMode(isGridMode)

        }
    }

    private fun setIconFromPref(isGridMode: Boolean) {
        if (isGridMode) {
            binding.ivMenuList.setImageResource(R.drawable.ic_menu_list)
        } else {
            binding.ivMenuList.setImageResource(R.drawable.ic_menu_grid)
        }
    }

}