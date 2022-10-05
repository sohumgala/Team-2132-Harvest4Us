package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amm.harvest4us.databinding.LoginScreenBinding

/**
 * The Login Screen fragment that acts as the default destination for the app and the main activity
 * to enter. Handles the API call to retrieve the username and password and checks if the user is
 * valid and accepted.
 */
class LoginScreen : Fragment() {
    // Binding to connect the fragment class with the layout
    private var _binding: LoginScreenBinding? = null
    private var backend = FlaskBackend

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reset button clears out the username and password fields
        binding.btnReset.setOnClickListener {
            binding.etUserName.setText("")
            binding.etPassword.setText("")
        }

        // Login button runs API call, then checks the credentials against database
        binding.btnLogin.setOnClickListener {
            val responseHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        200 -> onLoginAuthenticated(msg.obj as String)
                        401 -> onLoginAuthenticationFailure()
                        else -> onLoginRequestNoConnection()
                    }
                }
            }
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            backend.login(username, password, responseHandler)
        }

        // Register button sends user to the register their username and password
        binding.btnRegister.setOnClickListener {
            view.findNavController().navigate(R.id.action_LoginScreen_to_RegisterScreen)
        }

        binding.forgotPassword.setOnClickListener {
            view.findNavController().navigate(R.id.action_LoginScreen_to_forgotPassword)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* Callbacks to handle login attempts. */
    private fun onLoginRequestNoConnection() {
        Toast.makeText(activity, "Failed to connect to server.", Toast.LENGTH_LONG).show()
    }

    private fun onLoginAuthenticated(username: String) {
        val welcome = getString(R.string.str_welcome)
        Toast.makeText(activity, "$welcome $username!", Toast.LENGTH_LONG).show()
        val intent = Intent(activity, MarketplaceActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun onLoginAuthenticationFailure() {
        Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_LONG).show()
    }

}
