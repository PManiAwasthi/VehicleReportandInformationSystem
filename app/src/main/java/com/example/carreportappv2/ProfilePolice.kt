package com.example.carreportappv2

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_profile_police.*
import kotlinx.android.synthetic.main.activity_profile_user.*
import org.json.JSONObject

class ProfilePolice : AppCompatActivity(), View.OnClickListener {
    var userInfoClass = UserInfoClass()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_police)

        var sharedPreferences: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        var email = sharedPreferences.getString("email","")
        val type = sharedPreferences.getString("acctype","")
        val profileURL = getString(R.string.ip)+"/carreportsystem/get_user_profile.php?email="+email.toString()+"&type="+type.toString();
        val requestQ = Volley.newRequestQueue(this@ProfilePolice)
        var jsonAR = JsonArrayRequest(Request.Method.GET, profileURL,null,{
            response ->
            val jsonObject = response.getJSONObject(0)
            setValues(jsonObject)
            setSharedPreference(jsonObject)

        }, {
            error ->
            CarAppUtils.dialogBuilderFunction(this@ProfilePolice, error.message.toString())
        })
        requestQ.add(jsonAR)

        btnProfilePoliceEditiInfo.setOnClickListener(this::onClick)
        btnProfilePoliceSaveDetails.setOnClickListener(this::onClick)
        btnProfilePoliceViewProfile.setOnClickListener(this::onClick)

    }

    fun setValues(jsonObject: JSONObject){
        txtViewProfilePoliceEmail.text = jsonObject.getString("email")
        txtViewProfilePoliceName.text = jsonObject.getString("name")
        txtViewProfilePoliceAadharNumber.text = jsonObject.getString("aadhar_num")
        txtViewProfilePoliceMobile.text = jsonObject.getString("mobile_num")
        txtViewProfilePoliceAddress.text = jsonObject.getString("address")
        txtViewProfilePoliceStationPincode.text = jsonObject.getString("station_pincode")

        edtTextProfilePoliceEmail.setText(jsonObject.getString("email"))
        edtTextProfilePoliceName.setText(jsonObject.getString("name"))
        edtTextProfilePoliceAadharNumber.setText(jsonObject.getString("aadhar_num"))
        edtTextProfilePoliceAddress.setText(jsonObject.getString("address"))
        edtTextProfilePoliceMobile.setText(jsonObject.getString("mobile_num"))
        edtTextProfilePoliceStationPincode.setText(jsonObject.getString("station_pincode"))

        edtTextProfilePoliceEmail.isEnabled = false
    }

    fun setSharedPreference(jsonObject: JSONObject){
        val sharedPreferences: SharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", jsonObject.getString("email"))
        editor.putString("name",jsonObject.getString("name"))
        editor.putString("aadhar_num", jsonObject.getString("aadhar_num"))
        editor.putString("mobile_num", jsonObject.getString("mobile_num"))
        editor.putString("address", jsonObject.getString("address"))
        editor.putString("station_pincode", jsonObject.getString("station_pincode"))
        editor.putString("police_image_uri", jsonObject.getString("police_image_uri"))
        editor.putString("uid", jsonObject.getString("uid"))
        editor.apply()
        editor.commit()
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when(id){
            R.id.btnProfilePoliceEditiInfo -> {
                btnProfilePoliceEditiInfo.visibility = View.GONE
                btnProfilePoliceViewProfile.visibility = View.VISIBLE
                conProfilePolice.visibility = View.VISIBLE
                scrollViewProfilePolice.visibility = View.GONE
            }

            R.id.btnProfilePoliceViewProfile -> {
                btnProfilePoliceEditiInfo.visibility = View.VISIBLE
                btnProfilePoliceViewProfile.visibility = View.GONE
                conProfilePolice.visibility = View.GONE
                scrollViewProfilePolice.visibility = View.VISIBLE
            }

            R.id.btnProfilePoliceSaveDetails -> {
                val setProfileURL = getString(R.string.ip)+"/carreportsystem/set_user_profile.php?email="+edtTextProfilePoliceEmail.text.toString()+
                        "&type=admin" +
                        "&address=" + edtTextProfilePoliceAddress.text.toString() +
                        "&mobileno=" + edtTextProfilePoliceMobile.text.toString() +
                        "&aadhar=" + edtTextProfilePoliceAadharNumber.text.toString()+
                        "&pincode=" + edtTextProfilePoliceStationPincode.text.toString()
                val requestQ = Volley.newRequestQueue(this@ProfilePolice)
                var jsonAR = JsonArrayRequest(Request.Method.GET, setProfileURL,null,{
                    response ->
                    val jsonObject = response.getJSONObject(0)
                    setValues(jsonObject)
                    setSharedPreference(jsonObject)

                }, {
                    error ->
                    CarAppUtils.dialogBuilderFunction(this@ProfilePolice, error.message.toString())
                })
                requestQ.add(jsonAR)

                btnProfilePoliceEditiInfo.visibility = View.VISIBLE
                btnProfilePoliceViewProfile.visibility = View.GONE
                conProfilePolice.visibility = View.GONE
                scrollViewProfilePolice.visibility = View.VISIBLE
            }
        }
    }
}