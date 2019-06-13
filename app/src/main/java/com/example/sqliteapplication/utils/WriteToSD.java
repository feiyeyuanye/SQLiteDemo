package com.example.sqliteapplication.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  将assets文件夹下的数据库写入SD卡中
 **/
public class WriteToSD {

	private Context context;
	/**
	 * 	写入到sd卡中
	 */
//	public static String SDCARD_PATH = android.os.Environment
//			.getExternalStorageDirectory().getAbsolutePath() + "/dbfile/";

	/**
	 * 	写入到data目录下
	 */
	public static String SDCARD_PATH ;

	public WriteToSD(Context context){
		this.context = context;
		SDCARD_PATH = context.getFilesDir() + File.separator;
	}
	
	public String assetsWriteToSD(String fileName){
		String fileP = "";
		// 判断文件是否存在
		if(!isExist(fileName)){
			fileP = write(fileName);
		}else{
			fileP = SDCARD_PATH  + fileName;
		}
		return fileP;
	}
	
	private String write(String fileName){
		String fileAllPath = "";
		InputStream inputStream;
		try {
			inputStream = context.getResources().getAssets().open(fileName);
			File file = new File(SDCARD_PATH);
			if(!file.exists()){
				file.mkdirs();
			}
			fileAllPath = SDCARD_PATH  + fileName;
			FileOutputStream fileOutputStream = new FileOutputStream(SDCARD_PATH + fileName);
			byte[] buffer = new byte[512];
			int count = 0;
			while((count = inputStream.read(buffer)) > 0){
				fileOutputStream.write(buffer, 0 ,count);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileAllPath;
	}
	
	private boolean isExist(String fileName){
		File file = new File(SDCARD_PATH + fileName);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取sdcard路径
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
}