package tech.stacka.carrymarkdashboard.activity.login

import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.LoginResponse

interface LoginView {
    abstract fun onLoginSuccess(apiResponse: LoginResponse)
    abstract fun onLoginFailed(apiResponse: JSONArray)
    abstract fun onLoginFailedServerError(string: String)
    abstract fun onLoginNull(apiResponse: LoginResponse)
}