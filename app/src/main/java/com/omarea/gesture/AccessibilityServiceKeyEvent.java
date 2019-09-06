package com.omarea.gesture;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityServiceKeyEvent extends AccessibilityService {
    boolean isLandscapf = false;
    private FloatVitualTouchBar floatVitualTouchBar = null;
    private BroadcastReceiver configChanged = null;
    private BroadcastReceiver serviceDisable = null;

    private void hidePopupWindow() {
        if (floatVitualTouchBar != null) {
            floatVitualTouchBar.hidePopupWindow();
            floatVitualTouchBar = null;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /*
        Log.d("onAccessibilityEvent", "onAccessibilityEvent");
        // Log.d("onAccessibilityEvent", event.getPackageName().toString());
        CharSequence packageName = event.getPackageName();
        if (packageName != null) {
            AppHistory.putHistory(packageName.toString());
        }
        */
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        TouchIconCache.setContext(this.getBaseContext());

        if (configChanged == null) {
            configChanged = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    createPopupView();
                }
            };

            registerReceiver(configChanged, new IntentFilter(getString(R.string.action_config_changed)));
        }
        if (serviceDisable == null) {
            serviceDisable = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        disableSelf();
                    }
                    stopSelf();
                }
            };
            registerReceiver(serviceDisable, new IntentFilter(getString(R.string.action_service_disable)));
        }
        createPopupView();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        hidePopupWindow();
        return super.onUnbind(intent);
    }

    @Override
    public void onInterrupt() {

    }

    // 监测屏幕旋转
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (floatVitualTouchBar != null && newConfig != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                isLandscapf = false;
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                isLandscapf = true;
            }
            createPopupView();
        }
    }

    private void createPopupView() {
        hidePopupWindow();
        floatVitualTouchBar = new FloatVitualTouchBar(this, isLandscapf);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatVitualTouchBar != null) {
            floatVitualTouchBar.hidePopupWindow();
        }

        if (configChanged != null) {
            unregisterReceiver(configChanged);
            configChanged = null;
        }
    }
}
