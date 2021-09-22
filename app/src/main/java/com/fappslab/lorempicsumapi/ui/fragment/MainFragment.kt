package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.databinding.FragmentDetailsBinding
import com.fappslab.lorempicsumapi.databinding.FragmentMainBinding
import com.fappslab.lorempicsumapi.ui.adapter.RemoteAdapter
import com.fappslab.lorempicsumapi.ui.adapter.RemoteLoadState
import com.fappslab.lorempicsumapi.ui.viewmodel.MainViewModel
import com.fappslab.lorempicsumapi.utils.extensions.navigateWithAnimations
import com.fappslab.lorempicsumapi.utils.extensions.showSystemUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var getFavorite: GetFavorite

    private val adapter by lazy {
        RemoteAdapter(lifecycle, getFavorite) { view, photo, _ ->
            when (view.id) {
                R.id.check_favorite -> {
                    viewModel.setFavorite(photo)
                }
                else -> {
                    val directions =
                        MainFragmentDirections.actionMainFragmentToDetailsFragment(photo)
                    findNavController().navigateWithAnimations(directions)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        requireActivity().showSystemUI(view)
        initObserver()
        initRecyclerView()
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.jobCancel()
    }

    private fun initObserver() {
        viewModel.pagingEvent.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerPhotos.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recyclerPhotos.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RemoteLoadState { adapter.retry() },
                footer = RemoteLoadState { adapter.retry() }
            )
        }
    }

    private fun initListeners() {
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }

            fab.setOnClickListener {
                findNavController()
                    .navigateWithAnimations(R.id.action_mainFragment_to_favoritesFragment)
            }

            include.buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { loadStates ->
            loadStates.showEmptyList()
        }
    }

    private fun CombinedLoadStates.showEmptyList() {
        binding.include.apply {
            val isLoading = refresh is LoadState.Loading
            val isError = refresh is LoadState.Error

            val isListEmpty = (isLoading || isError) && adapter.itemCount == 0
            root.isVisible = isListEmpty

            buttonRetry.isVisible = isLoading.apply { progress.isVisible = !this }
            buttonRetry.isVisible = isError.apply { progress.isVisible = !this }

            textEmpty.text = when {
                isLoading -> getString(R.string.loading_list)
                else -> getString(R.string.empty_list)
            }

            if (append.endOfPaginationReached) {
                Snackbar.make(
                    root,
                    getString(R.string.pagination_reached),
                    Snackbar.LENGTH_INDEFINITE
                ).apply {
                    setAction(getString(R.string.retry)) { dismiss() }
                }.show()
            }
        }
    }
}
