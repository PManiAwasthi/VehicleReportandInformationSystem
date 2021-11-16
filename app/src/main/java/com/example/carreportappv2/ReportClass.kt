package com.example.carreportappv2

class ReportClass {
    var uri: String
    var ownname: String
    var plateno: String
    var uid: String
    var recon: String
    var pin: String
    var loc: String
    var username: String
    var aadharuser: String
    var userno : String
    var ownno: String
    var status: String
    constructor(uri: String,
                ownname: String,
                plateno:String,
                uid:String,
                recon:String,
                pin:String,
                loc:String,
                username:String,
                aadharuser:String,
                userno:String,
                ownno:String,
                status:String){
        this.uri = uri
        this.ownname = ownname
        this.plateno = plateno
        this.uid = uid
        this.recon = recon
        this.pin = pin
        this.loc = loc
        this.username = username
        this.aadharuser = aadharuser
        this.userno = userno
        this.ownno = ownno
        this.status = status
    }

}