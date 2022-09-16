package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myfirstapp.databinding.MarketplaceBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Marketplace : Fragment() {

    private var _binding: MarketplaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabLogout.setOnClickListener {
            findNavController().navigate(R.id.action_Marketplace_to_LoginScreen)
        }

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_HomeScreen_to_settingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
