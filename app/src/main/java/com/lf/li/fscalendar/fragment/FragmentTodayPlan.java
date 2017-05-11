package com.lf.li.fscalendar.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lf.li.fscalendar.R;

/**
 * Created by LiFei on 2016/11/2.
 */

public class FragmentTodayPlan extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_today_plan,null);
        return view;
    }
}
