package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LightsDao : BaseDao<Light> {

    @Query("select * from Light where divisionID = :id")
    suspend fun getDivisionLights(id: Long) : List<Light>

    @Query("delete from Light where id = :lightID")
    suspend fun deleteLight(lightID : Long) : Int

    @Query("update Light set lightStatus = :status where id = :id")
    suspend fun setStatus(status: DeviceStatus, id: Long) : Int
}