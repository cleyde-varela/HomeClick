package com.example.homesuveilapp_pdm.db

import androidx.room.Insert

interface BaseDao<T> {

    @Insert
    suspend fun insert(data: T) : Long
}