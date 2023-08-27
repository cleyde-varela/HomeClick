package com.example.homesuveilapp_pdm.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/* Division's data class Req.4 */
@Entity
data class Division(
    val divisionName: String,
    val divisionType: DivisionType,
    var imgURI: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Light's data class */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Division::class,
        parentColumns = ["id"],
        childColumns = ["divisionID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Light(
    val divisionID: Long,
    val lightName: String,
    var lightStatus: DeviceStatus,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Door's data class */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Division::class,
        parentColumns = ["id"],
        childColumns = ["divisionID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Door(
    val divisionID: Long,
    val doorName: String,
    val doorStatus: DeviceStatus,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Thermostat's data class */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Division::class,
        parentColumns = ["id"],
        childColumns = ["divisionID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Thermostat(
    val divisionID: Long,
    val thermostatName: String,
    val thermostatStatus: DeviceStatus,
    val temperature: Int,
    val thermostatMode: ThermostatMode,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Watering system's data class */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Division::class,
        parentColumns = ["id"],
        childColumns = ["divisionID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Watering(
    val divisionID: Long,
    val areaName: String,
    val areaStatus: DeviceStatus,
    val intensity: Int,
    val areaMode: WateringMode,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Title's data class */
@Entity
data class Title(
    val title : String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Schedule's class */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Thermostat::class,
        parentColumns = ["id"],
        childColumns = ["thermostatID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Schedule(
    val thermostatID: Long,
    val name: String,
    var thermostatMode: ThermostatMode,
    var modeTemperature: Int,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Watering schedule's class */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Watering::class,
        parentColumns = ["id"],
        childColumns = ["wateringID"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WateringSchedule(
    val wateringID: Long,
    val name: String,
    var wateringMode: WateringMode,
    var intensity: Int,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

/* Different division types */
enum class DivisionType { ROOM, BATHROOM, LIVING_ROOM, GARAGE, GARDEN, KITCHEN }

/* Device's standard status */
enum class DeviceStatus { ON, OFF }

/* Modes in which the thermostat can function */
enum class ThermostatMode { COOL, HEAT, FAN, MODE }

/* Modes in which the watering system can function */
enum class WateringMode { MOVING, FIXED, MODE }

