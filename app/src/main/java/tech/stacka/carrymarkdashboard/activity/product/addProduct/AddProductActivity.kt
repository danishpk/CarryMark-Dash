package tech.stacka.carrymarkdashboard.activity.product.addProduct

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity
import tech.stacka.carrymarkdashboard.models.AddProductCategoryResponse
import tech.stacka.carrymarkdashboard.models.AddProductResponse
import tech.stacka.carrymarkdashboard.models.AddSizeColorResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse
import tech.stacka.carrymarkdashboard.models.data.ArrAddProductCategory
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.io.File

class AddProductActivity : AppCompatActivity(),AddProductView{
    var arrSelectedSize: MutableList<String> = ArrayList()
    var arrSelectedColor: MutableList<String> = ArrayList()
    var arrSelectedColorCode: MutableList<String> = ArrayList()
    var strMaincategory:String=""
    var arrPassingColor=JsonArray()
    private var pos = 0
    var strToken:String = ""
    private var imgUrisOne: Uri? = null
    private var imgUrisTwo: Uri? = null
    private var imgUrisThree: Uri? = null
    var UPLOAD_IMAGE_VALUE:Int =0
    var CATEGORY_TYPE:Int=0
    var arrImageData=JsonArray()
    val presenter = AddProductPresenter(this,this)
    val path: ArrayList<File> = ArrayList()
    val list: MutableList<String> = ArrayList()
    var arrScheme=JsonArray()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        var categoryList= ArrayList<String>()
        categoryList.add("cln_size")
        categoryList.add("cln_color")
        presenter.addSizeColor(categoryList,10)
        nav_back.setOnClickListener {
            finish()
        }

        ibAddNewScheme1.setOnClickListener {
            trSchemeTwo.visibility=View.VISIBLE
            ibAddNewScheme1.visibility=View.GONE
        }

        ibAddNewScheme2.setOnClickListener {
            trSchemeThree.visibility=View.VISIBLE
            ibAddNewScheme2.visibility=View.GONE
        }
        ibAddNewScheme3.setOnClickListener {
            trScheme4.visibility=View.VISIBLE
            ibAddNewScheme3.visibility=View.GONE
        }
        ibAddNewScheme4.setOnClickListener {
            trScheme5.visibility=View.VISIBLE
            ibAddNewScheme4.visibility=View.GONE
        }




        Log.e("strTokenOnAddProduct",strToken)
        bt_upload_image.setOnClickListener {
                pbUploadImage.visibility=View.VISIBLE
                pbUploadImage.bringToFront()
            if(Utilities.checkInternetConnection(this)) {
                presenter.uploadImage(path, strToken)
            }else{
                pbUploadImage.visibility=View.GONE
                Snackbar.make(it!!,"check your internet connection",Snackbar.LENGTH_LONG).show()
            }
        }
        btAddProduct.setOnClickListener {
            if(UPLOAD_IMAGE_VALUE==1) {
                btAddProduct.visibility=View.GONE
                pbAddProduct.visibility=View.VISIBLE
                addProductClick(it)
            }else{
                Snackbar.make(it!!,"Upload one image",Snackbar.LENGTH_LONG).show()
            }

        }
        et_brandAddProduct.addTextChangedListener(object:TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                    val strValueBrand:String=et_brandAddProduct.text.toString()
                    val strCollectionBrand:String="cln_brand"
                    presenter.categoryList(strCollectionBrand,strValueBrand,strToken)
                    CATEGORY_TYPE=1


            }
        })

