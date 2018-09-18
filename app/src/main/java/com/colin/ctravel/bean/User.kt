package com.colin.ctravel.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Nullable

/**
 *create by colin
 */
@Entity(tableName = "t_user", primaryKeys = ["id"])
data class User(var id: Int = 0) : Parcelable {

    @Ignore constructor() : this(0)

    var account: String = ""
    var nickname: String = ""
    var gender: Int = 0
    @Nullable
    var headUrl: String = ""
    var fromWx: Boolean = false
    var token: String = ""

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        account = parcel.readString()
        nickname = parcel.readString()
        gender = parcel.readInt()
        headUrl = parcel.readString()
        fromWx = parcel.readByte() != 0.toByte()
        token = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(account)
        dest?.writeString(nickname)
        dest?.writeInt(gender)
        dest?.writeString(headUrl)
        dest?.writeByte(if (fromWx) 1.toByte() else 0.toByte())
        dest?.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }
}