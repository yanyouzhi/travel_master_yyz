package com.example.travel_master_yyz.wrapper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class LanguageContextWrapper extends ContextWrapper {
    public LanguageContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context, Locale newLocale) {
        Configuration configuration = context.getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = newLocale;
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }

        return new LanguageContextWrapper(context);
    }
}
