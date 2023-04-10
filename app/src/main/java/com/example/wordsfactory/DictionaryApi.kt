package com.example.wordsfactory

import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("{word}")
    suspend fun getWord(@Path("word") word: String): ArrayList<Word>
}