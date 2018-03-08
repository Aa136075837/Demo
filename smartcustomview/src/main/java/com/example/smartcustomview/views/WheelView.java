package com.example.smartcustomview.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.smartcustomview.R;
import com.example.smartcustomview.adapter.WheelViewAdapter;
import com.example.smartcustomview.listener.OnWheelChangedListener;
import com.example.smartcustomview.listener.OnWheelClickedListener;
import com.example.smartcustomview.listener.OnWheelScrollListener;
import com.example.smartcustomview.utils.DisplayUtil;
import java.util.LinkedList;
import java.util.List;

public class WheelView extends View {

	/** Top and bottom shadows colors */
    /*/ Modified by wulianghuan 2014-11-25
	private int[] SHADOWS_COLORS = new int[] { 0xFF111111,
			0x00AAAAAA, 0x00AAAAAA };
	//*/
	private int[] SHADOWS_COLORS = new int[] { 0x00E9E9E9,
			0x00E9E9E9, 0x00E9E9E9 };
	
	private int[] ITME_SHADOWS_COLORS = new int[] { 0xFF858585,
			0xffc2c2c2, 0xdfE9E9E9 };
	
	/** Top and bottom items offset (to hide that) */
	private static final int ITEM_OFFSET_PERCENT = 0;

	/** Left and right padding value */
	private static final int PADDING = 10;

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ITEMS = 5;

	// Wheel Values
	private int currentItem = 0;

	// Count of visible items
	private int visibleItems = DEF_VISIBLE_ITEMS;

	// Item height
	private int itemHeight = 0;

	// Center Line
	private Drawable centerDrawable;

	// Wheel drawables
	private int wheelBackground = R.drawable.wheel_bg;
	private int wheelForeground = R.drawable.wheel_val;

	// Shadows drawables
	private GradientDrawable topShadow;
	private GradientDrawable bottomShadow;
	
	private GradientDrawable topItemShadow;
	private GradientDrawable bottomItemShadow;
	private boolean hasItemShape = false;

	// Draw Shadows
	private boolean drawShadows = true;

	// Scrolling
	private WheelScroller scroller;
	private boolean isScrollingPerformed;
	private int scrollingOffset;

	// Cyclic
	boolean isCyclic = false;

	// Items layout
	private LinearLayout itemsLayout;

	// The number of first item in layout
	private int firstItem;

	// View adapter
	private WheelViewAdapter viewAdapter;

	// Recycle
	private WheelRecycle recycle = new WheelRecycle(this);

	// Listeners
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener> ();
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
	private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();
	private Paint mDrawTextPaint = new Paint();
	private TextView mFirstTv;

	private boolean  isFirst = true;
	private String mRightExtraString = "";
	private Paint mRightExtraPaint = new Paint();
	
	private int mSelectColor;
	private int mNormalColor;
	private	 static final int DEFALIT_SELECTCOLOR = R.color.text_sport_setting_color;
	private	 static final int DEFALIT_NORMALCOLOR = R.color.text_color;
	private static final int DEFAULT_SCALE_OFF =  2;
	private static final int DEFAULT_SELECT_TEXT_SIZE = 30;
	private static final int DEFAULT_NORMAL_TEXT_SIZE = 25;
	private boolean isScaleChange = false;
	private int mSelectSize = DEFAULT_SELECT_TEXT_SIZE;
	private int mNormalSize = DEFAULT_NORMAL_TEXT_SIZE;
	private int mScaleOff = DEFAULT_SCALE_OFF;
	private boolean isFillCenterLine = false ;
	private Paint mCenterLinePaint;
	private int mCenterLinerColor;
	private int mScaleApha;
	private boolean isChageAlpha;
	private int mChangeColors[] = new int[] {0x00ffffff,0x10ffffff,0x20ffffff};
	private int mRightExtraSize = DisplayUtil.sp2px(getContext(),14);
    private Typeface  mTypeFace = Typeface.createFromAsset(getContext ().getAssets(),"fonts/Montserrat-Regular.ttf");

