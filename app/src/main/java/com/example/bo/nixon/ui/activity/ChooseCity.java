package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.ChooseCityAdapter;
import com.example.bo.nixon.base.BaseMvpActivity;
import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.model.countrycode.GetCountryNameSort;
import com.example.bo.nixon.presenter.ChooseCityContract;
import com.example.bo.nixon.ui.view.SmartToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseCity extends BaseMvpActivity<ChooseCityContract.ChooseCityPresenter>
        implements ChooseCityContract.ChooseCityNixonView, AdapterView.OnItemClickListener {

    @BindView (R.id.choose_city_toolbar)
    SmartToolbar mChooseCityToolbar;
    @BindView (R.id.choose_city_list_view)
    ListView mChooseCityListView;
    private List<ChooseCityBean.ObjectBean> mList;
    private GetCountryNameSort mGetCountryNameSort;
    private ChooseCityAdapter mAdapter;
    public static final String START_CHOOSECITY_KEY = "START_CHOOSECITY_KEY";
    private boolean mIsBlack;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Bundle extras = getIntent ().getExtras ();
        mIsBlack = extras.getBoolean (START_CHOOSECITY_KEY, false);
        if (mIsBlack) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setToolbarColor (getResources ().getColor (R.color.black));
            }
            setContentView (R.layout.activity_choose_city);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setToolbarColor (getResources ().getColor (R.color.bg_color));
            }
            setContentView (R.layout.activity_choose_city_white);
        }
        ButterKnife.bind (this);
        initToolbar ();
        initListener ();
        initData ();
    }

    private void initListener () {
    }

    private void initToolbar () {
        View backView = View.inflate (this, R.layout.back_layout, null);

        ImageView back = (ImageView) backView.findViewById (R.id.back_img);
        if (mIsBlack) {
            backView.setBackgroundColor (getResources ().getColor (R.color.black));
            back.setImageResource (R.drawable.icon_last_step);
        } else {
            backView.setBackgroundColor (getResources ().getColor (R.color.bg_color));
            back.setImageResource (R.drawable.icon_return);
        }
        mChooseCityToolbar.setTittle (getResources ().getString (R.string.choose_city_title));
        mChooseCityToolbar.addBackView (backView);
    }

    private void initData () {
        mGetCountryNameSort = new GetCountryNameSort ();
        presenter.requestCityList ();
    }

    @Override
    protected ChooseCityContract.ChooseCityPresenter createPresenter () {
        return new ChooseCityContract.ChooseCityPresenter ();
    }

    @Override
    public void requestSuss (List<ChooseCityBean.ObjectBean> list) {
        mList = list;
        mAdapter = new ChooseCityAdapter (list, getWindowManager (), mIsBlack);
        mChooseCityListView.setAdapter (mAdapter);
        mChooseCityListView.setOnItemClickListener (this);
    }

    @Override
    public void requestFail () {
    }

    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
        if (mList != null) {
            Intent intent = new Intent ();
            intent.putExtra ("data", mList.get (i));
            setResult (RESULT_OK, intent);
            finish ();
        }
    }
}
