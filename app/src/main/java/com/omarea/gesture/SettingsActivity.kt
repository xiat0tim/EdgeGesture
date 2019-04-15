package com.omarea.gesture

import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import android.content.pm.PackageManager
import android.widget.Switch
import android.widget.Toast
import java.lang.Exception


class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val p = packageManager
        val startActivity = ComponentName(this.applicationContext, StartActivity::class.java)
        hide_start_icon.setOnClickListener {
            try {
                if ((it as Switch).isChecked) {
                    p.setComponentEnabledSetting(startActivity, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
                } else {
                    p.setComponentEnabledSetting(startActivity, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
                }
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        hide_start_icon.isChecked = p.getComponentEnabledSetting(startActivity) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }
}
