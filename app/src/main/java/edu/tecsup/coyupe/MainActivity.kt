package edu.tecsup.coyupe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.tecsup.coyupe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var user: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object{
        const val RC_SINGIN = 25
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestIdToken(getString(R.string.client_id))
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        user=Firebase.auth


        /** Funcion MANTENER LOGEADO */
          SignCessionFine()

        binding.btnLogin.setOnClickListener{
            registerUser()
        }

        binding.ButtoRegister.setOnClickListener{
            registerUserCoyuPe()
        }

        binding.btnSignIn.setOnClickListener{
            doSingIn()
        }

    }

    private fun doSingIn(){
        val signInClient=googleSignInClient.signInIntent
        startActivityForResult(signInClient, RC_SINGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== RC_SINGIN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account=task.getResult(ApiException::class.java)
                doAuthetication(account!!.idToken)

            }catch (e: ApiException){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doAuthetication(idToken: String?){
        val credentials=GoogleAuthProvider.getCredential(idToken, null)

        user.signInWithCredential(credentials)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    startActivity(Intent(this,HomeActivity::class.java))
                }else{
                    Toast.makeText(this, "Auth Fail", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private  fun registerUserCoyuPe(){
        startActivity(Intent(this, RegistroActivity::class.java))
    }

/** Funcion para que cuando cierres la aplicacion se mantenga el login del usuario*/
    private fun SignCessionFine(){
        if (user.currentUser !=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun registerUser(){

        val email = binding.CorreoEdit.text.toString()
        val password = binding.PassEdit.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){

            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity()) { task ->
                        user.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener {mtask ->
                                if (mtask.isSuccessful){
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }else{

                                    Toast.makeText(this, "Tu contraseña es incorrecta o no existe este uruario", Toast.LENGTH_SHORT).show()
//                                    Toast.makeText(this,
//                                        task.exception!!.message,
//                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                }
        }else{
            Toast.makeText(this,
                "El campo correo y contraseña estan vacios",
                Toast.LENGTH_SHORT).show()
        }
    }
}