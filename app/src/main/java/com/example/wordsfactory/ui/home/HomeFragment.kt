package com.example.wordsfactory.ui.home

import android.app.AlertDialog
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.wordsfactory.*
import com.example.wordsfactory.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var meaningItemsAdapter: MeaningItemsAdapter
    private lateinit var builder: AlertDialog.Builder

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var word = ""
    var phonetic = ""
    var audioURL = ""
    var partOfSpeech = ""
    var definitions = arrayListOf<Definitions>()
    val meanings = arrayListOf<String>()
    val examples = arrayListOf<String>()

    private val mediaPlayer = MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database= Room.databaseBuilder(requireActivity().applicationContext, WordDB::class.java,"words_database").fallbackToDestructiveMigration().build()
        builder = AlertDialog.Builder(requireActivity())


        binding.searchBtn.setOnClickListener{
            val retrofit=Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service=retrofit.create(DictionaryApi::class.java)
            val myResponse: MutableLiveData<Word> = MutableLiveData()
            lifecycleScope.launch (Dispatchers.IO){
                val enteredWord=binding.editTextTextSearch.text.toString()

                     if(!isNetwork()){
                         lifecycleScope.launch(Dispatchers.IO){
                             val words = database.wordDao().findWord(enteredWord)
                             val meanings = database.meaningDao().findMeanings(enteredWord)

                             if(words!==null){
                                 word = words.word
                                 phonetic = words.phonetic
                                 audioURL = words.audio
                                 partOfSpeech = words.partOfSpeech

                                 definitions.clear()
                                 meanings.forEach{
                                     definitions.add(Definitions(it.definition,it.example))
                                 }

                                 withContext(Dispatchers.Main){
                                     updateLayout()
                                 }
                             }
                             else{
                                 withContext(Dispatchers.Main){
                                     dialogCreate("No internet connection")
                                 }
                             }
                         }
                     }
                else {
                         try {
                             val result = service.getWord(enteredWord)
                             withContext(Dispatchers.Main) {
                                 myResponse.value = result[0]
                             }
                         }
                         catch(_:Throwable){
                             lifecycleScope.launch(Dispatchers.Main){
                                 dialogCreate("Word not found")
                             }
                         }
                     }
            }
            myResponse.observe(viewLifecycleOwner, Observer {
                word=it.word
                phonetic=it.phonetic
                audioURL=it.phonetics[0].audio
                partOfSpeech=it.meanings[0].partOfSpeech
                definitions=it.meanings[0].definitions
                meanings.clear()
                examples.clear()
                definitions.forEach {
                    val meaning=it.definition
                    meanings.add(meaning)

                    val example = it.example ?: ""
                    examples.add(example)
                }
                updateLayout()
            })
        }

        binding.imageSound.setOnClickListener {
            if(!isNetwork()){
                dialogCreate("No internet connection")
            }
            else{
                playAudio()
            }
        }

        binding.dictionaryBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val saveWord=database.wordDao().findWord(word)
                if(saveWord!=null)
                {
                    withContext(Dispatchers.Main) {
                        dialogCreate("Word has been added to the dictionary")
                    }
                }
                else{
                    database.wordDao()
                        .insertWord(WordEntity(word,phonetic,audioURL,partOfSpeech))
                         var i=0
                         meanings.forEach {
                             database.meaningDao()
                             .insertMeaning(MeaningEntity(0,word,it,examples[i]))
                             i++
                         }
                    }
            }
        }

        return root
    }

    private fun isNetwork(): Boolean {
        val connectivityManager = requireActivity().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun updateLayout () {
        binding.textWord.text = word
        binding.textTranscription.text = phonetic
        binding.textPartOfSpeech.text = partOfSpeech

        binding.titleTextPartOfSpeech.setText("Part of Speech")
        binding.textMeanings.setText("Meanings:")

        if (audioURL.isNotEmpty()) {
            binding.imageSound.setImageResource(R.drawable.sound)
        }
        else {
            binding.imageSound.setImageResource(0)
        }

        binding.recyclerViewMeanings.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMeanings.adapter = MeaningItemsAdapter(definitions)
    }

    private fun dialogCreate(message: String) {
        builder.setTitle("Error").
        setMessage(message).
        setCancelable(true).
        setPositiveButton("Ok") { dialog, id ->
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT)
        }.show()
    }

    private fun playAudio() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioURL)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}