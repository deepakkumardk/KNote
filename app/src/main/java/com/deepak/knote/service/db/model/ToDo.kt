package com.deepak.knote.service.db.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * To-Do model class for Room
 */
@Entity(tableName = "todo")
data class ToDo(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        var id: Int = 0,
        var todoTitle: String,
        var todoDescription: String
)