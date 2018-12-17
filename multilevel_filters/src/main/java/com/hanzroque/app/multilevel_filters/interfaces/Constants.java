package com.hanzroque.app.multilevel_filters.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hanzroque.app.multilevel_filters.interfaces.Constants.ViewType.NORMAL_TYPE;
import static com.hanzroque.app.multilevel_filters.interfaces.Constants.ViewType.SWITCH_TYPE;

public class Constants {
    @IntDef({NORMAL_TYPE, SWITCH_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
        int NORMAL_TYPE = 100;
        int SWITCH_TYPE = 200;
    }
}
