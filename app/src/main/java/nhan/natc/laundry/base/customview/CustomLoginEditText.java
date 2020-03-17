package nhan.natc.laundry.base.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;

import nhan.natc.laundry.Constants;
import nhan.natc.laundry.R;
import nhan.natc.laundry.util.CommonUtils;

public class CustomLoginEditText extends LinearLayout implements TextWatcher {

    private RelativeLayout mMaintInputLayout;
    private TextInputLayout mTextInputLayout;
    private TextInputEditText mTextInputEdt;
    private AppCompatTextView mStatusTv, mFloatHintTv;
    private boolean mIsPassword = false;
    private boolean mIsError = false;
    private boolean mIsShowFloatHint = false;
    private ViewTreeObserver.OnGlobalLayoutListener mFloatHintGlobalListener;

    public CustomLoginEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public CustomLoginEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public CustomLoginEditText(Context context) {
        super(context);
        initView(null);
    }

    private void initView(@Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.editable_view, this);
        mMaintInputLayout = view.findViewById(R.id.mainInputLayout);
        mTextInputLayout = view.findViewById(R.id.inputLayout);
        mTextInputEdt = view.findViewById(R.id.inputEdt);
        mStatusTv = view.findViewById(R.id.statusTv);
        mFloatHintTv = view.findViewById(R.id.floatHint);
        Typeface mediumTf = ResourcesCompat.getFont(getContext(), R.font.montserrat_medium);
        Typeface regularTf = ResourcesCompat.getFont(getContext(), R.font.montserrat_regular);
        mTextInputEdt.setTypeface(regularTf);
        mStatusTv.setTypeface(regularTf);
        mTextInputLayout.setTypeface(mediumTf);
        mMaintInputLayout.setBackgroundResource(R.drawable.input_login_border_layout);
        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.EditView, 0, 0);
            String text = typedArray.getString(R.styleable.EditView_text);
            if (text != null && !text.equals("")) {
                mTextInputEdt.setText(text);
            }
