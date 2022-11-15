package kr.or.mrhi.apps

import android.os.Parcel
import android.os.Parcelable

class SendParcel(val email: String, val profileName: String, val send: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString().toString(),
                                       parcel.readString().toString(),
                                       parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(profileName)
        parcel.writeString(send)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SendParcel> {
        override fun createFromParcel(parcel: Parcel): SendParcel {
            return SendParcel(parcel)
        }

        override fun newArray(size: Int): Array<SendParcel?> {
            return arrayOfNulls(size)
        }
    }
}