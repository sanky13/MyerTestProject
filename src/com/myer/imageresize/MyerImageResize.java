package com.myer.imageresize;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.io.FileUtils;
/*
 * Shows how to save an image in JPEG with different compression factors.
 * Based on code from http://forums.java.net/jive/thread.jspa?messageID=243429
 * and from http://www.universalwebservices.net/web-programming-resources/java/adjust-jpeg-image-compression-quality-when-saving-images-in-java
 */
public class MyerImageResize {
	static int numberofsuccessresize =0;
	static int numberofimagecopy =0;
	static int numberoffailure =0;
	
	/*
	 * Application starting point, open an image and save it in JPEG with a
	 * compression factor.
	 */
	public static void main(String[] args) throws IOException, WriteException
    {
    // Load the image (it is hard-coded here to make the code simpler).
		Date startDate = new Date();
	  if (args.length > 0) {
		  final File dir = new File(args[2]);
		  String action = args[1];
		  File tempfile = new File("temp.xls");
		  WritableWorkbook  wworkbook = Workbook.createWorkbook(tempfile);
		  if ("ANALYSE".equalsIgnoreCase(action)){
			  System.out.println("Starting the image analysis ******" + startDate.getTime());
			  loopDirectoryAndAnalyse(dir, wworkbook);
			  wworkbook.setOutputFile(new File(dir.toString() +"imageAnalysis.xls"));
			  wworkbook.write();
		      wworkbook.close(); 
		      tempfile.delete();
		      System.out.println("finished the image analysis ******" + startDate.getTime());
		  } else {
			  
			  System.out.println("Folder details passed :" +dir.toString());
			  System.out.println("Starting the image transformation ******" + startDate.getTime());
			  loopDirectoryAndTransform(dir);  
			  System.out.println("******** Completed Image Transformation ********");
			  System.out.println("1. Number of SuccessFull Image resize :" + numberofsuccessresize);
			  System.out.println("2. Number of Image copy (Resize not required) :" + numberofimagecopy);
			  System.out.println("3. Number of Image resize failures (Image format issue):" + numberoffailure);
			  Date endDate = new Date();
		        long diff = endDate.getTime() - startDate.getTime();
		        long diffSeconds = diff / 1000 % 60;
		        long diffMinutes = diff / (60 * 1000) % 60;
		        long diffHours = diff / (60 * 60 * 1000);
		        int diffInDays = (int) diff / (1000 * 60 * 60 * 24);	  
			  System.out.println("******** Finished Image Transformation ********" + endDate.getTime());
			  
			  
			  System.out.println("******** Total time taken for the transformation  :" + diffSeconds + " Seconds");
		  }
		 


//	        System.out.println(diffInDays+"  days");
//	        System.out.println(diffHours+"  Hour");
//	        System.out.println(diffMinutes+"  min");
//	        System.out.println(diffSeconds+"  sec");
	  } else {
		  System.out.println("Please enter the image directory path");
		  return;
	  }
	
	
		 
	  
// String imageFile =
// "C:/Sankar/Workspace1/TestWebProject/src/com/myer/imageresize/208542700_fs_2.jpg";
// BufferedImage i = ImageIO.read(new File(imageFile));
// showImage("Original Image", i);
// // Show results with different compression ratio.
// compressAndShow(i, 0.80f);
    }
	private static BufferedImage convertCMYK2RGB(BufferedImage image) throws IOException{
	  
	    //Create a new RGB image
	    BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(),
	    BufferedImage.TYPE_INT_BGR);
	    // then do a funky color convert
//	    ColorConvertOp op = new ColorConvertOp(null);
//	    op.filter(image, rgbImage);
//	    return rgbImage;
	    
	    ICC_ColorSpace srcCS =
            (ICC_ColorSpace) rgbImage.getColorModel().getColorSpace();

