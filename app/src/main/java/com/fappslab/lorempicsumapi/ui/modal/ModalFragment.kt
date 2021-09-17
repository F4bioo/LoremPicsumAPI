package com.fappslab.lorempicsumapi.ui.modal

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fappslab.lorempicsumapi.R
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.databinding.FragmentModalBinding
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.Utils.share
import com.fappslab.lorempicsumapi.utils.extensions.setNavigationResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
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

        args.detailsArgs?.let {
            photo = it
            viewBinding()
            initObserver()
            initListeners()
        }
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
            } else {
            }
        }

        viewModel.insertEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess) {

            } else {
            }
        }

        viewModel.saveEvent.observe(viewLifecycleOwner) { dataState ->
            if (dataState is DataState.OnSuccess) {
                Toast.makeText(requireContext(), getText(R.string.saved), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getText(R.string.error_try_again),
                    Toast.LENGTH_SHORT
                ).show()
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
            buttonNormal.setOnClickListener {
                setNavigationResult(result = Constants.MODAL_NORMAL)
            }

            buttonGrayscale.setOnClickListener {
                setNavigationResult(result = Constants.MODAL_GRAYSCALE)
            }

            buttonBlur.setOnClickListener {
                setNavigationResult(result = Constants.MODAL_BLUR)
            }

            buttonAuthor.setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(photo.url)).apply {
                    startActivity(this)
                }
            }

            buttonDownload.setOnClickListener {
                if (hasPermission()) {
                    viewModel.savePhoto(requireContext(), photo.bitmap)
                } else requestSavePermission()
            }

            buttonShare.setOnClickListener {
                requireContext().share(photo.url)
            }

            buttonFavorite.setOnClickListener {
                photo.favorite = !photo.favorite
                setFavoriteIcon(photo.favorite)
                viewModel.setFavorite(photo)
            }
        }
    }

    private fun setFavoriteIcon(isSelected: Boolean) {
        binding.apply {
            if (isSelected) {
                buttonFavorite.set(R.drawable.ic_favorite_selected, Color.RED)
            } else buttonFavorite.set(R.drawable.ic_favorite_unselected, Color.WHITE)
        }
    }

    private fun MaterialButton.set(@DrawableRes icon: Int, color: Int) {
        setIconResource(icon)
        iconTint = ColorStateList.valueOf(color)
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
        viewModel.savePhoto(requireContext(), photo.bitmap)
    }
}
