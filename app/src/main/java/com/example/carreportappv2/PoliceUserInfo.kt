package com.example.carreportappv2

class PoliceUserInfo {
    var name: String? = null
    var email: String? = null
    var aadharNum: String? = null
    var mobileNum: String? = null
    var address: String? = null
    var uid: String? = null
    var stationPincode: String? = null
    fun insertValues(name: String, email:String, aadharNum:String, mobileNum: String, address: String, uid: String, stationPincode: String){
        this.name = name
        this.email = email
        this.aadharNum = aadharNum
        this.mobileNum = mobileNum
        this.address = address
        this.uid = uid
        this.stationPincode = stationPincode
    }
}