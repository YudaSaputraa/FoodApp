package com.kom.foodapp.presentation.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kom.foodapp.R
import com.kom.foodapp.data.model.Category
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.databinding.FragmentHomeBinding
import com.kom.foodapp.presentation.detailmenu.DetailActivity
import com.kom.foodapp.presentation.home.adapter.CategoryAdapter
import com.kom.foodapp.presentation.home.adapter.MenuAdapter
import com.kom.foodapp.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModel()

    private var isDarkMode: Boolean = false
    private var menuAdapter: MenuAdapter? = null
    private var categories: List<Category>? = null
    private var menuItems: List<Menu>? = null
    private var lastSelectedCategory: String? = null

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val isUsingGridMode = homeViewModel.isUsingGridMode()
        bindModeList(isUsingGridMode)
        setClickAction(isUsingGridMode)
        setIconFromPref(isUsingGridMode)
        setCategoryData()
        loadCategoriesData()
        loadMenuData()
        setThemeMode()
        setDisplayName()
        setListMenuOnCategoryClicked()
        searchMenu()
    }

    private fun searchMenu() {
        binding.layoutHeader.layoutSearchBar.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    searchMenuByName(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                }
            },
        )
    }

    private fun searchMenuByName(query: String) {
        if (query.isNotEmpty()) {
            val filteredMenu = menuItems?.filter { it.name.contains(query, ignoreCase = true) }
            filteredMenu?.let { bindMenu(it) }
        } else {
            loadMenuData()
        }
    }

    private fun loadMenuData() {
        menuItems?.let { bindMenu(it) } ?: getMenuData()
    }

    private fun loadCategoriesData() {
        categories?.let { bindCategory(it) } ?: getCategoryData()
    }

    private fun setDisplayName() {
        if (!homeViewModel.userIsLoggedIn()) {
            binding.layoutHeader.tvName.apply {
                text = getString(R.string.text_user_not_login)
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            val currentUser = homeViewModel.getCurrentUser()
            binding.layoutHeader.tvName.text =
                getString(R.string.text_display_name, currentUser?.fullName)
        }
    }

    private fun setCategoryData() {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
    }

    private fun setListMenuOnCategoryClicked() {
        categoryAdapter.setOnItemClickListener { category ->
            if (category.name == lastSelectedCategory) {
                lastSelectedCategory = null
                getMenuData()
            } else {
                getMenuData(category.name)
                lastSelectedCategory = category.name
            }
        }
    }

    private fun getMenuData(categoryName: String? = null) {
        homeViewModel.getMenu(categoryName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.rvMenu.isVisible = true
                    it.payload?.let { data ->
                        menuItems = data
                        bindMenu(data)
                    }
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
                },
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
                ConstraintSet.BOTTOM,
            )
        } else {
            constraintSet.connect(
                R.id.tv_menu_title,
                ConstraintSet.TOP,
                R.id.rv_category,
                ConstraintSet.BOTTOM,
            )
        }
        constraintSet.applyTo(binding.clContent)
    }

    private fun getCategoryData() {
        homeViewModel.getCategories().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateCategory.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.rvCategory.isVisible = true
                    it.payload?.let { data ->
                        categories = data
                        bindCategory(data)
                    }
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
                },
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
                    getString(R.string.text_toast_theme_mode),
                    Toast.LENGTH_SHORT,
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
            if (isGridMode) {
                MenuAdapter.MODE_GRID
            } else {
                MenuAdapter.MODE_LIST
            }
        menuAdapter =
            MenuAdapter(
                listMode = listMode,
                listener =
                    object : MenuAdapter.OnItemClickedListener<Menu> {
                        override fun onItemSelected(item: Menu) {
                            pushToDetail(item)
                        }

                        override fun onItemAddedToCart(item: Menu) {
                            homeViewModel.addItemToCart(item)
                        }
                    },
            )
        binding.rvMenu.apply {
            adapter = this@HomeFragment.menuAdapter
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    if (isGridMode) {
                        2
                    } else {
                        1
                    },
                )
        }
        setMenuTitleConstraint(false)
    }

    private fun bindCategory(categories: List<Category>) {
        this.categories = categories
        categoryAdapter.submitData(categories)
    }

    private fun bindMenu(menu: List<Menu>) {
        this.menuItems = menu
        menuAdapter?.submitData(menu)
    }

    private fun pushToDetail(item: Menu) {
        DetailActivity.startActivity(
            requireContext(),
            item,
        )
    }

    private fun setClickAction(usingGrid: Boolean) {
        var isGridMode = usingGrid
        binding.ivMenuList.setOnClickListener {
            isGridMode = !isGridMode
            if (isGridMode) {
                binding.ivMenuList.setImageResource(R.drawable.ic_menu_list)
            } else {
                binding.ivMenuList.setImageResource(R.drawable.ic_menu_grid)
            }
            bindModeList(isGridMode)
            loadMenuData()
            homeViewModel.setUsingGridMode(isGridMode)
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
