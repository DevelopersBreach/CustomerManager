package com.developerbreach.customermanager.model

import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
class Customers {

    private var baseId: Int = 0
    var billNumber: Int = 0
    lateinit var name: String
    lateinit var contact: String
    lateinit var date: String

    constructor() {}

    constructor(baseId: Int, billNumber: Int, name: String, contact: String, date: String) {
        this.baseId = baseId
        this.billNumber = billNumber
        this.name = name
        this.contact = contact
        this.date = date
    }
}