		ICC_Profile srcProf = srcCS.getProfile();
		byte[] header = srcProf.getData(ICC_Profile.icSigHead);
		intToBigEndian(ICC_Profile.icSigInputClass,header,12);
		srcProf.setData(ICC_Profile.icSigHead,header);
		
		ColorConvertOp op = new ColorConvertOp(null);
		return op.filter(image, rgbImage);
	}
	
	private static void intToBigEndian(int value, byte[] array, int index) {
	    array[index]   = (byte) (value >> 24);
	    array[index+1] = (byte) (value >> 16);
	    array[index+2] = (byte) (value >>  8);
	    array[index+3] = (byte) (value);
	}
	
	public static void loopDirectoryAndTransform(File dir) throws IOException{
		
		File imgFile = null;
		final ImageFilter imageFilter = new ImageFilter();
		for (int count = 0; count < dir.listFiles().length; count++) {
			imgFile = dir.listFiles()[count];
			if(!"compressed".equalsIgnoreCase(imgFile.getName())){
				if (imgFile.isDirectory() ) {
					loopDirectoryAndTransform(imgFile);
				} else {
					
	
					if (imageFilter.accept(imgFile) ) {
						 System.out.println("converting the image : "+ imgFile.toString());	
						//
						
//						ImageInfo info = new ImageInfo(imgFile.toString());
//						MagickImage image = new MagickImage(info);
//						image.transformRgbImage(ColorspaceType.CMYKColorspace);
//						image.setFileName("rgb.tif");
//						image.writeImage(info);	
						
						////
						
						
						//
						
						BufferedImage img = null;
						ImageInputStream iis = new FileImageInputStream(imgFile);
						try {
						    for (Iterator  i = ImageIO.getImageReaders(iis); 
						         img == null && i.hasNext(); ) {
						        ImageReader r = (ImageReader) i.next();
						        try {
						            r.setInput(iis);
						            img = r.read(0);
						        } catch (IOException e) {
//						        	iis = new CMYKJPEGImageReader().read(0);
						        	
						        }catch (IllegalArgumentException ex) {
						         System.out.println("Not able to convert the image with IllegalArgumentException :" + imgFile);	
						        }catch (Exception ex) {
						         System.out.println("Not able to convert the image Exception:" + imgFile);	
						        }
						    }
						} finally {
						    iis.close();
						}
					//	return img;
						//
						
						
						
//						BufferedImage img = 
//						    JPEGCodec.createJPEGDecoder(new FileInputStream(imgFile)).decodeAsBufferedImage();
//						BufferedImage i = convertCMYK2RGB(img);
						try{
						//	BufferedImage i = ImageIO.read(imgFile);
							BufferedImage i = img;
							if(imageFilter.requireresize(imgFile)){
								float ratio  =imageFilter.returnratio(imgFile);
								if ( ratio < 0.1){
//									ratio =0.99f;
									String text = "Image " + imgFile.toString() + " compressed with a compression ratio of " + ratio;
									ImageFilter.writeToFile(text, dir.toString());
								}
								compressAndShow(i, ratio, imgFile, true);
								numberofsuccessresize++;
							} else {
								 FileUtils.copyFile(imgFile, new File (imgFile.getParent().toString()+"/compressed/" + imgFile.getName()));
								 numberofimagecopy++;
							}
						}catch(Exception e){
							numberoffailure++; 
						}
					}
				}
			} 
		}
	}

	