//            boolean isEdit = typedArray.getBoolean(R.styleable.EditView_isEdit, false);
//            if (isEdit) {
//                mIvEdit.setVisibility(View.VISIBLE);
//            }
            mIsPassword = typedArray.getBoolean(R.styleable.EditView_isPassword, false);
            mIsShowFloatHint = typedArray.getBoolean(R.styleable.EditView_isShowFloatHint, false);
            if (mIsPassword) {
                mTextInputEdt.setImeOptions(EditorInfo.IME_ACTION_DONE);
                mTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                mTextInputLayout.setEndIconVisible(true);
                mTextInputEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mTextInputLayout.setStartIconDrawable(R.drawable.lock);
            } else {
                mTextInputEdt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                mTextInputEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                mTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                mTextInputLayout.setEndIconDrawable(R.drawable.check);
                mTextInputLayout.setEndIconActivated(false);
                mTextInputLayout.setEndIconVisible(false);
                int startIcon = typedArray.getResourceId(R.styleable.EditView_edtStartIcon, -1);
                if (startIcon != -1)
                    mTextInputLayout.setStartIconDrawable(startIcon);
                else
                    mTextInputLayout.setStartIconDrawable(R.drawable.user);
            }
            String hint = typedArray.getString(R.styleable.EditView_hint);
            if (!CommonUtils.isNullOrEmpty(hint)) {
                mTextInputEdt.setHint(hint);
                if (mIsShowFloatHint)
                    mFloatHintTv.setText(hint);
            }
            mTextInputEdt.setFocusable(true);
            typedArray.recycle();
        }
        mTextInputEdt.addTextChangedListener(this);
        if (!mIsPassword)
            mTextInputLayout.setEndIconOnClickListener(null);
        mTextInputEdt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && mIsError) {
                mMaintInputLayout.setBackgroundResource(R.drawable.input_error_border_layout);
            } else
            if (hasFocus && !mIsError) {
                mMaintInputLayout.setBackgroundResource(R.drawable.input_focus_border_layout);
                mStatusTv.setText("");
                mStatusTv.setVisibility(View.GONE);
            } else if (!hasFocus && !mIsPassword) {
                validateEmail();
//                if (!validateEmail())
//                    onError(true, getContext().getString(R.string.email_format_warning), hasFocus);
//                else
//                    onError(false, "", hasFocus);
            } else if (!hasFocus && mIsPassword) {
                validatePassword();
            } else {
                mMaintInputLayout.setBackgroundResource(R.drawable.input_login_border_layout);
                mMaintInputLayout.setBackground(null);
                mStatusTv.setText("");
                mStatusTv.setVisibility(View.GONE);
            }
            changeHintStatus(mIsError, hasFocus);
        });

        ViewTreeObserver vto = mFloatHintTv.getViewTreeObserver();
        mFloatHintGlobalListener = () -> {
            if (mFloatHintTv.getVisibility() == View.VISIBLE) {
                mTextInputEdt.setPadding(0, getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), 0, 0);
            } else {
                mTextInputEdt.setPadding(0, 0 ,0 ,0);
            }
        };
        vto.addOnGlobalLayoutListener(mFloatHintGlobalListener);
    }

    public void onDestroy() {
        mFloatHintTv.getViewTreeObserver().removeOnGlobalLayoutListener(mFloatHintGlobalListener);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 0) {
            if (!mIsPassword) {

            }
        } else {
            if (mIsError)
                mIsError = false;
            if (!mIsPassword)
                mTextInputLayout.setEndIconVisible(false);
        }
        changeHintStatus(mIsError, true);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void changeHintStatus(boolean isError, boolean hasFocus) {
        if (!mIsShowFloatHint)
            return;
        if (CommonUtils.isNullOrEmpty(mTextInputEdt.getText().toString())) {
            mFloatHintTv.setVisibility(View.GONE);
            return;
        } else {
            mFloatHintTv.setVisibility(View.VISIBLE);
        }
        if (isError || (hasFocus && isError)) {
            mFloatHintTv.setTextColor(CommonUtils.getColor(R.color.colorTextError, getContext()));
        } else if (hasFocus) {
            mFloatHintTv.setTextColor(CommonUtils.getColor(R.color.colorLightBlue, getContext()));
        } else {
            mFloatHintTv.setTextColor(CommonUtils.getColor(R.color.colorGray, getContext()));
        }
    }

    public void onError(boolean flag, String message, boolean hasFocus) {
        this.mIsError = flag;
        if (flag) {
            mMaintInputLayout.setBackgroundResource(R.drawable.input_error_border_layout);
            mStatusTv.setText(message);
            mStatusTv.setVisibility(View.VISIBLE);
            if (!mIsPassword)
                mTextInputLayout.setEndIconVisible(false);
        } else {
            mMaintInputLayout.setBackgroundResource(R.drawable.input_login_border_layout);
            mStatusTv.setText("");
            mStatusTv.setVisibility(View.GONE);
            if (!mIsPassword && !CommonUtils.isNullOrEmpty(mTextInputEdt.getText().toString()))
                mTextInputLayout.setEndIconVisible(true);
            else
                mTextInputLayout.setEndIconVisible(false);
        }
        changeHintStatus(flag, hasFocus);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listner) {
        this.mTextInputEdt.setOnEditorActionListener(listner);
    }

    public void focus() {
        this.mTextInputEdt.requestFocus();
    }

    public boolean validateEmail() {
        String email = mTextInputEdt.getText().toString().trim();
        if (!CommonUtils.isNullOrEmpty(email)) {
            Matcher matcher = Constants.EMAIL_PATTERN.matcher(email);
            if (matcher.matches()) {
                return true;
            } else {
                onError(true, getContext().getString(R.string.email_format_warning), mTextInputEdt.isFocused());
                return false;
            }
        } else {
            onError(true, getContext().getString(R.string.email_empty_warning), mTextInputEdt.isFocused());
            return false;
        }
    }

    public boolean validatePassword() {
        String password = mTextInputEdt.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(password)) {
            onError(true, getContext().getString(R.string.password_empty_warning), mTextInputEdt.isFocused());
            return false;
        }
        return true;
    }

    public String getTextValue() {
        return mTextInputEdt.getText().toString().trim();
    }
}
