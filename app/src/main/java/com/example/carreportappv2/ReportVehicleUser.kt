package com.example.carreportappv2

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_my_crop_image.*
import kotlinx.android.synthetic.main.activity_report_vehicle_user.*

class ReportVehicleUser : AppCompatActivity() {
    var name = "xyz"
    var vehicleId = "xyz"
    var ownerNum = "xyz"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_vehicle_user)

        var plateString: String = intent.getStringExtra("plateno").toString()
        var imageUri: Uri = Uri.parse(intent.getStringExtra("uri"))

        var uploadedImageUri: Uri = Uri.parse(intent.getStringExtra("uploadeduri"))
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
    }

    fun checkValues(): Int{
        try{
            var sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
            var aadharNum = sharedPreferences.getString("aadhar_num", "")
            var mobileNum = sharedPreferences.getString("mobile_num", "")

            if(aadharNum!=null && aadharNum.toString() != ""){
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
}