package tech.stacka.carrymarkdashboard.activity.product.productUpdate


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

import kotlinx.android.synthetic.main.activity_add_master.*
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_update.*
import kotlinx.android.synthetic.main.activity_product_update.etCGST
import kotlinx.android.synthetic.main.activity_product_update.etKeyword
import kotlinx.android.synthetic.main.activity_product_update.etSGST
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity

import tech.stacka.carrymarkdashboard.models.*
import tech.stacka.carrymarkdashboard.models.data.ArrAddProductCategory
import tech.stacka.carrymarkdashboard.models.data.ArrScheme
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.io.File

class ProductUpdateActivity : AppCompatActivity(), ProductUpdateView {

    var arrScheme=JsonArray()
    private val arrSizeList = ArrayList<String>()
    private val arrColorList = ArrayList<String>()
    private val arrColorCodeList = ArrayList<String>()
    private var arrSelectedSize: MutableList<String> = ArrayList()
    private var arrSelectedColor: MutableList<String> = ArrayList()
    private var arrSelectedColorCode: MutableList<String> = ArrayList()
    var strMaincategory: String = ""
    private var arrPassingColor = JsonArray()
    private var pos = 0
    private var imgUrisOne: Uri? = null
    private var imgUrisTwo: Uri? = null
    private var imgUrisThree: Uri? = null
    var UPLOAD_IMAGE_VALUE: Int = 0
    var CATEGORY_TYPE: Int = 0
    var IMAGE_CHANGED=false
    private var arrImageData = JsonArray()
    private var arrImageDataNew = JsonArray()
    private var arrImageUrlData = ArrayList<String>()
    var arrImagePos = BooleanArray(10)
    val path: ArrayList<File> = ArrayList()
    val list: MutableList<String> = ArrayList()
    var strToken: String = ""
    var strProductId: String = ""
    var dblTotalSale=0
    var dblTotalDiscounts=0.0
    var dblSGST=0.0
    var dblCGST=0.0
    private var categoryList = ArrayList<String>()
    var strValueBrand:String=""
    var strValueCategory:String=""
    var strValueMaterial:String=""
    var strValueSubCategory:String=""
    var listBrand: MutableList<String> = ArrayList()
    var listCategory: MutableList<String> = ArrayList()
    var listSubCategory: MutableList<String> = ArrayList()
    var listMaterial: MutableList<String> = ArrayList()
    val presenter = ProductUpdatePresenter(this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_update)

        strToken = SharedPrefManager.getInstance(applicationContext).user.strToken!!
        categoryList.add("cln_size")
        categoryList.add("cln_color")
        strProductId = intent.getStringExtra("productId")
        nav_back.setOnClickListener {
            startActivity(Intent(this@ProductUpdateActivity,ProductListActivity::class.java))
            finish()
        }
        if (Utilities.checkInternetConnection(this)) {
            presenter.productDetails(strToken, strProductId)
        } else {
            pbUploadImageCategory.visibility = View.GONE

        }

        ibAddNewScheme1Update.setOnClickListener {
            trSchemeTwoUpdate.visibility=View.VISIBLE
            ibAddNewScheme1Update.visibility=View.GONE
        }

        ibAddNewScheme2Update.setOnClickListener {
            trSchemeThreeUpdate.visibility=View.VISIBLE
            ibAddNewScheme2Update.visibility=View.GONE
        }
        ibAddNewScheme3Update.setOnClickListener {
            trScheme4Update.visibility=View.VISIBLE
            ibAddNewScheme3Update.visibility=View.GONE
        }
        ibAddNewScheme4Update.setOnClickListener {
            trScheme5Update.visibility=View.VISIBLE
            ibAddNewScheme4Update.visibility=View.GONE
        }


        bt_upload_image_detail.setOnClickListener {
            pbUploadImageDetail.visibility = View.VISIBLE
            pbUploadImageDetail.bringToFront()
            if (Utilities.checkInternetConnection(this)) {
                presenter.uploadImage(path, strToken)
            } else {
                pbUploadImageDetail.visibility = View.GONE
                Snackbar.make(it!!, "check your internet connection", Snackbar.LENGTH_LONG).show()
            }
        }

