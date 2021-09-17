package com.fappslab.lorempicsumapi.ui.details

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.databinding.FragmentDetailsBinding
import com.fappslab.lorempicsumapi.extension.navigateWithAnimations
import com.fappslab.lorempicsumapi.utils.Constants.MODAL_BLUR
import com.fappslab.lorempicsumapi.utils.Constants.MODAL_GRAYSCALE
import com.fappslab.lorempicsumapi.utils.Constants.MODAL_NORMAL
import com.fappslab.lorempicsumapi.utils.Utils
import com.fappslab.lorempicsumapi.utils.extensions.getNavigationResult
import com.fappslab.lorempicsumapi.utils.extensions.set
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var photo: Photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSystemUI()
        args.detailsArgs?.let {
            photo = it
            viewBinding()
            initListeners()
            initObserver()

            view.postDelayed({
                openModal()
            }, 600)
        }
    }

    private fun initObserver() {
        getNavigationResult<String>()?.observe(viewLifecycleOwner) { result ->
            when (result) {
                MODAL_NORMAL -> {
                    binding.imagePhoto.set(Utils.normalUrl(id = photo.id))
                }
                MODAL_GRAYSCALE -> {
                    binding.imagePhoto.set(Utils.greyscaleUrl(id = photo.id))

                }
                MODAL_BLUR -> {
                    binding.imagePhoto.set(Utils.blurUrl(id = photo.id))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun viewBinding() {
        binding.imagePhoto.set(photo.downloadUrl)
    }

    private fun initListeners() {
        binding.apply {
            imageBack.setOnClickListener {
                findNavController().popBackStack()
            }

            imageMenu.setOnClickListener {
                openModal()
            }
        }
    }

    private fun openModal() {
        photo.bitmap = (binding.imagePhoto.drawable as BitmapDrawable).bitmap

        val directions =
            DetailsFragmentDirections.actionDetailsFragmentToModalFragment(photo)
        findNavController().navigateWithAnimations(directions)
    }

    private fun hideSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.detailsRoot).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
