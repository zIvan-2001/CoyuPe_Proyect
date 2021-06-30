package edu.tecsup.coyupe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.tecsup.coyupe.databinding.ActivityMainBinding
import edu.tecsup.coyupe.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        binding.BtnLoginHome.setOnClickListener{
            HomeLogin()
        }

        binding.BottonRegister.setOnClickListener{
            RegistrarNewUser()
        }
    }

    private fun HomeLogin(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun RegistrarNewUser(){

        val email = binding.RegisterEmail.text.toString()
        val password = binding.RegisterPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){

            user.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity()) { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,
                            "User added successfully",
                            Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()

                    }else{
                        user.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener {mtask ->
                                if (mtask.isSuccessful){
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }else{
                                    Toast.makeText(this,
                                        task.exception!!.message,
                                        Toast.LENGTH_SHORT).show()
                                }
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