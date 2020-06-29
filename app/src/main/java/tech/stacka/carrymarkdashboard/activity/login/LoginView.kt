package tech.stacka.carrymarkdashboard.activity.login

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.LoginResponse

interface LoginView {
    abstract fun onLoginSuccess(apiResponse: LoginResponse)
    abstract fun onLoginFailed(apiResponse: ResponseBody)
    abstract fun onLoginFailedServerError(string: String)
    abstract fun onLoginNull(apiResponse: LoginResponse)
}