    /**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * Initializes class data
	 * @param context the context
	 */
	private void initData(Context context) {
		scroller = new WheelScroller(getContext(), scrollingListener);
		mSelectColor = getResources().getColor(DEFALIT_SELECTCOLOR);
		mNormalColor = getResources().getColor(DEFALIT_NORMALCOLOR);
		mCenterLinerColor = getResources().getColor(R.color.text_color_tip);

	}

	// Scrolling listener
	WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
		@Override
		public void onStarted() {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}

		@Override
		public void onScroll(int distance) {
			doScroll(distance);
 
			int height = getHeight();
			if (scrollingOffset > height) {
				scrollingOffset = height;
				scroller.stopScrolling();
			} else if (scrollingOffset < -height) {
				scrollingOffset = -height;
				scroller.stopScrolling();
			}
		}

		@Override
		public void onFinished() {
			if (isScrollingPerformed) {
				notifyScrollingListenersAboutEnd();
				isScrollingPerformed = false;
			}

			scrollingOffset = 0;
			invalidate();
		}

		@Override
		public void onJustify() {
			if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
				scroller.scroll(scrollingOffset, 0);
			}
		}
	};

	/**
	 * Set the the specified scrolling interpolator
	 * @param interpolator the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.setInterpolator(interpolator);
	}

	/**
	 * Gets count of visible items
	 * 
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets the desired count of visible items.
	 * Actual amount of visible items depends on wheel layout parameters.
	 * To apply changes and rebuild view call measure().
	 * 
	 * @param count the desired count for visible items
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
	}

	/**
	 * Gets view adapter
	 * @return the view adapter
	 */
	public WheelViewAdapter getViewAdapter() {
		return viewAdapter;
	}

	// Adapter listener
	private DataSetObserver dataObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			invalidateWheel(false);
		}

		@Override
		public void onInvalidated() {
			invalidateWheel(true);
		}
	};

	/**
	 * Sets view adapter. Usually new adapters contain different views, so
	 * it needs to rebuild view by calling measure().
	 * 
	 * @param viewAdapter the view adapter
	 */
	public void setViewAdapter(WheelViewAdapter viewAdapter) {
		if (this.viewAdapter != null) {
			this.viewAdapter.unregisterDataSetObserver(dataObserver);
		}
		this.viewAdapter = viewAdapter;
		if (this.viewAdapter != null) {
			this.viewAdapter.registerDataSetObserver(dataObserver);
		}
		isFirst = true;
		invalidateWheel(true);
	}

	/**
	 * Adds wheel changing listener
	 * @param listener the listener
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * @param listener the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	/**
	 * Notifies changing listeners
	 * @param oldValue the old wheel value
	 * @param newValue the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * @param listener the listener
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * @param listener the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Adds wheel clicking listener
	 * @param listener the listener
	 */
	public void addClickingListener(OnWheelClickedListener listener) {
		clickingListeners.add(listener);
	}

	/**
	 * Removes wheel clicking listener
	 * @param listener the listener
	 */
	public void removeClickingListener(OnWheelClickedListener listener) {
		clickingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about clicking
	 */
	protected void notifyClickListenersAboutClick(int item) {
		for (OnWheelClickedListener listener : clickingListeners) {
			listener.onItemClicked(this, item);
		}
	}

	/**
	 * Gets current value
	 * 
	 * @return the current value
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * @param index the item index
	 * @param animated the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return; // throw?
		}

		int itemCount = viewAdapter.getItemsCount();
		if (index < 0 || index >= itemCount) {
			if (isCyclic) {
				while (index < 0) {
					index += itemCount;
				}
				index %= itemCount;
			} else{
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				int itemsToScroll = index - currentItem;
				if (isCyclic) {
					int scroll = itemCount + Math.min(index, currentItem) - Math.max(index, currentItem);
					if (scroll < Math.abs(itemsToScroll)) {
						itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
					}
				}
				scroll(itemsToScroll, 0);
			} else {
				scrollingOffset = 0;

				int old = currentItem;
				currentItem = index;

				notifyChangingListeners(old, currentItem);
                
				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * @param index the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);	
	}

	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * @param isCyclic the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		invalidateWheel(false);
	}

	/**
	 * Determine whether shadows are drawn
	 * @return true is shadows are drawn
	 */
	public boolean drawShadows() {
		return drawShadows;
	}

	/**
	 * Set whether shadows should be drawn
	 * @param drawShadows flag as true or false
	 */
	public WheelView setDrawShadows(boolean drawShadows) {
		this.drawShadows = drawShadows;
		return this;
	}

	/**
	 * Set the shadow gradient color
	 * @param start
	 * @param middle
	 * @param end
	 */
	public void setShadowColor(int start, int middle, int end) {
		SHADOWS_COLORS = new int[] {start, middle, end};
	}

	/**
	 * Sets the drawable for the wheel background
	 * @param resource
	 */
	public void setWheelBackground(int resource) {
		wheelBackground = resource;
		setBackgroundResource(wheelBackground);
	}

	/**
	 * Sets the drawable for the wheel foreground
	 * @param resource
	 */
	@SuppressWarnings("deprecation")
	public void setWheelForeground(int resource) {
		wheelForeground = resource;
		centerDrawable = getContext().getResources().getDrawable(wheelForeground);
	}

	/**
	 * Invalidates wheel
	 * @param clearCaches if true then cached views will be clear
	 */
	public void invalidateWheel(boolean clearCaches) {
		if (clearCaches) {
			recycle.clearAll();
			if (itemsLayout != null) {
				itemsLayout.removeAllViews();
			}
			scrollingOffset = 0;
		} else if (itemsLayout != null) {
			// cache all items
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		}

		invalidate();
	}

	/**
	 * Initializes resources
	 */
	@SuppressWarnings("deprecation")
	private void initResourcesIfNecessary() {
		if (centerDrawable == null) {
			centerDrawable = getContext().getResources().getDrawable(wheelForeground);
		}

		if (topShadow == null) {
			topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mChangeColors);
		}

		if (bottomShadow == null) {
			bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mChangeColors);
		}
		
		if (null == topItemShadow) {
			topItemShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, ITME_SHADOWS_COLORS);
		}
		
		if (null == bottomItemShadow) {
			bottomItemShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, ITME_SHADOWS_COLORS);
		}

		setBackgroundResource(wheelBackground);
	}

	/**
	 * Calculates desired height for layout
	 * 
	 * @param layout
	 *            the source layout
	 * @return the desired layout height
	 */
	private int getDesiredHeight(LinearLayout layout) {
		if (layout != null && layout.getChildAt(0) != null) {
			itemHeight = layout.getChildAt(0).getMeasuredHeight();
		}

		int desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50;

		return Math.max(desired, getSuggestedMinimumHeight());
	}

	/**
	 * Returns height of wheel item
	 * @return the item height
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		}

		if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
			itemHeight = itemsLayout.getChildAt(0).getHeight();
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	/**
	 * Calculates control width and creates text layouts
	 * @param widthSize the input layout width
	 * @param mode the layout mode
	 * @return the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		// TODO: make it static
		itemsLayout.setLayoutParams(new ViewGroup.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		itemsLayout.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int width = itemsLayout.getMeasuredWidth();

		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width += 2 * PADDING;

			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
			}
		}

		itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		return width;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		buildViewForMeasuring();

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layout(r - l, b - t);
	}

	/**
	 * Sets layouts width and height
	 * @param width the layout width
	 * @param height the layout height
	 */
	private void layout(int width, int height) {
		int itemsWidth = width - 2 * PADDING;

		itemsLayout.layout(0, 0, itemsWidth, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (viewAdapter != null && viewAdapter.getItemsCount() > 0) {
			updateView();

			drawItems(canvas);
			drawCenterRect(canvas);
		}

		if (drawShadows) drawShadows(canvas);
		
		if (isFirst) {
			doScroll(-1);
			isFirst = false;
		}
	}

	/**
	 * Draws shadows on top and bottom of control
	 * @param canvas the canvas for drawing
	 */
	private void drawShadows(Canvas canvas) {
		/*/ Modified by wulianghuan 2014-11-25
		int height = (int)(1.5 * getItemHeight());
		//*/
		//int height = (int)(3 * getItemHeight());
		//*/
		/*topShadow.setBounds(0, 0, getWidth(), height);
		topShadow.draw(canvas);

		bottomShadow.setBounds(0, getHeight() - height, getWidth(), getHeight());
		bottomShadow.draw(canvas); */
		topShadow.setBounds(0,0,getWidth(),getHeight());
		topShadow.draw(canvas);
		if (hasItemShape) {
				drawShape(canvas);
		} 
	}

	private void drawShape(Canvas canvas) {
	   int center = getHeight() / 2;
	   int itemCenter = getItemHeight() / 2;
	   topShadow.setBounds(0,0,getWidth(),center - itemCenter);
	   topShadow.draw(canvas);
	   bottomShadow.setBounds(0,center + itemCenter,getWidth(),getHeight());
	   bottomShadow.draw(canvas);
	}

	/**
	 * Draws items
	 * @param canvas the canvas for drawing
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();

		int top = (currentItem - firstItem) * getItemHeight() + (getItemHeight() - getHeight()) / 2;
		canvas.translate(PADDING, - top + scrollingOffset);

		itemsLayout.draw(canvas);

		canvas.restore();
	}

	/**
	 * Draws rect for current value
	 * @param canvas the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = (int) (getItemHeight() / 2 );
		Paint paint = new Paint();
		paint.setTextSize(DisplayUtil.sp2px(getContext(),20));
		paint.setColor(mCenterLinerColor);
		paint.setStrokeWidth((float) 2);
		float offX = 0;
		if (isFillCenterLine) {
			offX = getWidth();
		} else {
			if (viewAdapter.getItemsCount() > 0) {
				Object object = viewAdapter.getItemObject(0);
				if (object instanceof String) {	
					String text = (String)object;
					offX = paint.measureText(text);
					offX = DisplayUtil.dip2px(getContext(), 160);
				}
			}
		}
		canvas.drawLine(getWidth() / 2 - offX / 2, center - offset, getWidth() / 2 + offX / 2, center - offset, paint);
		canvas.drawLine(getWidth() / 2 - offX / 2, center + offset, getWidth() / 2 + offX / 2, center + offset, paint);
		
		if(!TextUtils.isEmpty(mRightExtraString)) {
            drawRightExtraText(canvas,offX);
		}
	}

	private void drawRightExtraText(Canvas canvas , float offX) {
		float textsize = DisplayUtil.sp2px(getContext(),mSelectSize -  3);
		mRightExtraPaint.setTextSize(textsize);
		mRightExtraPaint.setColor(mSelectColor);
		mRightExtraPaint.setAntiAlias(true);
		mRightExtraPaint.setStrokeWidth(10);
        mRightExtraPaint.setTypeface (Typeface.DEFAULT_BOLD);
		float textHeight = DisplayUtil.getFontHeight(18);
		int offTextX  = DisplayUtil.dip2px(getContext(), 18);
		if (isFillCenterLine) {
			offX = DisplayUtil.dip2px(getContext(), 60);
		}
		canvas.drawText(mRightExtraString, getWidth() / 2 + offX / 2 + offTextX, getHeight() / 2 + textHeight / 2 + 3, mRightExtraPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled() || getViewAdapter() == null) {
			return true;
		}

		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				break;

			case MotionEvent.ACTION_UP:
				if (!isScrollingPerformed) {
					int distance = (int) event.getY() - getHeight() / 2;
					if (distance > 0) {
						distance += getItemHeight() / 2;
					} else {
						distance -= getItemHeight() / 2;
					}
					int items = distance / getItemHeight();
					if (items != 0 && isValidItemIndex(currentItem + items)) {
						notifyClickListenersAboutClick(currentItem + items);
					}
				}
				break;
		}

		return scroller.onTouchEvent(event);
	}

	/**
	 * Scrolls the wheel
	 * @param delta the scrolling valuef
	 */
	public void doScroll(int delta) {
		scrollingOffset += delta;

		int itemHeight = getItemHeight();
		int count = scrollingOffset / itemHeight;

		int pos = currentItem - count;
		int itemCount = viewAdapter.getItemsCount();

		int fixPos = scrollingOffset % itemHeight;
		if (Math.abs(fixPos) <= itemHeight / 2) {
			fixPos = 0;
		}
		
		if (isCyclic && itemCount > 0) {
			if (fixPos > 0) {
				pos--;
				count++;
			} else if (fixPos < 0) {
				pos++;
				count--;
			}
			// fix position by rotating
			while (pos < 0) {
				pos += itemCount;
			}
			pos %= itemCount;
		} else {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= itemCount) {
				count = currentItem - itemCount + 1;
				pos = itemCount - 1;
			} else if (pos > 0 && fixPos > 0) {
				pos--;
				count++;
			} else if (pos < itemCount - 1 && fixPos < 0) {
				pos++;
				count--;
			}
		} 

		int offset = scrollingOffset; 
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		} 
         
		// update offset
		scrollingOffset = offset - count * itemHeight;
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
		int index  = itemsLayout.getChildCount(); 

		int centerIndex = currentItem - firstItem;
		for(int i = 0 ; i < index ; i++) {
			TextView tv = (TextView) itemsLayout.getChildAt(i);
            tv.setTypeface (mTypeFace);
            tv.setTypeface (Typeface.DEFAULT_BOLD);
				if (i == centerIndex) {
					tv.setTextColor(mSelectColor);
					tv.setTextSize(mSelectSize);
				} else {
					tv.setTextColor(getResources().getColor(R.color.text_color));
					tv.setTextSize(mNormalSize);
				}
		}
		
		if (isScaleChange) {
			changeShowScale(centerIndex,index);
		}
	}

	private void changeShowScale(int centerIndex,int index) {
		 int num = visibleItems / 2;
		 for (int i= 0 ;  i < num; i ++ ) {
			  int prePosition = centerIndex - (i + 1);
			  int nextPosition = centerIndex + (i + 1);
			  int scaleSize = mNormalSize - i * mScaleOff;
			  if (prePosition >= 0) {
				  TextView preTv  = (TextView) itemsLayout.getChildAt(prePosition);
				  if (null != preTv) {
                      preTv.setTypeface (mTypeFace);
                      preTv.setTypeface (Typeface.DEFAULT_BOLD);
                      preTv.setTextSize(scaleSize);
				  if (hasItemShape) {
					  preTv.setTextColor(ITME_SHADOWS_COLORS[i]);
				  }
			    }
			  }
			  if(nextPosition < index) {
				  TextView nestTv = (TextView) itemsLayout.getChildAt(nextPosition);
				  if (null != nestTv) {
                      nestTv.setTypeface (mTypeFace);
                      nestTv.setTypeface (Typeface.DEFAULT_BOLD);
                      nestTv.setTextSize(scaleSize);
				  if (hasItemShape) {
					  nestTv.setTextColor(ITME_SHADOWS_COLORS[i]);
				  }
				  }
			  }
		 }
	}

	/**
	 * Scroll the wheel
	 *
	 * @param time scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		int distance = itemsToScroll * getItemHeight() - scrollingOffset;
		scroller.scroll(distance, time);
	}

	/**
	 * Calculates range for wheel items
	 * @return the items range
	 */
	private ItemsRange getItemsRange() {
		if (getItemHeight() == 0) {
			return null;
		}

		int first = currentItem;
		int count = 1;

		while (count * getItemHeight() < getHeight()) {
			first--;
			count += 2; // top + bottom items
		}

		if (scrollingOffset != 0) {
			if (scrollingOffset > 0) {
				first--;
			}
			count++;

			// process empty items above the first or below the second
			int emptyItems = scrollingOffset / getItemHeight();
			first -= emptyItems;
			count += Math.asin(emptyItems);
		}
		return new ItemsRange(first, count);
	}

	/**
	 * Rebuilds wheel items if necessary. Caches all unused items.
	 * 
	 * @return true if items are rebuilt
	 */
	private boolean rebuildItems() {
		boolean updated = false;
		ItemsRange range = getItemsRange();
		if (itemsLayout != null) {
			int first = recycle.recycleItems(itemsLayout, firstItem, range);
			updated = firstItem != first;
			firstItem = first;
		} else {
			createItemsLayout();
			updated = true;
		}

		if (!updated) {
			updated = firstItem != range.getFirst() || itemsLayout.getChildCount() != range.getCount();
		}

		if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
			for (int i = firstItem - 1; i >= range.getFirst(); i--) {
				if (!addViewItem(i, true)) {
					break;
				}
				firstItem = i;
			}
		} else {
			firstItem = range.getFirst();
		}

		int first = firstItem;
		for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
			if (!addViewItem(firstItem + i, false) && itemsLayout.getChildCount() == 0) {
				first++;
			}
		}
		firstItem = first;

		return updated;
	}

	/**
	 * Updates view. Rebuilds items and label if necessary, recalculate items sizes.
	 */
	private void updateView() {
		if (rebuildItems()) {
			calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			layout(getWidth(), getHeight());
		}
	}

	/**
	 * Creates item layouts if necessary
	 */
	private void createItemsLayout() {
		if (itemsLayout == null) {
			itemsLayout = new LinearLayout(getContext());
			itemsLayout.setOrientation(LinearLayout.VERTICAL);
		}
	}

	/**
	 * Builds view for measuring
	 */
	private void buildViewForMeasuring() {
		// clear all items
		if (itemsLayout != null) {
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		} else {
			createItemsLayout();
		}

		// add views
		int addItems = visibleItems / 2;
		for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
			if (addViewItem(i, true)) {
				firstItem = i;
			}
		}
	}

	/**
	 * Adds view for item to items layout
	 * @param index the item index
	 * @param first the flag indicates if view should be first
	 * @return true if corresponding item exists and is added
	 */
	private boolean addViewItem(int index, boolean first) {
		View view = getItemView(index);
		if (view != null) {
			if (first) {
				itemsLayout.addView(view, 0);
			} else {
				itemsLayout.addView(view);
			}

			return true;
		}

		return false;
	}

	/**
	 * Checks whether intem index is valid
	 * @param index the item index
	 * @return true if item index is not out of bounds or the wheel is cyclic
	 */
	private boolean isValidItemIndex(int index) {
		return viewAdapter != null && viewAdapter.getItemsCount() > 0 &&
				(isCyclic || index >= 0 && index < viewAdapter.getItemsCount());
	}

	/**
	 * Returns view for specified item
	 * @param index the item index
	 * @return item view or empty view if index is out of bounds
	 */
	private View getItemView(int index) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return null;
		}
		int count = viewAdapter.getItemsCount();
		if (!isValidItemIndex(index)) {
			return viewAdapter.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
	}

	/**
	 * Stops scrolling
	 */
	public void stopScrolling() {
		scroller.stopScrolling();
	}
	
	public WheelView setRightExtraString(String extraString) {
		mRightExtraString = extraString;
		return this;
	}
	
	public WheelView setmSelectColor(int selectColor) {
		mSelectColor = selectColor;
		return this;
	}
	
   public WheelView setScaleChange(boolean scaleChange) {
	isScaleChange = scaleChange;
	return this;
   }
   
   public WheelView setHasItemShape(boolean hasItemShape) {
	this.hasItemShape = hasItemShape;
	return this;
} 
   public WheelView setScaleOff(int off) {
   mScaleOff = off;
   return this;
  }

	public WheelView setItemColor(int[] colors) {
		ITME_SHADOWS_COLORS = colors;
		return this;
	}
   
   public WheelView setNormalSize(int normalSize) {
	mNormalSize = normalSize;
	return this;
   }
   
   public WheelView setSelectSize (int selectSize) {
       mSelectSize = selectSize;
	   return this;
   }

	public WheelView setmRightExtraSize (int rightExtraSize) {
		mRightExtraSize = rightExtraSize;
		return this;
	}
   
   public WheelView setFillCenterLine (boolean isFill) {
	   isFillCenterLine = isFill;
	   return this;
   }
   
   public WheelView setCenterLineColor(int centerLineColor ) {
	   mCenterLinerColor = centerLineColor;
	   return this;
   }
   
   public void setBackgroupAlphaChange(boolean change) {
	   isChageAlpha  = change;
   }
}