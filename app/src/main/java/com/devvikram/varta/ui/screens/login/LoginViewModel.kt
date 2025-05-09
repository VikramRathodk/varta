package com.devvikram.varta.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.config.ModelMapper
import com.devvikram.varta.data.firebase.models.FContact
import com.devvikram.varta.data.firebase.repositories.FirebaseContactRepository
import com.devvikram.varta.data.retrofit.models.LoginInformation
import com.devvikram.varta.data.retrofit.models.LoginResponse
import com.devvikram.varta.data.retrofit.models.RegisterModel
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.utility.AppUtils
import com.devvikram.varta.workers.WorkerManagers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginPreference: LoginPreference,
    private val contactRepository: ContactRepository,
    @ApplicationContext private val applicationContext: Context,
    private val firebaseAuth : FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseContactRepository: FirebaseContactRepository

) : ViewModel() {


    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Ideal)
    val loginUiState = _loginUiState.asStateFlow()

    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Ideal)
    val registerUiState = _registerUiState.asStateFlow()

    fun register(registerInformation: RegisterModel) {
        viewModelScope.launch {
            _registerUiState.value = RegisterUiState.Loading
            val result = firebaseContactRepository.registerUser(registerInformation)
            _registerUiState.value = result.fold(
                onSuccess = { RegisterUiState.Success(it) },
                onFailure = { RegisterUiState.Error(it.localizedMessage ?: "Registration failed") }
            )
        }
    }


    fun login(loginInformation: LoginInformation) {
        if (!validInput(loginInformation, true)) {
            return
        }

        _loginUiState.value = LoginUiState.Loading

        firebaseAuth.signInWithEmailAndPassword(loginInformation.email, loginInformation.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        val userDocRef = firestore.collection(
                            com.devvikram.varta.data.firebase.config.Firebase.FIREBASE_USER_COLLECTION
                        ).document(it.uid)

                        userDocRef.get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val userData = documentSnapshot.data

                                val fContact = documentSnapshot.toObject(FContact::class.java)

                                if (fContact != null) {
                                    viewModelScope.launch {
                                        val bitmap = AppUtils.downloadImage(fContact.profilePic)
                                        println(bitmap)
                                        val localProfilePicPath =
                                            bitmap?.let { it1 -> AppUtils.saveImageToStorage(it1, fContact.name) }

                                        val proContact = localProfilePicPath?.let { it1 ->
                                            ModelMapper.mapToFContact(fContact,
                                                it1
                                            )
                                        }

                                        if (proContact != null) {
                                            contactRepository.insertUserContact(proContact)
                                        }
                                    }
                                }
                                _loginUiState.value = LoginUiState.Success(
                                    LoginResponse(success = true, message = "Login successful", data = userData)
                                )
                                loginPreference.saveLoginInfo(
                                    userId = it.uid,
                                    loginSessionId = it.uid,
                                    loginSessionStatus = true
                                )
                            } else {
                                _loginUiState.value = LoginUiState.Error("User data not found")
                            }
                        }.addOnFailureListener { e ->
                            _loginUiState.value = LoginUiState.Error(e.localizedMessage ?: "Failed to fetch user data")
                        }
                    }
                }

                else {
                    _loginUiState.value = LoginUiState.Error(task.exception?.localizedMessage ?: "Login failed")
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


    fun resetLoginState() {
        _loginUiState.value = LoginUiState.Ideal
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
