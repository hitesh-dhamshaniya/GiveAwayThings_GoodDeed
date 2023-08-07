package give.away.good.deeds.screens.profile

import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentProfileBinding
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    private lateinit var selectedImageUri: Uri

    override fun getLayout(): Int = R.layout.fragment_profile

    override fun getViewModel(): ProfileViewModel {
        return ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun initViewModel() {
        mViewModel.isLoadingLiveData.observe(this) {
            showProgress(it)
        }

        mViewModel.profilePicUploadLiveData.observe(this) {

        }
    }

    override fun onCreateView() {
        mViewDataBinding.btnUpdate.setOnClickListener {
            showImageSourceDialog()
        }

        mViewDataBinding.btnUpload.setOnClickListener {
            mViewModel.uploadImage(selectedImageUri)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            mViewDataBinding.ivUserProfilePic.setImageURI(selectedImageUri)
        }
    }


    private val pickGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            mViewDataBinding.ivUserProfilePic.setImageURI(selectedImageUri)
            //uploadImage(selectedImageUri)
        }
    }

    private fun showImageSourceDialog() {
        val dialog = AlertDialog.Builder(mActivity)
        dialog.setTitle("Select Image Source")
            .setItems(arrayOf("Take Picture", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> takePicture()
                    1 -> pickGallery()
                }
            }
            .show()
    }

    private fun createImageFile(): Uri {
        // Create an image file name
        val storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir
        )
        return FileProvider.getUriForFile(mActivity, getString(R.string.file_provider_path), imageFile)
    }

    private fun takePicture() {
        val imageUri = createImageFile()
        selectedImageUri = imageUri
        takePicture.launch(imageUri)
    }

    private fun pickGallery() {
        pickGallery.launch("image/*")
    }
}