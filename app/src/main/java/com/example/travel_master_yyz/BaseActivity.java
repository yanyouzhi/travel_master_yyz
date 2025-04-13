package com.example.travel_master_yyz;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_master_yyz.dao.SessionManager;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        SessionManager sessionManager = new SessionManager(newBase);
        String lang = sessionManager.getLanguage();

        if (lang == null || lang.equals("default")) {
            super.attachBaseContext(newBase);
            return;
        }

        Context context = updateBaseContextLocale(newBase, lang);
        super.attachBaseContext(context);
    }

    private Context updateBaseContextLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }
}
