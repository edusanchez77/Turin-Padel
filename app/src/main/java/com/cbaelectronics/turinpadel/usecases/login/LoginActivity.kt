package com.cbaelectronics.turinpadel.usecases.login

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityLoginBinding
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.usecases.home.HomeRouter
import com.cbaelectronics.turinpadel.util.Constants.TYPE_ADMIN
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.itdev.nosfaltauno.util.extension.font
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Created by CbaElectronics by Eduardo Sanchez on 10/05/2022.
 * www.cbaelectronics.com.ar
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var mToken: String? = null
    private lateinit var gso: GoogleSignInOptions
    private lateinit var auth: FirebaseAuth

    private val responseActivity =
        registerForActivityResult(StartActivityForResult()) { activityResult ->
            response(activityResult)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)
        supportActionBar?.hide()

        // ViewModel
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Setup
        getToken()
        instanceGoogle()
        instanceFirebaseAuth()
        localize()
        setup()
    }

    private fun localize() {
        binding.textViewLoginTitle.text = getString(viewModel.titleText)
        binding.textViewLoginBody.text = getString(viewModel.bodyText)
        binding.buttonLogin.text = getString(viewModel.buttonText)

    }

    private fun setup() {

        // UI

        binding.textViewLoginTitle.font(
            FontSize.TITLE,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )
        binding.textViewLoginBody.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )

        // Buttons

        buttons()
    }

    private fun buttons() {
        binding.buttonLogin.setOnClickListener {
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()
            signIn(googleSignInClient)
        }
    }

    private fun instanceGoogle() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    }

    private fun instanceFirebaseAuth() {
        auth = Firebase.auth
    }

    private fun signIn(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        responseActivity.launch(signInIntent)
    }

    private fun getToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                val message = getString(viewModel.errorToken)
                Log.w("Message", message, task.exception)
                return@OnCompleteListener
            }
            mToken = task.result.toString()
        })
    }

    private fun response(activityResult: ActivityResult) {

        val mProgress = Dialog(this)

        when (activityResult.resultCode) {

            RESULT_OK -> {
                val message = getString(viewModel.alertWait)
                mProgress.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mProgress.setContentView(R.layout.custom_loading)
                mProgress.setCanceledOnTouchOutside(false)
                mProgress.show()

                val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

                try {

                    val account = task.getResult(ApiException::class.java)

                    if (account != null) {

                        val mCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                        auth.signInWithCredential(mCredential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    mProgress.dismiss()
                                    loadUser(auth.currentUser?.email!!)
                                }
                            }

                    } else {
                        mProgress.dismiss()
                        val mMessage = getString(viewModel.alertErrorUser)
                        Toast.makeText(this, mMessage, Toast.LENGTH_SHORT).show()
                    }

                } catch (e: ApiException) {
                    val error = getString(viewModel.alertErrorUser)
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    println("ERRORRR: ${e.message}")
                }
            }
            else -> {
                Log.d("HOME: ", "Else")
            }

        }

    }

    private fun loadUser(email: String) = runBlocking{

        withContext(Dispatchers.Default) {
            viewModel.load(email)
        }

        if(viewModel.user.email == null){
            saveDatabase()
        }else{
            data()
            showHome()
        }

    }


    private fun saveDatabase() {

        val user = User(
            auth.currentUser?.displayName,
            auth.currentUser?.email,
            auth.currentUser?.photoUrl.toString(),
            mToken,
            TYPE_ADMIN
        )

        val settings = UserSettings()

        viewModel.user = user
        viewModel.user.settings = settings

        viewModel.saveUser(user)

        data()
        showHome()

    }

    private fun data() {

        viewModel.save(this)
        viewModel.configure(this)

    }

    private fun showHome() {
        HomeRouter().launch(this)
    }

}