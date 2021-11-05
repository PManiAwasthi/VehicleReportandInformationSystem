package com.example.carreportappv2

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_profile_police.*
import kotlinx.android.synthetic.main.activity_profile_user.*
import org.json.JSONObject

class ProfileUser : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)

        val sharedPreferences: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val email = sharedPreferences.getString("email","")
        val type = sharedPreferences.getString("acctype","")
        val profileURL = getString(R.string.ip)+"/carreportsystem/get_user_profile.php?email="+email.toString()+"&type="+type.toString();

        val requestQ = Volley.newRequestQueue(this@ProfileUser)
        var jsonAR = JsonArrayRequest(Request.Method.GET, profileURL,null,{
            response ->
            val jsonObject = response.getJSONObject(0)
            setValues(jsonObject)
            setSharedPreference(jsonObject)

        }, {
            error ->
            CarAppUtils.dialogBuilderFunction(this@ProfileUser, error.message.toString())
        })
        requestQ.add(jsonAR)



        btnProfileUserEditiInfo.setOnClickListener(this::onClick)
        btnProfileUserViewProfile.setOnClickListener(this::onClick)
        btnProfileUserSaveDetails.setOnClickListener(this::onClick)

    }


    fun setValues(jsonObject: JSONObject){
        txtViewProfileUserEmail.text = jsonObject.getString("email")
        txtViewProfileUserName.text = jsonObject.getString("name")
        txtViewProfileUserAadharNumber.text = jsonObject.getString("aadhar_num")
        txtViewProfileUserMobile.text = jsonObject.getString("mobile_num")
        txtViewProfileUserAddress.text = jsonObject.getString("address")

        edtTextProfileUserEmail.setText(jsonObject.getString("email"))
        edtTextProfileUserName.setText(jsonObject.getString("name"))
        edtTextProfileUserAadharNumber.setText(jsonObject.getString("aadhar_num"))
        edtTextProfileUserAddress.setText(jsonObject.getString("address"))
        edtTextProfileUserMobile.setText(jsonObject.getString("mobile_num"))

        edtTextProfileUserEmail.isEnabled = false
    }

    fun setSharedPreference(jsonObject: JSONObject){
        val sharedPreferences: SharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", jsonObject.getString("email"))
        editor.putString("name",jsonObject.getString("name"))
        editor.putString("aadhar_num", jsonObject.getString("aadhar_num"))
        editor.putString("mobile_num", jsonObject.getString("mobile_num"))
        editor.putString("address", jsonObject.getString("address"))
        editor.putString("police_image_uri", jsonObject.getString("user_image_uri"))
        editor.apply()
        editor.commit()
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id){
            R.id.btnProfileUserEditiInfo -> {
                btnProfileUserViewProfile.visibility = View.VISIBLE
                btnProfileUserEditiInfo.visibility = View.GONE
                scrollViewProfileUser.visibility = View.GONE
                conProfileUser.visibility = View.VISIBLE
            }

            R.id.btnProfileUserViewProfile -> {
                btnProfileUserViewProfile.visibility = View.GONE
                btnProfileUserEditiInfo.visibility = View.VISIBLE
                scrollViewProfileUser.visibility = View.VISIBLE
                conProfileUser.visibility = View.GONE
            }

            R.id.btnProfileUserSaveDetails -> {
                val setProfileURL = getString(R.string.ip)+"/carreportsystem/set_user_profile.php?email="+edtTextProfileUserEmail.text.toString()+
                        "&type=user" +
                        "&address=" + edtTextProfileUserAddress.text.toString() +
                        "&mobileno=" + edtTextProfileUserMobile.text.toString() +
                        "&aadhar=" + edtTextProfileUserAadharNumber.text.toString()
                val requestQ = Volley.newRequestQueue(this@ProfileUser)
                var jsonAR = JsonArrayRequest(Request.Method.GET, setProfileURL,null,{
                    response ->
                    val jsonObject = response.getJSONObject(0)
                    setValues(jsonObject)
                    setSharedPreference(jsonObject)

                }, {
                    error ->
                    CarAppUtils.dialogBuilderFunction(this@ProfileUser, error.message.toString())
                })
                requestQ.add(jsonAR)

                btnProfileUserViewProfile.visibility = View.GONE
                btnProfileUserEditiInfo.visibility = View.VISIBLE
                scrollViewProfileUser.visibility = View.VISIBLE
                conProfileUser.visibility = View.GONE

            }
        }
    }


}