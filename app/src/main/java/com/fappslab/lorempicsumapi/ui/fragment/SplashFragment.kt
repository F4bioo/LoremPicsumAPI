package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.model.Photos
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentSplashBinding
import com.fappslab.lorempicsumapi.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver(view: View) {
        viewModel.getPhotos()

        viewModel.getPhotosEvent.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.OnSuccess -> {
                    val photos = Photos(dataState.data)
                    val directions =
                        SplashFragmentDirections.actionSplashFragmentToMainFragment(photos)
                    view.postDelayed({
                        findNavController().navigate(directions)
                    }, 1000)
                }
                is DataState.OnError -> {

                }
                is DataState.OnException -> {
                    showError()
                }
            }
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
            viewModel.getPhotos()
        }.show()
    }
}
