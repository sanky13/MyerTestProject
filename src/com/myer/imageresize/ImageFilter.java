package com.myer.imageresize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;

public class ImageFilter {

	
	String GIF = "gif";
	String PNG = "png";
	String JPG = "jpg";
	String BMP = "bmp";
	String JPEG = "jpeg";
	
	public static void writeToFile(String text, String Path) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					Path+"/log.txt"), true));
			bw.write(text);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
		}
	}
	
	public boolean accept(File file) {
		if(file != null) {
		if(file.isDirectory())
		return false;
		String extension = getExtension(file);
		if(extension != null && isSupported(extension))
		return true;
		}
		return false;
	}
	public boolean requireresize(File file) {
		if(file != null) {
		if(file.isDirectory())
			return false;
			String filename = file.getName();
			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			if(filename.contains("_zm_") && kilobytes > 100){
		
			return true;
			} else if (filename.contains("_fs_") && kilobytes > 20){
				return true;	
			} else if (filename.contains("_th_") && kilobytes > 5){
				return true;	
			}else{
				return false;
			}
		}
		return false;
	}
	
	public MyerImageDTO analyseImage(File file) {
		MyerImageDTO imageDTO = new MyerImageDTO();
		if(file != null) {
		if(file.isDirectory())
			return null;
			String filename = file.getName();
			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			
			if(filename.contains("_zm_") ){
				imageDTO.setImageType(MyerConstants.IMAGE_ZOOM);
				imageDTO.setOverShootValue(kilobytes - MyerConstants.IMAGE_ZOOM_RECOMMENDED_SIZE);
				imageDTO.setRecommendedSize(MyerConstants.IMAGE_ZOOM_RECOMMENDED_SIZE);
				imageDTO.setCurrentSize(kilobytes);
		
			} else if (filename.contains("_fs_") ){
				imageDTO.setImageType(MyerConstants.IMAGE_FULL);
				imageDTO.setOverShootValue(kilobytes - MyerConstants.IMAGE_FULL_RECOMMENDED_SIZE);
				imageDTO.setRecommendedSize(MyerConstants.IMAGE_FULL_RECOMMENDED_SIZE);
				imageDTO.setCurrentSize(kilobytes);	
			} else if (filename.contains("_th_") ){
				imageDTO.setImageType(MyerConstants.IMAGE_THUMBNAIL);
				imageDTO.setOverShootValue(kilobytes - MyerConstants.IMAGE_THUMBNAIL_RECOMMENDED_SIZE);
				imageDTO.setRecommendedSize(MyerConstants.IMAGE_THUMBNAIL_RECOMMENDED_SIZE);
				imageDTO.setCurrentSize(kilobytes);
			}else{
				imageDTO.setImageType(MyerConstants.IMAGE_SWATCH);
				imageDTO.setOverShootValue(kilobytes - MyerConstants.IMAGE_SWATCH_RECOMMENDED_SIZE);
				imageDTO.setRecommendedSize(MyerConstants.IMAGE_SWATCH_RECOMMENDED_SIZE);
				imageDTO.setCurrentSize(kilobytes);
			}
		}
		return imageDTO;
	}
	
	public float returnratio(File file) {
		float f = 0;
		if(file != null) {
		if(file.isDirectory())
			return f;
			String filename = file.getName();
			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			if(filename.contains("_zm_") && kilobytes > 150){
				 BigDecimal bd = new BigDecimal(Float.toString(new Float(150/kilobytes).floatValue()));
			        bd = bd.setScale(12, BigDecimal.ROUND_UP);
			        return bd.floatValue();
			
			} else if (filename.contains("_fs_") && kilobytes > 20){
				BigDecimal bd = new BigDecimal(Float.toString(new Float(20/kilobytes).floatValue()));
		        bd = bd.setScale(12, BigDecimal.ROUND_UP);
		        return bd.floatValue();
			} else if (filename.contains("_th_") && kilobytes > 5){
				BigDecimal bd = new BigDecimal(Float.toString(new Float(5/kilobytes).floatValue()));
		        bd = bd.setScale(12, BigDecimal.ROUND_UP);
		        return bd.floatValue();
			}
		}
		return f;
	}
		
	
	private String getExtension(File file) {
		if(file != null) {
		String filename = file.getName();
		int dot = filename.lastIndexOf('.');
		if(dot > 0 && dot < filename.length()-1)
		return filename.substring(dot+1).toLowerCase();
		}
		return null;
	}
	private boolean isSupported(String ext) {
		return ext.equalsIgnoreCase(GIF) || ext.equalsIgnoreCase(PNG) ||
		ext.equalsIgnoreCase(JPG) || ext.equalsIgnoreCase(BMP) ||
		ext.equalsIgnoreCase(JPEG);
	}
}
