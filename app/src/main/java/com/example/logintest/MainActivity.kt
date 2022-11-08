package com.example.pertemuan2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.pertemuan2.databinding.ActivityMainBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Flowable.combineLatest
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        processedLogin()
        onAction()

    }

    private fun onAction(){
        binding.apply {
            btnLogin.setOnClickListener {
                Intent(this@MainActivity, HomeActivity::class.java).also { intent ->  startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("checkResult")
    private fun processedLogin(){
        binding.apply {
            val emailstream = RxTextView.textChanges(edtEmail).skipInitialValue().map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

            emailstream.subscribe { showEmailExistAlert(it)

            }

            val passwordstream = RxTextView.textChanges(edtPassword)
                .skipInitialValue().map { password -> password.length < 8
                }
            passwordstream.subscribe { showPasswordExistAlert(it)

            }

            val invalidFieldStream = Observable.combineLatest(emailstream, passwordstream){
                emailInvalid,passwordInvalid -> !emailInvalid && !passwordInvalid
            }

            invalidFieldStream.subscribe {
                showButtonLogin(it)
            }
        }
    }

    private fun showButtonLogin(state: Boolean){
        binding.btnLogin.isEnabled = state
    }

    private fun showEmailExistAlert(state: Boolean){
        binding.edtEmail.error = if (state) "Email tidak valid" else null
    }

    private fun showPasswordExistAlert(state: Boolean) {
        binding.edtPassword.error = if (state) "Password kurang dari 8 karakter" else null
    }
}