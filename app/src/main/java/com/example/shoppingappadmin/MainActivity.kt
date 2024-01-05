package com.example.shoppingappadmin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingappadmin.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class MainActivity : AppCompatActivity() {

    // image picker hei ye

    val productModel = productModel()


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                // ye code mene kiya hei

                // animation ka code

                binding.mainLayout.visibility = View.GONE
                binding.spinKit.visibility = View.VISIBLE
                binding.bogusView1.visibility =
                    View.VISIBLE  // ye bogus view isliye lagaye hei tanki vo animation centre mei show ho
                binding.bogusView2.visibility = View.VISIBLE
                binding.mainLayout.setBackgroundColor(Color.LTGRAY);


                // upload kerne ka code


                Firebase.storage.reference.child("productImage/${UUID.randomUUID()}")
                    .putFile(fileUri).addOnCompleteListener {
                        // isme hamne UUID use ker hei kyunki image ka name har bar different chahiye and uske liye ye hoga ye humesha ek lambi si string generate ker dega

                        if (it.isSuccessful) {
                            // get the URL of the image;

                            it.result.storage.downloadUrl.addOnSuccessListener {
                                productModel.imageURL = it.toString()


                                // jab mere pass successfully URL aa jaye tab ye kam ho


                                // yha hamne image ko placeholder ( app mei) show karwa diya
                                binding.productImage.setImageURI(fileUri)


                                // success hone per ham visibility ulta ker denge


                                binding.mainLayout.visibility = View.VISIBLE
                                binding.spinKit.visibility = View.GONE
                                binding.bogusView1.visibility =
                                    View.GONE  // ye bogus view isliye lagaye hei tanki vo animation centre mei show ho
                                binding.bogusView2.visibility = View.GONE
                                binding.mainLayout.setBackgroundColor(Color.WHITE);
                            }


                        }
                    }


                // yha tak

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }




    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        binding.productImage.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }





        binding.addProduct.setOnClickListener {
            if (binding.productName.text.isEmpty()) {
                binding.productName.setError("This Feild can't remain empty!");
            } else if (binding.productPrice.text.isEmpty()) {
                binding.productPrice.setError("This Feild can't remain empty!");
            } else if (binding.productDisp.text.isEmpty()) {
                binding.productDisp.setError("This Feild can't remain empty!");
            } else if (productModel.imageURL.isEmpty()) {
                Toast.makeText(this, "Upload an Image!", Toast.LENGTH_SHORT).show()
            } else {
                productModel.name = binding.productName.text.toString()
                productModel.catagory = binding.productCatagory.text.toString()
                productModel.price = binding.productPrice.text.toString()
                productModel.disp = binding.productDisp.text.toString()
                Toast.makeText(this, "han ho gya ", Toast.LENGTH_SHORT).show()
                Firebase.firestore.collection("products").document(UUID.randomUUID().toString())
                    .set(productModel).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        binding.rcv.layoutManager = layoutManager


    }
}