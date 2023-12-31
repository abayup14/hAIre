package com.haire.ui.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityEditProfileBinding
import com.haire.ui.profile.ProfileViewModel
import com.haire.util.showLoading
import com.haire.util.showText

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnOpenFile.setOnClickListener {
            startGallery()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.edtDescStory.text.toString()
            if (description.isEmpty()) {
                showText(this, "Description and age are still empty")
            } else {
                viewModel.getUser().observe(this) {
                    saveProfile(it.id,  description)
                }
            }
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivAddStory.setImageURI(it)
        }
    }

    private fun saveProfile(idUser: Int, description: String) {
        currentImageUri?.let { uri ->
            viewModel.saveProfile(
                uri,
                onSuccess = { imageUrl ->
                    viewModel.updateDatabase(
                        idUser,
                        description,
                        imageUrl,
                        onSuccess = {
                            finish()
                        },
                        onFailure = { exception ->
                            exception.printStackTrace()
                        }
                    )
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                }
            )
        } ?: showText(this, getString(R.string.message_null_picture))
    }
}