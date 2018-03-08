package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.EditPersonalPagerAdapter;
import com.example.bo.nixon.adapter.UnitAdapter;
import com.example.bo.nixon.base.BaseMvpNotitleActivity;
import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.presenter.EditUserInfoContract;
import com.example.bo.nixon.ui.view.SmartSeekBar;
import com.example.bo.nixon.ui.view.SmartSwitchView;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;
import com.example.bo.nixon.utils.TimeZoneAbbreUtils;
import com.example.smartcustomview.views.DstView;
import com.example.smartcustomview.views.SmartNoScrollViewPager;
import com.example.smartcustomview.views.seekbar.BubbleSeekBar;
import com.smart.smartble.utils.TimeZoneUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditUserInfoActivity extends BaseMvpNotitleActivity<EditUserInfoContract
        .EditUserInfoPresenter> implements EditUserInfoContract.EditUserInfoView, BubbleSeekBar
        .OnProgressChangedListener, UnitAdapter.SwitchChangedListener {

    private static final int REQUESTCODE = 0x123;
    @BindView (R.id.act_edit_pager)
    SmartNoScrollViewPager mActEditPager;
    @BindView (R.id.edit_user_next)
    TextView mEditUserNext;
    @BindView (R.id.act_edit_back)
    ImageView mActEditBack;
    @BindView (R.id.edit_userinfo_skip)
    TextView mEditUserinfoSkip;
    @BindView (R.id.edit_user_title)
    TextView mEditUserTitle;
    @BindView (R.id.skip_and_start)
    TextView mSkipAndStart;
    private View mWatchName;
    private View mUserTall;
    private View mUserWeight;
    private View mActGoal;
    private View mAutoZone;
    private EditText mEditText;
    private TextView mTallTv;
    private TextView mWeightTv;
    private TextView mWeightUnitTv;
    private TextView mGoalTv;
    private TextView mGoalUnitTv;
    private SmartSwitchView mAutoTimeZone;
    private SmartSeekBar mTallSeek;
    private SeekBar mWeightSeek;
    private BubbleSeekBar mGoalSeek;
    private boolean isAutoTimeZone;//是否自动同步时区
    private TextView mTallUnitBig;
    private TextView mMTallIntv;
    private TextView mTallUnitSmall;
    private InputMethodManager mImm;
    private View mUnit;
    private PersonalInfoBean mTimeFormat;
    private PersonalInfoBean mDistance;
    private PersonalInfoBean mHeight;
    private PersonalInfoBean mWeight;
    private final int TIME = 0;
    private final int DISTANCE = 0;
    private final int HEIGHT = 0;
    private final int WEIIGHT = 0;
    private View mHomeLocation;
    private View mSecondLocation;
    private EditText mHomeLoca;
    private EditText mHomeLocaAbbre;
    private EditText mSecondLoca;
    private View mSelectSecond;
    private DstView mDstView;
    private SmartSwitchView mSwitchView;
    private TextView mAbbreTV;
    private boolean isChecked;
    private EditText mSecondLocaAbbre;
    private TextView mWeightReduce;
    private TextView mWeihtAdd;
    private int mWeightProgress;
    private TextView mTallReduce;
    private TextView mTallAdd;
    private int mTallProgress;
    private String mWeightContent;
    private boolean mMHeightShowSwitch;
    private boolean mMWeightShowSwitch;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_edit_user_info);
        clearCustomTimeZoneName ();
        ButterKnife.bind (this);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_edit_user_info));
        mImm = (InputMethodManager) getSystemService (NixonApplication.INPUT_METHOD_SERVICE);
        initPager ();
    }

    /**
     * 清除上个用户保存的时区信息
     */
    private void clearCustomTimeZoneName () {
        SPUtils.putString (this, Constant.SECOND_ZONE_FUll_NAME_KEY, "");
        SPUtils.putString (this, Constant.SECOND_ZONE_NAME_KEY, "");
        SPUtils.putString (this, Constant.HOME_LOCATION_KEY, "");
        SPUtils.putString (this, Constant.HOME_LOCATION_ABBRE, "");
    }

    @Override
    protected EditUserInfoContract.EditUserInfoPresenter createPresenter () {
        return new EditUserInfoContract.EditUserInfoPresenter ();
    }

    private void initPager () {
        List<View> mDate = new ArrayList<> ();
        mWatchName = View.inflate (this, R.layout.black_edit_watch_name, null);
        mUserTall = View.inflate (this, R.layout.black_edit_personal_tall, null);
        mUserWeight = View.inflate (this, R.layout.black_edit_personal_weight, null);
        mActGoal = View.inflate (this, R.layout.black_edit_personal_goal, null);
        mAutoZone = View.inflate (this, R.layout.black_edit_personal_auto, null);
        View pager1 = View.inflate (this, R.layout.help_pager1, null);
        View pager2 = View.inflate (this, R.layout.help_pager2, null);
        View pager3 = View.inflate (this, R.layout.help_pager3, null);
        View pager4 = View.inflate (this, R.layout.help_pager4, null);
        View pager5 = View.inflate (this, R.layout.help_pager5, null);
        View pager6 = View.inflate (this, R.layout.help_pager6, null);
        View pager7 = View.inflate (this, R.layout.help_pager7, null);
        View pager8 = View.inflate (this, R.layout.help_pager8, null);
        View pager9 = View.inflate (this, R.layout.help_pager9, null);
        View pager10 = View.inflate (this, R.layout.help_pager10, null);
        View pager11 = View.inflate (this, R.layout.help_pager11, null);
        View pager12 = View.inflate (this, R.layout.help_pager12, null);
        View pager13 = View.inflate (this, R.layout.help_pager13, null);
        mUnit = View.inflate (this, R.layout.help_unit, null);
        mHomeLocation = View.inflate (this, R.layout.black_edit_home_location, null);
        mSecondLocation = View.inflate (this, R.layout.black_edit_second_location, null);
        mSelectSecond = LayoutInflater.from (this).inflate (R.layout
                .black_select_second_location, null);
        mDate.add (mWatchName);
        mDate.add (mUnit);
        mDate.add (mUserWeight);
        mDate.add (mUserTall);
        mDate.add (mActGoal);
        mDate.add (mAutoZone);
        mDate.add (mHomeLocation);
        mDate.add (mSecondLocation);
        mDate.add (mSelectSecond);
        mDate.add (pager1);
        mDate.add (pager2);
        mDate.add (pager3);
        mDate.add (pager4);
        mDate.add (pager5);
        mDate.add (pager6);
        mDate.add (pager7);
        mDate.add (pager8);
        mDate.add (pager9);
        mDate.add (pager10);
        mDate.add (pager11);
        mDate.add (pager12);
        mDate.add (pager13);
        EditPersonalPagerAdapter adapter = new EditPersonalPagerAdapter (mDate);
        mActEditPager.setAdapter (adapter);
        mActEditPager.setOffscreenPageLimit (mDate.size ());
        initView ();
        initListener ();
    }

    private void initView () {
        initUnit ();
        mEditText = (EditText) mWatchName.findViewById (R.id.edit_watch_name);

        mTallTv = (TextView) mUserTall.findViewById (R.id.act_tall_seek_text_ft);
        mTallReduce = (TextView) mUserTall.findViewById (R.id.act_tall_seek_text_reduce);
        mTallAdd = (TextView) mUserTall.findViewById (R.id.act_tall_seek_text_add);
        mTallUnitBig = (TextView) mUserTall.findViewById (R.id.act_tall_seek_text_unit_big);
        mMTallIntv = (TextView) mUserTall.findViewById (R.id.act_tall_seek_text_in);
        mTallUnitSmall = (TextView) mUserTall.findViewById (R.id.act_tall_seek_text_unit_small);
        mTallSeek = (SmartSeekBar) mUserTall.findViewById (R.id.act_tall_smart_seekbar);
        setTallInfo ();

        mWeightTv = (TextView) mUserWeight.findViewById (R.id.act_weight_seek_text);
        mWeightReduce = (TextView) mUserWeight.findViewById (R.id.act_weight_text_reduce);
        mWeihtAdd = (TextView) mUserWeight.findViewById (R.id.act_weight_text_add);
        mWeightUnitTv = (TextView) mUserWeight.findViewById (R.id.act_weight_seek_text_unit);
        mWeightSeek = (SeekBar) mUserWeight.findViewById (R.id.act_weight_seekbar);
        setWeightInfo ();

        mGoalTv = (TextView) mActGoal.findViewById (R.id.act_ambitious_seek_text);
        mGoalUnitTv = (TextView) mActGoal.findViewById (R.id.act_ambitious_seek_text_unit);
        mGoalSeek = (BubbleSeekBar) mActGoal.findViewById (R.id.act_ambitious_seekbar);

        mGoalSeek.getConfigBuilder ().min (0).max (39500).sectionCount (158).trackColor
                (ContextCompat.getColor (NixonApplication.getContext (), R.color.transparent))
                .secondTrackColor (ContextCompat.getColor (NixonApplication.getContext (), R
                        .color.transparent)).trackSize (1).secondTrackSize (1).seekBySection ()
                .thumbColor (ContextCompat.getColor (NixonApplication.getContext (), R.color
                        .sup_color)).thumbRadius (18).bubbleColor (ContextCompat.getColor
                (NixonApplication.getContext (), R.color.transparent)).bubbleTextColor
                (ContextCompat.getColor (NixonApplication.getContext (), R.color.transparent))
                .build ();
        mGoalSeek.setOnProgressChangedListener (this);
        mGoalSeek.setBubProgress (9500);
        setGoalInfo ();

        mAutoTimeZone = (SmartSwitchView) mAutoZone.findViewById (R.id.act_auto_time_zone);

        initCustomName ();
        initSecond ();
        initWeightListener ();
        initHeightListener ();
        initTextChangeListener ();
    }

    private void initTextChangeListener () {
        mWeightContent = mWeightTv.getText ().toString ().trim ();
        mWeightTv.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                mWeightContent = s.toString ().trim ();
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        });
    }

    private void initHeightListener () {
        mTallProgress = mTallSeek.getProgress ();
        mTallReduce.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mMHeightShowSwitch) {
                    reduceTall ();
                } else {
                    mTallProgress = mTallSeek.getProgress ();
                    mTallProgress--;
                    if (mTallProgress <= 12) {
                        return;
                    }
                    initTallTvContent (String.valueOf (mTallProgress));
                    mTallSeek.setProgress (mTallProgress);
                }
            }
        });

        mTallAdd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mMHeightShowSwitch) {
                    addTall ();
                } else {
                    mTallProgress = mTallSeek.getProgress ();
                    mTallProgress++;
                    if (mTallProgress >= 108) {
                        return;
                    }
                    initTallTvContent (String.valueOf (mTallProgress));
                    mTallSeek.setProgress (mTallProgress);
                }
            }
        });
    }

    private void reduceTall () {
        String content = mTallTv.getText ().toString ().trim ();
        int progress = 0;
        if (content.contains (".")) { //m
            String substring = content.substring (0, content.length () - 2);
            double d = Double.parseDouble (substring);
            if (d < 0.3048) {
                return;
            }
            d = d - 0.01;
            DecimalFormat df = new DecimalFormat ("#0.00");
            progress = (int) (Double.parseDouble (StringUtils.cm2in (d * 100)) + 0.75f);
            content = df.format (d) + " m";
            Log.e ("addTall", " reduce   content  === " + content);
        }
        mTallTv.setText (content);
        mTallSeek.setProgress (progress - 12);
    }

    private void addTall () {
        String content = mTallTv.getText ().toString ().trim ();
        int progress = 0;
        if (content.contains (".")) { //m
            String substring = content.substring (0, content.length () - 2);
            double d = Double.parseDouble (substring);
            if (d > 3.048) {
                return;
            }
            d = d + 0.01;
            DecimalFormat df = new DecimalFormat ("#0.00");
            content = df.format (d) + " m";
            Log.e ("addTall", " add    content  === " + content);
            progress = (int) Double.parseDouble (StringUtils.cm2in (d * 100));
        }
        mTallTv.setText (content);
        mTallSeek.setProgress (progress - 12);
    }

    private void initWeightListener () {
        mWeightProgress = mWeightSeek.getProgress ();
        mWeightReduce.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mMWeightShowSwitch) {
                    reduceWeight ();
                } else {
                    mWeightProgress = mWeightSeek.getProgress ();
                    mWeightProgress--;
                    initWeightTvContent (String.valueOf (mWeightProgress + 10));
                    mWeightSeek.setProgress (mWeightProgress);
                }
            }
        });
        mWeihtAdd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mMWeightShowSwitch) {
                    addWeight ();
                } else {
                    mWeightProgress = mWeightSeek.getProgress ();
                    mWeightProgress++;
                    initWeightTvContent (String.valueOf (mWeightProgress + 10));
                    mWeightSeek.setProgress (mWeightProgress);
                }
            }
        });
    }

    private void reduceWeight () {
        int progress = 0;
        if (mWeightContent.contains (".")) { //kg
            String substring = mWeightContent.substring (0, mWeightContent.length () - 3);
            double d = Double.parseDouble (substring);
            if (d < 4.5359) {
                return;
            }
            d = d - 0.1;
            DecimalFormat df = new DecimalFormat ("#0.0");
            mWeightContent = df.format (d) + " kg";
            progress = (int) Double.parseDouble (StringUtils.g2lbs (d * 1000));
        }
        mWeightTv.setText (mWeightContent);
        mWeightSeek.setProgress (progress - 10);
    }

    private void addWeight () {
        int progress = 0;
        if (mWeightContent.contains (".")) { //kg
            String substring = mWeightContent.substring (0, mWeightContent.length () - 3);
            double d = Double.parseDouble (substring);
            if (d > 226.796) {
                return;
            }
            d = d + 0.1;
            DecimalFormat df = new DecimalFormat ("#0.0");
            mWeightContent = df.format (d) + " kg";
            progress = (int) Double.parseDouble (StringUtils.g2lbs (d * 1000));
        }
        mWeightTv.setText (mWeightContent);
        mWeightSeek.setProgress (progress);
    }

    private void initCustomName () {
        mHomeLoca = (EditText) mHomeLocation.findViewById (R.id.edit_home_location);
        mHomeLocaAbbre = (EditText) mHomeLocation.findViewById (R.id.edit_home_location_abbre);
        mSecondLoca = (EditText) mSecondLocation.findViewById (R.id.edit_second_location);
        mSecondLocaAbbre = (EditText) mSecondLocation.findViewById (R.id
                .edit_second_location_abbre);

        String currentTimeZone = TimeZoneUtil.getCurrentTimeZone (Calendar.getInstance ()
                .getTimeZone ());
        TimeZoneAbbreUtils zoneAbbreUtils = TimeZoneAbbreUtils.getInstance ();
        String abbreName = zoneAbbreUtils.getAbbreName (currentTimeZone);
        String tineZoneName = zoneAbbreUtils.getTineZoneName (currentTimeZone);
        mHomeLoca.setText (tineZoneName);
        mHomeLocaAbbre.setText (abbreName);
        mSecondLoca.setText (getResources ().getString (R.string.pacific_standard_time));
        mSecondLocaAbbre.setText (getResources ().getString (R.string.pacific_standard_time_abbre));
    }

    private void saveCustomeName () {
        String homeLocaName = mHomeLoca.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (homeLocaName)) {
            SPUtils.putString (this, Constant.HOME_LOCATION_KEY, homeLocaName);
        }
        String homeLocaAbbre = mHomeLocaAbbre.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (homeLocaAbbre)) {
            SPUtils.putString (this, Constant.HOME_LOCATION_ABBRE, homeLocaAbbre);
        }
        String secondLocaName = mSecondLoca.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (secondLocaName)) {
            SPUtils.putString (this, Constant.SECOND_ZONE_FUll_NAME_KEY, secondLocaName);
        }
        String secondLocaAbbre = mSecondLocaAbbre.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (secondLocaAbbre)) {
            Log.d ("secondLocaAbbre", "  保存自定义第二市区缩写  " + secondLocaAbbre);
            SPUtils.putString (this, Constant.SECOND_ZONE_NAME_KEY, secondLocaAbbre);
        }
    }

    private void initSecond () {
        mDstView = (DstView) mSelectSecond.findViewById (R.id.select_second_location_dstview);
        mSwitchView = (SmartSwitchView) mSelectSecond.findViewById (R.id
                .select_second_location_switch);
        mAbbreTV = (TextView) mSelectSecond.findViewById (R.id.select_second_location_abbre);
        mSwitchView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                mSwitchView.setCheck (!isChecked);
                mDstView.changeUI (!isChecked);
                isChecked = !isChecked;
            }
        });

        mAbbreTV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (EditUserInfoActivity.this, ChooseCity.class);
                intent.putExtra (ChooseCity.START_CHOOSECITY_KEY, true);
                EditUserInfoActivity.this.startActivityForResult (intent, REQUESTCODE);
            }
        });
    }

    private void saveDstSettings () {
        String content = mDstView.getContent ();
        if (isChecked) {
            content = content.substring (0, content.length () - 1);
        } else {
            content = "0.0";
        }
        Log.e ("dstsettings", "content = " + content);
        SPUtils.putString (this, Constant.DST_KEY, content);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            ChooseCityBean.ObjectBean objectBean = (ChooseCityBean.ObjectBean) data
                    .getSerializableExtra ("data");
            String city = objectBean.getCity ();
            mAbbreTV.setText (city);
            String timezone = objectBean.getTimezone ();
            SPUtils.putString (this, Constant.SECOND_ZONE_KEY, timezone);
        }
    }

    private void initUnit () {
        ListView unitList = (ListView) mUnit.findViewById (R.id.unit_list);
        List<PersonalInfoBean> mData = new ArrayList<> ();
        mTimeFormat = new PersonalInfoBean (getResources ().getString (R.string.time_fom), "24",
                "12", /*SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_TIME)
                */false, TIME);
        mDistance = new PersonalInfoBean (getResources ().getString (R.string.distance), "km",
                "mile", /*SPUtils.getBoolean (NixonApplication.getContext (), Constant
                .UNIT_DISTANCE)*/false, DISTANCE);
        mHeight = new PersonalInfoBean (getResources ().getString (R.string.height), "m", "ft",
                /*SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT)
                * */false, HEIGHT);
        mWeight = new PersonalInfoBean (getResources ().getString (R.string.weight), "kg", "lb",
                /*SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT)
                * */false, WEIIGHT);
        mData.add (mTimeFormat);
        mData.add (mDistance);
        mData.add (mHeight);
        mData.add (mWeight);

        mMHeightShowSwitch = mHeight.isShowSwitch ();
        mMWeightShowSwitch = mWeight.isShowSwitch ();
        UnitAdapter adapter = new UnitAdapter (mData, true);
        adapter.setSwitchChangedListener (this);
        unitList.setAdapter (adapter);
    }

    private void saveUnitSettings () {
        SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_TIME, mTimeFormat
                .isShowSwitch ());
        SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_DISTANCE, mDistance
                .isShowSwitch ());
        SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT, mHeight
                .isShowSwitch ());
        SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT, mWeight
                .isShowSwitch ());
    }

    private void setWeightInfo () {

        initWeightTvContent (String.valueOf (mWeightSeek.getProgress () + 10));
    }

    private void initWeightTvContent (String stempWeight) {
        //        SPUtils.getBoolean(NixonApplication.getContext(), Constant.UNIT_WEIGHT, false)
        StringBuilder content = new StringBuilder ();
        if (mMWeightShowSwitch) {
            content.append (StringUtils.lbs2kg (stempWeight)).append (" kg");
        } else {
            content.append (stempWeight).append (" lbs");
        }
        mWeightTv.setText (content);
    }

    private void setGoalInfo () {
        mGoalTv.setText ((mGoalSeek.getProgress () + 500 + ""));
        mGoalUnitTv.setText (" steps");
    }

    private void setTallInfo () {
        int progress = mTallSeek.getProgress ();
        initTallTvContent (String.valueOf (progress));
    }

    private void initTallTvContent (String tall) {
        if (tall.contains (".")) {
            tall = tall.split ("\\.")[0];
        }
        int s = Integer.parseInt (tall);
        if (s == 120) {
            tall = s + "";
        } else {
            tall = s + 12 + "";
        }
        StringBuilder content = new StringBuilder ();
        //        SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT, false)
        Log.e ("mHeight.isShowSwitch ()", "    mHeight.isShowSwitch ()  == " + mHeight
                .isShowSwitch ());
        if (mMHeightShowSwitch) {
            content.append (StringUtils.inch2m (tall)).append (" m");
        } else {
            content.append (StringUtils.getFt (tall)).append ("ft ").append (StringUtils.getIn
                    (tall)).append ("in");
        }
        mTallTv.setText (content.toString ());
    }

    private void initListener () {
        mTallSeek.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged (SeekBar seekBar, int i, boolean b) {
                Log.e ("setChangeListener", "  TALL  b   = " + b);
                if (!b) {  //from user
                    setTallInfo ();
                }
            }

            @Override
            public void onStartTrackingTouch (SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch (SeekBar seekBar) {

            }
        });
        mWeightSeek.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged (SeekBar seekBar, int i, boolean b) {
                Log.e ("setChangeListener", "  weight  b   = " + b);
                if (b) {
                    setWeightInfo ();
                }
            }

            @Override
            public void onStartTrackingTouch (SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch (SeekBar seekBar) {

            }
        });
        mAutoTimeZone.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (isAutoTimeZone) {
                    mAutoTimeZone.setCheck (!isAutoTimeZone);
                    isAutoTimeZone = false;
                } else {
                    mAutoTimeZone.setCheck (!isAutoTimeZone);
                    isAutoTimeZone = true;
                }
            }
        });
    }

    @OnClick ({R.id.act_edit_back, R.id.edit_user_next, R.id.edit_userinfo_skip})
    public void onViewClicked (View view) {

        switch (view.getId ()) {
            case R.id.act_edit_back:
                goToPrePager ();
                break;
            case R.id.edit_user_next:
                goToNextPager ();
                break;
            case R.id.edit_userinfo_skip:
                updateUserInfo ();
                //                mActEditPager.setCurrentItem (5, false);
                mEditUserTitle.setVisibility (View.VISIBLE);
                mActEditBack.setVisibility (View.VISIBLE);
                mEditUserinfoSkip.setVisibility (View.GONE);
                Intent intent = new Intent (this, ConnectWatchHomeActivity.class);
                startActivity (intent);
                finish ();
                break;
        }
    }

    private void goToNextPager () {
        int currentItem = mActEditPager.getCurrentItem ();
        if (currentItem < 21) {
            if (currentItem == 0) {
                mActEditBack.setVisibility (View.VISIBLE);
                mEditUserinfoSkip.setVisibility (View.GONE);
                mSkipAndStart.setVisibility (View.GONE);
            }
            if (currentItem == 5) {
                updateUserInfo ();//上传数据
            }
            if (currentItem == 9) {
                mEditUserTitle.setVisibility (View.VISIBLE);
                mSkipAndStart.setVisibility (View.VISIBLE);
            }
            mActEditPager.setCurrentItem (currentItem + 1, false);
        } else {
            Intent intent = new Intent (this, ConnectWatchHomeActivity.class);
            startActivity (intent);
            finish ();
        }
    }

    private void goToPrePager () {
        int currentItem = mActEditPager.getCurrentItem ();
        if (currentItem > 0) {
            if (currentItem == 1) {
                mActEditBack.setVisibility (View.GONE);
                mEditUserinfoSkip.setVisibility (View.VISIBLE);
            }
            if (currentItem == 5) {
                mEditUserTitle.setVisibility (View.GONE);
                mSkipAndStart.setVisibility (View.GONE);
            }
            mActEditPager.setCurrentItem (currentItem - 1, false);
        }
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mActEditPager.getCurrentItem () != 0) {
                goToPrePager ();
                return false;
            }
        }
        return super.onKeyDown (keyCode, event);
    }

    private void updateUserInfo () {
        presenter.upLoadUserInfo (initParmas ());
    }

    private Map<String, String> initParmas () {
        Map<String, String> parmas = new HashMap<> ();
        parmas.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant
                .CUSTOMER_ID));
        parmas.put ("nickname", getWatchName ());
        parmas.put ("weight", StringUtils.lbs2g (mWeightSeek.getProgress () + 10));
        parmas.put ("height", StringUtils.inch2cm (mTallSeek.getProgress () + 12));
        parmas.put ("activityGoal", (mGoalSeek.getProgress () + 500 + ""));
        parmas.put ("unit", 0 + "");

        saveUserInfo ();
        Log.e ("NIXONLOGIN", " height = " + StringUtils.inch2cm (mTallSeek.getProgress () + 12) +
                " weight = " + StringUtils.lbs2g (mWeightSeek.getProgress () + 10));

        presenter.setTarget2Watch (mGoalSeek.getProgress () + 500); // 将运动目标设置到手表
        return parmas;
    }

    private void saveUserInfo () {
        SPUtils.putString (NixonApplication.getContext (), Constant.WEIGHT_KEY, mWeightSeek
                .getProgress () + 10 + "");
        SPUtils.putString (NixonApplication.getContext (), Constant.TALL_KEY, mTallSeek
                .getProgress () + 12 + "");
        SPUtils.putString (NixonApplication.getContext (), Constant.GOAL_KEY, (mGoalSeek
                .getProgress () + 500 + ""));
        SPUtils.putBoolean (NixonApplication.getContext (), Constant.IS_AUTO_TIME_KEY,
                isAutoTimeZone);
        SPUtils.putString (NixonApplication.getContext (), Constant.FALSE_TALL_KEY, mTallTv
                .getText ().toString ().trim ());
        SPUtils.putString (NixonApplication.getContext (), Constant.FALSE_WEIGHT_KEY, mWeightTv
                .getText ().toString ().trim ());
    }

    private String getWatchName () {
        String watchName = mEditText.getText ().toString ().trim ();
        if (TextUtils.isEmpty (watchName)) {
            SPUtils.putString (NixonApplication.getContext (), Constant.WATCH_NAME_KEY, "NAME");
            return "NAME";
        } else {
            SPUtils.putString (NixonApplication.getContext (), Constant.WATCH_NAME_KEY, watchName);
            return watchName;
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        saveUnitSettings ();
        saveCustomeName ();
        saveDstSettings ();
        System.gc ();
    }

    /**
     * 根据点击的位置判断是否隐藏键盘
     *
     * @return
     */
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        if (ev.getAction () == MotionEvent.ACTION_UP) {
            View view = getCurrentFocus ();
            if (inputIsShow (view, ev)) {
                if (null != mImm) {
                    mImm.hideSoftInputFromWindow (view.getWindowToken (), 0);
                }
            }
        }
        return super.dispatchTouchEvent (ev);
    }

    @Override
    public void upLoadSuccess () {
        SPUtils.putBoolean (this, Constant.UPLOAD_INFO_SUCC_KEY, true);
    }

    @Override
    public void upLoadFail () {

    }

    @Override
    public void onProgressChanged (int progress, float progressFloat) {
        setGoalInfo ();
    }

    @Override
    public void getProgressOnActionUp (int progress, float progressFloat) {

    }

    @Override
    public void getProgressOnFinally (int progress, float progressFloat) {

    }

    @Override
    public void switchChanged (boolean b, int position) {
        switch (position) {
            case 0: // hour format
                break;
            case 1: // distance
                break;
            case 2: // height
                mMHeightShowSwitch = !b;
                setTallInfo ();
                break;
            case 3: // weight
                mMWeightShowSwitch = !b;
                setWeightInfo ();
                break;
        }
    }
}
