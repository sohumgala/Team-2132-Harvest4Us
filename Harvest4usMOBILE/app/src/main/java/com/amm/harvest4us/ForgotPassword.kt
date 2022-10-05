package com.amm.harvest4us

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amm.harvest4us.databinding.FragmentForgotPasswordBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.IOException

class ForgotPassword : Fragment() {
    // Binding to connect the fragment class with the layout
    private var _binding: FragmentForgotPasswordBinding? = null

    // Client to handle API call
    private val client = OkHttpClient()

    // Response from API call
    var responseResult: Response? = null

    val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    var responseString = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submit.setOnClickListener {
            val email = binding.email.text
            val password = binding.password.text

            post(
                "https://q74g0wn56a.execute-api.us-east-1.amazonaws.com/dev/reset-passwword/",
                "\"username\" : \"$email\", \"password\" : \"$password\""
            )

            Thread.sleep(1500)
            println("CODE: " + responseResult?.code)
            if (responseResult?.code == 200) {
                Toast.makeText(this.activity, "Verification email sent", Toast.LENGTH_SHORT).show()
            } else {
                println(responseResult)
                Toast.makeText(this.activity, "Error with verification", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                responseResult = response
            }
        })
    }
}
