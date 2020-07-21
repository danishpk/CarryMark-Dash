package tech.stacka.carrymarkdashboard.activity.login
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.models.LoginResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class LoginActivity : AppCompatActivity() ,LoginView{
    private lateinit var sharedPreference: SharedPreferences
    var strFirebaseToken=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
        val presenter = LoginPresenter(this@LoginActivity, this@LoginActivity)
        btLogin.setOnClickListener {
            pbLogin.visibility=View.VISIBLE
            btLogin.visibility=View.GONE
            val strName = etUserName.text.toString().trim()
            val strPassword = etPassword.text.toString().trim()

            strFirebaseToken= sharedPreference.getString("fireBaseToken","").toString()
            if (strName.isEmpty()) {
                etUserName.error = "Username required"
                etUserName.requestFocus()
                pbLogin.visibility=View.GONE
                btLogin.visibility=View.VISIBLE
                return@setOnClickListener
            }
            if (strPassword.isEmpty()) {
                etPassword.error = "Password required"
                etPassword.requestFocus()
                pbLogin.visibility=View.GONE
                btLogin.visibility=View.VISIBLE
                return@setOnClickListener
            }

            if(Utilities.checkInternetConnection(this)) {
                presenter.loginUserPass(strName,strPassword,strFirebaseToken)

            }else{
                pbLogin.visibility=View.GONE
                btLogin.visibility=View.VISIBLE
                AlertHelper.showNoInternetSnackBar(this@LoginActivity, object :
                    AlertHelper.SnackBarListener {
                    override fun onOkClick() {
                        pbLogin.visibility=View.VISIBLE
                        btLogin.visibility=View.GONE
                        presenter.loginUserPass(strName,strPassword,strFirebaseToken)
                    }
                })
            }



        }
    }
    override fun onLoginSuccess(apiResponse: LoginResponse) {
        val objUser= apiResponse
        pbLogin.visibility=View.GONE
        SharedPrefManager.getInstance(applicationContext).saveUser(objUser)
        Log.e("strTokenOnLogin",objUser.strToken.toString())
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
     //   Toast.makeText(applicationContext,apiResponse.strToken.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onLoginFailed(apiResponse: JSONArray) {
        pbLogin.visibility=View.GONE
        btLogin.visibility=View.VISIBLE
        val listData = ArrayList<String>()
            for (i in 0 until apiResponse.length()) {
                listData += apiResponse.getString(i)
                Log.e("ListData",listData.toString())
                for(i in listData){
                  if(i=="INVALID_USER_NAME"){
                      etUserName.error = "Incorrect username *"
                      etUserName.requestFocus()
                      pbLogin.visibility=View.GONE
                      btLogin.visibility=View.VISIBLE
                  }else if(i=="CREDENTIAL_INVALID"){
                      etPassword.error = "Incorrect password *"
                      etPassword.requestFocus()
                      pbLogin.visibility=View.GONE
                      btLogin.visibility=View.VISIBLE
                  }else if(i=="INVALID_USER_TYPE"){
                      etUserName.error = "Admin username required *"
                      etUserName.requestFocus()
                      pbLogin.visibility=View.GONE
                      btLogin.visibility=View.VISIBLE
                  }
                }
            }




       // Toast.makeText(applicationContext,apiResponse.string(),Toast.LENGTH_LONG).show()
    }

    override fun onLoginFailedServerError(string: String) {
        pbLogin.visibility=View.GONE
        btLogin.visibility=View.VISIBLE
        AlertHelper.showOKSnackBarAlert(this@LoginActivity, getString(R.string.no_internet_connection))
     //   Toast.makeText(applicationContext,string,Toast.LENGTH_LONG).show()
    }

    override fun onLoginNull(apiResponse: LoginResponse) {
        pbLogin.visibility=View.GONE
        btLogin.visibility=View.VISIBLE
     //   Toast.makeText(applicationContext,"Data Empty",Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "FireBase Id",
                        "getInstanceId failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token

                // Log and toast
                Log.d("FireBase Id", token)
                val editor = sharedPreference.edit()
                editor.putString("fireBaseToken", token)
                editor.apply()
                //    Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
            })
        if(SharedPrefManager.getInstance(applicationContext).isLoggedIn){
        //    Toast.makeText(applicationContext,SharedPrefManager.getInstance(applicationContext).isLoggedIn.toString(),Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}