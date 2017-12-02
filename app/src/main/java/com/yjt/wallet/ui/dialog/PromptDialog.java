package com.yjt.wallet.ui.dialog;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.yjt.wallet.base.constant.Temp;
import com.yjt.wallet.base.dialog.BaseDialogFragment;
import com.yjt.wallet.base.dialog.listener.OnDialogNegativeListener;
import com.yjt.wallet.base.dialog.listener.OnDialogNeutralListener;
import com.yjt.wallet.base.dialog.listener.OnDialogPositiveListener;
import com.yjt.wallet.components.utils.BundleUtil;
import com.yjt.wallet.ui.dialog.builder.PromptDialogBuilder;


public class PromptDialog extends BaseDialogFragment {

    @Override
    protected Builder build(Builder builder) {
        CharSequence title = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence prompt = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_PROMPT.getContent());
        CharSequence positive = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence negative = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        CharSequence neutral = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEUTRAL.getContent());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(prompt)) {
            builder.setMessage(prompt);
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDialogPositiveListener listener : getDialogListeners(OnDialogPositiveListener.class)) {
                        listener.onPositiveButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNegativeListener listener : getDialogListeners(OnDialogNegativeListener.class)) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(neutral)) {
            builder.setNeutralButton(neutral, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNeutralListener listener : getDialogListeners(OnDialogNeutralListener.class)) {
                        listener.onNeutralButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        return builder;
    }

    public static PromptDialogBuilder createBuilder(FragmentManager fragmentManager) {
        return new PromptDialogBuilder(fragmentManager, PromptDialog.class);
    }
}
