package com.example.homesuveilapp_pdm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Division::class, Light::class, Door::class, Thermostat::class, Watering::class, Title::class, Schedule::class, WateringSchedule::class], version = 9, exportSchema = false)
abstract class HomeClickDB : RoomDatabase() {

    abstract fun divisionsDao() : DivisionDao
    abstract fun lightsDao(): LightsDao
    abstract fun doorDao(): DoorDao
    abstract fun thermostatDao(): ThermostatDao
    abstract fun wateringDao(): WateringDao
    abstract fun titleDao(): TitleDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun wateringScheduleDao(): WateringScheduleDao

    companion object {
        @Volatile private var instance: HomeClickDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it}}

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, HomeClickDB::class.java, "homeClickDB.db")
                .fallbackToDestructiveMigration()
                //.allowMainThreadQueries()  Req.5
                .build()
    }
}