package com.kom.foodapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.model.Menu
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.category.DummyCategoryDataSource
import com.kom.foodapp.data.datasource.menu.DummyMenuDataSource
import com.kom.foodapp.data.model.Category
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.CategoryRepositoryImpl
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.data.repository.MenuRepositoryImpl
import com.kom.foodapp.databinding.FragmentHomeBinding
import com.kom.foodapp.presentation.detailmenu.DetailActivity
import com.kom.foodapp.presentation.home.adapter.CategoryAdapter
import com.kom.foodapp.presentation.home.adapter.MenuAdapter
import com.kom.foodapp.utils.GenericViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels {
        val menuDataSource = DummyMenuDataSource()
        val menuRepository: MenuRepository = MenuRepositoryImpl(menuDataSource)
        val categoryDataSource = DummyCategoryDataSource()
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(categoryDataSource)
        GenericViewModelFactory.create(HomeViewModel(categoryRepository, menuRepository))
    }
    private var isGridMode: Boolean = true
    private var isDarkMode: Boolean = false
    private var adapter: MenuAdapter? = null

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {

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
        bindCategory(viewModel.getCategories())
        bindModeList(true)
        setClickAction()
        setThemeMode()


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
        adapter = MenuAdapter(
            listMode = listMode,
            listener = object : MenuAdapter.OnItemClickedListener<Menu> {
                override fun onItemSelected(item: Menu) {
                    pushToDetail(item)
                }

            }
        )
        binding.rvMenu.apply {
            adapter = this@HomeFragment.adapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (isGridMode)
                    2
                else
                    1
            )
        }
        adapter?.submitData(viewModel.getMenu())
    }

    private fun bindCategory(categories: List<Category>) {
        binding.rvCategory.apply {
            adapter = categoryAdapter
        }
        categoryAdapter.submitData(categories)
    }

    private fun pushToDetail(item: Menu) {
        DetailActivity.startActivity(
            requireContext(), item
        )
    }

    private fun setClickAction() {
        binding.ivMenuList.setOnClickListener {
            isGridMode = !isGridMode
            if (isGridMode)

                binding.ivMenuList.setImageResource(R.drawable.ic_menu_list)
            else
                binding.ivMenuList.setImageResource(R.drawable.ic_menu_grid)
            bindModeList(isGridMode)

        }
    }


}