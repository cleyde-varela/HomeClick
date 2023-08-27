package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ThermostatDao : BaseDao<Thermostat> {

    @Query("select * from Thermostat where divisionID = :id")
    suspend fun getDivisionThermostat(id: Long) : List<Thermostat>

    @Query("delete from Thermostat where id = :thermostatID")
    suspend fun deleteThermostat(thermostatID : Long) : Int

    @Query("update Thermostat set thermostatStatus = :status where id = :id")
    suspend fun setStatus(status: DeviceStatus, id: Long) : Int

    @Query("update Thermostat set temperature = :temperature where id = :id")
    suspend fun setTemperature(temperature: Int, id: Long) : Int

    @Query("update Thermostat set thermostatMode = :mode where id = :id")
    suspend fun setMode(mode: ThermostatMode, id: Long) : Int
}