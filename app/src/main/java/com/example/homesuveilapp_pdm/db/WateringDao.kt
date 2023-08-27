package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WateringDao : BaseDao<Watering> {

    @Query("select * from Watering where divisionID = :id")
    suspend fun getWateringAreas(id: Long) : List<Watering>

    @Query("delete from Watering where id = :areaID")
    suspend fun deleteArea(areaID : Long) : Int

    @Query("update Watering set areaStatus = :status where id = :id")
    suspend fun setStatus(status: DeviceStatus, id: Long) : Int

    @Query("update Watering set intensity = :intensity where id = :id")
    suspend fun setIntensity(intensity: Int, id: Long) : Int

    @Query("update Watering set areaMode = :mode where id = :id")
    suspend fun setMode(mode: WateringMode , id: Long) : Int

}