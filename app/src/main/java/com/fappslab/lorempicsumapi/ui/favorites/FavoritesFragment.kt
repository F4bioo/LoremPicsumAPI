package com.fappslab.lorempicsumapi.ui.favorites

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fappslab.lorempicsumapi.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()
}