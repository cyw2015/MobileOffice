package com.cyw.mobileoffice.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cyw.mobileoffice.activity.OfficeActivity;
import com.cyw.mobileoffice.fragment.ContactsFragment;
import com.cyw.mobileoffice.fragment.FacilityFragment;
import com.cyw.mobileoffice.fragment.MessageFragment;
import com.cyw.mobileoffice.fragment.MySettingFragment;

/**
 * Created by cyw on 2016/5/26.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private final static int PAGER_COUNT=4;
    private ContactsFragment contactsFragment = null;
    private MessageFragment messageFragment = null;
    private FacilityFragment facilityFragment = null;
    private MySettingFragment mySettingFragment = null;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        contactsFragment = new ContactsFragment();
        messageFragment = new MessageFragment();
        facilityFragment = new FacilityFragment();
        mySettingFragment = new MySettingFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case OfficeActivity.PAGE_ONE:
                fragment = contactsFragment;
                break;
            case OfficeActivity.PAGE_TWO:
                fragment = messageFragment;
                break;
            case OfficeActivity.PAGE_THREE:
                fragment = facilityFragment;
                break;
            case OfficeActivity.PAGE_FOUR:
                fragment = mySettingFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position "+position);
        super.destroyItem(container, position, object);
    }
}
