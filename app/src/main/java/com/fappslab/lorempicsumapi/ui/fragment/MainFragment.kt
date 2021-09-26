package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.databinding.FragmentMainBinding
import com.fappslab.lorempicsumapi.ui.adapter.RemoteAdapter
import com.fappslab.lorempicsumapi.ui.adapter.paging.PhotoLoadState
import com.fappslab.lorempicsumapi.ui.viewmodel.MainViewModel
import com.fappslab.lorempicsumapi.utils.CheckNetwork
import com.fappslab.lorempicsumapi.utils.extensions.navigateWithAnimations
import com.fappslab.lorempicsumapi.utils.extensions.networkToast
import com.fappslab.lorempicsumapi.utils.extensions.showSystemUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private var auxHasReachedTop = false
    private var auxCnnLost = true

    @Inject
    lateinit var getFavorite: GetFavorite

    @Inject
    lateinit var checkNetwork: CheckNetwork

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
        handleOnBackPressed()
        networkRules()
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

        checkNetwork.observe(viewLifecycleOwner) { hasConnection ->
            networkRules(hasConnection)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerPhotos.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recyclerPhotos.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadState { adapter.retry() },
                footer = PhotoLoadState { adapter.retry() }
            )
        }
    }

    private fun initListeners() {
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                viewModel.getPhotosFromMediator()
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

        binding.recyclerPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                auxHasReachedTop = !recyclerView.canScrollVertically(-1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE
            }
        })
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
                    setAction(getString(R.string.ok)) { dismiss() }
                }.show()
            }
        }
    }

    private fun networkRules(hasConnection: Boolean = checkNetwork.isConnected()) {
        binding.apply {
            if (!hasConnection) {
                networkToastConfig(R.color.red, getString(R.string.connection_lost))
                auxCnnLost = false
            } else if (hasConnection && !auxCnnLost) {
                networkToastConfig(R.color.green, getString(R.string.connected))
                swipeRefresh.swipe()
                auxCnnLost = true
            }
        }
    }

    private fun networkToastConfig(@ColorRes color: Int, text: String) {
        val getColor = ContextCompat.getColor(requireContext(), color)
        binding.apply {
            textConnection.setBackgroundColor(getColor)
            textConnection.text = text
            textConnection.networkToast()
        }
    }

    private fun SwipeRefreshLayout.swipe() {
        isRefreshing = true
        adapter.retry()

        postDelayed({
            isRefreshing = false
        }, 1000)
    }

    private fun handleOnBackPressed() {
        val fa = requireActivity()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (auxHasReachedTop) {
                    fa.finish()
                } else binding.recyclerPhotos.smoothScrollToPosition(0)
            }
        }
        fa.onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, callback)
    }
}
