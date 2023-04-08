package com.example.wordsfactory

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class, Meanings::class], version = 1)
abstract class WordDB: RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun meaningDao(): MeaningDao
}