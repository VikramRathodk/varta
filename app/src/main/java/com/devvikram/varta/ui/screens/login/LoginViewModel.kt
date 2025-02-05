package com.devvikram.varta.ui.screens.login

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.retrofit.RetrofitInstance
import com.devvikram.varta.data.retrofit.models.LoginInformation
import com.devvikram.varta.data.retrofit.models.LoginResponse
import com.devvikram.varta.data.retrofit.models.RegisterModel
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.workers.WorkerManagers
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginPreference: LoginPreference,
    private val contactRepository: ContactRepository,
    @ApplicationContext private val applicationContext: Context

) : ViewModel() {


    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Ideal)
    val loginUiState = _loginUiState.asStateFlow()

    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Ideal)
    val registerUiState = _registerUiState.asStateFlow()



    fun login(loginInformation: LoginInformation) {
        if (!validInput(loginInformation,true)) {
            return
        }
        Log.d(TAG, "login:  $loginInformation")
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                val response: Response<LoginResponse> =
                    RetrofitInstance.getApiService().login(loginInformation)

                if (response.isSuccessful) {
                    val loginResponse: LoginResponse? = response.body()
                    if (loginResponse != null) {
                        if (loginResponse.success) {

                            _loginUiState.value = LoginUiState.Success(loginResponse)

                            val userInformation = loginResponse.data?.userData
                            val sessionId = loginResponse.data?.sessionId
                            val sessionStatus = loginResponse.data!!.sessionStatus

                            if (userInformation != null) {
                                loginPreference.saveLoginInfo(
                                    userId = userInformation.userId.toString(),
                                    loginSessionId = sessionId.toString(),
                                    loginSessionStatus = sessionStatus
                                )

                                contactRepository.insertUserContact(userInformation)

                            } else {
                                _loginUiState.value = LoginUiState.Error(
                                    loginResponse.message
                                        ?: "User information is unavailable, please try again"
                                )
                            }
                        } else {
                            _loginUiState.value = LoginUiState.Error(
                                loginResponse.message ?: "Invalid credentials , please try again"
                            )
                        }
                    } else {
                        _loginUiState.value =
                            LoginUiState.Error("Invalid credentials , please try again")
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val apiError = gson.fromJson(errorResponse, LoginResponse::class.java)
                    _loginUiState.value = LoginUiState.Error(apiError.message)
                }

            } catch (exception: Exception) {
                Log.d(TAG, "login: $exception")
                exception.printStackTrace()
                _loginUiState.value = when (exception) {
                    is java.net.UnknownHostException -> LoginUiState.Error("No Internet connection. Please check your network settings.")
                    is java.net.SocketTimeoutException -> LoginUiState.Error("Request timed out. Please try again.")
                    is java.net.ConnectException -> LoginUiState.Error("Unable to connect to the server. Please check your internet connection.")
                    is java.net.NoRouteToHostException -> LoginUiState.Error("No route to host. Please check your internet connection.")
                    is java.net.ProtocolException -> LoginUiState.Error("Protocol error. Please try again.")
                    is java.net.UnknownServiceException -> LoginUiState.Error("Unknown service error. Please try again.")
                    else -> LoginUiState.Error("An unexpected error occurred, please try again.")
                }
            }
        }
    }

    private fun validInput(loginInformation: LoginInformation, isLogin : Boolean): Boolean {
        if(isLogin){
            if (loginInformation.email.isEmpty()) {
                _loginUiState.value = LoginUiState.Error("Email is required")
                return false
            }

            if (loginInformation.password.isEmpty()) {
                _loginUiState.value = LoginUiState.Error("Password is required")
                return false
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(loginInformation.email).matches()) {
                _loginUiState.value = LoginUiState.Error("Invalid email address")
                return false
            }


        }else {
            if (loginInformation.email.isEmpty()) {
                _registerUiState.value = RegisterUiState.Error("Email is required")
                return false
            }
            if (loginInformation.password.isEmpty()) {
                _registerUiState.value = RegisterUiState.Error("Password is required")
                return false
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(loginInformation.email).matches()) {
                _registerUiState.value = RegisterUiState.Error("Invalid email address")
                return false
            }
        }

        return true
    }

    fun logout() {
        loginPreference.clearSharedPref()
        viewModelScope.launch {
            contactRepository.clearContact()
        }
        _loginUiState.value = LoginUiState.Ideal
    }

    fun resetLoginState() {
        _loginUiState.value = LoginUiState.Ideal
    }

    fun register(registerInformation: RegisterModel) {
        Log.d(TAG, "register:  $registerInformation")
        viewModelScope.launch {
            _registerUiState.value = RegisterUiState.Loading
            try {
                val response: Response<LoginResponse> =
                    RetrofitInstance.getBCSTEPApiService().register(registerInformation)
                if (response.isSuccessful) {
                    val registerResponse: LoginResponse? = response.body()
                    if (registerResponse!= null) {
                        if (registerResponse.success) {
                            _registerUiState.value = RegisterUiState.Success(registerResponse)
                        } else {
                            _registerUiState.value = RegisterUiState.Error(
                                registerResponse.message
                                   ?: "Registration failed, please try again"
                            )
                        }
                    } else {
                        _registerUiState.value = RegisterUiState.Error(
                            "Registration failed, please try again"
                        )
                    }
                }

            } catch (exception: Exception) {
                Log.d(TAG, "login: $exception")
                exception.printStackTrace()
                _registerUiState.value = when (exception) {
                    is java.net.UnknownHostException -> RegisterUiState.Error("No Internet connection. Please check your network settings.")
                    is java.net.SocketTimeoutException -> RegisterUiState.Error("Request timed out. Please try again.")
                    is java.net.ConnectException -> RegisterUiState.Error("Unable to connect to the server. Please check your internet connection.")
                    is java.net.NoRouteToHostException -> RegisterUiState.Error("No route to host. Please check your internet connection.")
                    is java.net.ProtocolException -> RegisterUiState.Error("Protocol error. Please try again.")
                    is java.net.UnknownServiceException -> RegisterUiState.Error("Unknown service error. Please try again.")
                    else -> RegisterUiState.Error("An unexpected error occurred, please try again.")
                }
            }
        }
    }

    fun resetRegisterState() {
        _registerUiState.value = RegisterUiState.Ideal
    }

    fun syncContacts() {
        viewModelScope.launch {
            WorkerManagers.getInstance(applicationContext).syncContacts()
        }
    }

    sealed class LoginUiState {
        data class Success(val loginResponse: LoginResponse) : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        data object Loading : LoginUiState()
        data object Ideal : LoginUiState()
    }
    sealed class RegisterUiState{
        data class Success(val registerResponse: LoginResponse) : RegisterUiState()
        data class Error(val message: String) : RegisterUiState()
        data object Loading : RegisterUiState()
        data object Ideal : RegisterUiState()
    }
}
