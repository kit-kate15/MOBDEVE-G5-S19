package com.mobdeve.s19.group5.mco.main

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.mobdeve.s19.group5.mco.main.databinding.ActivityLoginBinding



class LoginActivity: ComponentActivity() {
    //get login button
    private lateinit var loginBtn: Button
    private lateinit var getSignInGoogle: GetSignInWithGoogleOption

//    private val credentialLauncher =
//        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val credentialResponse: GetCredentialResponse? =
//                    result.data?.let { GetCredentialResponse.fromIntent(it) }
//
//                if (credentialResponse != null) {
//                    handleSignIn(credentialResponse)
//                } else {
//                    Log.e(TAG, "Credential response is null.")
//                }
//            } else {
//                Log.e(TAG, "Credential request failed with result code: ${result.resultCode}")
//            }
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        loginBtn = findViewById(R.id.loginBtn)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("781197963271-v472hku9cd17os4cnftq3h76h4tcov6b.apps.googleusercontent.com")
            .setAutoSelectEnabled(true)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewBinding.googleButton.setOnClickListener {
            try{

            } catch(e: Exception){
                e.printStackTrace()
            }
        }


        //set on click listener for login button
        loginBtn.setOnClickListener {
            //start main activity
            finish()
        }
    }
}