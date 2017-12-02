package com.yjt.wallet.ui.dialog.builder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;

import com.yjt.wallet.base.constant.Temp;
import com.yjt.wallet.base.dialog.BaseDialogBuilder;
import com.yjt.wallet.ui.dialog.ImagePromptDialog;


public class ImagePromptDialogBuilder extends BaseDialogBuilder<ImagePromptDialogBuilder> {

    private CharSequence mTitle;
    private int mPromptImage;
    private CharSequence mPrompt;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private CharSequence mNeutralButtonText;

    public ImagePromptDialogBuilder(FragmentManager fragmentManager, Class<? extends ImagePromptDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public ImagePromptDialogBuilder setTitle(Context ctx, int titleResourceId) {
        mTitle = ctx.getString(titleResourceId);
        return this;
    }

    public ImagePromptDialogBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public ImagePromptDialogBuilder setPromptImage(int promptImageResourceId) {
        mPromptImage = promptImageResourceId;
        return this;
    }

    public ImagePromptDialogBuilder setPrompt(Context ctx, int messageResourceId) {
        mPrompt = ctx.getText(messageResourceId);
        return this;
    }

    public ImagePromptDialogBuilder setPrompt(Context ctx, int resourceId, Object... formatArgs) {
        mPrompt = Html.fromHtml(String.format(Html.toHtml(new SpannedString(ctx.getText(resourceId))), formatArgs));
        return this;
    }

    public ImagePromptDialogBuilder setPrompt(CharSequence message) {
        mPrompt = message;
        return this;
    }

    public ImagePromptDialogBuilder setPositiveButtonText(Context ctx, int textResourceId) {
        mPositiveButtonText = ctx.getString(textResourceId);
        return this;
    }

    public ImagePromptDialogBuilder setPositiveButtonText(CharSequence text) {
        mPositiveButtonText = text;
        return this;
    }

    public ImagePromptDialogBuilder setNegativeButtonText(Context ctx, int textResourceId) {
        mNegativeButtonText = ctx.getString(textResourceId);
        return this;
    }

    public ImagePromptDialogBuilder setNegativeButtonText(CharSequence text) {
        mNegativeButtonText = text;
        return this;
    }

    public ImagePromptDialogBuilder setNeutralButtonText(Context ctx, int textResourceId) {
        mNeutralButtonText = ctx.getString(textResourceId);
        return this;
    }

    public ImagePromptDialogBuilder setNeutralButtonText(CharSequence text) {
        mNeutralButtonText = text;
        return this;
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), mTitle);
        bundle.putInt(Temp.DIALOG_PROMPT_IMAGE.getContent(), mPromptImage);
        bundle.putCharSequence(Temp.DIALOG_PROMPT.getContent(), mPrompt);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent(), mPositiveButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent(), mNegativeButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEUTRAL.getContent(), mNeutralButtonText);
        return bundle;
    }

    @Override
    protected ImagePromptDialogBuilder self() {
        return this;
    }
}
