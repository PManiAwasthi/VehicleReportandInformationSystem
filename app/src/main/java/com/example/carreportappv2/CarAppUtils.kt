package com.example.carreportappv2

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class CarAppUtils {
    companion object {
        fun dialogBuilderFunction(context : Context, message : String){
            var dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle("Message")
            dialogBuilder.setMessage(message)
            dialogBuilder.show()
        }

        fun requestProcessorFunction(context : Context, stringUrl : String){
            val requestQ = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, stringUrl,
                {
                        response ->
                    if(response.contains("exists")){
                        dialogBuilderFunction(context, response)
                    }else{
                        dialogBuilderFunction(context, response)
                    }


                },{
                        error -> dialogBuilderFunction(context, error.toString())
                });
            requestQ.add(stringRequest)
        }
    }
}