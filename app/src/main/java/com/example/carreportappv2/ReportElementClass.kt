package com.example.carreportappv2

class ReportElementClass {
    var uri: String
    var ownname: String
    var status: String
    var incharge: String
    var recon: String
    var report_num: Int
    var vehicle_uid: String

    constructor(uri: String, ownname: String, status: String, incharge: String, recon: String, report_num: Int, vehicle_uid: String){
        this.uri = uri
        this.ownname = ownname
        this.status = status
        this.incharge = incharge
        this.recon = recon
        this.report_num = report_num
        this.vehicle_uid = vehicle_uid
    }
}