package com.example.android.sunshine.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by johnpavlicek on 12/3/15.
 */
public class LocationEditTextPreference extends EditTextPreference
{
    private int mMinLength;
    private final int DEFAULT_MINIMUM_LOCATION_LENGTH = 2;

    private final String LOCATION_EDIT_TEXT_LOGTAG = "LOCATION_EDIT_PREF";

    public LocationEditTextPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LocationEditTextPreference,0,0);

        try
        {
            mMinLength = a.getInt(R.styleable.LocationEditTextPreference_minLength, DEFAULT_MINIMUM_LOCATION_LENGTH);
            Log.d(LOCATION_EDIT_TEXT_LOGTAG, "Minimum length is " + mMinLength);
        }
        finally
        {
            a.recycle();
        }
    }

    @Override
    protected void showDialog(Bundle state)
    {
        super.showDialog(state);
        EditText editText = getEditText();


        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() < mMinLength)
                {
                    //mButton.
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() < mMinLength)
                {
                    //mButton.
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

                Dialog d = getDialog();
                if (d instanceof AlertDialog)
                {
                    AlertDialog dialog = (AlertDialog) d;
                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    if (editable.length() < mMinLength)
                    {
                        button.setEnabled(false);
                    }
                    else
                    {
                        button.setEnabled(true);
                    }
                }

            }
        });
    }
}

