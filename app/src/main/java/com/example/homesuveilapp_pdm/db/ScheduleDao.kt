package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ScheduleDao : BaseDao<Schedule> {

    @Query("select * from Schedule where thermostatID = :id")
    suspend fun getThermostatSchedules(id: Long) : List<Schedule>

    @Query("delete from Schedule where id = :id")
    suspend fun deleteSchedule(id: Long): Int
}