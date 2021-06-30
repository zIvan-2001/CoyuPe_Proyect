package edu.tecsup.coyupe

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.tecsup.coyupe.R
import edu.tecsup.coyupe.databinding.ActivityHomeBinding

class   HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var user: FirebaseAuth
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var googleSignInClient: GoogleSignInClient

//    val EmailUser:TextView=findViewById(R.id.EmailUsers)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestIdToken(getString(R.string.client_id))
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso)



        toogle = ActionBarDrawerToggle(this, binding.draweLayout, R.string.open_drawer, R.string.close_drawer)
        binding.draweLayout.addDrawerListener(toogle)

        toogle.syncState()

        user = FirebaseAuth.getInstance()



//        if (user.currentUser != null){
//            user.currentUser?.let {
//                EmailUser.text = it.email
//            }
//        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.NavContainer.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home ->   {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer,HomeFragment() )
                        commit()
                    }
                }

                R.id.nav_contact ->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, ContactFragment())
                        commit()
                    }
                }

                R.id.nav_help ->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, HelpFragment())
                        commit()
                    }
                }

                R.id.nav_ubication ->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, LocationFragment())
                        commit()
                    }
                }

                R.id.nav_carrito ->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainer, ShoppingFragment())
                        commit()
                    }
                }

                R.id.nav_SignUp -> {
                    user.signOut()
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )

                    Firebase.auth.signOut()
                    googleSignInClient.signOut().addOnCompleteListener(this){
                        finish()
                    }
                }
//                    Toast.makeText(this, "Salte D:", Toast.LENGTH_SHORT).show()

            }
            binding.draweLayout.closeDrawer(GravityCompat.START)
            true
        }

//        val homeFragment = HomeFragment()
//        val locationFragment = LocationFragment()
//        val shoppingFragment = ShoppingFragment()
//
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.nav_home ->{
//                    setCurrentFragment(homeFragment)
//                    true
//                }
//
//                R.id.nav_ubication->{
//                    setCurrentFragment(locationFragment)
//                    true
//                }
//
//                R.id.nav_carrito ->{
//                    setCurrentFragment(shoppingFragment)
//                    true
//                }
//                else ->false
//            }
//
//        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}