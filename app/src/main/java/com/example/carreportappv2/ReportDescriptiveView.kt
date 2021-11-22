package com.example.carreportappv2

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TabHost
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_report_descriptive_view.*
import kotlinx.android.synthetic.main.activity_report_vehicle_user.*
import kotlinx.android.synthetic.main.report_element.view.*
import org.json.JSONObject

class ReportDescriptiveView : AppCompatActivity() {
    var authentic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_descriptive_view)
        var rno = intent.getIntExtra("rno", 0)
        var vno: String? = intent.getStringExtra("vno")

        var sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
        var stationPincode = sharedPreferences.getString("station_pincode","").toString()


        var host = findViewById<TabHost>(R.id.tabHostReportDescriptiveView)
        host.setup()

        var spec = host.newTabSpec("Report Info")
        spec.setContent(R.id.tab1)
        spec.setIndicator("Report Info")
        host.addTab(spec)

        spec = host.newTabSpec("Vehicle Info")
        spec.setContent(R.id.tab2)
        spec.setIndicator("Vehicle Info")
        host.addTab(spec)

        spec = host.newTabSpec("Progress")
        spec.setContent(R.id.tab3)
        spec.setIndicator("Progress")
        host.addTab(spec)


        val dataUrl = getString(R.string.ip) + "/carreportsystem/get_report_des_view.php?vno=" + vno +
                "&rno=" + rno
        val requestQ = Volley.newRequestQueue(this@ReportDescriptiveView)
        val jsonAR = JsonArrayRequest(Request.Method.GET, dataUrl, null, {
            response ->
            val jsonObject = response.getJSONObject(0)
            setValues(jsonObject)
        },{

        })
        requestQ.add(jsonAR)

        btnReportDescriptiveViewConclusionSubmitButton.setOnClickListener { submitConclusion(rno, stationPincode) }
    }

    fun submitConclusion(rno: Int, stationPincode: String){
        if(editTextReportDescriptiveViewReportConclusion.text.toString() != ""){
            val submissionUrl = getString(R.string.ip) + "/carreportsystem/set_report_conclusion.php?" +
                    "rno=" + rno +
                    "&pin=" + stationPincode +
                    "&con=" + editTextReportDescriptiveViewReportConclusion.text.toString()
            Log.d("hello", submissionUrl)
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Please fill the conclusion section")
            builder.setPositiveButton("yes"){
                dialog, which->
            }
            builder.setCancelable(false)
            builder.show()
        }


    }

    fun setValues(jsonObject: JSONObject){
        //block1
        Picasso.get().load(Uri.parse(getString(R.string.ip) + jsonObject.getString("vehicle_image_uri"))).
        into(imgViewReportDescriptiveViewCarImage)

        Picasso.get().load(Uri.parse(getString(R.string.ip) + jsonObject.getString("owner_image_uri"))).
        into(imgViewReportDescriptiveViewOwnerImage)

        txtViewReportDescriptiveViewOwnerName.text = jsonObject.getString("owner_name")

        txtViewReportDescriptiveViewCarPlateNumber.text = jsonObject.getString("model_name")

        //block2 layout1
        Picasso.get().load(Uri.parse(getString(R.string.ip) + jsonObject.getString("report_image_uri"))).into(imgViewReportDescriptiveViewReportedCarImage)

        val locationValue = jsonObject.getString("location")
        txtViewReportDescriptiveViewReportTitle.text = jsonObject.getString("report_content_title")

        txtReportDescriptiveViewReportContent.text = jsonObject.getString("report_content")

        txtViewReportDescriptiveViewCarModelName.text = jsonObject.getString("model_name")

        var userUrl = getString(R.string.ip) + "/carreportsystem/get_report_user_image_uri.php?aadhar=" + jsonObject.getString("aadhar_num_user")
        var newRequestnewQ = Volley.newRequestQueue(this@ReportDescriptiveView)
        var newJsonAR = JsonArrayRequest(Request.Method.GET, userUrl, null, {
            response ->
            val jsonObjectinside = response.getJSONObject(0)
            Picasso.get().load(Uri.parse(getString(R.string.ip) + jsonObjectinside.getString("user_image_uri"))).
                    into(imgViewReportDescriptiveViewCitizenImage)
        },{

        })
        newRequestnewQ.add(newJsonAR)
        txtViewReportDescriptiveViewCitizenUsername.text = jsonObject.getString("username")

        txtViewReportDescriptiveViewCitizenAadharNum.text = jsonObject.getString("aadhar_num_user")

        txtViewReportDescriptiveViewCitizienNumber.text = jsonObject.getString("mobile_num_user")

        //block2 layout2
        txtViewReportDescriptiveViewOwnAAd.text = jsonObject.getString("owner_aadhar")

        txtViewReportDescriptiveViewOwnPincoe.text = jsonObject.getString("owner_pincode")

        txtViewReportDescriptiveViewOwnNumber.text = jsonObject.getString("owner_num")

        txtViewReportDescriptiveViewDealName.text = jsonObject.getString("dealer_name")

        txtViewReportDescriptiveViewDealAddress.text = jsonObject.getString("dealer_address")

        txtViewReportDescriptiveViewDealPincode.text = jsonObject.getString("dealer_pincode")

        txtViewReportDescriptiveViewDealNumber.text = jsonObject.getString("dealer_num")


    }


}