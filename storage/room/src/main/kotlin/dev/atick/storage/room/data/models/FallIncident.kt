package dev.atick.storage.room.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "falls")
data class FallIncident(
    @ColumnInfo(name = "victim_name") val victimName: String,
    @ColumnInfo(name = "high_risk") val highRisk: Boolean = false,
    @ColumnInfo(name = "read_by_user") val readByUser: Boolean = false,
    @PrimaryKey val timestamp: Long = Date().time
)
