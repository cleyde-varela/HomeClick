package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WateringScheduleDao: BaseDao<WateringSchedule> {

    @Query("select * from WateringSchedule where wateringID = :id")
    suspend fun getWateringSchedules(id: Long) : List<WateringSchedule>

    @Query("delete from WateringSchedule where id = :id")
    suspend fun deleteSchedule(id: Long): Int
}