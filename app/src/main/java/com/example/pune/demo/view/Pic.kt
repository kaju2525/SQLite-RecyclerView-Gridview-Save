package com.example.pune.demo.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.pune.demo.R
import com.example.pune.demo.db_optration.MyDB
import com.example.pune.demo.model.DataModel
import kotlinx.android.synthetic.main.pic_layout.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Pic : AppCompatActivity() {

    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private lateinit var bitmap: Bitmap
    private lateinit var db: MyDB

    companion object {
        private const val IMAGE_DIRECTORY = "/storage/sdcard0/MyEmployee"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pic_layout)

        db = MyDB(this)


        imageview = findViewById<View>(R.id.iv) as ImageView

        imageview!!.setOnClickListener {
            showPictureDialog()
        }


        btn_save.setOnClickListener {

            val name = edit_name.text.toString()
            val path = saveImage(bitmap)

            Log.d("TAG", "File Saved path ::--->$path")

           val i = db.AddData(DataModel(1, name, path))
            if(i) {
                Handler().postDelayed({
                    Toast.makeText(this@Pic, "Image Saved!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                },2000)


            }else
            Toast.makeText(this@Pic, "Image Not Saved!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                    //  Toast.makeText(this@Pic, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview!!.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@Pic, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {

            bitmap = data!!.extras!!.get("data") as Bitmap
            imageview!!.setImageBitmap(bitmap)


        }
    }

    private fun saveImage(myBitmap: Bitmap): String {

        val bytes = ByteArrayOutputStream()
        getResizedBitmap(myBitmap,400).compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        val onlyTimeStamp=(Calendar.getInstance().timeInMillis).toString()

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ("$onlyTimeStamp.jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            Log.d("TAG", "File Saved onlyTimeStamp ::--->$onlyTimeStamp")

           // return f.absolutePath
            return onlyTimeStamp
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio < 1 && width > maxSize) {

            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else if (height > maxSize) {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

}