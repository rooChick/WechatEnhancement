package me.firesun.wechat.enhancement.plugin;


import android.app.Activity;
import android.widget.Button;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.firesun.wechat.enhancement.PreferencesUtils;
import me.firesun.wechat.enhancement.util.HookClasses;
import me.firesun.wechat.enhancement.util.ReflectionUtil;

import static de.robv.android.xposed.XposedBridge.log;


public class AutoLogin {
    private static AutoLogin instance = null;

    private AutoLogin() {
    }

    public static AutoLogin getInstance() {
        if (instance == null)
            instance = new AutoLogin();
        return instance;
    }

    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(android.app.Activity.class, "onStart", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                try {
                    if (!PreferencesUtils.isAutoLogin())
                        return;
                    Activity activity = (Activity) param.thisObject;
                    if (activity.getClass().getName().equals(HookClasses.WebWXLoginUIClassName)) {
                        Class clazz = activity.getClass();
                        Field field = XposedHelpers.findFirstFieldByExactType(clazz, Button.class);
                        Button button = (Button) field.get(activity);
                        if (button != null) {
                            button.performClick();
                        }
                    }

                } catch (Error | Exception e) {
                    log("error:" + e);
                }
            }
        });

    }

}
