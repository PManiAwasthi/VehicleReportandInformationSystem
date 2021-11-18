package com.example.carreportappv2

import android.app.LocalActivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TabHost

class ReportDescriptiveView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_descriptive_view)

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
    }
}