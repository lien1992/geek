package com.thinksns.jkfs.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboRepostBean;

import android.os.Environment;

public class BackupToTxt {
	private FileWriter nwriter;
	private String BASE_DIR = "/sdcard";
	private String MID_DIR = "thinksns";
	private WeiboBean weibo;

	public BackupToTxt(WeiboBean weibo) {
		this.weibo = weibo;
	}

	public boolean writeTxt() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (weibo != null) {
				File dir = new File(BASE_DIR + File.separator + MID_DIR);
				if (!dir.exists())
					dir.mkdir();
				String path = BASE_DIR + File.separator + MID_DIR
						+ File.separator + weibo.getUname() + "-"
						+ weibo.getCtime() + ".txt";
				try {
					File file = new File(path);
					if (!file.exists()) {
						file.createNewFile();
					}
					// 以覆盖的方式写
					nwriter = new FileWriter(file, false);
					nwriter.write("-----Thinksns微博备份-----" + "\n");
					nwriter.write(weibo.getUname() + "\t" + weibo.getCtime()
							+ "\n");
					nwriter.write(weibo.getContent() + "\n");
					WeiboRepostBean repost = weibo.getTranspond_data();
					if (repost != null) {
						nwriter.write("-----被转发微博-----" + "\n");
						nwriter.write(repost.getUname() + "\t"
								+ repost.getCtime() + "\n");
						nwriter.write(repost.getContent() + "\n");
					}
					nwriter.write("-----END-----" + "\n");
					nwriter.flush();
					nwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return false;
			}
			return true;
		}
		return false;
	}
}
