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

        val article2: CardView = view.findViewById(R.id.article_2)
        article2.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.localharvest.org"))
            startActivity(browserIntent)
        })

        val article3: CardView = view.findViewById(R.id.article_3)
        article3.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fns.usda.gov/snap/supplemental-nutrition-assistance-program"))
            startActivity(browserIntent)
        })

        val article4: CardView = view.findViewById(R.id.article_4)
        article4.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fns.usda.gov/wic"))
            startActivity(browserIntent)
        })

        val article5: CardView = view.findViewById(R.id.article_5)
        article5.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fns.usda.gov/sfmnp/senior-farmers-market-nutrition-program"))
            startActivity(browserIntent)
        })

        val article6: CardView = view.findViewById(R.id.article_6)
        article6.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://findfoodsupport.withgoogle.com"))
            startActivity(browserIntent)
        })

        val article7: CardView = view.findViewById(R.id.article_7)
        article7.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nutrition.gov/topics/basic-nutrition/healthy-eating"))
            startActivity(browserIntent)
        })

        val article8: CardView = view.findViewById(R.id.article_8)
        article8.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.heart.org/en/healthy-living/healthy-eating"))
            startActivity(browserIntent)
        })

        val article9: CardView = view.findViewById(R.id.article_9)
        article9.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://freedge.org/locations/"))
            startActivity(browserIntent)
        })

        val article10: CardView = view.findViewById(R.id.article_10)
        article10.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blackchurchfoodsecurity.net/become-a-member/"))
            startActivity(browserIntent)
        })

        val article11: CardView = view.findViewById(R.id.article_11)
        article11.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blackfarmersnetwork.com/category/education/"))
            startActivity(browserIntent)
        })
        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}