        btUpdateProduct.setOnClickListener {
            if (UPLOAD_IMAGE_VALUE == 1) {
                UpdateProductClick(it)
            } else {
                Snackbar.make(it!!, "Upload one image", Snackbar.LENGTH_LONG).show()
            }
        }


        et_brandDetail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (et_brandDetail != null) {
                    strValueBrand = et_brandDetail.text.toString()
                    val strCollectionBrand: String = "cln_brand"
                    presenter.categoryList(strCollectionBrand, strValueBrand, strToken)
                    CATEGORY_TYPE = 1;

                }
            }
        })

        et_categoryDetail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (et_categoryDetail != null) {
                    strValueCategory= et_categoryDetail.text.toString()
                    val strCollectionCategory: String = "cln_category"
                    presenter.categoryList(strCollectionCategory, strValueCategory, strToken)
                    CATEGORY_TYPE = 2;

                }
            }
        })

        et_categoryDetail.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                strMaincategory = parent.getItemAtPosition(position).toString()
                et_subcategoryDetail.visibility = View.VISIBLE
                // Toast.makeText(applicationContext,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
            }


        et_subcategoryDetail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (et_categoryDetail != null) {
                    strValueSubCategory= et_subcategoryDetail.text.toString()
                    val strCollectionSubCategory: String = "cln_sub_category"
                    presenter.subCategoryList(
                        strCollectionSubCategory,
                        strValueSubCategory,
                        strToken,
                        strMaincategory
                    )
                    CATEGORY_TYPE = 4;

                }
            }
        })


        etProductMaterialDetail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (etProductMaterialDetail != null) {
                    strValueMaterial= etProductMaterialDetail.text.toString()
                    val strCollectionMaterial: String = "cln_material"
                    presenter.categoryList(strCollectionMaterial, strValueMaterial, strToken)
                    CATEGORY_TYPE = 3

                }
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val imgURI: Uri = result.uri
                when (pos) {
                    0 -> {
                        imgUrisOne = imgURI
                        Glide.with(this).load(imgURI).into(ivMainDetail)
                        Glide.with(this).load(imgURI).into(ivThumb1Detail)
                        val file1 = File(imgURI.path!!)
                        arrImagePos[0] = true
                        path.add(file1)

                    }
                    1 -> {
                        imgUrisTwo = imgURI
                        Glide.with(this).load(imgURI).into(ivThumb2Detail)
                        val file2 = File(imgURI.path!!)
                        arrImagePos[1] = true
                        path.add(file2)
                    }
                    2 -> {
                        imgUrisThree = imgURI
                        Glide.with(this).load(imgURI).into(ivThumb3Detail)
                        var file3 = File(imgURI.path!!)
                        arrImagePos[2] = true
                        path.add(file3)
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    fun setMainDetailImage(view: View) {
        pos = 0
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
            .start(this)
    }

    fun setDetailImageTwo(view: View) {
        pos = 1
        if (!(imgUrisOne == null && arrImageUrlData[0].equals(""))) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
                .start(this)
        } else Snackbar.make(view, "Please set first image", Snackbar.LENGTH_SHORT).show()
    }

    fun setDetailImageThree(view: View) {
        pos = 2
        if (!(imgUrisTwo == null && arrImageUrlData[1].equals(""))) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
                .start(this)
        } else Snackbar.make(view, "Please set second image", Snackbar.LENGTH_SHORT).show()
    }


    override fun onProductDetailSuccess(apiResponse: ProductDetailResponse) {
        var apiData=apiResponse.arrImageUrl
        val genderList = listOf<String>("Select Gender *", "All", "Kids", "Women", "Men")
        dblTotalSale=apiResponse.dblTotalSales
        dblTotalDiscounts=apiResponse.dblTotalDiscounts
        dblCGST=apiResponse.dblCGST
        dblSGST=apiResponse.dblSGST
        etCGST.setText(apiResponse.dblCGST.toString())
        etSGST.setText(apiResponse.dblSGST.toString())
        etProductIdDetail.setText(apiResponse.strProductId)
        etProductTitleDetail.setText(apiResponse.strName)
        et_brandDetail.setText(apiResponse.strBrandId)
        et_categoryDetail.setText(apiResponse.strCategoryId)
        et_subcategoryDetail.setText(apiResponse.strSubCategory)
        etProductMaterialDetail.setText(apiResponse.strMaterial)
        etProductDescriptionDetail.setText(apiResponse.strDescription)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, genderList)
        sp_genderDetail.setSelection(arrayAdapter.getPosition(apiResponse.strGenderCategory))
        etSellingPriceDetail.setText(apiResponse.dblSellingPrice.toString())
        etKeyword.setText(apiResponse.strKeyWords);
        etMrpDetail.setText("${apiResponse.dblMRP}")
        etRetailerPriceDetail.setText("${apiResponse.dblRetailerPrice}")
        et_stockDetail.setText("${apiResponse.dblTotalStock}")
        var arrSchema=ArrayList<ArrScheme>()
        arrSchema=apiResponse.arrScheme as ArrayList<ArrScheme>
        if(arrSchema.size!=0){
            etSchemeOneUpdate.setText(arrSchema[0].intBuyNo)
            etOfferOneUpdate.setText(arrSchema[0].intGetNo)
            if(arrSchema.size>1){
                trSchemeTwoUpdate.visibility=View.VISIBLE
                etSchemeTwUpdate.setText(arrSchema[1].intBuyNo)
                etOfferTwUpdate.setText(arrSchema[1].intGetNo)
            }
            if(arrSchema.size>2){
                trSchemeThreeUpdate.visibility=View.VISIBLE
                etSchemeTrUpdate.setText(arrSchema[2].intBuyNo)
                etOfferTrUpdate.setText(arrSchema[2].intGetNo)
            }
            if(arrSchema.size>3){
                trScheme4Update.visibility=View.VISIBLE
                etSchemeFrUpdate.setText(arrSchema[3].intBuyNo)
                etOfferFrUpdate.setText(arrSchema[3].intGetNo)
            }
        }
      //  arrSchema=apiResponse.arrScheme

        UPLOAD_IMAGE_VALUE = 1
        for (i in arrImageData) {
            arrImageData.remove(i)
        }
