package com.example.homesuveilapp_pdm.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DivisionDao : BaseDao<Division> {

    @Query("select * from Division")
    fun getAllDivisionsFlow() : Flow<List<Division>>

    @Query("delete from Division where id = :divisionID")
    suspend fun deleteDivision(divisionID : Long) : Int

    @Query("update Division set imgURI = :imgURI where id = :id")
    suspend fun setImageUri(imgURI: String, id: Long)
}