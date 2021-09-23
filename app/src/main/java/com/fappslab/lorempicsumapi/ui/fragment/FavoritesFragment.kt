package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentFavoritesBinding
import com.fappslab.lorempicsumapi.ui.adapter.LocalAdapter
import com.fappslab.lorempicsumapi.ui.viewmodel.FavoritesViewModel
import com.fappslab.lorempicsumapi.utils.extensions.navigateWithAnimations
import com.fappslab.lorempicsumapi.utils.extensions.showSystemUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoritesViewModel>()
    private var pos = -1

    private val adapter by lazy {
        LocalAdapter { view, photo, position ->
            when (view.id) {
                R.id.check_favorite -> {
                    pos = position
                    viewModel.deleteFavorite(photo.id.toLong())
                }
                else -> {
                    val directions =
                        FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(photo)
                    findNavController().navigateWithAnimations(directions)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
        requireActivity().showSystemUI(view)
        getFavorites()
        initObserver()
        initRecyclerView()
        initListeners()
        handleOnBackPressed()
    }

    private fun initListeners() {
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                getFavorites()
            }
        }
    }

    private fun getFavorites() {
        viewModel.getFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.getFavoritesEvent.observe(viewLifecycleOwner) { dataState ->
            binding.swipeRefresh.isRefreshing = false

            if (dataState is DataState.OnSuccess) {
                val photos = dataState.data.toMutableList()
                adapter.clearList()
                adapter.submitList(photos)
                emptyLayout()
            }
        }

        viewModel.deleteEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess) {
                adapter.removeItemList(pos)
                emptyLayout()
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerPhotos.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerPhotos.setHasFixedSize(true)
            recyclerPhotos.adapter = adapter
        }
    }

    private fun emptyLayout() {
        binding.include.root.isVisible =
            adapter.itemCount == 0
    }

    private fun handleOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
