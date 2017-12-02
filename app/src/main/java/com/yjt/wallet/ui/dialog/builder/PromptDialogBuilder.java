package com.yjt.wallet.ui.dialog.builder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;

import com.yjt.wallet.base.constant.Temp;
import com.yjt.wallet.base.dialog.BaseDialogBuilder;
import com.yjt.wallet.ui.dialog.PromptDialog;

public class PromptDialogBuilder extends BaseDialogBuilder<PromptDialogBuilder> {

    private CharSequence title;
    private CharSequence prompt;
    private CharSequence positiveButtonText;
    private CharSequence negativeButtonText;
    private CharSequence neutralButtonText;

    public PromptDialogBuilder(FragmentManager fragmentManager, Class<? extends PromptDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public PromptDialogBuilder setTitle(Context ctx, int titleResourceId) {
        this.title = ctx.getString(titleResourceId);
        return this;
    }


    public PromptDialogBuilder setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public PromptDialogBuilder setPrompt(Context ctx, int messageResourceId) {
        this.prompt = ctx.getText(messageResourceId);
        return this;
    }

    public PromptDialogBuilder setPrompt(Context ctx, int resourceId, Object... formatArgs) {
        this.prompt = Html.fromHtml(String.format(Html.toHtml(new SpannedString(ctx.getText(resourceId))), formatArgs));
        return this;
    }

    public PromptDialogBuilder setPrompt(CharSequence message) {
        this.prompt = message;
        return this;
    }

    public PromptDialogBuilder setPositiveButtonText(Context ctx, int textResourceId) {
        this.positiveButtonText = ctx.getString(textResourceId);
        return this;
    }

    public PromptDialogBuilder setPositiveButtonText(CharSequence text) {
        this.positiveButtonText = text;
        return this;
    }

    public PromptDialogBuilder setPositiveButtonText(String text) {
        this.positiveButtonText = text;
        return this;
    }

    public PromptDialogBuilder setNegativeButtonText(Context ctx, int textResourceId) {
        this.negativeButtonText = ctx.getString(textResourceId);
        return this;
    }

    public PromptDialogBuilder setNegativeButtonText(CharSequence text) {
        this.negativeButtonText = text;
        return this;
    }

    public PromptDialogBuilder setNegativeButtonText(String text) {
        this.negativeButtonText = text;
        return this;
    }

    public PromptDialogBuilder setNeutralButtonText(Context ctx, int textResourceId) {
        this.neutralButtonText = ctx.getString(textResourceId);
        return this;
    }

    public PromptDialogBuilder setNeutralButtonText(CharSequence text) {
        this.neutralButtonText = text;
        return this;
    }

    public PromptDialogBuilder setNeutralButtonText(String text) {
        this.neutralButtonText = text;
        return this;
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), title);
        bundle.putCharSequence(Temp.DIALOG_PROMPT.getContent(), prompt);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent(), positiveButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent(), negativeButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEUTRAL.getContent(), neutralButtonText);
        return bundle;
    }

    @Override
    protected PromptDialogBuilder self() {
        return this;
    }
}
