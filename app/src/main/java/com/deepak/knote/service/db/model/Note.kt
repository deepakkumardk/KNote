package com.deepak.knote.service.db.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Note model class for Room
 */
@Parcelize
@Entity(tableName = "notes")
data class Note(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        var id: Int = 0,
        var noteTitle: String,
        var noteContent: String
) : Parcelable