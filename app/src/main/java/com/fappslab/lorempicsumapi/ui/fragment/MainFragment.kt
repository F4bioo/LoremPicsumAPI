package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.databinding.FragmentMainBinding
import com.fappslab.lorempicsumapi.ui.adapter.RemoteAdapter
import com.fappslab.lorempicsumapi.ui.adapter.RemoteLoadState
import com.fappslab.lorempicsumapi.ui.viewmodel.MainViewModel
import com.fappslab.lorempicsumapi.utils.extensions.navigateWithAnimations
import com.fappslab.lorempicsumapi.utils.extensions.showSystemUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var getFavorite: GetFavorite

    private val adapter by lazy {
        RemoteAdapter(lifecycle, getFavorite) { view, photo, _ ->
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
        requireActivity().showSystemUI(view)
        initObserver()
        initRecyclerView()
        initListeners()
        initViewBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.jobCancel()
    }

    private fun initObserver() {
        viewModel.pagingEvent.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        adapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading || it.refresh is LoadState.Error) {
                binding.inEmpty.emptyRoot.isVisible = adapter.itemCount == 0
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerPhotos.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recyclerPhotos.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RemoteLoadState { },
                footer = RemoteLoadState { adapter.retry() }
            )
        }
    }

    private fun initListeners() {
        binding.apply {
            fab.setOnClickListener {
                findNavController()
                    .navigateWithAnimations(R.id.action_mainFragment_to_favoritesFragment)
            }

            inEmpty.buttonTry.setOnClickListener {
                adapter.retry()
            }
        }
    }

    private fun initViewBinding() {
        binding.apply {
            inEmpty.buttonTry.transformationMethod = null
        }
    }
}
