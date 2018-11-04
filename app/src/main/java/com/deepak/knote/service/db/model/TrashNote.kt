package com.deepak.knote.service.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

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