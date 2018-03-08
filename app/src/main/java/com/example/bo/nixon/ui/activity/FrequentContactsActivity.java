package com.example.bo.nixon.ui.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpActivity;
import com.example.bo.nixon.presenter.FrequentContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FrequentContactsActivity extends BaseMvpActivity<FrequentContract.FrequentPresenter> {

    @BindView(R.id.frequent_listview)
    ListView mFrequentListView;
    @BindView(R.id.frequent_add)
    TextView mFrequentAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequent_contacts);
        ButterKnife.bind(this);
    }

    @Override
    protected FrequentContract.FrequentPresenter createPresenter() {
        return new FrequentContract.FrequentPresenter();
    }

    @OnClick(R.id.frequent_add)
    public void onViewClicked() {
      //  Intent intent = new Intent(this, ContactActivity.class);
      //  startActivityForResult(intent, 789);
    }

}
