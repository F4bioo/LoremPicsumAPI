package com.fappslab.lorempicsumapi.ui.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentModalBinding
import com.fappslab.lorempicsumapi.ui.viewmodel.ModalViewModel
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.Utils.share
import com.fappslab.lorempicsumapi.utils.extensions.set
import com.fappslab.lorempicsumapi.utils.extensions.setNavigationResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModalFragment : BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentModalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModalViewModel by viewModels()
    private val args: ModalFragmentArgs by navArgs()
    private lateinit var photo: Photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(dialog as BottomSheetDialog).behavior.peekHeight = 200

        args.detailsArgs?.let { photo = it }
        if (!this::photo.isInitialized) {
            findNavController().popBackStack()
            return
        }

        viewBinding()
        initObserver()
        initListeners()
        iconButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.getFavorite(photo.id.toLong())

        viewModel.selectEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess) {
                photo.favorite = dataState.data.favorite
                setFavoriteIcon(photo.favorite)
            }
        }
    }

    private fun viewBinding() {
        binding.apply {
            buttonAuthor.transformationMethod = null
            textId.text = String.format("#%s", photo.id)
            textSquare.text = String.format("%sx%s", photo.width, photo.height)
            buttonAuthor.text = photo.author
        }
    }

    private fun initListeners() {
        binding.apply {
            normal.button.setOnClickListener {
                setNavigationResult(result = Constants.MODAL_NORMAL)
            }

            grayscale.button.setOnClickListener {
                setNavigationResult(result = Constants.MODAL_GRAYSCALE)
            }

            blur.button.setOnClickListener {
                setNavigationResult(result = Constants.MODAL_BLUR)
            }

            buttonAuthor.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(photo.url)).apply {
                    startActivity(this)
                }
            }

            download.button.setOnClickListener {
                if (hasPermission()) {
                    setNavigationResult(result = Constants.MODAL_SAVE)
                } else requestSavePermission()
            }

            share.button.setOnClickListener {
                requireContext().share(photo.url)
            }

            favorite.button.setOnClickListener {
                photo.favorite = !photo.favorite
                setFavoriteIcon(photo.favorite)
                viewModel.setFavorite(photo)
            }
        }
    }

    private fun iconButtons() {
        binding.apply {
            normal.button.set(R.drawable.ic_default)
            grayscale.button.set(R.drawable.ic_grayscale)
            blur.button.set(R.drawable.ic_blur)
            download.button.set(R.drawable.ic_download)
            share.button.set(R.drawable.ic_share)
            favorite.button.set(R.drawable.ic_favorite_unselected)
        }
    }

    private fun setFavoriteIcon(isSelected: Boolean) {
        binding.apply {
            if (isSelected) {
                favorite.button.set(R.drawable.ic_favorite_selected, Color.RED)
            } else favorite.button.set(R.drawable.ic_favorite_unselected, Color.WHITE)
        }
    }

    private fun hasPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private fun requestSavePermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.request_permission),
            Constants.RC_PERMISSION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else requestSavePermission()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        setNavigationResult(result = Constants.MODAL_SAVE)
    }
}
