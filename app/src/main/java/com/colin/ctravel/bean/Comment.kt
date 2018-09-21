package com.colin.ctravel.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *create by colin 2018/9/21
 */
data class Comment(var id: Int) : Parcelable {

    var content: String = ""

    var postId: Int = -1

    var sendUid: Int = -1

    var atUid: Int = -1

    var time: Long = 0L

    var ownUser: User? = null

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        content = parcel.readString()
        postId = parcel.readInt()
        sendUid = parcel.readInt()
        atUid = parcel.readInt()
        time = parcel.readLong()
        ownUser = parcel.readParcelable(User::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(content)
        parcel.writeInt(postId)
        parcel.writeInt(sendUid)
        parcel.writeInt(atUid)
        parcel.writeLong(time)
        parcel.writeParcelable(ownUser, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }

}