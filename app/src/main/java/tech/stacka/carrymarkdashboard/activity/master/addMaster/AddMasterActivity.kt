package tech.stacka.carrymarkdashboard.activity.master.addMaster

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_master.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.io.File

class AddMasterActivity : AppCompatActivity(), AddMasterView {
    private var imgUrisOne: Uri? = null
    var UPLOAD_IMAGE_VALUE:Int =0
    var arrImageData= JsonArray()
    val path: ArrayList<File> = ArrayList()
    var strToken:String = ""
    var strImageData:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_master)
        val presenter=AddMasterPresenter(this,this)
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        btUploadImageCategory.setOnClickListener {
            pbUploadImageCategory.visibility=View.VISIBLE
            pbUploadImageCategory.bringToFront()
            if(Utilities.checkInternetConnection(this)) {
                presenter.uploadImage(path, strToken)
            }else{
                pbUploadImageCategory.visibility=View.GONE
                Snackbar.make(it!!,"check your internet connection", Snackbar.LENGTH_LONG).show()
            }
        }

        btAddCategory.setOnClickListener {

            val strMainCategory = sp_master_categorry.selectedItem.toString().trim()
            val strMasterName = etAddMaster.text.toString().trim()

            if(strMainCategory.equals("Select Master Category *")){
                Toast.makeText(applicationContext,"please select a master category",Toast.LENGTH_SHORT).show()
            }else{
                if (strMasterName.isEmpty()) {
                    etAddMaster.error = "Master name required*"
                    etAddMaster.requestFocus()
                    return@setOnClickListener
                }
                if(UPLOAD_IMAGE_VALUE!=1){
                    Toast.makeText(applicationContext,"please upload an Image",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(Utilities.checkInternetConnection(this)) {
                    presenter.createMaster(strToken,strMainCategory,strMasterName,strImageData)
                }else{
                    Snackbar.make(it!!,"check your internet connection", Snackbar.LENGTH_LONG).show()
                }


            }




        }

    }

    fun setCategoryImage(view: View) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val imgURI: Uri = result.uri
                        imgUrisOne = imgURI
                        Glide.with(this).load(imgURI).into(ivAddMaster)
                        val file1 = File(imgURI.path!!)
                        path.add(file1)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun uploadImageSuccess(apiResponse: UploadProductImageResponse) {
        pbUploadImageCategory.visibility=View.GONE
        UPLOAD_IMAGE_VALUE=1
//        strImageData=apiResponse.strImgUrl_0
//        objImageData.addProperty("strImgUrl_0",apiResponse.strImgUrl_0)
        strImageData=apiResponse.arrImageUrl.get(0).toString()
        ivSuccess.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)

    }

    override fun uploadImageNull(apiResponse: UploadProductImageResponse) {
        pbUploadImageCategory.visibility=View.GONE
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun uploadImageFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()
        pbUploadImageCategory.visibility=View.GONE

    }

    override fun uploadImageFailedServerError(toString: String) {
        Toast.makeText(applicationContext,toString.toString(),Toast.LENGTH_SHORT).show()
        pbUploadImageCategory.visibility=View.GONE

    }

    override fun addMasterSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun addMasterNull(apiResponse: DefaultResponse) {
        TODO("Not yet implemented")
    }

    override fun addMasterFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun addMasterFailedServerError(toString: String) {
        Toast.makeText(applicationContext,toString,Toast.LENGTH_SHORT).show()
    }
}