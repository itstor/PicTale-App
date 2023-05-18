package com.itstor.pictale.ui.views.post

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.itstor.pictale.R
import com.itstor.pictale.common.Status
import com.itstor.pictale.databinding.ActivityPostBinding
import com.itstor.pictale.utils.FileUtils.Companion.uriToFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPostBinding
    private var currentPhotoPath: String? = null
    private lateinit var mMap: GoogleMap
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentPhotoPath = intent.getStringExtra(EXTRA_POST_IMAGE)

        if (currentPhotoPath == null) {
            Toast.makeText(this, getString(R.string.msg_no_image), Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.ivPostPreview.setImageURI(currentPhotoPath?.toUri())
        binding.mapCard.visibility = View.GONE

        setUpMap()
        setUpToolbar()
        setUpStatusBar()
        listenUploadButton()
        listenLocationToggle()
    }

    private fun listenLocationToggle() {
        binding.switchShareLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
                binding.mapCard.visibility = View.VISIBLE
            } else {
                binding.mapCard.visibility = View.GONE
                mMap.clear()
            }
        }
    }

    private fun setUpMap() {
        val mapFragment = binding.postMap.getFragment<SupportMapFragment>()
        mapFragment.getMapAsync(this)
    }

    private fun listenUploadButton() {
        binding.buttonAdd.setOnClickListener {
            val caption = binding.edAddDescription.text.toString()
            if (caption.isEmpty()) {
                showToast(getString(R.string.msg_caption_empty))
                return@setOnClickListener
            }
            val imageFile = uriToFile(currentPhotoPath?.toUri()!!, this)

            val isLocationShared = binding.switchShareLocation.isChecked

            val lat = if (isLocationShared) mMap.cameraPosition.target.latitude.toFloat() else null
            val lng = if (isLocationShared) mMap.cameraPosition.target.longitude.toFloat() else null

            viewModel.uploadStory(imageFile, caption, lat, lng).observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.buttonAdd.isEnabled = false
                        showToast(getString(R.string.msg_uploading))
                    }

                    Status.SUCCESS -> {
                        binding.buttonAdd.isEnabled = true
                        showToast(getString(R.string.msg_post_uploaded))
                        finish()
                    }

                    Status.FAILURE -> {
                        binding.buttonAdd.isEnabled = true
                        Log.d(TAG, "Upload Error: ${it.message}")
                        showToast(it.message ?: getString(R.string.msg_something_went_wrong))
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpStatusBar() {
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.new_post)

        val materialShapeDrawable = binding.mainToolbar.background as MaterialShapeDrawable
        materialShapeDrawable.shapeAppearanceModel = materialShapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 64f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 64f)
            .build()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isScrollGesturesEnabled = false
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val markerOptions = MarkerOptions().position(currentLatLng).title("My Location")
                    mMap.addMarker(markerOptions)

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    companion object {
        const val EXTRA_POST_IMAGE = "postImage"
        const val TAG = "PostActivity"
    }
}