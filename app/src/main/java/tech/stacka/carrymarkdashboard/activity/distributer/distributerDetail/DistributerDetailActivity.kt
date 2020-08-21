package tech.stacka.carrymarkdashboard.activity.distributer.distributerDetail

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_distributer_detail.*
import kotlinx.android.synthetic.main.activity_distributer_detail.btDeleteUser
import kotlinx.android.synthetic.main.activity_distributer_detail.tvMailId
import kotlinx.android.synthetic.main.activity_distributer_detail.tvMobile
import kotlinx.android.synthetic.main.activity_distributer_detail.tvName
import kotlinx.android.synthetic.main.activity_distributer_detail.tvUid
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.distributer.distributerList.DistributorListActivity
import tech.stacka.carrymarkdashboard.activity.distributer.updateDistributer.UpdateDistributerActivity
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.DistributorDetailResponse
import tech.stacka.carrymarkdashboard.models.data.ArrDistributorDetails
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class DistributerDetailActivity : AppCompatActivity(), DistributorDetailView {

    var strDistributorId:String=""
    var strToken:String=""
    private var distributorDetail = ArrayList<ArrDistributorDetails>()
    val presenter=DistributorDetailPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributer_detail)
        nav_back.setOnClickListener {
            startActivity(Intent(this@DistributerDetailActivity, DistributorListActivity::class.java))
            finish()
        }

        strDistributorId = intent.getStringExtra("distributorId")!!
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        if(Utilities.checkInternetConnection(this)) {
            presenter.distributorDetails(strToken,strDistributorId)

        }else{
            AlertHelper.showNoInternetSnackBar(this@DistributerDetailActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.distributorDetails(strToken,strDistributorId)
                }
            })
        }

        btDeleteUser.setOnClickListener {

            showDeleteDialog()
        }
        btUpdateUser.setOnClickListener {
            val intent = Intent(this, UpdateDistributerActivity::class.java)
            intent.putExtra("distributorId",strDistributorId)
            startActivity(intent)
        }


    }

    override fun onDistributorDetailSuccess(apiResponse: DistributorDetailResponse?) {
        if(apiResponse!=null) {
            distributorDetail=apiResponse.arrList as ArrayList<ArrDistributorDetails>
            tvName.text = apiResponse.arrList[0].strName
            tvMobile.text = apiResponse.arrList[0].strMobileNo
            tvMailId.text = apiResponse.arrList[0].strEmail
            tvUid.text = apiResponse.arrList[0].strUserId
            tvExecutiveName.text = apiResponse.arrList[0].strExecutiveName
        }
    }

    override fun onDistributorDetailNull(apiResponse: DistributorDetailResponse) {

    }

    override fun onDistributorDetailFailed(apiResponse: ResponseBody) {
        Toast.makeText(this,"Data Loading Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onDistributorDetailFailedServerError(string: String) {
        Toast.makeText(this,"Check your internet connection",Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteUserSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(this,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@DistributerDetailActivity,DistributorListActivity::class.java))
        finish()
    }

    override fun onDeleteUserNull(apiResponse: DefaultResponse) {

    }

    override fun onDeleteUserFailed(apiResponse: ResponseBody) {
        Toast.makeText(this,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteUserServerError(string: String) {
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.normal_pop_up_custom)
        val heading = dialog.findViewById<View>(R.id.tvCustomPopupHeading) as TextView
        heading.text = "Are you sure you want to delete"
        val details = dialog.findViewById<View>(R.id.tvCustomPopupDetail) as TextView
        details.text=details.toString()
        details.visibility= View.GONE
        val btYes = dialog.findViewById<View>(R.id.btCustomPopupOk) as Button
        val btNo = dialog.findViewById<View>(R.id.btCustomPopupCancel) as Button
        btYes.setOnClickListener {
            if(Utilities.checkInternetConnection(this)) {
                presenter.deleteUser(strToken,strDistributorId)

            }else{
                AlertHelper.showNoInternetSnackBar(this@DistributerDetailActivity, object :
                    AlertHelper.SnackBarListener {
                    override fun onOkClick() {
                        presenter.deleteUser(strToken,strDistributorId)
                    }
                })
            }
            dialog.dismiss()
        }
        btNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DistributerDetailActivity, DistributorListActivity::class.java))
        finish()
    }

}