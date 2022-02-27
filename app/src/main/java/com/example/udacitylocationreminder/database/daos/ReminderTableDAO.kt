package com.example.udacitylocationreminder.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderTableDAO {

    @Insert(entity = ReminderTableEntity::class, onConflict = REPLACE)
    fun insertLocationReminder(reminderInfo: ReminderTableEntity)

    @Query("DELETE FROM location_reminder_table WHERE latitude = :latitude AND longitude = :longitude")
    fun deleteSpecificLocationReminder(latitude: Double, longitude: Double)

    @Query("UPDATE location_reminder_table SET description = :description WHERE latitude = :latitude AND longitude = :longitude")
    fun updateLocationReminder(latitude: Double, longitude: Double, description: String)

    @Query("SELECT * FROM location_reminder_table")
    fun getAllLocationReminder(): Flow<List<ReminderTableEntity>>

    @Query("SELECT * FROM location_reminder_table WHERE poi_address = :poiAddress")
    fun getSpecificLocationReminder(poiAddress: String): Flow<List<ReminderTableEntity>>
}