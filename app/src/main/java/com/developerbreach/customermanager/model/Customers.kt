package com.developerbreach.customermanager.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Customers : Parcelable {

    var billNumber: String? = null
    var totalItems: String? = null
    var itemType: String? = null
    var name: String? = null
    var email: String? = null
    var contact: String? = null
    var status: Boolean = false
    var date: String? = null

    // Requires empty constructor for firestore
    constructor()

    internal constructor(
        billNumber: String?,
        totalItems: String?,
        itemType: String?,
        name: String?,
        email: String?,
        contact: String?,
        status: Boolean?,
        date: String?
    ) {
        this.billNumber = billNumber
        this.totalItems = totalItems
        this.itemType = itemType
        this.name = name
        this.email = email
        this.contact = contact
        this.status = status!!
        this.date = date
    }

    private constructor(`in`: Parcel) {
        billNumber = `in`.readString()
        totalItems = `in`.readString()
        itemType = `in`.readString()
        name = `in`.readString()
        email = `in`.readString()
        contact = `in`.readString()
        status = `in`.readBoolean()
        date = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(billNumber)
        dest.writeString(totalItems)
        dest.writeString(itemType)
        dest.writeString(name)
        dest.writeString(email)
        dest.writeString(contact)
        dest.writeBoolean(status)
        dest.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<Customers> {
        override fun createFromParcel(parcel: Parcel): Customers {
            return Customers(parcel)
        }

        override fun newArray(size: Int): Array<Customers?> {
            return arrayOfNulls(size)
        }
    }
}
