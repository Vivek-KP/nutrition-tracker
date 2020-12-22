package com.example.nutrition.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nutrition.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_camera.*
import org.w3c.dom.Text
import java.io.File
import java.util.jar.Manifest

private const val REQUEST_CODE = 42
private const val FILE_NAME = "photo.jpg"
private lateinit var photoFile: File

class CameraFragment : Fragment() {

    val localModel = AutoMLImageLabelerLocalModel.Builder()
        .setAssetFilePath("mode/manifest.json")
        .build()
    val autoMLImageLabelerOptions = AutoMLImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.0F)
        .build()
    val labeler = ImageLabeling.getClient(autoMLImageLabelerOptions)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_camera, container, false)
        val choose: Button = view.findViewById(R.id.button)
        val localModel = AutoMLImageLabelerLocalModel.Builder()
            .setAssetFilePath("mode/manifest.json")
            .build()
        val autoMLImageLabelerOptions = AutoMLImageLabelerOptions.Builder(localModel)
            .setConfidenceThreshold(0.0F)
            .build()
        val labeler = ImageLabeling.getClient(autoMLImageLabelerOptions)

        choose.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            val fileProvider =
                FileProvider.getUriForFile(
                    activity!!,
                    "com.example.nutrition.fileprovider",
                    photoFile
                )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (intent.resolveActivity(activity!!.packageManager) != null) {
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                Toast.makeText(activity!!, "unable to open", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val taken = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView2.setImageBitmap(taken)
            val image = InputImage.fromBitmap(taken, 0)
            val result: TextView = activity!!.findViewById(R.id.textView)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        result.append(text + "  " + confidence)
                    }
                }
                .addOnFailureListener { e ->
                }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}


