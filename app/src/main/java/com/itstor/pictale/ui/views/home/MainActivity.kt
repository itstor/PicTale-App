package com.itstor.pictale.ui.views.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowInsetsController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.itstor.pictale.R
import com.itstor.pictale.databinding.ActivityMainBinding
import com.itstor.pictale.ui.views.auth.AuthActivity
import com.itstor.pictale.ui.views.post.PostActivity
import com.itstor.pictale.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val feedFragment = FeedFragment()
    private val mapFragment = MapFragment()
    private val favoriteFragment = FavoriteFragment()

    private lateinit var currentPhotoPath: String
    private var previousSelectedItemId = R.id.action_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchFragment(feedFragment)
        setUpStatusBar()
        setUpToolbar()
        listenBottomNavigation()
        listenTokenChanges()
    }

    private fun listenTokenChanges() {
        // Observe token changes. If token got cleared, then redirect to login screen
        viewModel.getAuthToken().asLiveData().observe(this) {
            if (it == null) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(AuthActivity.EXTRA_IS_LOGIN, true)
                startActivity(intent)
            }
        }
    }

    private fun setUpStatusBar() {
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mainToolbar)

        val materialShapeDrawable = binding.mainToolbar.background as MaterialShapeDrawable
        materialShapeDrawable.shapeAppearanceModel = materialShapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 64f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 64f)
            .build()
    }

    private fun listenBottomNavigation() {
        binding.mainBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    switchFragment(feedFragment)
                    previousSelectedItemId = it.itemId
                    title = getString(R.string.title_feed)
                }

                R.id.action_logout -> {
                    viewModel.logout()
                }

                R.id.action_post -> {
                    createPostDialog()
                }

                R.id.action_favorite -> {
                    switchFragment(favoriteFragment)
                    previousSelectedItemId = it.itemId
                    title = getString(R.string.title_favorite)
                }

                R.id.action_map -> {
                    switchFragment(mapFragment)
                    previousSelectedItemId = it.itemId
                    title = getString(R.string.title_map)
                }
            }
            true
        }
    }

    private fun createPostDialog() {
        val items = arrayOf("Camera", "Gallery")

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.title_create_post))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        startTakePhoto()
                    }

                    1 -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        launcherIntentGallery.launch(intent)
                    }
                }
            }.setOnDismissListener {
                binding.mainBottomNav.selectedItemId = previousSelectedItemId
            }
            .show()
    }

    private fun startPostActivity(Uri: Uri) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra(PostActivity.EXTRA_POST_IMAGE, Uri.toString())
        startActivity(intent)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        FileUtils.createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@MainActivity,
                "com.itstor.pictale",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startPostActivity(Uri.fromFile(File(currentPhotoPath)))
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            startPostActivity(selectedImg)
        }
    }

    private fun switchFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().apply {
            // hide current fragment
            supportFragmentManager.fragments.forEach {
                if (it.isVisible) hide(it)
            }
            if (fragment.isAdded) show(fragment)
            else add(binding.mainFrame.id, fragment)
            commit()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}