package com.example.axrorxoja.feedlist.storage.db.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter fun timeStamp2Date(timeStamp: Long): Date = Date(timeStamp)

    @TypeConverter fun date2TimeStamp(date: Date): Long = date.time
}