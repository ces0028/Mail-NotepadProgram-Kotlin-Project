package kr.or.mrhi.apps

import android.os.Parcel
import android.os.Parcelable

class NoteParcel(val dateTime: String, val content: String, val important: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString().toString(),
                                       parcel.readString().toString(),
                                       parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateTime)
        parcel.writeString(content)
        parcel.writeString(important)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteParcel> {
        override fun createFromParcel(parcel: Parcel): NoteParcel {
            return NoteParcel(parcel)
        }

        override fun newArray(size: Int): Array<NoteParcel?> {
            return arrayOfNulls(size)
        }
    }
}