package com.fappslab.lorempicsumapi.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var page = 1
    private var limite = 10

    private val mainAdapter by lazy {
        MainAdapter { photo ->
            println("author: ${photo.author} ")
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
        viewModel.getPhotos(page, limite)
        initObserver()
        initRecyclerView()
        initScrollListener()
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

                    println("<> aaa ${dataState.data.size}")
                }
                is DataState.OnError -> {
                    println("<> bbb ${dataState.error.code}")
                }
                is DataState.OnException -> {
                    println("<> ccc ${dataState.e.message}")
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.run {
            recyclerPhotos.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerPhotos.adapter = mainAdapter
        }
    }

    private fun initScrollListener() {
        binding.run {
            scrollPhotos.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // Has reached last item
                    // Increase page size
                    page++
                    progressPhotos.isVisible = true

                    viewModel.getPhotos(page, limite)
                }
            })
        }
    }
}
