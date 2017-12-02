package com.yjt.wallet.components.validation;

import android.content.Context;

public abstract class ValidationExecutor {

    public abstract boolean doValidate(Context context, String text);

}
