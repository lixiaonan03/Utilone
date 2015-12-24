package com.lxn.utilone.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

public class BitmapUtil {
	/**
	 * Bitmap 转 byte[]
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * byte[] 转 Bitmap
	 * @param bm
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	
	/**
	 * 根据Uri转实际物理路径
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String convertUri2Path(Context context,Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA};
		Cursor actualimagecursor = context.getContentResolver().query(uri,proj,null,null,null);
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor.getString(actual_image_column_index);
		return img_path;
	}
	
	/**
	 * uri 转  Bitmap
	 * @param resolver getContentResolver()
	 * @param uri
	 * @return
	 */
	public static Bitmap decodeUriAsBitmap(Context context,Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	/**
	 * 获取旋转后的图片BitMap
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap getRotateBitmap(Context context,Uri uri){
		String path = "";
		Bitmap bm = BitmapUtil.decodeUriAsBitmap(context, uri);
		String scheme = uri.getScheme();
		if (scheme.equals("file"))
			path = uri.toString();
		else if (scheme.equals("content"))
			path = BitmapUtil.convertUri2Path(context,uri);
		else return null;
		
		int degree = BitmapUtil.readPictureDegree(path);
		return BitmapUtil.rotateBitmap(bm,degree);
	}
	
	/**
	 * 压缩图片大小  按压缩率
	 * @param image
	 * @param options 30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0 
	 * @return
	 */
	public static Bitmap getCompressBitmap(Bitmap image,int options) {
		ByteArrayOutputStream baos = null;
		ByteArrayInputStream isBm = null;
		Bitmap bitmap = null;
		
        try{  
    		baos = new ByteArrayOutputStream();
    		image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
    		isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
    		bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        }finally{  
            try {  
                if(baos != null) baos.close() ;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
            try {  
                if(isBm != null) isBm.close() ;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        }
		return bitmap;
	}
	
	/**
	 * 压缩图片大小  按图片大小
	 * @param image
	 * @param size 缩后图片是否大于100kb
	 * @return
	 */
	public static Bitmap getCompressBitmapBySize(Bitmap image,int size) {
		ByteArrayOutputStream baos = null;
		ByteArrayInputStream isBm = null;
		Bitmap bitmap = null;
		
        try{  
    		baos = new ByteArrayOutputStream();
    		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    		int options = 100;
    		while ( baos.toByteArray().length / 1024 > size) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
    			baos.reset();//重置baos即清空baos
    			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
    			options -= 10;//每次都减少10
    		}
    		isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
    		bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        }finally{  
            try {  
                if(baos != null) baos.close() ;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
            try {  
                if(isBm != null) isBm.close() ;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        }
		return bitmap;
	}

	/**
	 * 读取图片角度
	 * @param path
	 * @return
	 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	/**
	 * 旋转图片
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){
		if(bitmap == null)
			return null ;
		
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}
	
	public static Bitmap createImageThumbnail(String filePath) {
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;

		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (Exception e) {

		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
