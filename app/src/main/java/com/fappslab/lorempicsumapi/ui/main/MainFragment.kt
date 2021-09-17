package com.fappslab.lorempicsumapi.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentMainBinding
import com.fappslab.lorempicsumapi.extension.navigateWithAnimations
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val args: MainFragmentArgs by navArgs()
    private var page = 1
    private var limit = 10
    private var dy = 0

    private val mainAdapter by lazy {
        MainAdapter { photo ->
            val directions = MainFragmentDirections.actionMainFragmentToDetailsFragment(photo)
            findNavController().navigateWithAnimations(directions)
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
            if (mainAdapter.itemCount == 0) {
                mainAdapter.submitList(it.photos.toMutableList())
            }
        }
        showSystemUI()
        initObserver()
        initRecyclerView()
        initScrollListener()
        handleOnBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.getPhotosEvent.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.OnSuccess -> {
                    mainAdapter.submitList(dataState.data.toMutableList())
                    binding.progressPhotos.isVisible = false
                }
                is DataState.OnError -> {
                }
                is DataState.OnException -> showError()
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerPhotos.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerPhotos.setHasFixedSize(true)
            recyclerPhotos.adapter = mainAdapter
        }
    }

    private fun initScrollListener() {
        binding.apply {
            recyclerPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (hasBottomReached(dy)) {
                        // Has reached last item
                        // Increase page size
                        progressPhotos.isVisible = true
                        viewModel.getPhotos(page, page++)
                    }
                }
            })
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
            viewModel.getPhotos(page, limit)
        }.show()
    }

    private fun showSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window, binding.mainRoot
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun hasTopReached(): Boolean {
        return !binding.recyclerPhotos.canScrollVertically(-1) && dy < 0
    }

    private fun hasBottomReached(dy: Int): Boolean {
        this.dy = dy
        return !binding.recyclerPhotos.canScrollVertically(1) && dy > 0
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
