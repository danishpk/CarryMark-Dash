package tech.stacka.carrymarkdashboard.activity.product.productDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_product_detail2.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.ProductDetailResponse
import tech.stacka.carrymarkdashboard.models.data.ArrScheme
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class ProductDetailActivity : AppCompatActivity(), ProductDetailView {
    var arrSchema=ArrayList<ArrScheme>()
    var strProductId:String=""
    var strToken:String=""
    var strQty:String=""
    var imgOne = ""
    var imgTwo = ""
    var imgThree = ""
    var strItemColor = ""
    var schemeOne=0
    var schemeOneOfr=0
    var schemeTw=0
    var schemeTwOfr=0
    var schemeThr=0
    var schemeThrOfr=0
  //  private var colorList: ArrayList<ArrColorStock> = ArrayList()
    var strItemSize =""
    val presenter= ProductDetailPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail2)

        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        strProductId = intent.getStringExtra("productId")

        if(Utilities.checkInternetConnection(this)) {
            presenter.productDetails(strToken,strProductId)

        }else{

            Toast.makeText(this,"check your internet connection", Toast.LENGTH_LONG).show()
        }

        ivImgOne.setOnClickListener {
            Glide.with(this).load(imgOne).into(ivImgPreview)
            ivImgOne.background = getDrawable(R.drawable.bg_image_selector)
            ivImgTwo.background = null
            ivImgThree.background = null
        }

        ivImgTwo.setOnClickListener {
            Glide.with(this).load(imgTwo).into(ivImgPreview)
            ivImgTwo.background = getDrawable(R.drawable.bg_image_selector)
            ivImgOne.background = null
            ivImgThree.background = null
        }
        ivImgThree.setOnClickListener {
            Glide.with(this).load(imgThree).into(ivImgPreview)
            ivImgThree.background = getDrawable(R.drawable.bg_image_selector)
            ivImgOne.background = null
            ivImgTwo.background = null
        }

        nav_back.setOnClickListener{
            finish()
        }
    }

    override fun onProductDetailSuccess(apiResponse: ProductDetailResponse) {
        tvProductName.text = apiResponse.strName
        tvProductId.text = apiResponse.strProductId
        tvDecrpsn.text = apiResponse.strDescription
        tvSellingPrice.text = "₹" +apiResponse.dblSellingPrice.toString()
        tvMrp.text = "${"₹" + apiResponse.dblMRP}"
        if(apiResponse.arrImageUrl!=null) {
            if (apiResponse.arrImageUrl.isNotEmpty()) {

                imgOne = apiResponse.arrImageUrl[0]

                if (apiResponse.arrImageUrl.size > 1) {
                    imgTwo = apiResponse.arrImageUrl[1]
                }
                if (apiResponse.arrImageUrl.size > 2) {
                    imgThree = apiResponse.arrImageUrl[2]
                }

                Glide.with(this).load(imgOne).into(ivImgPreview)
                Glide.with(this).load(imgOne).into(ivImgOne)
                if(apiResponse.arrImageUrl.size>1) {
                    if (apiResponse.arrImageUrl[1] != "") Glide.with(this)
                        .load(apiResponse.arrImageUrl[1]).into(ivImgTwo)
                }
                if(apiResponse.arrImageUrl.size>2) {
                    if (apiResponse.arrImageUrl.get(2) != "") Glide.with(this)
                        .load(apiResponse.arrImageUrl.get(2)).into(ivImgThree)
                }
            }
        }
        tvBrandName.text=apiResponse.strBrandId

        arrSchema=apiResponse.arrScheme as ArrayList<ArrScheme>
        if(arrSchema.size!=0){
            textView12.visibility=View.VISIBLE
            tvScheme1.visibility=View.VISIBLE
            schemeOne=(arrSchema[0].intBuyNo).toInt()
            schemeOneOfr=(arrSchema[0].intGetNo).toInt()
            tvScheme1.text = "Buy $schemeOne Get $schemeOneOfr"
            if(arrSchema.size>1){
                tvScheme2.visibility=View.VISIBLE
                schemeTw=(arrSchema[1].intBuyNo).toInt()
                schemeTwOfr=(arrSchema[1].intGetNo).toInt()
                tvScheme2.text="Buy $schemeTw Get $schemeTwOfr"
            }
            if(arrSchema.size>2){
                tvScheme3.visibility=View.VISIBLE
                schemeThr=(arrSchema[2].intBuyNo).toInt()
                schemeThrOfr=(arrSchema[2].intGetNo).toInt()
                tvScheme3.text="Buy $schemeThr Get $schemeThrOfr"
            }
        }


    }

    override fun onProductDetailNull(apiResponse: ProductDetailResponse) {
        AlertHelper.showOKSnackBarAlert(this@ProductDetailActivity,"Product Detail Empty")
    }

    override fun onProductDetailFailed(apiResponse: ResponseBody) {
        AlertHelper.showOKSnackBarAlert(this@ProductDetailActivity,"Product Detail Failed")
    }

    override fun onProductDetailServerFailed(toString: String) {
        AlertHelper.showNoInternetSnackBar(this@ProductDetailActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}