package com.mohammadmawed.textrecogntion

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class MainActivity : AppCompatActivity() {

    var imageView: ImageView? = null
    var button: Button? = null
    var textView: TextView? = null
    var progressBar: ProgressBar? = null
    var ImageUri: Uri? = null
    var selectedImage: Bitmap? = null

    private val PICK_IMAGE_REQUEST = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.textView2)

       imageView?.setOnClickListener( View.OnClickListener {
           chooseImage()
           button?.isEnabled = true
       })

        button?.setOnClickListener( View.OnClickListener {
            progressBar?.visibility = View.VISIBLE
            processImage()
        })

    }

    private fun processImage() {
        val inputImage: InputImage = InputImage.fromBitmap(selectedImage!!, 0)
        val recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage)
            .addOnSuccessListener {
                progressBar?.visibility = View.GONE
                textView?.text = it.text
                Log.d("Text", "Secc")
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }


    private fun chooseImage() {
        //Open the gallery
        val oenGalleryIntent = Intent()
        oenGalleryIntent.type = "image/*"
        oenGalleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(oenGalleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            assert(data != null)
            ImageUri = data!!.data
            imageView?.setImageURI(ImageUri)
            selectedImage = MediaStore.Images.Media.getBitmap(this.contentResolver, ImageUri)

        }
    }
}
