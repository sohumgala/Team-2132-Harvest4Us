package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myfirstapp.databinding.LoginScreenBinding
import okhttp3.*

/**
 * The Login Screen fragment that acts as the default destination for the app and the main activity
 * to enter. Handles the API call to retrieve the username and password and checks if the user is
 * valid and accepted.
 */
class LoginScreen : Fragment() {
    // Binding to connect the fragment class with the layout
    private var _binding: LoginScreenBinding? = null

    // Client to handle API call
    private val client = OkHttpClient()

    // Response from API call
    var responseResult: Response? = null

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
            val welcome = getString(R.string.str_welcome)
            val exclamation = "!"
            val username = binding.etUserName.text
            val password = binding.etPassword.text
            // Run the API call
            responseResult = MockBackend.newUser(username, password)
            Thread.sleep(1700)

            // if inputted credentials match with database, enter the Marketplace
            // if username is correct, but password is not, notify user that password is incorrect
            // if username does not exist in the database, notify user
            if (responseResult?.code == 200) {
                Toast.makeText(activity, "$welcome $username$exclamation", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, MarketplaceActivity::class.java)
                intent.putExtra("username", username.toString())
                startActivity(intent)
            } else if (responseResult?.code == 401) {
                Toast.makeText(activity, "Invalid password", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "User does not exist", Toast.LENGTH_LONG).show()
            }
        }

        // Register button sends user to the register their username and password
        binding.btnRegister.setOnClickListener {
            view.findNavController().navigate(R.id.action_LoginScreen_to_RegisterScreen)
        }

        binding.forgotPassword.setOnClickListener {
            view.findNavController().navigate(R.id.action_LoginScreen_to_forgotPassword)
        }
    }

    // DEPRECATED: Previous team's backend call
//    fun run(url: String) {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response) {
//                responseResult = response
//            }
//        })
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