public static void loopDirectoryAndAnalyse(File dir,  WritableWorkbook  wworkbook ) throws IOException, WriteException{
		
		File imgFile = null;
		final ImageFilter imageFilter = new ImageFilter();
		int rowValue =0;
		for (int count = 0; count < dir.listFiles().length; count++) {
			imgFile = dir.listFiles()[count];
			if(!"compressed".equalsIgnoreCase(imgFile.getName())){
				if (imgFile.isDirectory() ) {
					loopDirectoryAndAnalyse(imgFile,    wworkbook );
				} else {
					
	
					if (imageFilter.accept(imgFile) ) {
						 System.out.println("converting the image : "+ imgFile.toString());	
						//
						
//						ImageInfo info = new ImageInfo(imgFile.toString());
//						MagickImage image = new MagickImage(info);
//						image.transformRgbImage(ColorspaceType.CMYKColorspace);
//						image.setFileName("rgb.tif");
//						image.writeImage(info);	
						
						////
						
						
						//
						
						BufferedImage img = null;
						ImageInputStream iis = new FileImageInputStream(imgFile);
						try {
						    for (Iterator  i = ImageIO.getImageReaders(iis); 
						         img == null && i.hasNext(); ) {
						        ImageReader r = (ImageReader) i.next();
						        try {
						            r.setInput(iis);
						            img = r.read(0);
						        } catch (IOException e) {
//						        	iis = new CMYKJPEGImageReader().read(0);
						        	
						        }catch (IllegalArgumentException ex) {
						         System.out.println("Not able to convert the image with IllegalArgumentException :" + imgFile);	
						        }catch (Exception ex) {
						         System.out.println("Not able to convert the image Exception:" + imgFile);	
						        }
						    }
						} finally {
						    iis.close();
						}

						   
						try{
						//	BufferedImage i = ImageIO.read(imgFile);
							MyerImageDTO imageDTO=imageFilter.analyseImage(imgFile);
							if(imageDTO != null && (imageDTO.getOverShootValue() >0)){
								
								float ratio = imageFilter.returnratio(imgFile);
								  new MyerExcelHelper().writeExcel(imgFile.toString(), imageDTO.getImageType(), 
										   imageDTO.getRecommendedSize(), imageDTO.getCurrentSize()
										   , imageDTO.getOverShootValue(),  wworkbook , rowValue ,  ratio);
								   rowValue++;
								   for(int i=0 ; i< wworkbook.getSheetNames().length ;i++) {
									   System.out.println("details :" + wworkbook.getSheetNames()[i].toString()); 
									   System.out.println("details :" + wworkbook.getSheet(i).getRows()); 
								   }
								   wworkbook.write();
								  
								   
								   
							} else {
								System.out.println("No need to write the image as the size is with in limits");
							}
								
						}catch(Exception e){
							numberoffailure++; 
						}
					}
				}
			} 
		}

	}


	
	public static void compressAndShow(BufferedImage image, float quality,
			File imgFile, boolean isResize) throws IOException {
		// Get a ImageWriter for jpeg format.
		String name = imgFile.getName();
		String path = imgFile.getParent().toString();
		Iterator writers = ImageIO.getImageWritersBySuffix("jpg");
		if (!writers.hasNext())
			throw new IllegalStateException("No writers found");
		ImageWriter writer = (ImageWriter) writers.next();
		// Create the ImageWriteParam to compress the image.
		ImageWriteParam param = writer.getDefaultWriteParam();
	
		// The output will be a ByteArrayOutputStream (in memory)



//		showImage("Compressed to " + quality + ": " + size + " bytes", out);
	
		// Uncomment code below to save the compressed files.
	

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(quality);
//			ByteArrayOutputStream bos = new ByteArrayOutputStream(3240000);
//			ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
//			writer.setOutput(ios);
//			writer.write(null, new IIOImage(image, null, null), param);
//			ios.flush(); // otherwise the buffer size will be zero!
			// From the ByteArrayOutputStream create a RenderedImage.
			File file = new File(path+"/compressed" );
			if(!file.exists()){
				file.mkdirs();
			}
			 file = new File( path+"/compressed" , name );
			FileImageOutputStream output = new FileImageOutputStream(file);
			writer.setOutput(output);
			writer.write(null, new IIOImage(image, null, null), param);	
	
			
		
		
	}

	/*
	 * This method just create a JFrame to display the image. Closing the window
	 * will close the whole application.
	 */
	
}
