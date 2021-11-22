package com.example.carreportappv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_view_reports.*

class ViewReports : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)

        var reportList = ArrayList<ReportElementClass>()
        try {
            var sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
            var pincode = sharedPreferences.getString("station_pincode","")
            if (pincode == "" || pincode == null){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Please update your station Pincode to proceed further")
                builder.setPositiveButton("yes"){
                    dialog, which->
                    finish()
                }
                builder.setCancelable(false)
                builder.show()
            }else{
                val reportsUrl = getString(R.string.ip) + "/carreportsystem/get_reports_normal_view.php?pincode=" + pincode
                val requestQ = Volley.newRequestQueue(this@ViewReports)
                var jsonAR =  JsonArrayRequest(Request.Method.GET, reportsUrl, null, {
                    response ->
                    for(reportJOIndex in 0.until(response.length())){
                        reportList.add(ReportElementClass(response.getJSONObject(reportJOIndex).getString("report_image_uri"),
                                response.getJSONObject(reportJOIndex).getString("name_owner"),
                                response.getJSONObject(reportJOIndex).getString("status"),
                                response.getJSONObject(reportJOIndex).getString("incharge_id"),
                                response.getJSONObject(reportJOIndex).getString("report_content_title"),
                                response.getJSONObject(reportJOIndex).getInt("report_num"),
                                response.getJSONObject(reportJOIndex).getString("vehicle_uid")
                        ))
                    }

                    val rAdapter = ReportViewAdapter(this@ViewReports, reportList)
                    recyclerViewViewReports.layoutManager = LinearLayoutManager(this@ViewReports)
                    recyclerViewViewReports.adapter = rAdapter
                },{
                    error ->
                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setTitle("message")
                    dialogBuilder.setMessage(error.message)
                    dialogBuilder.create().show()
                })
                requestQ.add(jsonAR)
            }
        }catch (e : Exception){
            Log.d("hello", e.message.toString())
        }
    }
}