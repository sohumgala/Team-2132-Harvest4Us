package com.example.educationalresources_demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.educationalresources_demo.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article1: CardView = view.findViewById(R.id.article_1)
        article1.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ams.usda.gov/local-food-directories/farmersmarkets"))
            startActivity(browserIntent)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}