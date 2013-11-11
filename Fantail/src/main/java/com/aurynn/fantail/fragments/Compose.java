package com.aurynn.fantail.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.aurynn.fantail.R;

/**
 * Created by aurynn on 7/11/13.
 */
public class Compose extends DialogFragment {

    private AppDotNetClient client;
    private EditText textArea;

    public interface ComposeListener {
        public void onPostMessage(DialogFragment dialog);
    }

    public String getPostMessage() {
        return textArea.getText().toString();
    }

//    private DialogInterface.OnClickListener positiveHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.compose_dialog, container);
        textArea = (EditText) v.findViewById(R.id.newPostEditText);
        ImageButton button = (ImageButton) v.findViewById(R.id.postButton);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPostMessage(Compose.this);
                dismiss();
            }
        });
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return v;
    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View v = inflater.inflate(R.layout.compose_dialog, null);
//
//
//        builder.setView(v);
//
//        return builder.create();
//    }

    // Use this instance of the interface to deliver action events
    ComposeListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ComposeListener so we can send events to the host
            mListener = (ComposeListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ComposeListener");
        }
    }
}
