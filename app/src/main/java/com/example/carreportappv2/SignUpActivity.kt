package com.example.carreportappv2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : AppCompatActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    var authFlag = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        txtLogInTransition.setOnClickListener(this@SignUpActivity)
        rdgprSignUp.setOnCheckedChangeListener(this@SignUpActivity)
        btnSignUp.setOnClickListener(this@SignUpActivity)

    }

    override fun onClick(v: View?) {
        var idV = v?.id
        when(idV){
            txtLogInTransition.id -> {
                var intent = Intent(this@SignUpActivity, LogInActivity::class.java)
                setResult(21, intent)
                finish()
            }
            btnSignUp.id -> {
                if(authFlag == 1){
                    signUpAdmin()
                }else if(authFlag == 2){
                    signUpUser()
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            rdbtnSignUpAuthUser.id -> {
                authFlag = 1
                edtSignUpAuthId.visibility = View.VISIBLE
            }
            rdbtnSignUpUnAuthUser.id -> {
                authFlag = 2
                edtSignUpAuthId.visibility  = View.INVISIBLE
            }
        }
    }

    fun signUpAdmin(){
        val signUpUrl = getString(R.string.ip)+"/carreportsystem/signup_admin.php?idno="+edtSignUpAuthId.text.toString()+
                "&username="+edtSignUpUsername.text.toString()+
                "&email="+edtSignUpEmail.text.toString()+
                "&password="+edtSignUpPassword.text.toString();
        val requestQ = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, signUpUrl,
                { response ->
                    if (response.contains("user Signed up")) {
                        saveData("admin")
                    }else if(response.contains("user exists")){
                        com.example.carreportappv2.CarAppUtils.dialogBuilderFunction(this@SignUpActivity,"User exists Please try again")
                    }


                }, { error ->
        });
        requestQ.add(stringRequest)
    }
    fun signUpUser(){
        val signUpUrl = getString(R.string.ip)+"/carreportsystem/signup_user.php?username="+edtSignUpUsername.text+
                "&email="+edtSignUpEmail.text+
                "&password="+edtSignUpPassword.text
        val requestQ = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, signUpUrl,
                { response ->
                    if (response.contains("user signed up")) {
                        saveData("user")
                    }else if(response.contains("user exists")){
                        CarAppUtils.dialogBuilderFunction(this@SignUpActivity,"User exists Please try again")
                    }


                }, { error ->
        });
        requestQ.add(stringRequest)
    }


    fun saveData(acctype:String) {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", edtSignUpEmail.text.toString())
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
        val requestQ = Volley.newRequestQueue(this@SignUpActivity)
        var jsonAR = JsonArrayRequest(Request.Method.GET, profileURL,null,{
            response ->
            val jsonObject = response.getJSONObject(0)
            if (type != null) {
                setSharedPreference(jsonObject,type)
            }

        }, {
            error ->
            CarAppUtils.dialogBuilderFunction(this@SignUpActivity, error.message.toString())
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