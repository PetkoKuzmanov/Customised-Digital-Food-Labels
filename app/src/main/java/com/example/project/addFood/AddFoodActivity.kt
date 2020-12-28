package com.example.project.addFood

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFoodActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val textRecoReqCode = 100
    private val barcodeRecoReqCode = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolBar))

    }

    fun quickAdd(view: View) {
        val inflater = LayoutInflater.from(this)
        val quickAddView = inflater.inflate(R.layout.quick_add_layout, null)
        val enterCalories = quickAddView.findViewById(R.id.enterCalories) as EditText
        val section = intent.getStringExtra("section").toString()

        database = Firebase.database.reference

        //Set the current date as the date
        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")

        val formattedDate = current.format(formatterDate)
        val formattedTime = current.format(formatterTime)

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(quickAddView)
            .setTitle("Quick add")
        builder.create()
        builder.setPositiveButton("ADD") { _, _ ->
            val input = enterCalories.text.toString()


            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
//                mAdapter.addItem(weight, dateTextView.text.toString())
//                mAuth.currentUser?.let {
//                    database.child("users").child(it.uid).child(formattedDate).child("diary")
//                        .child(section).child(formattedTime).child("id").setValue(id)
//                }
                mAuth.currentUser?.let {
                    database.child("users").child(it.uid).child(formattedDate).child("diary")
                        .child(section).child("calories-$formattedTime").setValue(input)
                }
                this.onBackPressed()
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    fun scanItem(view: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, barcodeRecoReqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == textRecoReqCode) {
            when (resultCode) {
                RESULT_OK -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    textRecognition(photo)
                }
                RESULT_CANCELED -> {
                    println("Operation cancelled by user")
                }
                else -> {
                    println("Failed to capture image")
                }
            }
        }

        if (requestCode == barcodeRecoReqCode) {
            if (resultCode == RESULT_OK) {
                val photo = data?.extras?.get("data") as Bitmap
                barcodeRecognition(photo)
            }
        }
    }

    private fun barcodeRecognition(photo: Bitmap) {
        val image = InputImage.fromBitmap(photo, 0)
        val scanner = BarcodeScanning.getClient()

        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                if (barcodes.size == 0) {
                    println("WWWWWWWWWWWWWW")

                } else {
                    for (barcode in barcodes) {
                        val bounds = barcode.boundingBox
                        val corners = barcode.cornerPoints

                        val rawValue = barcode.rawValue
                        println("AAAAAAAAAAAAAAAAAAAAAAAAAA" + rawValue)

                    }
                }
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
                println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")
            }
    }

    private fun textRecognition(photo: Bitmap) {

    }
}