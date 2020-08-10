package com.developerbreach.customermanager.model

import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
class Customers {

    private var baseId: Int = 0
    var billNumber: Int = 0
    var totalItems = 0
    lateinit var itemType: String
    lateinit var name: String
    lateinit var email: String
    lateinit var contact: String
    var status: Boolean = false
    lateinit var date: String

    constructor() {}

    constructor(
        baseId: Int,
        billNumber: Int,
        totalItems: Int,
        itemType: String,
        name: String,
        email: String,
        contact: String,
        status: Boolean,
        date: String
    ) {
        this.baseId = baseId
        this.billNumber = billNumber
        this.totalItems = totalItems
        this.itemType = itemType
        this.name = name
        this.email = email
        this.contact = contact
        this.status = status
        this.date = date
    }
}
