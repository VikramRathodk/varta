package com.devvikram.varta.data.firebase.repositories

import com.devvikram.varta.data.firebase.config.Firebase.FIREBASE_USER_COLLECTION
import com.devvikram.varta.data.firebase.models.FContact
import com.devvikram.varta.data.retrofit.models.LoginResponse
import com.devvikram.varta.data.retrofit.models.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseContactRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun registerUser(registerInformation: RegisterModel): Result<LoginResponse> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                registerInformation.username,
                registerInformation.password
            ).await()
            val user = authResult.user ?: return Result.failure(Exception("User creation failed"))

            val userData = FContact(
                name = registerInformation.username,
                email = registerInformation.username,
                profilePic = "",
                userId = user.uid,
                userStatus = true,
                gender = registerInformation.gender,
                statusText = registerInformation.statusText
            )

            firestore.collection(FIREBASE_USER_COLLECTION)
                .document(user.uid)
                .set(userData)
                .await()

            Result.success(LoginResponse(true, "Registration successful", null))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
