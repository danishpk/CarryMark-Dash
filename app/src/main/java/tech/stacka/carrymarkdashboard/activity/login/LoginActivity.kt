package tech.stacka.carrymarkdashboard.activity.login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.models.LoginResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

class LoginActivity : AppCompatActivity() ,LoginView{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btLogin.setOnClickListener {
            val strName = etUserName.text.toString().trim()
            val strPassword = etPassword.text.toString().trim()
            if (strName.isEmpty()) {
                etUserName.error = "Username required"
                etUserName.requestFocus()
                return@setOnClickListener
            }
            if (strPassword.isEmpty()) {
                etPassword.error = "Password required"
                etPassword.requestFocus()
                return@setOnClickListener
            }
            val presenter = LoginPresenter(this@LoginActivity, this@LoginActivity)
            presenter.loginUserPass(strName,strPassword)
        }
    }
    override fun onLoginSuccess(apiResponse: LoginResponse) {
        val objUser= apiResponse
        SharedPrefManager.getInstance(applicationContext).saveUser(objUser)
        Log.e("strTokenOnLogin",objUser.strToken.toString())
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
     //   Toast.makeText(applicationContext,apiResponse.strToken.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onLoginFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.string(),Toast.LENGTH_LONG).show()
    }

    override fun onLoginFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_LONG).show()
    }

    override fun onLoginNull(apiResponse: LoginResponse) {
        Toast.makeText(applicationContext,"Data Empty",Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        if(SharedPrefManager.getInstance(applicationContext).isLoggedIn){
        //    Toast.makeText(applicationContext,SharedPrefManager.getInstance(applicationContext).isLoggedIn.toString(),Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}