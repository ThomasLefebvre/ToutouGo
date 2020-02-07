package fr.thomas.lefebvre.toutougo.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import fr.thomas.lefebvre.toutougo.ui.MainActivity
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.ui.presentation.WelcomeActivity
import fr.thomas.lefebvre.toutougo.database.helper.UserHelper

class LoginActivity : AppCompatActivity() {

    lateinit var listProvider: List<AuthUI.IdpConfig>
    private val  userHelper=UserHelper()
    private val CODE_LOGIN: Int = 123
    private val currentUser=FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        listProvider = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        if (currentUser== null) {
            signInOptions()
        }
        else{
            launchActivity(currentUser)
        }
    }

    private fun signInOptions() {
            Log.d("LOGIN","Current user is nul")
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.AppTheme)
                    .setLogo(R.drawable.ic_dog)//TODO  MAKE LOGO
                    .setAvailableProviders(listProvider)
                    .setIsSmartLockEnabled(false)
                    .build(),
                CODE_LOGIN
            )
    }

    // --- CREATE USER TO DATABASE IF NOT ALREADY EXIST ---

    private fun launchActivity(currentUser: FirebaseUser?) {
            userHelper.getUserById(currentUser!!.uid).addOnSuccessListener { documentSnapshot ->
                if(!documentSnapshot.exists()){
                    Log.d("LOGIN","Create User no exist")
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }
                else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Log.d("LOGIN","Document snapshot exist")
                }
            }
        }


    // --- ACTIVITY LOGIN RESULT---

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                Log.d("LOGIN","Result OK")
                launchActivity(FirebaseAuth.getInstance().currentUser)

            } else {
                finish()
            }
        }
    }
}
