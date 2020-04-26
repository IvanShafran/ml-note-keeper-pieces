package com.github.ivanshafran.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), ImageCapture.OnImageSavedCallback {

    companion object {
        private const val CAMERA_REQUEST_CODE = 0
    }

    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    Toast.makeText(this, R.string.need_permission, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.permission_in_settings, Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startCamera() {
        camera.captureMode = CameraView.CaptureMode.IMAGE
        camera.bindToLifecycle(this as LifecycleOwner)
        val photoFile = generatePictureFile()
        this.photoFile = photoFile
        takePhotoButton.setOnClickListener {
            camera.takePicture(
                photoFile,
                AsyncTask.SERIAL_EXECUTOR,
                this
            )
        }
    }

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        val photoFile = photoFile ?: return
        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
    }

    override fun onError(exception: ImageCaptureException) {
    }

    private fun generatePictureFile(): File {
        return File(filesDir, UUID.randomUUID().toString())
    }

}
