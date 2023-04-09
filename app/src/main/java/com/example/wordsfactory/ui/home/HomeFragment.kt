package com.example.wordsfactory.ui.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wordsfactory.DictionaryApi
import com.example.wordsfactory.MeaningItemsAdapter
import com.example.wordsfactory.ThirdActivity
import com.example.wordsfactory.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var meaningItemsAdapter: MeaningItemsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val retrofit=Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()






        val service=retrofit.create(DictionaryApi::class.java)
        lifecycleScope.launch (Dispatchers.IO){
            var result=service.getWordMeaning("cat")
            Log.d("HomeFragment",result.toString())
            }

        //val sharedPreferences=activity?.getSharedPreferences(
         //   getString(R.string.preference_file_key), Context.MODE_PRIVATE
       // )
      //  "com.example"
        val sharedPreferences=activity?.getSharedPreferences("my_shared_pref",MODE_PRIVATE)
        val editor=sharedPreferences?.edit()
        editor?.putString("my_name","Alex")
        editor?.apply()

        val value=sharedPreferences?.getString("my_name","")!!
        Log.d("MainActivity",value)






        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}