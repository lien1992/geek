package com.thinksns.think;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class MainFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "主页", "聊天", "与我有关", "收藏", "频道","微吧"};
    protected static final int[] ICONS = new int[] {
            R.drawable.home_selector,
            R.drawable.chat_selector,
            R.drawable.at_selector,
            R.drawable.collection_selector,
            R.drawable.chanel_selector,            
            R.drawable.weiba_selector,
    };

    private int mCount = CONTENT.length;

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	switch(position){
    	case 1:return new ChatFragment();
    	case 2:return new AboutMeFragment();
    	case 3:return new CollectionFragment();
    	case 4:return new ChanelFragment();
    	case 5:return new WeibaFragment();
    	default:return new HomeFragment();
    	}
    }
    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return MainFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}