//        for (i in apiResponse.arrImageUrl) {
//            arrImageData.add(i)
//        }

//        for (i in apiResponse.arrImageUrl) {
//            arrImageUrlData.add(i)
//        }
        arrImageUrlData.clear()
//        arrImageUrlData.add(apiResponse.arrImageUrl[0])
//
//        if(apiResponse.arrImageUrl.size>1){
//            arrImageUrlData.add(apiResponse.arrImageUrl[1])
//        }
//        if(apiResponse.arrImageUrl.size>2){
//            arrImageUrlData.add(apiResponse.arrImageUrl[2])
//        }
        for(i in apiResponse.arrImageUrl){
            arrImageUrlData.add(i)
        }

        for(i in arrImageUrlData){
            arrImageData.add(i)
        }

        Log.e("arrImageData",arrImageData.size().toString())
        Glide.with(this).load(apiResponse.arrImageUrl[0]).into(ivMainDetail)
        Glide.with(this).load(apiResponse.arrImageUrl[0].toString()).into(ivThumb1Detail)
        if (apiResponse.arrImageUrl.size > 1) {
            if (apiResponse.arrImageUrl[1] != "") Glide.with(this)
                .load(apiResponse.arrImageUrl[1]).into(ivThumb2Detail)
        }
        if (apiResponse.arrImageUrl.size > 2) {
            if (apiResponse.arrImageUrl.get(2) != "") Glide.with(this)
                .load(apiResponse.arrImageUrl[2]).into(ivThumb3Detail)
        }

        //Set Unit
        if (apiResponse.strUnit == "Qty") {
            rbItemDetail.isChecked = true
        } else if (apiResponse.strUnit == "Kg") {
            rbKgDetail.isChecked = true
        } else if (apiResponse.strUnit == "Liter") {
            rbLiterDetail.isChecked = true
        }
        //Set target
        if (apiResponse.strTargetType == "ALL") {
            chBtoBDetail.isChecked = true
            chBtoCDetail.isChecked = true
        } else if (apiResponse.strTargetType == "B2B") {
            chBtoBDetail.isChecked = true
            chBtoCDetail.isChecked = false
        } else if (apiResponse.strTargetType == "B2C") {
            chBtoBDetail.isChecked = false
            chBtoCDetail.isChecked = true
        }
        val arrSizeName = ArrayList<String>()
        if(apiResponse.arrSizeStock!=null){
            for (i in apiResponse.arrSizeStock) {
                arrSizeName.add(i.strName)
            }
        }


        val arrColorName = ArrayList<String>()

        if(apiResponse.arrColorStock!=null){
            for (k in apiResponse.arrColorStock) {
                arrColorName.add(k.strName)
            }
        }

        val strSizes: String = arrSizeName.joinToString(separator = ",")
        bt_sizeDetail.setText("Sizes - $strSizes")
        val strColors: String = arrColorName.joinToString(separator = ",")
        bt_colorDetail.setText("Colors - $strColors")

        presenter.addSizeColor(categoryList, 10)

    }

    override fun onProductDetailNull(apiResponse: ProductDetailResponse) {

    }

    override fun onProductDetailFailed(apiResponse: ResponseBody) {

    }

    override fun onProductDetailServerFailed(toString: String) {

    }

    override fun updateProductSuccess(apiResponse: AddProductResponse) {
        Toast.makeText(applicationContext, "Product Updated", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@ProductUpdateActivity, ProductListActivity::class.java))
        finish()
    }

    override fun updateProductNull(apiResponse: AddProductResponse) {
        Toast.makeText(applicationContext, apiResponse.toString(), Toast.LENGTH_LONG).show()
    }

    override fun updateProductFailed(apiResponse: JSONArray) {
        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                if(i=="PRODUCT NAME ALREADY EXIST"){
                    etProductTitleDetail.error = "PRODUCT ALREADY EXIST *"
                    etProductTitleDetail.requestFocus()
                    pbUdateProduct.visibility=View.GONE
                    btUpdateProduct.visibility=View.VISIBLE
                }else if(i=="PRODUCT ID ALREADY EXIST"){
                    etProductIdDetail.error = "PRODUCT ID ALREADY EXIST *"
                    etProductIdDetail.requestFocus()
                    pbUdateProduct.visibility=View.GONE
                    btUpdateProduct.visibility=View.VISIBLE
                }
                else if(i=="BRAND DOES NOT EXIST"){
                    et_brandDetail.error = "BRAND DOES NOT EXIST *"
                    et_brandDetail.requestFocus()
                    pbUdateProduct.visibility=View.GONE
                    btUpdateProduct.visibility=View.VISIBLE
                }
                else if(i=="CATEGORY DOES NOT EXIST"){
                    et_categoryDetail.error = "CATEGORY DOES NOT EXIST *"
                    et_categoryDetail.requestFocus()
                    pbUdateProduct.visibility=View.GONE
                    btUpdateProduct.visibility=View.VISIBLE
                }
                else if(i=="SUB CATEGORY DOES NOT EXIST"){
                    et_subcategoryDetail.error = "SUB CATEGORY DOES NOT EXIST *"
                    et_subcategoryDetail.requestFocus()
                    pbUdateProduct.visibility=View.GONE
                    btUpdateProduct.visibility=View.VISIBLE
                }
                else if(i=="MATERIAL DOES NOT EXIST"){
                    etProductMaterialDetail.error = "MATERIAL DOES NOT EXIST *"
                    etProductMaterialDetail.requestFocus()
                    pbUdateProduct.visibility=View.GONE
                    btUpdateProduct.visibility=View.VISIBLE
                }else{
                    Toast.makeText(this,apiResponse.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Toast.makeText(applicationContext, apiResponse.toString(), Toast.LENGTH_LONG).show()
    }

    override fun updateProductFailedServerError(toString: String) {
        Toast.makeText(applicationContext, toString, Toast.LENGTH_LONG).show()
    }

    override fun uploadImageSuccess(apiResponse: UploadProductImageResponse) {
        Log.e("arrImageDataNew",arrImageData.size().toString())
        Log.e("arrImageUrlDataNew",arrImageUrlData.size.toString())

       IMAGE_CHANGED=true
        UPLOAD_IMAGE_VALUE = 1
        for (imgUrl in apiResponse.arrImageUrl) {
            //arrImageUrlNewData.add(i)
            for (i in arrImagePos.indices) {
                if (arrImagePos[i]) {
                    if (arrImageUrlData.size<apiResponse.arrImageUrl.size)
                        arrImageUrlData.add(imgUrl)
                    else
                        arrImageUrlData[i] = imgUrl
                }
            }

            for(i in arrImageUrlData){
                arrImageDataNew.add(i)
            }
        }

        pbUploadImageDetail.visibility = View.GONE
        Toast.makeText(applicationContext, "Upload Success", Toast.LENGTH_SHORT).show()
    }

    override fun uploadImageNull(apiResponse: UploadProductImageResponse) {

    }

    override fun uploadImageFailed(apiResponse: ResponseBody) {
        pbUploadImageDetail.visibility = View.GONE
        Toast.makeText(applicationContext, "Please Choose different Image", Toast.LENGTH_SHORT)
            .show()
    }

    override fun uploadImageFailedServerError(toString: String) {
        pbUploadImageDetail.visibility = View.GONE

        Toast.makeText(applicationContext, "Network Problem", Toast.LENGTH_SHORT).show()
    }

    override fun onProductCategoryListSuccess(apiResponse: AddProductCategoryResponse) {
        var categoryData = apiResponse.arrList as ArrayList<ArrAddProductCategory>
        if (CATEGORY_TYPE == 1) {
            listBrand.clear()
            for (i in categoryData) { listBrand.add(i.strName) }
            val acTextView1 = findViewById<AutoCompleteTextView>(R.id.et_brandDetail)
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listBrand)
            acTextView1.setAdapter(arrayAdapter)
            acTextView1.showDropDown()
        }
        if (CATEGORY_TYPE == 2) {
            listCategory.clear()
            for (i in categoryData) { listCategory.add(i.strName) }
            val acTextView = findViewById<AutoCompleteTextView>(R.id.et_categoryDetail)
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listCategory)
            acTextView.setAdapter(arrayAdapter)
            acTextView.showDropDown()
        }

        if (CATEGORY_TYPE == 3) {
            listMaterial.clear()
            for (i in categoryData) { listMaterial.add(i.strName) }
            val acTextView = findViewById<AutoCompleteTextView>(R.id.etProductMaterialDetail)
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listMaterial)
            acTextView.setAdapter(arrayAdapter)
            acTextView.showDropDown()

        }

        if (CATEGORY_TYPE == 4) {
            listSubCategory.clear()
            for (i in categoryData) { listSubCategory.add(i.strName) }
            val acTextView = findViewById<AutoCompleteTextView>(R.id.et_subcategoryDetail)
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listSubCategory)
            acTextView.setAdapter(arrayAdapter)
            acTextView.showDropDown()
        }
    }

    override fun onProductCategoryListNull(apiResponse: AddProductCategoryResponse) {
        TODO("Not yet implemented")
    }

    override fun onProductCategoryListFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onProductCategoryListFailedServerError(string: String) {
        TODO("Not yet implemented")
    }

    override fun onAddSizeColorSuccess(apiResponse: AddSizeColorResponse) {

        // val categorymaterialList= ArrayList<String>()
        var i: Int = 0
        for (st in apiResponse.cln_size) {
            arrSizeList.add(apiResponse.cln_size.get(i).strName)
            i++
        }
        var k: Int = 0
        for (_id in apiResponse.cln_color) {
            arrColorList.add(apiResponse.cln_color.get(k).strName)
            arrColorCodeList.add(apiResponse.cln_color.get(k).strColorCode)
            k++
        }


        bt_sizeDetail.setOnClickListener {
            var checkedItems1: BooleanArray = BooleanArray(arrSizeList.size)
            var mUserItems1 = java.util.ArrayList<Int>()
            val list = arrSizeList.toTypedArray<CharSequence>()
            val selectedList = ArrayList<String>()
            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setTitle("Select Size")
            mBuilder.setMultiChoiceItems(
                list,
                checkedItems1,
                DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if (isChecked) {
                        mUserItems1.add(which)
                    } else {
                        mUserItems1.remove(Integer.valueOf(which))
                    }
                }
            )

            mBuilder.setCancelable(false)
            mBuilder.setPositiveButton("Apply",
                DialogInterface.OnClickListener { dialogInterface, which ->
                    var item = ""

                    for (i in mUserItems1.indices) {
                        arrSelectedSize.add(arrSizeList.get(mUserItems1[i]))
                        item += arrSizeList.get(mUserItems1[i])
                        //  listitems.add(list.get(mUserItems1.get(i)))
                        if (i != mUserItems1.size - 1) {
                            item = "$item, "
                        }
                    }
                    bt_sizeDetail.setText("Sizes : $item")
                })

            mBuilder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })

            mBuilder.setNeutralButton(
                "Clear",
                DialogInterface.OnClickListener { dialogInterface, which ->
                    for (i in checkedItems1.indices) {
                        checkedItems1[i] = false
                        mUserItems1.clear()
                    }
                    bt_sizeDetail.setText("Select Sizes")
                })

            val mDialog: AlertDialog = mBuilder.create()
            mDialog.show()
            val buttonbackground: Button = mDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonbackground.setBackgroundColor(Color.WHITE)
            buttonbackground.setTextColor(Color.BLACK)

            val buttonbackground1: Button = mDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            buttonbackground1.setBackgroundColor(Color.WHITE)
            buttonbackground1.setTextColor(Color.BLACK)

            val buttonbackground2: Button = mDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
            buttonbackground2.setBackgroundColor(Color.WHITE)
            buttonbackground2.setTextColor(Color.BLACK)
        }
        bt_colorDetail.setOnClickListener {
            lateinit var checkedItems2: BooleanArray
            checkedItems2 = BooleanArray(arrColorList.size)
            var mUserItems2 = java.util.ArrayList<Int>()
            val list = arrColorList.toTypedArray<CharSequence>()
            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setTitle("Select Colors")
            mBuilder.setMultiChoiceItems(list, checkedItems2,
                DialogInterface.OnMultiChoiceClickListener { dialog, position, isChecked -> //                        if (isChecked) {

                    if (isChecked) {
                        mUserItems2.add(position)
                    } else {
                        mUserItems2.remove(Integer.valueOf(position))
                    }
                })

            mBuilder.setCancelable(false)
            mBuilder.setPositiveButton("Apply",
                DialogInterface.OnClickListener { dialogInterface, which ->
                    var item = ""
                    for (i in mUserItems2.indices) {
                        val objColorData = JsonObject()
                        objColorData.addProperty("strName", arrColorList.get(mUserItems2.get(i)))
                        objColorData.addProperty(
                            "strColorCode",
                            arrColorCodeList.get(mUserItems2.get(i))
                        )
                        arrPassingColor.add(objColorData)
                        arrSelectedColor.add(arrColorList.get(mUserItems2.get(i)))
                        arrSelectedColorCode.add(arrColorCodeList.get(mUserItems2.get(i)))
                        item = item + arrColorList.get(mUserItems2.get(i))
                        if (i != mUserItems2.size - 1) {
                            item = "$item, "
                        }
                    }
                    bt_colorDetail.setText("Colors : $item")
                })

            mBuilder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })

            mBuilder.setNeutralButton(
                "Clear",
                DialogInterface.OnClickListener { dialogInterface, which ->
                    for (i in checkedItems2.indices) {
                        checkedItems2[i] = false
                        mUserItems2.clear()
                    }
                    bt_colorDetail.setText("Select Colors")
                })

            val mDialog: AlertDialog = mBuilder.create()
            mDialog.show()

            val buttonbackground: Button = mDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            buttonbackground.setBackgroundColor(Color.WHITE)
            buttonbackground.setTextColor(Color.BLACK)

            val buttonbackground1: Button = mDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            buttonbackground1.setBackgroundColor(Color.WHITE)
            buttonbackground1.setTextColor(Color.BLACK)

            val buttonbackground2: Button = mDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
            buttonbackground2.setBackgroundColor(Color.WHITE)
            buttonbackground2.setTextColor(Color.BLACK)
        }
    }

    override fun onAddSizeColorNull(apiResponse: AddSizeColorResponse) {

    }

    override fun onAddSizeColorFailed(apiResponse: ResponseBody) {

    }

    override fun onAddSizeColorFailedServerError(string: String) {

    }


    private fun UpdateProductClick(it: View?) {
        val productId = etProductIdDetail.text.toString().trim()
        val productTitle = etProductTitleDetail.text.toString().trim()
        val productBrand = et_brandDetail.text.toString().trim()
        val subCategory = et_subcategoryDetail.text.toString().trim()
        val category = et_categoryDetail.text.toString().trim()
        val productMaterial = etProductMaterialDetail.text.toString().trim()
        val productDescription = etProductDescriptionDetail.text.toString().trim()
        val targetedGender = sp_genderDetail.selectedItem.toString().trim()
        val sellingPrice = etSellingPriceDetail.text.toString().trim()
        val mrp = etMrpDetail.text.toString().trim()
        val retailerPrice = etRetailerPriceDetail.text.toString().trim()
        val stock = et_stockDetail.text.toString().trim()
        val strKeyword = etKeyword.text.toString().trim()
        var dblSgst=etSGST.text.toString().trim()
        var dblCgst=etCGST.text.toString().trim()


        if (productId.isEmpty()) {
            etProductIdDetail.error = "Product id required*"
            etProductIdDetail.requestFocus()
            return
        }
        if (productTitle.isEmpty()) {
            etProductTitleDetail.error = "Product title required*"
            etProductTitleDetail.requestFocus()
            return
        }
        if (mrp.isEmpty()) {
            etMrpDetail.error = "Mrp required*"
            etMrpDetail.requestFocus()
            return
        }
        if (sellingPrice.isEmpty()) {
            etSellingPriceDetail.error = "Selling price required*"
            etSellingPriceDetail.requestFocus()
            return
        }
        if (retailerPrice.isEmpty()) {
            etRetailerPriceDetail.error = "Retail price required*"
            etRetailerPriceDetail.requestFocus()
            return
        }

        if(etSGST.text.isEmpty()){
            dblSgst= "9.0"
        }
        if(etCGST.text.isEmpty()){
            dblCgst= "9.0"
        }

        if (stock.isEmpty()) {
            et_stockDetail.error = "Stock required*"
            et_stockDetail.requestFocus()
            return
        }
        if (productDescription.isEmpty()) {
            etProductDescriptionDetail.error = "Product discription required*"
            etProductDescriptionDetail.requestFocus()
            return
        }
        if (productBrand.isEmpty()) {
            et_brandDetail.error = "Product brand required*"
            et_brandDetail.requestFocus()
            return
        }

        if (strKeyword.isEmpty()) {
            etKeyword.error = "Keyword required*"
            etKeyword.requestFocus()
            return
        }


        if(listBrand.size!=0){
            var BRAND_ERROR_FLAG=true
            for(i in listBrand){
                if(i==strValueBrand){
                    BRAND_ERROR_FLAG=false
                }
            }
            if(BRAND_ERROR_FLAG){
                et_brandDetail.error = "BRAND NOT EXIST IN MASTER*"
                et_brandDetail.requestFocus()
                return
            }

        }

        if(listCategory.size!=0){
            var CATEGORY_ERROR_FLAG=true
            for(i in listCategory){
                if(i==strValueCategory){
                    CATEGORY_ERROR_FLAG=false
                }

                if(CATEGORY_ERROR_FLAG){
                    et_categoryDetail.error = "CATEGORY NOT EXIST IN MASTER*"
                    et_categoryDetail.requestFocus()
                    return
                }
            }

        }

        if(listSubCategory.size!=0){
            if(et_subcategoryDetail.text.isNotEmpty()) {
                var SUBCATEGORY_ERROR_FLAG = true
                for (i in listSubCategory) {
                    if (i == strValueSubCategory) {
                        SUBCATEGORY_ERROR_FLAG = false
                    }
                    if (SUBCATEGORY_ERROR_FLAG) {
                        et_subcategoryDetail.error = "SUB CATEGORY NOT EXIST IN MASTER*"
                        et_subcategoryDetail.requestFocus()
                        return
                    }
                }
            }

        }
//        else{
//            if(strValueSubCategory!="") {
//                et_subcategoryDetail.error = "SUB CATEGORY NOT EXIST IN MASTER*"
//                et_subcategoryDetail.requestFocus()
//                return
//            }
//        }

        if(listMaterial.size!=0){
            if(etProductMaterialDetail.text.isNotEmpty()) {
                var MATERIAL_ERROR_FLAG = true
                for (i in listMaterial) {
                    if (i == strValueMaterial) {
                        MATERIAL_ERROR_FLAG = false
                    }

                    if (MATERIAL_ERROR_FLAG) {
                        etProductMaterialDetail.error = "MATERIAL NOT EXIST IN MASTER*"
                        etProductMaterialDetail.requestFocus()
                        return
                    }
                }
            }

        }


        var targetApp = "ALL"
        //Target App
        if (chBtoBDetail.isChecked && !chBtoCDetail.isChecked)
            targetApp = "B2B"
        else if (chBtoCDetail.isChecked && !chBtoBDetail.isChecked)
            targetApp = "B2C"
        //Item Unit
        var itemUnitId: Int = rgUnitDetail.checkedRadioButtonId
        var itemUnit = "Qty"
        if (itemUnitId != -1) {
            val itemUnitRd: RadioButton = findViewById(itemUnitId)
            itemUnit = itemUnitRd.text.toString()
        } else {

            Snackbar.make(it!!, "Choose any Unit", Snackbar.LENGTH_SHORT).show()
            pbUdateProduct.visibility = View.GONE
            btUpdateProduct.visibility = View.VISIBLE
        }

        val strSchemeOne=etSchemeOneUpdate.text.toString().trim()
        val strScheme1Offer=etOfferOneUpdate.text.toString().trim()
        val strSchemeTwo=etSchemeTwUpdate.text.toString().trim()
        val strScheme2Offer=etOfferTwUpdate.text.toString().trim()
        val strSchemeThree=etSchemeTrUpdate.text.toString().trim()
        val strScheme3Offer=etOfferTrUpdate.text.toString().trim()


        if(strSchemeOne!=null&&!strSchemeOne.equals("")){
            val objScheme1=JsonObject()
            objScheme1.addProperty("intBuyNo",strSchemeOne)
            objScheme1.addProperty("intGetNo",strScheme1Offer)
            arrScheme.add(objScheme1)
        }
        if(strSchemeTwo!=null&&!strSchemeTwo.equals("")){
            val objScheme2=JsonObject()
            objScheme2.addProperty("intBuyNo",strSchemeTwo)
            objScheme2.addProperty("intGetNo",strScheme2Offer)
            arrScheme.add(objScheme2)
        }
        if(strSchemeThree!=null&&!strSchemeThree.equals("")){
            val objScheme3=JsonObject()
            objScheme3.addProperty("intBuyNo",strSchemeThree)
            objScheme3.addProperty("intGetNo",strScheme3Offer)
            arrScheme.add(objScheme3)
        }

        if(IMAGE_CHANGED) {

            presenter.updateProduct(
                productTitle,
                productId,
                productDescription,
                category,
                productBrand,
                targetedGender,
                mrp,
                sellingPrice,
                retailerPrice,
                targetApp,
                stock,
                dblTotalSale,
                "",
                itemUnit,
                arrImageDataNew,
                strToken,
                productMaterial,
                arrSelectedSize,
                arrPassingColor,
                subCategory,
                arrSelectedColorCode,
                strProductId,
                strKeyword,
                arrScheme,
                dblCgst,
                dblSgst,
                dblTotalDiscounts
            )
        }else{

            presenter.updateProduct(
                productTitle,
                productId,
                productDescription,
                category,
                productBrand,
                targetedGender,
                mrp,
                sellingPrice,
                retailerPrice,
                targetApp,
                stock,
                dblTotalSale,
                "",
                itemUnit,
                arrImageData,
                strToken,
                productMaterial,
                arrSelectedSize,
                arrPassingColor,
                subCategory,
                arrSelectedColorCode,
                strProductId,
                strKeyword,
                arrScheme,
                dblCgst,
                dblSgst,
                dblTotalDiscounts
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ProductUpdateActivity,ProductListActivity::class.java))
        finish()
    }
}