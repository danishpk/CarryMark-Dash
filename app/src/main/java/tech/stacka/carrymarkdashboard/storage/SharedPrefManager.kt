package tech.stacka.carrymarkdashboard.storage

import android.content.Context
import tech.stacka.carrymarkdashboard.models.LoginResponse


class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return  sharedPreferences.getString("_id", null) != null
        }

    val user: LoginResponse
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return LoginResponse(

                sharedPreferences.getString("_id", null),
                sharedPreferences.getString("chrStatus", null),
                sharedPreferences.getString("strAddress", null),
                sharedPreferences.getString("strMobileNo", null),
                sharedPreferences.getString("strName", null),
                sharedPreferences.getString("strPinCode", null),
                sharedPreferences.getString("strToken", null),
                sharedPreferences.getBoolean("blnAPIStatus", true)
            )
        }


    fun saveUser(user: LoginResponse) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("_id", user._id)
        editor.putString("chrStatus", user.chrStatus)
        editor.putString("strAddress", user.strAddress)
        editor.putString("strMobileNo", user.strMobileNo)
        editor.putString("strName", user.strName)
        editor.putString("strPinCode", user.strPinCode)
        editor.putString("strToken", user.strToken)
        editor.putBoolean("blnAPIStatus",true)
        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}