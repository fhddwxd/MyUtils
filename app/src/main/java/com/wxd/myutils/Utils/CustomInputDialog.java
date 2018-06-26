package com.wxd.myutils.Utils.Utils;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.myapplication.R;

/**
 * 自定义的可输入的提示对话框
 *
 * @author zhaojy
 * @date 2017/9/27 13:56
 */

public class CustomInputDialog extends Dialog {
    private TextView titleTextView;
    private Button closeButton;
    private Button submitButton;
    private EditText inputEditText;
    private View titleView;
    private ImageView titleLeftMark;

    public CustomInputDialog(Context context) {
        super(context, R.style.customInputDialogStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_custom_input_content_layout, null);
        this.setContentView(layout);
        titleView = layout.findViewById(R.id.title);
        titleLeftMark = (ImageView) layout.findViewById(R.id.title_left_mark);
        titleTextView = (TextView) layout.findViewById(R.id.tv_dialog_title_text);

        closeButton = (Button) layout.findViewById(R.id.bt_dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomInputDialog.this.dismiss();
            }
        });
        submitButton = (Button) layout.findViewById(R.id.bt_dialog_submit);
        this.setCancelable(false);
        inputEditText = (EditText) layout.findViewById(R.id.et_dialog_input);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = inputEditText.getSelectionStart() - 1;
                if (index >= 0) {
                    if (TextUtil.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = inputEditText.getText();
                        edit.delete(index, index + 1);
                    }
                }
            }
        });
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(Button closeButton) {
        this.closeButton = closeButton;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(Button submitButton) {
        this.submitButton = submitButton;
    }

    public EditText getInputEditText() {
        return inputEditText;
    }

    public void setInputEditText(EditText inputEditText) {
        this.inputEditText = inputEditText;
    }

    public View getTitleView() {
        return titleView;
    }

    public void setTitleView(View titleView) {
        this.titleView = titleView;
    }

    public ImageView getTitleLeftMark() {
        return titleLeftMark;
    }

    public void setTitleLeftMark(ImageView titleLeftMark) {
        this.titleLeftMark = titleLeftMark;
    }
}
