package com.egco428.sendfireimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        selectImage.setOnClickListener(this)
        uploadBtn.setOnClickListener(this)
    }

    override fun onClick(event: View?) {
        if(event==selectImage){
            showImage()
        }
        else if (event==uploadBtn){
            uploadImage()
        }

    }

    private fun showImage(){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data !=null){
            filePath = data.data
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){
        if(filePath!=null){
            val imageReferece = storageReference!!.child("Images/"+ UUID.randomUUID().toString())
            imageReferece.putFile(filePath!!)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Upload Success",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Upload Failed",Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener {
                        Toast.makeText(this,"Uploading",Toast.LENGTH_SHORT).show()
                    }
        }
    }
}
