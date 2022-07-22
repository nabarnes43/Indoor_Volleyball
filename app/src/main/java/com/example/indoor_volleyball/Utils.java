package com.example.indoor_volleyball;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class Utils {


    public static void setTextOrHide(Context context, Object text, TextView view, int formatId) {
        if (text != null) {
            view.setVisibility(View.VISIBLE);
            //Todo get use formatted string in other places like this.
            String formattedString = context.getString(formatId, text.toString());
            view.setText(formattedString);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
