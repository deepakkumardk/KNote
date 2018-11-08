package com.deepak.knote.service.db.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Note model class for Room
 */
@Entity(tableName = "notes")
data class Note(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        var id: Int = 0,
        var noteTitle: String,
        var noteContent: String
)