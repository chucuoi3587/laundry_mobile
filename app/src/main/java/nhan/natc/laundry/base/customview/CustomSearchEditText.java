package nhan.natc.laundry.base.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import nhan.natc.laundry.R;
import nhan.natc.laundry.util.CommonUtils;

public class CustomSearchEditText extends LinearLayout {
    private TextInputLayout mInputLayout;
    private TextInputEditText mInputEdt;
    private AppCompatTextView mFloatHintTv;
    private ImageView mExtraIcon, mExtraPlusIcon;
    private boolean mHintEnable = false;
    private Integer mTag;
    private int mEndIcon;
    private CustomerSearchViewListener mListener;
    private ViewTreeObserver.OnGlobalLayoutListener mFloatHintGlobalListener;
    private int mStartIcon;
    private boolean mIsQuickClearText = false;

    public interface CustomerSearchViewListener {
        void onTextChanged(Integer tag, View view, CharSequence s);
        boolean OnEditorAction(Integer tag, int actionId, KeyEvent event);
        void onEndIconClick(Integer tag, View view);
        void onClearText(Integer tag, View view);
    }

    public CustomSearchEditText(Context context) {
        super(context);
        initViews(null);
    }

    public CustomSearchEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_search_layout, this);
        mInputLayout = view.findViewById(R.id.inputLayout);
        mInputEdt = view.findViewById(R.id.inputEdt);
        mFloatHintTv = view.findViewById(R.id.floatHint);
        mExtraIcon = view.findViewById(R.id.extraIcn);
        mExtraPlusIcon = view.findViewById(R.id.extraPlusIcn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mExtraIcon.setBackground(CommonUtils.getResourceDrawable(getContext(), R.drawable.ripple_end_icon_background));
            mExtraPlusIcon.setBackground(CommonUtils.getResourceDrawable(getContext(), R.drawable.ripple_end_icon_background));
        } else {
            mExtraIcon.setBackground(CommonUtils.getResourceDrawable(getContext(), R.drawable.end_icon_bg_selector));
            mExtraPlusIcon.setBackground(CommonUtils.getResourceDrawable(getContext(), R.drawable.end_icon_bg_selector));
        }
        Typeface regularTf = ResourcesCompat.getFont(getContext(), R.font.montserrat_regular);
        mInputEdt.setTypeface(regularTf);
        mInputLayout.setTypeface(regularTf);
        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSearchEditText, 0, 0);
            mStartIcon = typedArray.getResourceId(R.styleable.CustomSearchEditText_startIcon, -1);
            mEndIcon = typedArray.getResourceId(R.styleable.CustomSearchEditText_endIcon, -1);
            int extraIcon = typedArray.getResourceId(R.styleable.CustomSearchEditText_extraIcon, -1);
            int hint = typedArray.getResourceId(R.styleable.CustomSearchEditText_hintSearch, -1);
            int floatHint = typedArray.getResourceId(R.styleable.CustomSearchEditText_floatHintSearch, -1);
            float textSize = typedArray.getDimension(R.styleable.CustomSearchEditText_textSize, 0);
            mTag = typedArray.getResourceId(R.styleable.CustomSearchEditText_tag, -1);
            mHintEnable = typedArray.getBoolean(R.styleable.CustomSearchEditText_hintEnable, false);
            mIsQuickClearText = typedArray.getBoolean(R.styleable.CustomSearchEditText_isQuickClearText, false);
            if (mStartIcon != -1)
                mInputLayout.setStartIconDrawable(mStartIcon);
            else {
                mInputLayout.setStartIconDrawable(null);
                if (mEndIcon != -1) {
                    mInputEdt.setPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), 0, getContext().getResources().getDimensionPixelOffset(R.dimen.toggle_width_size), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                } else {
                    mInputEdt.setPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), 0, getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                }
            }
            if (mIsQuickClearText) {
                mInputLayout.setEndIconDrawable(R.drawable.close);
                mInputLayout.setEndIconVisible(false);
            }
            if (mEndIcon != -1) {
                if (mIsQuickClearText) {
                    mExtraIcon.setImageResource(mEndIcon);
                    mExtraIcon.setVisibility(View.VISIBLE);
                } else {
                    mInputLayout.setEndIconDrawable(mEndIcon);
                    mInputLayout.setEndIconVisible(true);
                }
            } else if (!mIsQuickClearText){
                mInputLayout.setEndIconDrawable(null);
                mInputLayout.setEndIconVisible(false);
            }
            if (extraIcon != -1) {
                if (mIsQuickClearText && mEndIcon != -1) {
                    mExtraPlusIcon.setImageResource(extraIcon);
                    mExtraPlusIcon.setVisibility(View.VISIBLE);
                } else {
                    mExtraIcon.setImageResource(extraIcon);
                    mExtraIcon.setVisibility(View.VISIBLE);
                }
            }
            if (hint != -1) {
                if (mHintEnable) {
                    if (floatHint != -1)
                        mFloatHintTv.setText(floatHint);
                    else
                        mFloatHintTv.setText(hint);
                }
                mInputEdt.setHint(hint);
            }
            if (textSize > 0) {
                mInputEdt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            // process text color
            int color = typedArray.getColor(R.styleable.CustomSearchEditText_textColor, CommonUtils.getColor(R.color.colorGray, getContext()));
            mInputEdt.setTextColor(color);
            int inputType = typedArray.getInt(R.styleable.CustomSearchEditText_inputType, -1);
            if (inputType != -1) {
                mInputEdt.setInputType(inputType);
            } else {
                mInputEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            }
            int maxlength = typedArray.getInt(R.styleable.CustomSearchEditText_maxlength, 0);
            if (maxlength != 0) {
                mInputEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
            }
            typedArray.recycle();
        }
        mInputEdt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mInputEdt.setFocusable(true);
        mInputEdt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mIsQuickClearText && !CommonUtils.isNullOrEmpty(mInputEdt.getText().toString().trim()))
                    mInputLayout.setEndIconVisible(true);
                this.setBackgroundResource(R.drawable.input_focus_border_layout);
                mInputLayout.setHintTextAppearance(R.style.FilterHintTextFocusAppearance);
                if (mHintEnable)
                    mFloatHintTv.setTextColor(CommonUtils.getColor(R.color.colorLightBlue, getContext()));
            } else {
                if (mIsQuickClearText)
                    mInputLayout.setEndIconVisible(false);
                this.setBackgroundResource(R.drawable.home_top_border_layout);
                mInputLayout.setHintTextAppearance(R.style.FilterHintTextAppearance);
                if (mHintEnable)
                    mFloatHintTv.setTextColor(CommonUtils.getColor(R.color.colorGray, getContext()));
            }
        });
        mInputEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mListener != null)
                    mListener.onTextChanged(mTag, CustomSearchEditText.this, s);
                if (s.length() > 0 && mHintEnable) {
                    mFloatHintTv.setVisibility(View.VISIBLE);
                } else if (s.length() == 0 && mHintEnable){
                    mFloatHintTv.setVisibility(View.GONE);
                }
                if (mIsQuickClearText && mInputEdt.hasFocus()) {
                    if (s.length() > 0) {
                        mInputLayout.setEndIconVisible(true);
                    } else {
                        mInputLayout.setEndIconVisible(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (mIsQuickClearText) {
            mExtraIcon.setOnClickListener(l -> {
                if (mListener != null)
                    mListener.onEndIconClick(mTag, l);
            });
            mInputLayout.setEndIconOnClickListener(l -> {
                if (mListener != null)
                    mListener.onClearText(mTag, l);
            });
        } else {
            mInputLayout.setEndIconOnClickListener(l -> {
                if (mListener != null)
                    mListener.onEndIconClick(mTag, l);
            });
        }
        mInputEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (mListener != null) {
                mListener.OnEditorAction(mTag, actionId, event);
                return true;
            }
            return false;
        });
        ViewTreeObserver vto = mFloatHintTv.getViewTreeObserver();
        mFloatHintGlobalListener = () -> {
            if (mFloatHintTv.getVisibility() == View.VISIBLE) {
                if (mEndIcon != -1) {
                    mInputEdt.setPadding(0, getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), getContext().getResources().getDimensionPixelOffset(R.dimen.toggle_width_size), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                } else {
                    mInputEdt.setPadding(0, getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                }
            } else if (mStartIcon != -1) {
                if (mEndIcon != -1) {
                    mInputEdt.setPadding(0, 0, getContext().getResources().getDimensionPixelOffset(R.dimen.toggle_width_size), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                } else {
                    mInputEdt.setPadding(0, 0, getContext().getResources().getDimensionPixelOffset(R.dimen.button_padding_left), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                }
            } else {
                if (mEndIcon != -1) {
                    mInputEdt.setPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), 0, getContext().getResources().getDimensionPixelOffset(R.dimen.toggle_width_size), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                } else {
                    mInputEdt.setPadding(getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), 0, getContext().getResources().getDimensionPixelOffset(R.dimen.country_gap), getContext().getResources().getDimensionPixelOffset(R.dimen.two_dp));
                }
            }
        };
        vto.addOnGlobalLayoutListener(mFloatHintGlobalListener);
    }

    public void setViewId(int id) {
        setId(id);
    }

    public void setListener(CustomerSearchViewListener listener) {
        this.mListener = listener;
    }

    public void clearFocus() {
        mInputEdt.clearFocus();
    }

    public void destroy() {
        mFloatHintTv.getViewTreeObserver().removeOnGlobalLayoutListener(mFloatHintGlobalListener);
    }

    public void focus() {
        mInputEdt.requestFocus();
    }

    public TextInputEditText getInputEditText() {
        return mInputEdt;
    }

    public ImageView getExtraIcon() {
        return mExtraIcon;
    }

    public ImageView getExtraPlusIcon() {
        return mExtraPlusIcon;
    }

    public LinearLayout getMainLayout() {
        return findViewById(R.id.mainLayout);
    }

    public void setInputEnable(boolean flag) {
        mInputEdt.setFocusable(flag);
    }
}
