package tech.stacka.carrymarkdashboard.activity.master.addMaster

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_master.*
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.AddProductCategoryResponse
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse
import tech.stacka.carrymarkdashboard.models.data.ArrAddProductCategory
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.Utilities
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.io.File

class AddMasterActivity : AppCompatActivity(), AddMasterView {
    private var imgUrisOne: Uri? = null
    var UPLOAD_IMAGE_VALUE:Int =0
    var arrImageData= JsonArray()
    val path: ArrayList<File> = ArrayList()
    var strToken:String = ""
    var strCategoryParent:String = ""
    var strImageData:String = ""
    var SUBCATEGORY_SELECTTED:Int=0
    var color: Int = -0x100
    val presenter=AddMasterPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_master)
        nav_back.setOnClickListener {
            finish()
        }

        sp_master_categorry.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                if(position==1){
                    btColorPick.visibility=View.GONE
                    btUploadImageCategory.visibility=View.VISIBLE
                    ivAddMaster.visibility=View.VISIBLE
                    ivSuccess.visibility=View.VISIBLE
                    autotvListMainCategory.visibility=View.GONE
                    SUBCATEGORY_SELECTTED=0
                    strCategoryParent="cln_brand"
                }
                if(position==2){
                    btColorPick.visibility=View.GONE
                    btUploadImageCategory.visibility=View.VISIBLE
                    ivSuccess.visibility=View.VISIBLE
                    ivAddMaster.visibility=View.VISIBLE
                    autotvListMainCategory.visibility=View.GONE
                    SUBCATEGORY_SELECTTED=0
                    strCategoryParent="cln_category"
                }
                if(position==3){
                    btColorPick.visibility=View.GONE
                    btUploadImageCategory.visibility=View.VISIBLE
                    ivSuccess.visibility=View.VISIBLE
                    ivAddMaster.visibility=View.VISIBLE
                    autotvListMainCategory.visibility=View.GONE
                    SUBCATEGORY_SELECTTED=0
                    strCategoryParent="cln_material"
                }
                if(position==4){
                    btColorPick.visibility=View.GONE
                    btUploadImageCategory.visibility=View.VISIBLE
                    ivSuccess.visibility=View.VISIBLE
                    ivAddMaster.visibility=View.VISIBLE
                    autotvListMainCategory.visibility=View.GONE
                    SUBCATEGORY_SELECTTED=0
                    strCategoryParent="cln_size"
                }
                if(position==5){
                    btColorPick.visibility=View.VISIBLE
                    btUploadImageCategory.visibility=View.GONE
                    ivSuccess.visibility=View.GONE
                    ivAddMaster.visibility=View.GONE
                    autotvListMainCategory.visibility=View.GONE
                    SUBCATEGORY_SELECTTED=0
                    strCategoryParent="cln_color"
                }
                if(position==6){
                    btColorPick.visibility=View.GONE
                    btUploadImageCategory.visibility=View.VISIBLE
                    ivSuccess.visibility=View.VISIBLE
                    ivAddMaster.visibility=View.VISIBLE
                    autotvListMainCategory.visibility=View.GONE
                    SUBCATEGORY_SELECTTED=0
                    strCategoryParent="cln_location"
                }
                if(position==7){
                    btColorPick.visibility=View.GONE
                    btUploadImageCategory.visibility=View.VISIBLE
                    ivSuccess.visibility=View.VISIBLE
                    ivAddMaster.visibility=View.VISIBLE
                    strCategoryParent="cln_sub_category"
                    autotvListMainCategory.visibility=View.VISIBLE
                    SUBCATEGORY_SELECTTED=1
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

        btColorPick.setOnClickListener {

        openDialog(false)
        }



        autotvListMainCategory.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(autotvListMainCategory!=null){
                    val strValue:String=autotvListMainCategory.text.toString()
                    val strCollection:String="cln_category"
                    presenter.categoryList(strCollection,strValue,strToken)

                }
            }
        })
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
                if(strMainCategory.equals("Color")){
                    if (Utilities.checkInternetConnection(this)) {
                        presenter.createMaster(
                            strToken,
                            strCategoryParent,
                            strMasterName,
                            strImageData,"",String.format("%08x", color)
                        )
                    } else {
                        Snackbar.make(it!!, "check your internet connection", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }else{

                    if(UPLOAD_IMAGE_VALUE!=1){
                        Toast.makeText(applicationContext,"please upload an Image",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if(SUBCATEGORY_SELECTTED==1){
                        var strCategory=autotvListMainCategory.text.toString().trim()
                        if(Utilities.checkInternetConnection(this)) {
                            presenter.createMaster(strToken,strCategoryParent,strMasterName,strImageData,strCategory,"")
                        }else{
                            Snackbar.make(it!!,"check your internet connection", Snackbar.LENGTH_LONG).show()
                        }
                    }else {
                        if (Utilities.checkInternetConnection(this)) {
                            presenter.createMaster(
                                strToken,
                                strCategoryParent,
                                strMasterName,
                                strImageData,"",
                                ""
                            )
                        } else {
                            Snackbar.make(it!!, "check your internet connection", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
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

    }

    override fun addMasterFailed(apiResponse: JSONArray) {
        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                if(i=="ITEM NAME ALREADY EXIST"){
                    etAddMaster.error = "ITEM NAME ALREADY EXIST *"
                    etAddMaster.requestFocus()
                }
            }
        }
        //Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun addMasterFailedServerError(toString: String) {
        Toast.makeText(applicationContext,toString,Toast.LENGTH_SHORT).show()
    }

    override fun onMasterCategoryListSuccess(apiResponse: AddProductCategoryResponse) {

        var categoryData= apiResponse.arrList as ArrayList<ArrAddProductCategory>
        val list: MutableList<String> = ArrayList()
        for(i in categoryData){ list.add(i.strName) }

        val acTextView = findViewById(R.id.autotvListMainCategory) as AutoCompleteTextView
        acTextView.threshold = 1
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,list)
        acTextView.setAdapter(arrayAdapter)
    }

    override fun onMasterCategoryListNull(apiResponse: AddProductCategoryResponse) {

    }

    override fun onMasterCategoryListFailed(apiResponse: ResponseBody) {

    }

    override fun onMasterCategoryListFailedServerError(string: String) {

    }
    fun openDialog(supportsAlpha: Boolean) {
        val dialog = AmbilWarnaDialog(this@AddMasterActivity, color, supportsAlpha,
            object : OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    Toast.makeText(applicationContext, "ok", Toast.LENGTH_SHORT).show()
                    this@AddMasterActivity.color = color
                    displayColor()
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                    Toast.makeText(applicationContext, "cancel", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        dialog.show()
    }


    fun displayColor() {
        btColorPick.setBackgroundColor(color)
        btColorPick.setText(String.format(" %08x", color))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}