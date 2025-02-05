package com.devvikram.varta.data.retrofit


import com.devvikram.varta.data.retrofit.models.LoginInformation
import com.devvikram.varta.data.retrofit.models.LoginResponse
import com.devvikram.varta.data.retrofit.models.RegisterModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppApiService {

    @POST("prochat_registration_new.php")
    suspend fun register(@Body registerInformation: RegisterModel): Response<LoginResponse>


    @POST("login-mobile")
    suspend fun login(@Body loginInformation: LoginInformation): Response<LoginResponse>

    @GET("get-internal-connections/{userId}")
    suspend fun getInternalContacts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<ProContactsResponse>

    @GET("get-external-connections/{userId}")
    suspend fun getExternalContacts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<ProContactsResponse>

//    @POST("register-mobile")
//    suspend fun register(@Body registerInformation: LoginInformation): Response<LoginResponse>


//
//    @PUT("update-token")
//    suspend fun updateUserToken(@Body userTokenBody: UserTokenBody): Response<UserTokenResponse>
//
//    @POST("send-notification")
//    suspend fun sendNotification(@Body pushNotificationBody: PushNotificationBody): Response<PushNotificationResponse>

}