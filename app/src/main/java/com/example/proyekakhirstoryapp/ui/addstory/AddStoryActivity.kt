package com.example.proyekakhirstoryapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.ActivityAddStoryBinding
import com.example.proyekakhirstoryapp.ui.home.MainActivity
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.example.proyekakhirstoryapp.utils.reduceFileImage
import com.example.proyekakhirstoryapp.utils.rotateFile
import com.example.proyekakhirstoryapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private var _binding: ActivityAddStoryBinding? = null
    private lateinit var factory: ViewModelFactory
    private val addStoryViewModel: AddStoryViewModel by viewModels { factory }
    private var getFile: File? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this)

        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.buttonAdd.setOnClickListener {
            if (getFile != null) {
                if (binding.edAddDescription.text != null){
                    val file = reduceFileImage(getFile as File)
                    val desc =
                        binding.edAddDescription.text.toString()
                            .toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        KEY_PHOTO,
                        file.name,
                        requestImageFile
                    )

                    addStoryViewModel.getUserToken().observe(this) { token ->
                        uploadStory(imageMultipart, desc, "bearer $token")
                        addStoryViewModel.error.observe(this) { error ->
                            if (!error) {
                                val intentToMain = Intent(this, MainActivity::class.java)
                                startActivity(intentToMain)
                                finish()
                            }else{
                                addStoryViewModel.message.observe(this){
                                        message ->
                                    val msg = getString(R.string.error_upload)
                                    displayToast("$message: $msg")
                                }
                            }
                        }
                    }
                }
                else{
                    val msg = getString(R.string.error_no_desc)
                    displayToast(msg)
                }
            } else {
                val msg = getString(R.string.error_no_photo)
                displayToast(msg)
            }
        }

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null
    ) {
        addStoryViewModel.uploadStory(photo, description, token, lat, lon)
    }

    private fun startCamera() {
        val intentToCamera = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intentToCamera)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                val msg = getString(R.string.error_no_permission)
                displayToast(msg)
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra(KEY_PHOTO, File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra(KEY_PHOTO)
            } as? File

            val isBackCamera =
                it.data?.getBooleanExtra(KEY_CAMERA_STATUS, true) as Boolean


            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonAdd.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.buttonAdd.isEnabled = true
        }
    }

    private fun displayToast(msg: String) {
        return Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val KEY_PHOTO = "photo"
        const val KEY_CAMERA_STATUS = "isBackCamera"
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.title_add_story)
    }

}