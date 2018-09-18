package com.colin.ctravel.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *create by colin 2018/9/13
 */
data class PostInfo(var id: Int = 0) : Parcelable {
    var title: String? = null
    var destination: String? = null
    var departure: String? = null
    var startTime: String? = null
    var contact: String? = null
    var content: String? = null
    var imgs: String? = null
    var createTime: String? = null
    var user: User? = null

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        title = parcel.readString()
        destination = parcel.readString()
        departure = parcel.readString()
        startTime = parcel.readString()
        contact = parcel.readString()
        content = parcel.readString()
        imgs = parcel.readString()
        createTime = parcel.readString()
        user = parcel.readParcelable(User::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(destination)
        parcel.writeString(departure)
        parcel.writeString(startTime)
        parcel.writeString(contact)
        parcel.writeString(content)
        parcel.writeString(imgs)
        parcel.writeString(createTime)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostInfo> {
        override fun createFromParcel(parcel: Parcel): PostInfo {
            return PostInfo(parcel)
        }

        override fun newArray(size: Int): Array<PostInfo?> {
            return arrayOfNulls(size)
        }
    }


}