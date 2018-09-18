package com.colin.ctravel.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.colin.ctravel.bean.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Long


    @Query("select * from t_user where id=:userId")
    fun selectUserById(userId: Int): User?

    @Update
    fun updateUser(user: User): Int
}