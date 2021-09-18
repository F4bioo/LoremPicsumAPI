package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentMainBinding
import com.fappslab.lorempicsumapi.ui.adapter.PhotoAdapter
import com.fappslab.lorempicsumapi.ui.viewmodel.MainViewModel
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.extensions.navigateWithAnimations
import com.fappslab.lorempicsumapi.utils.extensions.showSystemUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val args: MainFragmentArgs by navArgs()
    private var page = 2
    private var dy = 0

    private val photoAdapter by lazy {
        PhotoAdapter { view, photo, _ ->
            when (view.id) {
                R.id.card_root -> {
                    val directions =
                        MainFragmentDirections.actionMainFragmentToDetailsFragment(photo)
                    findNavController().navigateWithAnimations(directions)
                }

                R.id.check_favorite -> {
                    viewModel.setFavorite(photo)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.mainArgs?.let {
            if (photoAdapter.itemCount == 0) {
                val photos = it.photos.toMutableList()
                photoAdapter.submitList(photos)
            }
        }

        requireActivity().showSystemUI(view)
        initObserver()
        initRecyclerView()
        initListeners()
        handleOnBackPressed()
        emptyLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.getPhotosEvent.observe(viewLifecycleOwner) { dataState ->
            binding.progressPhotos.isVisible = false

            when (dataState) {
                is DataState.OnSuccess -> {
                    val photos = dataState.data.distinct().toMutableList()
                    photoAdapter.submitList(photos)
                }
                else -> showError()
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<List<Photo>>(Constants.FAVORITE_RESULT)?.observe(
                viewLifecycleOwner
            ) { bundle ->
                println("<> bundle: ${bundle.size}")
            }
    }

    private fun initRecyclerView() {
        binding.recyclerPhotos.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = photoAdapter
        }
    }

    private fun initListeners() {
        binding.apply {
            recyclerPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (hasBottomReached(dy)) {
                        // Has reached last item
                        // Increase page size
                        progressPhotos.isVisible = true
                        page++
                        viewModel.getPhotos(page)
                    }
                }
            })

            fab.setOnClickListener {
                findNavController()
                    .navigateWithAnimations(R.id.action_mainFragment_to_favoritesFragment)
            }

            recyclerPhotos.onFlingListener
        }
    }

    private fun showError() {
        val snackBar = Snackbar.make(
            binding.root,
            getString(R.string.error_splash),
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction(getString(R.string.try_again)) {
            snackBar.dismiss()
            viewModel.getPhotos(page)
        }.show()
    }

    private fun hasTopReached(): Boolean {
        return !binding.recyclerPhotos.canScrollVertically(-1) && dy < 0
    }

    private fun hasBottomReached(dy: Int): Boolean {
        this.dy = dy
        return !binding.recyclerPhotos.canScrollVertically(1) && dy > 0
    }

    private fun emptyLayout() {
        binding.include.emptyRoot.isVisible =
            photoAdapter.itemCount == 0
    }

    private fun handleOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!hasTopReached()) {
                    binding.recyclerPhotos.smoothScrollToPosition(0)
                } else requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
