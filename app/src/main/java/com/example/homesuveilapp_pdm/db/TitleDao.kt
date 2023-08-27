package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TitleDao : BaseDao<Title> {

    @Query("select * from Title")
    fun getTitles() : List<Title>
}