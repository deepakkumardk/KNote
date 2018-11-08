package com.deepak.knote.service.db.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TrashNote model class for Room
 */
@Entity(tableName = "trash_notes")
data class TrashNote(
        @PrimaryKey
        @NonNull
        var id: Int = 0,
        var noteTitle: String,
        var noteContent: String
)