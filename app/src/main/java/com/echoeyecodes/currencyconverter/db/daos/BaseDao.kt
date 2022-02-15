package com.echoeyecodes.currencyconverter.db.daos

import androidx.room.*

abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addItem(item:T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addItems(item:List<T>)

    @Delete
    abstract suspend fun removeItem(item:T)

    @Update
    abstract suspend fun updateItem(item:T)

    @Update
    abstract suspend fun updateItems(item:List<T>)
}