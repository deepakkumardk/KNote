package com.deepak.knote.service.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "notes")
data class Note(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        var id: Int = 0,
        var noteTitle: String,
        var noteContent: String
)