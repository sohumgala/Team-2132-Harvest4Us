package com.example.myfirstapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myfirstapp.databinding.RegisterScreenBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import java.io.IOException
import okhttp3.RequestBody


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterScreen : Fragment() {

    private var _binding: RegisterScreenBinding? = null

    val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()


    private val client = OkHttpClient()
    var responseString = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = RegisterScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val first = binding.etFirst.text
            val last = binding.etLast.text
            val username = binding.etUsername.text
            val password = binding.etPasswordRegister.text
            post("https://q74g0wn56a.execute-api.us-east-1.amazonaws.com/dev/new-user",
                "\"username\" : \"$username\", \"password\" : \"$password\", \"first_name\" : \"$first\", \"last_name\" : \"$last\""
            )
            findNavController().navigate(R.id.action_RegisterScreen_to_LoginScreen)
        }
    }

    fun post(url: String, json: String) {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                responseString = response.body?.string()!!
                println(responseString)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}