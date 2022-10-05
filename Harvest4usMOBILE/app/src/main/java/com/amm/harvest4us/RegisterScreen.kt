package com.amm.harvest4us

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amm.harvest4us.databinding.RegisterScreenBinding

/**
 *
 */
class RegisterScreen : Fragment() {

    private var _binding: RegisterScreenBinding? = null

    private val backend = FlaskBackend
    var responseString = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val first = binding.etFirst.text.toString()
            val last = binding.etLast.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPasswordRegister.text.toString()
            val responseHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        401 -> {
                            Toast.makeText(activity, "User already exists. Please login or choose a different username.", Toast.LENGTH_LONG).show()
                        }
                        200 -> {
                            Toast.makeText(activity, "User ${msg.obj} created successfully.", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_RegisterScreen_to_LoginScreen)
                        }
                        else -> {
                            Toast.makeText(activity, "Failed to connect to server.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            backend.newUser(username, password, first, last, responseHandler)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
