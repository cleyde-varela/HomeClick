package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DoorDao : BaseDao<Door> {

    @Query("select * from Door where divisionID = :id")
    suspend fun getDivisionDoors(id: Long) : List<Door>

    @Query("delete from Door where id = :doorID")
    suspend fun deleteDoor(doorID : Long) : Int

    @Query("update Door set doorStatus = :status where id = :id")
    suspend fun setStatus(status: DeviceStatus, id: Long) : Int
}