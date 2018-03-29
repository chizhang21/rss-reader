package com.cashzhang.ashley;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

/**
 * Created by zhangchi on 2018/2/7.
 */


class DialogEditFeed extends Dialog {
    private final MainActivity m_activity;
    private final int m_pos;
    private AsyncTask<Void, Void, IndexItem> m_task;

    private DialogEditFeed(MainActivity activity, int position) {
        super(activity, R.style.MyDialog);
        m_activity = activity;
        m_pos = position;
    }

    static Dialog newInstance(MainActivity activity, int position) {
        Dialog dialog = new DialogEditFeed(activity, position);

        // Get the text resources and set the title of the dialog.
        int title = -1 == position ? R.string.dialog_title_add : R.string.dialog_title_edit;
        dialog.setTitle(activity.getString(title));

        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_dialog);

        MultiAutoCompleteTextView tagEdit = (MultiAutoCompleteTextView) findViewById(R.id.dialog_tags);
        tagEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        final IndexItem oldItem;

        // If this is an edit dialog, set the EditTexts and save the old information.
        if (-1 != m_pos) {
            oldItem = m_activity.m_index.get(m_pos);

            ((TextView) findViewById(R.id.dialog_url)).setText(oldItem.m_url);
        } else {
            oldItem = null;
        }

        final Dialog dialog = this;

        Button buttonNegative = (Button) findViewById(R.id.dialog_button_negative);
        final Button buttonPositive = (Button) findViewById(R.id.dialog_button_positive);

        // Set the click listeners.
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the positive button says checking...
                if (buttonPositive.getText().equals(m_activity.getString(R.string.dialog_checking))) {
                    // Cancel the Async task.
                    if (null != m_task) {
                        m_task.cancel(true);
                        buttonPositive.setText(R.string.dialog_accept);
                        buttonPositive.setEnabled(true);
                    }
                } else {
                    dismiss();
                }
            }
        });
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_task = AsyncCheckFeed.newInstance(m_activity, dialog, oldItem);
            }
        });
    }
}
