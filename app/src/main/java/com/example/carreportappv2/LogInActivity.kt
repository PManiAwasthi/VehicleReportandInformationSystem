package com.example.carreportappv2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_log_in.*
import org.json.JSONObject

class LogInActivity : AppCompatActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    var authFlag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        rdgprLogIn.setOnCheckedChangeListener(this@LogInActivity)
        txtSignUpTransition.setOnClickListener(this@LogInActivity)
        btnLogIn.setOnClickListener(this@LogInActivity)
    }

    override fun onClick(v: View?) {
        var idV = v?.id
        when(idV){
            txtSignUpTransition.id -> {
                var intent = Intent(this@LogInActivity, SignUpActivity::class.java)
                startActivityForResult(intent, 1018)
            }
            btnLogIn.id -> {
                if (authFlag == 1) {
                    logInAdmin()
                } else {
                    logInUser()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1018){
            if(resultCode == RESULT_OK){
                var sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                val email = sharedPreferences.getString("email","")
                val intent = Intent()
                if(email!="" && email!=null){
                    setResult(RESULT_OK, intent)
                }else{
                    setResult(RESULT_CANCELED, intent)
                }
                finish()
            }else if(resultCode == 21){

            }
            else{
                CarAppUtils.dialogBuilderFunction(this@LogInActivity, "Please Register first if not an existing user")
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            rdbtnLogInAuthUser.id -> {
                authFlag = 1
                edtLogInAuthId.visibility = View.VISIBLE
            }
            rdbtnLogInUnAuthUser.id -> {
                authFlag = 2
                edtLogInAuthId.visibility = View.INVISIBLE
            }
        }
    }

    fun logInAdmin(){
        val stringUrl = getString(R.string.ip)+"/carreportsystem/login_admin.php?idno="+edtLogInAuthId.text+
                "&email="+edtLogInEmail.text+
                "&password="+edtLogInPass.text
        val requestQ = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, stringUrl,
                { response ->
                    if (response.contains("exists")) {
                        saveData("admin")
                    }


                }, { error ->
        });
        requestQ.add(stringRequest)

    }
    fun logInUser(){

        val stringUrl = getString(R.string.ip)+"/carreportsystem/login_user.php?email="+edtLogInEmail.text+
                "&password="+edtLogInPass.text
        val requestQ = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, stringUrl,
                { response ->
                    if (response.contains("exists")) {
                        saveData("user")
                    }


                }, { error ->
                    Log.d("hello", error.message.toString())
        });
        requestQ.add(stringRequest)

    }

    fun saveData(acctype:String) {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", edtLogInEmail.text.toString())
        editor.putString("acctype", acctype)
        editor.apply()
        editor.commit()

        val email = sharedPreferences.getString("email","")
        val type = sharedPreferences.getString("acctype", "")
        val intent = Intent()
        if(email!="" && email!=null){
            setResult(RESULT_OK, intent)
        }else{
            setResult(RESULT_CANCELED, intent)
        }

        val profileURL = getString(R.string.ip)+"/carreportsystem/get_user_profile.php?email="+email.toString()+"&type="+type.toString();
        val requestQ = Volley.newRequestQueue(this@LogInActivity)
        var jsonAR = JsonArrayRequest(Request.Method.GET, profileURL,null,{
            response ->
            val jsonObject = response.getJSONObject(0)
            if (type != null) {
                setSharedPreference(jsonObject,type)
            }

        }, {
            error ->
            CarAppUtils.dialogBuilderFunction(this@LogInActivity, error.message.toString())
        })
        requestQ.add(jsonAR)

        finish()
    }

    fun setSharedPreference(jsonObject: JSONObject, type: String){
        val sharedPreferences: SharedPreferences = getSharedPreferences("profile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", jsonObject.getString("email"))
        editor.putString("name",jsonObject.getString("name"))
        editor.putString("aadhar_num", jsonObject.getString("aadhar_num"))
        editor.putString("mobile_num", jsonObject.getString("mobile_num"))
        editor.putString("address", jsonObject.getString("address"))
        if(type.equals("admin")){
            editor.putString("station_pincode", jsonObject.getString("station_pincode"))
            editor.putString("uid", jsonObject.getString("uid"))
        }

        editor.apply()
        editor.commit()
    }




}