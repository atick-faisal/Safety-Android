package dev.atick.storage.room.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "high_risk") val highRisk: Boolean = false,
    @ColumnInfo(name = "selected") val selected: Boolean = false
)
