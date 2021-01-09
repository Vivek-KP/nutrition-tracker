package com.example.nutrition.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nutrition.R
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.nutrition.HitsItem
import com.example.nutrition.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE = 42
private const val REQUEST_CODE_G = 43
private const val FILE_NAME = "photo.jpg"
private lateinit var photoFile: File
lateinit var mDatabase: DatabaseReference

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
        val gChoose: Button = view.findViewById(R.id.gButton)
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
                Toast.makeText(activity!!, "Unable to open", Toast.LENGTH_SHORT).show()
            }
        }

        gChoose.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_G)
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
            img_food.setImageBitmap(taken)
            val image = InputImage.fromBitmap(taken, 0)
            val result: TextView = activity!!.findViewById(R.id.txt_food)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        result.append(text + "  " + confidence)
                    }

                    val foodItem = result.getText().substring(0, result.getText().indexOf(" "))
                    if (foodItem.contains("_")) {
                        foodItem.replace("_", "")
                    }

                    result.setText("")

                    val request = Request.Builder()
                        .url("https://api.nutritionix.com/v1_1/search/" + foodItem + "?results=0:1&fields=item_name,nf_calories,nf_total_fat,nf_saturated_fat,nf_cholesterol,nf_total_carbohydrate,nf_protein,nf_sodium,nf_sugars&appId=ff63be37&appKey=d6e40fb0375d22e831782c05f672aac1")
                        .build()

                    val api: Call = OkHttpClient().newCall(request)

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO) { api.execute() }
                        val data = Gson().fromJson(
                            response.body?.string(), Response::class.java
                        )
                        launch(Dispatchers.Main) {
                            bindFoodData(data.hits)
                        }
                    }

                }

                .addOnFailureListener { e ->
                }
        } else if (requestCode == REQUEST_CODE_G && resultCode == Activity.RESULT_OK) {
            val gTaken = data?.data
            img_food.setImageURI(gTaken)
            val gImage = InputImage.fromFilePath(activity!!, gTaken!!)
            val gResult: TextView = activity!!.findViewById(R.id.txt_food)

            labeler.process(gImage)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        gResult.append(text + "  " + confidence)
                    }

                    val foodItem = gResult.getText().substring(0, gResult.getText().indexOf(" "))
                    if (foodItem.contains("_")) {
                        foodItem.replace("_", "")
                    }

                    gResult.setText("")

                    val request = Request.Builder()
                        .url("https://api.nutritionix.com/v1_1/search/" + foodItem + "?results=0:1&fields=item_name,nf_calories,nf_total_fat,nf_saturated_fat,nf_cholesterol,nf_total_carbohydrate,nf_protein,nf_sodium,nf_sugars&appId=ff63be37&appKey=d6e40fb0375d22e831782c05f672aac1")
                        .build()

                    val api: Call = OkHttpClient().newCall(request)

                    GlobalScope.launch {
                        val response = withContext(Dispatchers.IO) { api.execute() }
                        val data = Gson().fromJson(
                            response.body?.string(), Response::class.java
                        )
                        launch(Dispatchers.Main) {
                            bindFoodData(data.hits)
                        }
                    }
                }

                .addOnFailureListener { e ->
                }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun bindFoodData(hits: List<HitsItem?>?) {
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("Food_Items")
        mDatabase.child(currentDate).setValue(hits?.get(0)?.fields)
        txt_food.setText(hits?.get(0)?.fields?.itemName)
    }

}