package com.example.massigym_android.ui.workout

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityWorkoutVideoBinding
import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.widget.Toast


class WorkoutVideo : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutVideoBinding

    private var videoUrl: String? = null

    private lateinit var workoutName: String

    private val STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutVideoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        videoUrl = intent.getStringExtra("videoUrl")
        workoutName = intent.getStringExtra("name")!!

        binding.toolbarWorkoutVideo.setNavigationOnClickListener { onBackPressed() }

        binding.toolbarWorkoutVideo.setTitle("$workoutName - Video")

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.workoutVideoView)
        val uri = Uri.parse(videoUrl)
        binding.workoutVideoView.setMediaController(mediaController)
        binding.workoutVideoView.setVideoURI(uri)
        binding.workoutVideoView.requestFocus()
        binding.workoutVideoView.start()

        binding.downloadVideoButton.setOnClickListener {
            // handle runtime permission for WRITE_EXTERNAL_STORAGE, if system OS >= Marshmallow
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission((Manifest.permission.WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_DENIED) {
                    // permission denied, request it

                    // show popup for runtime permission
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE)
                } else {
                    // permission already granted, perform download
                    startDownloading()
                }
            } else {
                // system os is less than marshmallow, runtime permission not required, perform download
                startDownloading()
            }
        }
    }

    private fun startDownloading() {
        val request = DownloadManager.Request(Uri.parse(videoUrl))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("${workoutName}.mp4")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility((DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED))
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${workoutName}.mp4")

        //get download service, and enqueue file
        val manager = getSystemService((Context.DOWNLOAD_SERVICE)) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup was granted, perform download
                    startDownloading()
                } else {
                    // permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}