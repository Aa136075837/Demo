package com.example.bo.nixon.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.bo.nixon.R;

public class SmartSwitchView  extends LinearLayout {

   private ImageView mSelectImageView;
   private ImageView mNormalImageView;
   private View mView;
   private boolean isSelect = false;
    private boolean mB;

    public SmartSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		init(context,attrs,defStyleAttr);
	}

	public SmartSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs,0);
	}

	public SmartSwitchView(Context context) {
		super(context);
		init(context,null,0);
	}

	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		LayoutInflater.from(context).inflate(R.layout.switch_layout, this, true);
		mSelectImageView = (ImageView) findViewById(R.id.item_kaiguan_open);
		mNormalImageView = (ImageView) findViewById(R.id.item_kaiguan_close);
		mView = findViewById(R.id.item_kaiguan_layout);
        TypedArray typedArray = context.obtainStyledAttributes (R.styleable.SmartSwitchView);
        mB = typedArray.getBoolean (R.styleable.SmartSwitchView_switch_selected, false);
        setStatus(mB);
	}

	private void setStatus(boolean b) {
        if (b) {
			mSelectImageView.setVisibility(View.VISIBLE);
			mNormalImageView.setVisibility(View.INVISIBLE);
            mView.setSelected (true);
		} else {
			mSelectImageView.setVisibility(View.INVISIBLE);
			mNormalImageView.setVisibility(View.VISIBLE);
            mView.setSelected (false);
		}
	}

	public void setCheck(boolean b) {
		//isSelect = b;
		setStatus(b);
	}

	public boolean isSelected() {
		return isSelect;
	}

	public void setIsSelected(boolean b) {
		//isSelect = b;
		setStatus(b);
	}

	public void changeTheStyle(int drawableBg,int normalBg,int selectBg) {
		mView.setBackgroundResource(drawableBg);
		Drawable norDrawable = getResources().getDrawable(normalBg);
		mNormalImageView.setImageDrawable(norDrawable);
		Drawable selDrawable = getResources().getDrawable(selectBg);
		mSelectImageView.setImageDrawable(selDrawable);
		invalidate();
	}

}
