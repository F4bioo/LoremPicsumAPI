package com.fappslab.lorempicsumapi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fappslab.lorempicsumapi.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.postDelayed({
            lifecycleScope.launchWhenResumed {
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            }
        }, 500)
    }
}
