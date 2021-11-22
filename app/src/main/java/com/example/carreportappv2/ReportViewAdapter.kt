package com.example.carreportappv2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.report_element.view.*

class ReportViewAdapter(var context: Context, var arrayList: ArrayList<ReportElementClass>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userInfo = PoliceUserInfo()
    var sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(context).inflate(R.layout.report_element, parent, false)

        return ProductViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductViewHolder).initializeRowUiComponents(arrayList[position].uri,
                arrayList[position].ownname,
                arrayList[position].status,
                arrayList[position].incharge,
                arrayList[position].recon,
                arrayList[position].report_num,
                arrayList[position].vehicle_uid)

        var name = sharedPreferences.getString("name", "").toString()
        var email = sharedPreferences.getString("email", "").toString()
        var aadhar_num = sharedPreferences.getString("aadhar_num", "").toString()
        var mobile_num = sharedPreferences.getString("mobile_num", "").toString()
        var address = sharedPreferences.getString("address", "").toString()
        var station_pincode = sharedPreferences.getString("station_pincode", "").toString()
        var uid = sharedPreferences.getString("uid","").toString()

        userInfo.insertValues(name, email, aadhar_num, mobile_num, address,uid,station_pincode)

        (holder as ProductViewHolder).itemView.setOnClickListener { openReportDesView(arrayList[position].incharge, position )}
    }

    fun openReportDesView(incharge: String, position: Int){
        if(incharge == "" || incharge == null){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Cannot open")
            builder.setMessage("You need to be the assigned officer to see further details, if you" +
                    " would like to accept the case press yes else press cancel")
            builder.setPositiveButton("yes"){
                dialog, which->
                set_incharge(position)

            }
            builder.setNegativeButton("cancel"){
                dialog, which->
            }
            builder.setCancelable(false)
            builder.show()
        }else{
            if(userInfo.uid == incharge){
                var intent = Intent(context, ReportDescriptiveView::class.java)
                intent.putExtra("vno", arrayList[position].vehicle_uid)
                intent.putExtra("rno", arrayList[position].report_num)
                context.startActivity(intent)
            }else{
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Cannot open")
                builder.setMessage("The case is already under investigation by Police id " + incharge)
                builder.setPositiveButton("Okay"){
                    dialog, which->

                }
                builder.setCancelable(false)
                builder.show()
            }
        }
    }

    fun set_incharge(position: Int){
        val inchargeUrl = context.getString(R.string.ip) + "/carreportsystem/set_report_incharge.php?repno=" + arrayList[position].report_num+
                "&inid=" + userInfo.uid+
                "&spin=" + userInfo.stationPincode+
                "&vuid=" + arrayList[position].vehicle_uid +
                "&pname=" + userInfo.name +
                "&pemail=" + userInfo.email
        val requestQ = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, inchargeUrl, {
            response ->
            var intent = Intent(context, ReportDescriptiveView::class.java)
            intent.putExtra("vno", arrayList[position].vehicle_uid)
            intent.putExtra("rno", arrayList[position].report_num)
            context.startActivity(intent)

        },{ error ->
            Log.d("hello", error.message.toString())

        })
        requestQ.add(stringRequest)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ProductViewHolder(pView: View) : RecyclerView.ViewHolder(pView){
        fun initializeRowUiComponents(uri: String, ownname: String, status: String, incharge: String, recon: String, report_num: Int, vehicle_uid: String){
            itemView.txtViewReportElementOwnerName.text = ownname
            itemView.txtViewReportElementStatus.text = status
            itemView.txtViewReportElementTitle.text = recon
            var newuri = context.getString(R.string.ip)+uri
            var picUrl = Uri.parse(newuri)
            Picasso.get().load(picUrl).into(itemView.imgViewReportElementCarImage)
        }
    }
}