//        et_brandAddProduct.setOnTouchListener { v, event ->
//            val strValueBrand:String=et_brandAddProduct.text.toString()
//            val strCollectionBrand:String="cln_brand"
//            presenter.categoryList(strCollectionBrand,strValueBrand,strToken)
//            false
//        }

        et_brandAddProduct.setOnTouchListener { v, motionEvent ->
            val strCollectionBrand:String="cln_brand"
            presenter.categoryList(strCollectionBrand,"",strToken)
            CATEGORY_TYPE=1
            false
        }

        et_category.setOnTouchListener { v, motionEvent ->
            val strCollectionCategory:String="cln_category"
            presenter.categoryList(strCollectionCategory,"",strToken)
            CATEGORY_TYPE=2
            false
        }

        et_subcategory.setOnTouchListener { v, motionEvent ->
            val strCollectionSubCategory:String="cln_sub_category"
            presenter.subCategoryList(strCollectionSubCategory,"",strToken,strMaincategory)
            CATEGORY_TYPE=4
            false
        }

        etProductMaterial.setOnTouchListener { v, motionEvent ->
            val strCollectionMaterial:String="cln_material"
            presenter.categoryList(strCollectionMaterial,"",strToken)
            CATEGORY_TYPE=3
            false
        }

        et_category.addTextChangedListener(object:TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                    val strValueCategory:String=et_category.text.toString()
                    val strCollectionCategory:String="cln_category"
                    presenter.categoryList(strCollectionCategory,strValueCategory,strToken)
                    CATEGORY_TYPE=2


            }
        })

        et_category.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            strMaincategory = parent.getItemAtPosition(position).toString()
            et_subcategory.visibility=View.VISIBLE
        }


        et_subcategory.addTextChangedListener(object:TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                    val strValueSubCategory:String=et_subcategory.text.toString()
                    val strCollectionSubCategory:String="cln_sub_category"
                    presenter.subCategoryList(strCollectionSubCategory,strValueSubCategory,strToken,strMaincategory)
                    CATEGORY_TYPE=4

            }
        })


        etProductMaterial.addTextChangedListener(object:TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                    val strValueMaterial:String=etProductMaterial.text.toString()
                    val strCollectionMaterial:String="cln_material"
                    presenter.categoryList(strCollectionMaterial,strValueMaterial,strToken)
                    CATEGORY_TYPE=3

            }
        })


    }

    private fun addProductClick(it: View?) {
        val productId = etProductId.text.toString().trim()
        val productTitle = etProductTitle.text.toString().trim()
        val productBrand = et_brandAddProduct.text.toString().trim()
        val subCategory=et_subcategory.text.toString().trim()
        val category = et_category.text.toString().trim()
        val productMaterial = etProductMaterial.text.toString().trim()
        val productDescription = etProductDescription.text.toString().trim()
        val targetedGender = sp_gender.selectedItem.toString().trim()
        val sellingPrice = etSellingPrice.text.toString().trim()
        val mrp = etMrp.text.toString().trim()
        val retailerPrice=etRetailerPrice.text.toString().trim()
        val stock = sp_stock.text.toString().trim()
        val strSchemeOne=etSchemeOne.text.toString().trim()
        val strScheme1Offer=etOfferOne.text.toString().trim()
        val strSchemeTwo=etSchemeTw.text.toString().trim()
        val strScheme2Offer=etOfferTw.text.toString().trim()
        val strSchemeThree=etSchemeTr.text.toString().trim()
        val strScheme3Offer=etOfferTr.text.toString().trim()
//        val strSchemeFour=etSchemeOne.text.toString().trim()
//        val strScheme4Offer=etOfferOne.text.toString().trim()

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



        if (productId.isEmpty()) {
            etProductId.error = "Product id required*"
            etProductId.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
        if (productTitle.isEmpty()) {
            etProductTitle.error = "Product title required*"
            etProductTitle.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
        if (mrp.isEmpty()) {
            etMrp.error = "Mrp required*"
            etMrp.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
        if (sellingPrice.isEmpty()) {
            etSellingPrice.error = "Selling price required*"
            etSellingPrice.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
        if (retailerPrice.isEmpty()) {
            etRetailerPrice.error = "Retail price required*"
            etRetailerPrice.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }

        if (stock.isEmpty()) {
            sp_stock.error = "Retail price required*"
            sp_stock.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
        if (productDescription.isEmpty()) {
            etProductDescription.error = "Product discription required*"
            etProductDescription.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
        if (productBrand.isEmpty()) {
            et_brandAddProduct.error = "Product brand required*"
            et_brandAddProduct.requestFocus()
            pbAddProduct.visibility=View.GONE
            btAddProduct.visibility=View.VISIBLE
            return
        }
//        if (productMaterial.isEmpty()) {
//            etProductMaterial.error = "Product material required*"
//            etProductMaterial.requestFocus()
//            return
//        }

        var targetApp="ALL"
        //Target App
        if (chBtoB.isChecked && !chBtoC.isChecked)
            targetApp="B2B"
        else if(chBtoC.isChecked && !chBtoB.isChecked)
            targetApp="B2C"
        //Item Unit
        var itemUnitId: Int=rgUnit.checkedRadioButtonId
        var itemUnit="Qty"
        if(itemUnitId!=-1) {
            val itemUnitRd: RadioButton = findViewById(itemUnitId)
            itemUnit=itemUnitRd.text.toString()
        }else{

            Snackbar.make(it!!, "Choose any Unit", Snackbar.LENGTH_SHORT).show()
            pbAddProduct.visibility = View.GONE
            btAddProduct.visibility = View.VISIBLE
        }
        if(Utilities.checkInternetConnection(this)) {
            presenter.addProduct(productTitle,productId,productDescription,category,productBrand,
                targetedGender,mrp,sellingPrice,retailerPrice,targetApp,stock,"","",itemUnit,
                arrImageData,strToken,productMaterial,arrSelectedSize,arrPassingColor,subCategory,arrSelectedColorCode,arrScheme)

        }else{
            pbAddProduct.visibility = View.GONE
            btAddProduct.visibility = View.VISIBLE
            Snackbar.make(it!!,"check your internet connection",Snackbar.LENGTH_LONG).show()
        }

    }

    fun setMainImage(view: View) {
        pos = 0
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
            .start(this)
    }

    fun setImageTwo(view: View) {
        pos = 1
        if (imgUrisOne != null) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
                .start(this)
        } else Snackbar.make(view, "Please set first image", Snackbar.LENGTH_SHORT).show()
    }
    fun setImageThree(view: View) {
        pos = 2
        if (imgUrisTwo != null) {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4)
                .start(this)
        } else Snackbar.make(view, "Please set second image", Snackbar.LENGTH_SHORT).show()
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
                        Glide.with(this).load(imgURI).into(ivMain)
                        Glide.with(this).load(imgURI).into(ivThumb1)
                        val file1 = File(imgURI.path!!)
                        path.add(file1)

                    }
                    1 -> {
                        imgUrisTwo = imgURI
                        Glide.with(this).load(imgURI).into(ivThumb2)
                        val file2 = File(imgURI.path!!)
                        path.add(file2)
                    }
                    2 -> {
                        imgUrisThree = imgURI
                        Glide.with(this).load(imgURI).into(ivThumb3)
                        var file3 = File(imgURI.path!!)
                        path.add(file3)
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun addProductSuccess(apiResponse: AddProductResponse) {
        pbAddProduct.visibility=View.GONE
        Toast.makeText(applicationContext,"Product Added",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@AddProductActivity,ProductListActivity::class.java))
        finish()


    }

    override fun addProductFailed(apiResponse: JSONArray) {
        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                if(i=="PRODUCT NAME ALREADY EXIST"){
                    etProductTitle.error = "PRODUCT ALREADY EXIST *"
                    etProductTitle.requestFocus()
                    pbAddProduct.visibility=View.GONE
                    btAddProduct.visibility=View.VISIBLE
                }else if(i=="PRODUCT ID ALREADY EXIST"){
                    etProductId.error = "PRODUCT ID ALREADY EXIST *"
                    etProductId.requestFocus()
                    pbAddProduct.visibility=View.GONE
                    btAddProduct.visibility=View.VISIBLE
                }
            }
        }


        //Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_LONG).show()

    }

    override fun addProductFailedServerError(string: String) {
        pbAddProduct.visibility=View.GONE
        btAddProduct.visibility=View.VISIBLE
        Toast.makeText(applicationContext,string,Toast.LENGTH_LONG).show()
    }

    override fun uploadImageSuccess(apiResponse: UploadProductImageResponse) {
        UPLOAD_IMAGE_VALUE=1
        for(i in apiResponse.arrImageUrl){
            arrImageData.add(i)
        }
        //for(i in imageData){ list.add(i.strImgUrl_0) }
        pbUploadImage.visibility=View.GONE
//        objImageData.addProperty("strImgUrl_0",apiResponse.strImgUrl_0)
//        objImageData.addProperty("strImgUrl_1",apiResponse.strImgUrl_1)
//        objImageData.addProperty("strImgUrl_2",apiResponse.strImgUrl_2)
        Toast.makeText(applicationContext,"Upload Succes",Toast.LENGTH_SHORT).show()
    }

    override fun uploadImageFailed(apiResponse: ResponseBody) {
        pbUploadImage.visibility=View.GONE
        Toast.makeText(applicationContext,"Upload failed",Toast.LENGTH_SHORT).show()
    }

    override fun uploadImageFailedServerError(string: String) {
        pbUploadImage.visibility=View.GONE
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

    override fun onProductCategoryListSuccess(apiResponse: AddProductCategoryResponse) {
         //var categoryData = ArrayList<AddProductCategoryResponse>()
        var categoryData= apiResponse.arrList as ArrayList<ArrAddProductCategory>
        val list: MutableList<String> = ArrayList()
        for(i in categoryData){ list.add(i.strName) }
        if(CATEGORY_TYPE==1) {
            var acTextView1 = findViewById<AutoCompleteTextView>(R.id.et_brandAddProduct)
            var arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            et_brandAddProduct.setAdapter(arrayAdapter1)
            acTextView1.showDropDown()
        }
        if(CATEGORY_TYPE==2){
            val acTextView2 = findViewById<AutoCompleteTextView>(R.id.et_category)
            val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            acTextView2.setAdapter(arrayAdapter2)
            acTextView2.showDropDown()
        }

        if(CATEGORY_TYPE==3){
            val acTextView3 = findViewById<AutoCompleteTextView>(R.id.etProductMaterial)
            val arrayAdapter3 = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            acTextView3.setAdapter(arrayAdapter3)
            acTextView3.showDropDown()
//            acTextView.onItemSelectedListener = object : OnItemSelectedListener {
//                override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, arg2: Int, arg3: Long
//                ) {
//                    val acTextViewSub = findViewById(R.id.et_subcategory) as AutoCompleteTextView
//                    acTextViewSub.visibility= View.VISIBLE
//                    strMaincategory=acTextView.text.toString()
//                }
//
//                override fun onNothingSelected(arg0: AdapterView<*>?) {
//                   acTextView.text.clear()
//                }
//            }
        }

        if(CATEGORY_TYPE==4){
            val acTextView = findViewById<AutoCompleteTextView>(R.id.et_subcategory)
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            acTextView.setAdapter(arrayAdapter)
        }
    }

    override fun onProductCategoryListFailed(apiResponse: ResponseBody) {

    }

    override fun onProductCategoryListFailedServerError(string: String) {
    }

    override fun addProductNull(apiResponse: AddProductResponse) {
        TODO("Not yet implemented")
    }

    override fun uploadImageNull(apiResponse: UploadProductImageResponse) {
        TODO("Not yet implemented")
    }

    override fun onProductCategoryListNull(apiResponse: AddProductCategoryResponse) {
        TODO("Not yet implemented")
    }

    override fun onAddSizeColorSuccess(apiResponse: AddSizeColorResponse) {
        val arrSizeList= ArrayList<String>()
        val arrColorList= ArrayList<String>()
        val arrColorCodeList= ArrayList<String>()
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


        bt_size.setOnClickListener {
            var checkedItems1: BooleanArray = BooleanArray(arrSizeList.size)
            var mUserItems1 = java.util.ArrayList<Int>()
            val list = arrSizeList.toTypedArray<CharSequence>()
            val selectedList=ArrayList<String>()
            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setTitle("Select Size")
            mBuilder.setMultiChoiceItems(list, checkedItems1,
                OnMultiChoiceClickListener { dialog, position, isChecked -> //                        if (isChecked) {

                    if (isChecked) {
                        mUserItems1.add(position)
                    } else {
                        mUserItems1.remove(Integer.valueOf(position))
                    }
                })

            mBuilder.setCancelable(false)
            mBuilder.setPositiveButton("Apply",
                DialogInterface.OnClickListener { dialogInterface, which ->
                    var item=""

                    for (i in mUserItems1.indices) {
                        arrSelectedSize.add(arrSizeList.get(mUserItems1.get(i)))
                        item = item + arrSizeList.get(mUserItems1.get(i))
                      //  listitems.add(list.get(mUserItems1.get(i)))
                        if (i != mUserItems1.size - 1) {
                            item = "$item, "
                        }
                    }
                    bt_size.text = "Sizes : $item"
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
                    bt_size.text = "Select Sizes"
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
        bt_color.setOnClickListener {
            lateinit var checkedItems2: BooleanArray
            checkedItems2 = BooleanArray(arrColorList.size)
            var mUserItems2 = java.util.ArrayList<Int>()
            val list = arrColorList.toTypedArray<CharSequence>()
            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setTitle("Select Colors")
            mBuilder.setMultiChoiceItems(list, checkedItems2,
                OnMultiChoiceClickListener { dialog, position, isChecked -> //                        if (isChecked) {

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
                        val objColorData=JsonObject()
                        objColorData.addProperty("strName",arrColorList.get(mUserItems2.get(i)))
                        objColorData.addProperty("strColorCode",arrColorCodeList.get(mUserItems2.get(i)))
                        arrPassingColor.add(objColorData)
                        arrSelectedColor.add(arrColorList.get(mUserItems2.get(i)))
                        arrSelectedColorCode.add(arrColorCodeList.get(mUserItems2.get(i)))
                        item = item + arrColorList.get(mUserItems2.get(i))
                        if (i != mUserItems2.size - 1) {
                            item = "$item, "
                        }
                    }
                    bt_color.text = "Colors : $item"
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
                    bt_color.text = "Select Colors"
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
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}