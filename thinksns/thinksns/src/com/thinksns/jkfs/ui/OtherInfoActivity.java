package com.thinksns.jkfs.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;

public class OtherInfoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_otherpage);
	
		
		Bundle extras = getIntent().getExtras();
		UserFollowBean userfollow = (UserFollowBean) extras.get("userinfo");
		String fo = extras.getString("following");
		
		
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
	    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    AboutMeFragment fragment = new AboutMeFragment("1",userfollow,fo);
	    fragmentTransaction.replace(R.id.other_page_layout, fragment);
	    fragmentTransaction.commit();
	    
	    

	}

}
