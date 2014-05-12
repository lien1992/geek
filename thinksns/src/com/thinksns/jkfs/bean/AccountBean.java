package com.thinksns.jkfs.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 帐户
 *
 */

public class AccountBean implements Parcelable {
	private String uid;
	private String oauth_token;
	private String oauth_token_secret;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauthToken) {
		oauth_token = oauthToken;
	}

	public String getOauth_token_secret() {
		return oauth_token_secret;
	}

	public void setOauth_token_secret(String oauthTokenSecret) {
		oauth_token_secret = oauthTokenSecret;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(uid);
		dest.writeString(oauth_token);
		dest.writeString(oauth_token_secret);

	}

	public static final Parcelable.Creator<AccountBean> CREATOR = new Parcelable.Creator<AccountBean>() {
		public AccountBean createFromParcel(Parcel in) {
			AccountBean ab = new AccountBean();
			ab.uid = in.readString();
			ab.oauth_token = in.readString();
			ab.oauth_token_secret = in.readString();
			return ab;
		}

		public AccountBean[] newArray(int size) {
			return new AccountBean[size];
		}
	};

}
