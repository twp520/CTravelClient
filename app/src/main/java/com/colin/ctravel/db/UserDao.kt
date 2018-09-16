package com.colin.ctravel.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.colin.ctravel.bean.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Long


    @Query("select * from t_user where id=:userId")
    fun getLoginUser(userId: Int): User
}