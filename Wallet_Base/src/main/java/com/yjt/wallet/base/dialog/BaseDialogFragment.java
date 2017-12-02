package com.yjt.wallet.base.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yjt.wallet.base.R;
import com.yjt.wallet.base.constant.Constant;
import com.yjt.wallet.base.constant.Temp;
import com.yjt.wallet.base.dialog.listener.OnDialogCancelListener;
import com.yjt.wallet.components.utils.BundleUtil;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.TypefaceUtil;
import com.yjt.wallet.components.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseDialogFragment extends DialogFragment implements DialogInterface.OnShowListener {

    protected int mRequestCode;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.getInstance().d(getClass().getName(), this.getClass().getSimpleName() + " onCreateDialog() invoked!!");
        Dialog dialog = new Dialog(getActivity(), resolveTheme());
        dialog.setCanceledOnTouchOutside(BundleUtil.getInstance().getBooleanData(getArguments(), Temp.CANCELABLE_ON_TOUCH_OUTSIDE.getContent()));
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getDialog() == null) {
            LogUtil.getInstance().print("getDialog() is null");
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
        LogUtil.getInstance().d(getClass().getName(), this.getClass().getSimpleName() + " onActivityCreated() invoked!!");
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            mRequestCode = getTargetRequestCode();
        } else {
            mRequestCode = BundleUtil.getInstance().getIntData(getArguments(), Temp.REQUEST_CODE.getContent());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.getInstance().d(getClass().getName(), this.getClass().getSimpleName() + " onCreateView() invoked!!");
        return build(new Builder(inflater, container)).create(getContext());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        LogUtil.getInstance().d(getClass().getName(), this.getClass().getSimpleName() + " onCancel() invoked!!");
        for (OnDialogCancelListener listener : getDialogListeners(OnDialogCancelListener.class)) {
            listener.onCanceled(mRequestCode);
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
        LogUtil.getInstance().d(getClass().getName(), this.getClass().getSimpleName() + " onDestroyView() invoked!!");
    }

    @StyleRes
    public int resolveTheme() {
        int theme = getTheme();
        if (theme != 0) {
            return theme;
        }
        boolean isLightTheme = isActivityThemeLight();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean(Temp.USE_DARK_THEME.getContent())) {
                isLightTheme = false;
            } else if (bundle.getBoolean(Temp.USE_LIGHT_THEME.getContent())) {
                isLightTheme = true;
            }
        }
        return isLightTheme ? R.style.Light_Dialog_Appearance : R.style.Dark_Dialog_Appearance;
    }

    private boolean isActivityThemeLight() {
        try {
            TypedValue val = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.isLightTheme, val, true);
            TypedArray styledAttributes = getActivity().obtainStyledAttributes(val.data, new int[]{R.attr.isLightTheme});
            boolean lightTheme = styledAttributes.getBoolean(0, false);
            styledAttributes.recycle();
            return lightTheme;
        } catch (RuntimeException e) {
            return true;
        }
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(this, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (RuntimeException e) {
            LogUtil.getInstance().print(e.getMessage());
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (getView() != null) {
            ScrollView svDialogContent = ViewUtil.getInstance().findView(getView(), R.id.svDialogContent);
            FrameLayout flDialogCustom = ViewUtil.getInstance().findView(getView(), R.id.flDialogCustom);
            ListView lvDialogContent = ViewUtil.getInstance().findView(getView(), R.id.lvDialogContent);
            boolean scrollable = false;
            if (flDialogCustom.getChildCount() > 0) {
                View firstChild = flDialogCustom.getChildAt(0);
                if (firstChild instanceof ViewGroup) {
                    scrollable = ViewUtil.getInstance().isScrollable((ViewGroup) firstChild);
                }
            }
            modifyButtonsBasedOnScrollableContent(ViewUtil.getInstance().isScrollable(lvDialogContent)
                                                          || ViewUtil.getInstance().isScrollable(svDialogContent)
                                                          || scrollable);
        }
    }

    private void modifyButtonsBasedOnScrollableContent(boolean scrollable) {
        if (getView() == null) {
            return;
        }
        View vDialogDivider = ViewUtil.getInstance().findView(getView(), R.id.vDialogDivider);
        View vDialogButtonsBottomSpace = ViewUtil.getInstance().findView(getView(), R.id.vDialogButtonsBottomSpace);
        RelativeLayout rlDialogLayoutButtons = ViewUtil.getInstance().findView(getView(), R.id.rlDialogLayoutButtons);
        LinearLayout llDialogLayoutButtonsStacked = ViewUtil.getInstance().findView(getView(), R.id.llDialogLayoutButtonsStacked);
        if (ViewUtil.getInstance().isGone(rlDialogLayoutButtons)
                && ViewUtil.getInstance().isGone(llDialogLayoutButtonsStacked)) {
            ViewUtil.getInstance().setViewGone(vDialogDivider);
            ViewUtil.getInstance().setViewGone(vDialogButtonsBottomSpace);
        } else if (scrollable) {
            ViewUtil.getInstance().setViewVisible(vDialogDivider);
            ViewUtil.getInstance().setViewGone(vDialogButtonsBottomSpace);
        } else {
            ViewUtil.getInstance().setViewGone(vDialogDivider);
            ViewUtil.getInstance().setViewVisible(vDialogButtonsBottomSpace);
        }
    }

    protected <T> List<T> getDialogListeners(Class<T> listenerInterface) {
        final Fragment targetFragment = getTargetFragment();
        List<T> listeners = new ArrayList<>(2);
        if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.getClass())) {
            listeners.add((T) targetFragment);
        }
        if (getActivity() != null && listenerInterface.isAssignableFrom(getActivity().getClass())) {
            listeners.add((T) getActivity());
        }
        return Collections.unmodifiableList(listeners);
    }

    protected static class Builder {
        private final ViewGroup mContainer;
        private final LayoutInflater mInflater;
        private CharSequence mTitle;
        private CharSequence mPositiveButtonText;
        private View.OnClickListener mPositiveButtonListener;
        private CharSequence mNegativeButtonText;
        private View.OnClickListener mNegativeButtonListener;
        private CharSequence mNeutralButtonText;
        private View.OnClickListener mNeutralButtonListener;
        private CharSequence mMessage;
        private View mCustomView;
        private ListAdapter mListAdapter;
        private int mListCheckedItemIdx;
        private int mChoiceMode;
        private int[] mListCheckedItemMultipleIds;
        private AdapterView.OnItemClickListener mOnItemClickListener;
        private View.OnClickListener mOnDismissListener;

        Builder(LayoutInflater inflater, ViewGroup container) {
            this.mContainer = container;
            this.mInflater = inflater;
        }

        public LayoutInflater getLayoutInflater() {
            return mInflater;
        }

        public Builder setTitle(Context ctx, int titleId) {
            this.mTitle = ctx.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setPositiveButton(Context ctx, int textId, final View.OnClickListener listener) {
            mPositiveButtonText = ctx.getText(textId);
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(Context ctx, int textId, final View.OnClickListener listener) {
            mNegativeButtonText = ctx.getText(textId);
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(Context ctx, int textId, final View.OnClickListener listener) {
            mNeutralButtonText = ctx.getText(textId);
            mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
            mNeutralButtonText = text;
            mNeutralButtonListener = listener;
            return this;
        }

        public Builder setDismissButton(final View.OnClickListener listener) {
            mOnDismissListener = listener;
            return this;
        }

        public Builder setMessage(Context ctx, int messageId) {
            mMessage = ctx.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setItems(ListAdapter listAdapter, int[] checkedItemIds, int choiceMode, final AdapterView.OnItemClickListener listener) {
            mListAdapter = listAdapter;
            mListCheckedItemMultipleIds = checkedItemIds;
            mOnItemClickListener = listener;
            mChoiceMode = choiceMode;
            mListCheckedItemIdx = -1;
            return this;
        }

        public Builder setItems(ListAdapter listAdapter, int checkedItemIdx, final AdapterView.OnItemClickListener listener) {
            mListAdapter = listAdapter;
            mOnItemClickListener = listener;
            mListCheckedItemIdx = checkedItemIdx;
            mChoiceMode = AbsListView.CHOICE_MODE_NONE;
            return this;
        }

        public Builder setView(View view) {
            mCustomView = view;
            return this;
        }

        @SuppressLint("NewApi")
        View create(Context ctx) {
            LinearLayout content = (LinearLayout) mInflater.inflate(R.layout.dialog_base, mContainer, false);
            TextView tvDialogTitle = ViewUtil.getInstance().findView(content, R.id.tvDialogTitle);
            TextView tvDialogMessage = ViewUtil.getInstance().findView(content, R.id.tvDialogMessage);
            FrameLayout flDialogCustom = ViewUtil.getInstance().findView(content, R.id.flDialogCustom);
            RelativeLayout rlDialogLayoutButtons = ViewUtil.getInstance().findView(content, R.id.rlDialogLayoutButtons);
            Button btnDialogNeutral = ViewUtil.getInstance().findView(content, R.id.btnDialogNeutral);
            Button btnDialogNegative = ViewUtil.getInstance().findView(content, R.id.btnDialogNegative);
            Button btnDialogPositive = ViewUtil.getInstance().findView(content, R.id.btnDialogPositive);
            LinearLayout llDialogLayoutButtonsStacked = ViewUtil.getInstance().findView(content, R.id.llDialogLayoutButtonsStacked);
            Button btnDialogButtonPositiveStacked = ViewUtil.getInstance().findView(content, R.id.btnDialogButtonPositiveStacked);
            Button btnDialogButtonNegativestacked = ViewUtil.getInstance().findView(content, R.id.btnDialogButtonNegativestacked);
            Button btnDialogButtonNeutralStacked = ViewUtil.getInstance().findView(content, R.id.btnDialogButtonNeutralStacked);
            ListView lvDialogContent = ViewUtil.getInstance().findView(content, R.id.lvDialogContent);
            LinearLayout llDialogListDismiss = ViewUtil.getInstance().findView(content, R.id.llDialogListDismiss);
//            lvDialogContent.setTranslationZ(100);
            Typeface regularFont = TypefaceUtil.getInstance().get(ctx, Constant.View.ROBOTO_REGULAR);
            Typeface mediumFont = TypefaceUtil.getInstance().get(ctx, Constant.View.ROBOTO_MEDIUM);

            ViewUtil.getInstance().setText(tvDialogTitle, mTitle, regularFont);
            ViewUtil.getInstance().setText(tvDialogMessage, mMessage, mediumFont);
            setPaddingOfTitleAndMessage(ctx, tvDialogTitle, tvDialogMessage);

            if (mCustomView != null) {
                flDialogCustom.addView(mCustomView);
            }
            if (mListAdapter != null) {
                lvDialogContent.setAdapter(mListAdapter);
                lvDialogContent.setOnItemClickListener(mOnItemClickListener);
                if (mListCheckedItemIdx != -1) {
                    lvDialogContent.setSelection(mListCheckedItemIdx);
                }
                if (mListCheckedItemMultipleIds != null) {
                    lvDialogContent.setChoiceMode(mChoiceMode);
                    for (int i : mListCheckedItemMultipleIds) {
                        lvDialogContent.setItemChecked(i, true);
                    }
                }
                llDialogListDismiss.setOnClickListener(mOnDismissListener);
                lvDialogContent.setDivider(new ColorDrawable(ctx.getResources().getColor(R.color.color_cbcacf)));
                lvDialogContent.setDividerHeight(1);
            }
            if (shouldStackButtons()) {
                ViewUtil.getInstance().setText(btnDialogButtonPositiveStacked, mPositiveButtonText, mediumFont, mPositiveButtonListener);
                ViewUtil.getInstance().setText(btnDialogButtonNegativestacked, mNegativeButtonText, mediumFont, mNegativeButtonListener);
                ViewUtil.getInstance().setText(btnDialogButtonNeutralStacked, mNeutralButtonText, mediumFont, mNeutralButtonListener);
                ViewUtil.getInstance().setViewGone(rlDialogLayoutButtons);
                ViewUtil.getInstance().setViewVisible(rlDialogLayoutButtons);
                ViewUtil.getInstance().setViewVisible(llDialogLayoutButtonsStacked);
            } else {
                ViewUtil.getInstance().setText(btnDialogPositive, mPositiveButtonText, mediumFont, mPositiveButtonListener);
                ViewUtil.getInstance().setText(btnDialogNegative, mNegativeButtonText, mediumFont, mNegativeButtonListener);
                ViewUtil.getInstance().setText(btnDialogNeutral, mNeutralButtonText, mediumFont, mNeutralButtonListener);
                ViewUtil.getInstance().setViewVisible(rlDialogLayoutButtons);
                ViewUtil.getInstance().setViewGone(llDialogLayoutButtonsStacked);
            }
            if (TextUtils.isEmpty(mPositiveButtonText) && TextUtils.isEmpty(mNegativeButtonText) && TextUtils.isEmpty(mNeutralButtonText)) {
                ViewUtil.getInstance().setViewGone(rlDialogLayoutButtons);
            }
            return content;
        }

        private void setPaddingOfTitleAndMessage(Context ctx, TextView title, TextView message) {
            int dp24 = ctx.getResources().getDimensionPixelSize(R.dimen.dp_24);
            int dp16 = ctx.getResources().getDimensionPixelSize(R.dimen.dp_16);
            if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mMessage)) {
                title.setPadding(dp24, dp24, dp24, dp16);
                message.setPadding(dp24, 0, dp24, dp16);
            } else if (TextUtils.isEmpty(mTitle)) {
                message.setPadding(dp24, dp16, dp24, dp16);
            } else if (TextUtils.isEmpty(mMessage)) {
                title.setPadding(dp24, dp24, dp24, dp16);
            }
        }

        private boolean shouldStackButtons() {
            return shouldStackButton(mPositiveButtonText) || shouldStackButton(mNegativeButtonText)
                    || shouldStackButton(mNeutralButtonText);
        }

        private boolean shouldStackButton(CharSequence text) {
            return text != null && text.length() > 12;
        }
    }

    protected abstract Builder build(Builder initialBuilder);
}
