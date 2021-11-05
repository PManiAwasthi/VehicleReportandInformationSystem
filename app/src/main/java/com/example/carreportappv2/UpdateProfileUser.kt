package com.example.carreportappv2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_update_profile_user.*
import org.json.JSONObject

class UpdateProfileUser : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile_user)

        btnUpdateProfileUserSubmit.setOnClickListener(this::onClick)
    }

    private fun setSharedPreference(jsonObject: JSONObject){
        val sharedPreferences: SharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("aadhar_num", jsonObject.getString("aadhar_num"))
        editor.putString("mobile_num", jsonObject.getString("mobile_num"))
        editor.apply()
        editor.commit()

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    fun setValues(){
        if(edtTextUpdateProfileUserAadharNumber.text.toString() != "" &&
                edtTextUpdateProfileUserMobileNumber.text.toString() != ""){
            val updateURL = getString(R.string.ip) + "/carreportsystem/updateAadharNum.php?email=" +
                    "c@gmail.com" +
                    "&type=user" +
                    "&aadhar="+ edtTextUpdateProfileUserAadharNumber.text.toString() +
                    "&mobileno=" + edtTextUpdateProfileUserMobileNumber.text.toString()

            val requestQ = Volley.newRequestQueue(this@UpdateProfileUser)
            var jsonAR = JsonArrayRequest(Request.Method.GET, updateURL, null, {
                response ->
                val jsonObject = response.getJSONObject(0)
                setSharedPreference(jsonObject)
            }, {

            })
            requestQ.add(jsonAR)
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Value Error")
            builder.setMessage("Please fill Both fields to proceed")
            builder.setPositiveButton("Yes"){
                dialog, which ->
            }
            builder.show()
        }
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id){
            R.id.btnUpdateProfileUserSubmit -> {
                setValues()

            }
        }
    }
}