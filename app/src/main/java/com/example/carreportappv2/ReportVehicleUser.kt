package com.example.carreportappv2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_my_crop_image.*
import kotlinx.android.synthetic.main.activity_report_vehicle_user.*

class ReportVehicleUser : AppCompatActivity(){
    var name = "xyz"
    var vehicleId = "xyz"
    var ownerNum = "xyz"
    var stationCode = "492007"
    var location = "xyz"
    var userAadharNum = "xyz"
    var userMobileNum = "xyz"
    var userName = "xyz"
    var status = "not accepted yet"
    var userEmail = "xyz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_vehicle_user)

        var plateString: String = intent.getStringExtra("plateno").toString()
        val imageUri: Uri = Uri.parse(intent.getStringExtra("uri"))

        edtTextReportVehicleCReport.isEnabled = false

        val uploadedImageUri: Uri = Uri.parse(intent.getStringExtra("uploadeduri"))
        plateString = plateString.filterNot { it.isWhitespace() }
        imgViewReportVehicleUserImage.setImageURI(imageUri)
        val vehicleUrl = getString(R.string.ip)+"/carreportsystem/get_vehicle_info.php?number="+plateString.toUpperCase()
        val requestQ = Volley.newRequestQueue(this@ReportVehicleUser)
        val jsonAR = JsonArrayRequest(Request.Method.GET, vehicleUrl, null, {
            response ->
            val jsonObject = response.getJSONObject(0)
            txtViewReportVehicleName.text = jsonObject.getString("owner_name")
            txtViewReportVehicleNumber.text = plateString.capitalize()
            name = jsonObject.getString("owner_name")
            vehicleId = jsonObject.getString("vehicle_uid")
            ownerNum = jsonObject.getString("owner_num")
        },{ error ->
            Log.d("error", error.message.toString())

        })
        requestQ.add(jsonAR)

        var check = checkValues()
        if(check == -1){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("Aadhar and mobile number required for further processing Please fill the details in the next window")
            builder.setPositiveButton("Okay"){
                dialog, which->
                var intent = Intent(this, UpdateProfileUser::class.java)
                startActivityForResult(intent, 1029)
            }
            builder.setCancelable(false)
            builder.show()

        }


        btnReportVehcleSubmit.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                reportUser(uploadedImageUri.toString(),
                        name,
                        plateString,
                        vehicleId,
                        edtTextReportVehicleCReport.text.toString(),
                        stationCode,
                        location,
                        userName,
                        userAadharNum,
                        userMobileNum,
                        ownerNum,
                        status,
                        userEmail
                )
            }
        })
    }

    fun checkValues(): Int{
        try{
            var sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
            userAadharNum = sharedPreferences.getString("aadhar_num", "").toString()
            userMobileNum = sharedPreferences.getString("mobile_num", "").toString()
            userName = sharedPreferences.getString("name", "").toString()
            userEmail = sharedPreferences.getString("email","").toString()

            if(userAadharNum!=null && userAadharNum.toString() != ""){
                return 1
            }else{
                return -1
            }
        }catch (e: Exception){
            return -2;
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1029){
            if(resultCode == RESULT_OK){

            }else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Please Update Your Aadhar and Mobile Number")
                builder.setPositiveButton("yes"){
                    dialog, which->
                    var intent = Intent(this, UpdateProfileUser::class.java)
                    startActivityForResult(intent, 1029)
                }
                builder.show()
                
            }
        }
    }

    fun reportUser(uri : String,
                   ownname : String,
                   platno : String,
                   uid : String,
                   recon : String,
                   pin : String,
                   loc : String,
                   username : String,
                   aadharuser : String,
                   userno : String,
                   ownno : String,
                   status : String, email : String){
        var reportUrl = getString(R.string.ip) + "/carreportsystem/set_reports_user.php?type=user" +
                "&uri=" + uri.toString() +
                "&ownname=" + ownname +
                "&plateno=" + platno +
                "&uid=" + uid +
                "&recon=" + recon.toString() +
                "&pin=" + pin +
                "&loc=" + loc +
                "&username=" + username +
                "&aadharuser=" + aadharuser +
                "&userno=" + userno +
                "&ownno=" + ownno +
                "&status=" + status.toString()+
                "&email=" + email.toString()
        val requestQ = Volley.newRequestQueue(this@ReportVehicleUser)
        val stringRequest = StringRequest(Request.Method.GET, reportUrl,
                { response ->

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Success")
                        builder.setMessage("Report made successfully thank you for your contribution, " +
                                "authorities will contact you for verification of report." + response.toString())
                        builder.setPositiveButton("yes"){
                            dialog, which->
                            finish()
                        }
                        builder.setCancelable(false)
                        builder.show()


                }, { error ->
                    Log.d("hello",error.message.toString())

        });
        requestQ.add(stringRequest)
    }



    fun onRadioButtonClicked(view: View){
        if (view is RadioButton){
            val checked = view.isChecked
            when(view.getId()){
                R.id.rdbtnReportVehicleSus -> if (checked){
                    edtTextReportVehicleCReport.setText("Abandoned Vehicle")
                    edtTextReportVehicleCReport.isEnabled = false
                }
                R.id.rdbtnReportVehicleOS -> if (checked){
                    edtTextReportVehicleCReport.setText("Over Speeding")
                    edtTextReportVehicleCReport.isEnabled = false
                }
                R.id.rdbtnReportVehicleWP -> if (checked){
                    edtTextReportVehicleCReport.setText("Illegal Parking")
                    edtTextReportVehicleCReport.isEnabled = false
                }
                R.id.rdbtnReportVehicleHR -> if (checked){
                    edtTextReportVehicleCReport.setText("Hit and Run")
                    edtTextReportVehicleCReport.isEnabled = false
                }
                R.id.rdbtnReportVehicleSB -> if (checked){
                    edtTextReportVehicleCReport.setText("Signal Bypassing")
                    edtTextReportVehicleCReport.isEnabled = false
                }
                R.id.rdbtnReportVehicleC -> if (checked){
                    edtTextReportVehicleCReport.setText("")
                    edtTextReportVehicleCReport.isEnabled = true

                }
            }
        }
    }
}