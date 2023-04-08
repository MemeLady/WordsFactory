package com.example.wordsfactory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity class MeaningEntity (@PrimaryKey(autoGenerate = true) val Id: Int=0, val word: String,  val definitions: String, val example: String)