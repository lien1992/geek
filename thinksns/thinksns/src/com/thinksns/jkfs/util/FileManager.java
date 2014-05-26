package com.thinksns.jkfs.util;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.thinksns.jkfs.base.ThinkSNSApplication;

public class FileManager {

	private static final String AVATAR_SMAll = "avatar_small";
	private static final String AVATAR_LARGE = "avatar_large";
	private static final String PICTURE_THUMBNAIL = "picture_thumbnail";
	private static final String PICTURE_BMIDDLE = "picture_bmiddle";
	private static final String PICTURE_LARGE = "picture_large";
	private static final String EMOTION = "emotion";

	private static String getSdCardPath() {
		if (isExternalStorageMounted()) {
			return ThinkSNSApplication.getInstance().getExternalCacheDir()
					.getAbsolutePath();
		} else {
			return "";
		}
	}

	public File getAlbumStorageDir(String albumName) {

		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			AppLogger.e("Directory not created");
		}
		return file;
	}

	public static boolean isExternalStorageMounted() {

		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);

		return !(!canRead || onlyRead || unMounted);
	}

	public static String getUploadPicTempFile() {

		if (!isExternalStorageMounted())
			return "";
		else
			return getSdCardPath() + File.separator + "upload.jpg";
	}

	public static File createNewFileInSDCard(String absolutePath) {
		if (!isExternalStorageMounted()) {
			AppLogger.e("sdcard unavailiable");
			return null;
		}

		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		} else {
			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}

			try {
				if (file.createNewFile()) {
					return file;
				}
			} catch (IOException e) {
				AppLogger.d(e.getMessage());
				return null;

			}

		}
		return null;

	}

	public static List<String> getCachePath() {
		List<String> path = new ArrayList<String>();
		if (isExternalStorageMounted()) {
			String thumbnailPath = getSdCardPath() + File.separator
					+ PICTURE_THUMBNAIL;
			String middlePath = getSdCardPath() + File.separator
					+ PICTURE_BMIDDLE;
			String oriPath = getSdCardPath() + File.separator + PICTURE_LARGE;

			path.add(thumbnailPath);
			path.add(middlePath);
			path.add(oriPath);
		}
		return path;
	}

	public static boolean deleteCache() {
		String path = getSdCardPath() + File.separator;
		return deleteDirectory(new File(path));
	}

	public static boolean deletePictureCache() {
		String thumbnailPath = getSdCardPath() + File.separator
				+ PICTURE_THUMBNAIL;
		String middlePath = getSdCardPath() + File.separator + PICTURE_BMIDDLE;
		String oriPath = getSdCardPath() + File.separator + PICTURE_LARGE;

		deleteDirectory(new File(thumbnailPath));
		deleteDirectory(new File(middlePath));
		deleteDirectory(new File(oriPath));

		return true;
	}

	private static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static boolean saveToPicDir(String path) {
		if (!isExternalStorageMounted())
			return false;

		File file = new File(path);
		String name = file.getName();
		String newPath = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getAbsolutePath()
				+ File.separator + name;
		try {
			FileManager.createNewFileInSDCard(newPath);
			copyFile(file, new File(newPath));
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	private static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} finally {
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
}
