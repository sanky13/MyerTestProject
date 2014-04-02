/*    */ package org.monte.media.jpeg;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.ImageReader;
/*    */ import javax.imageio.spi.ImageReaderSpi;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ 
/*    */ public class CMYKJPEGImageReaderSpi extends ImageReaderSpi
/*    */ {
/*    */   public CMYKJPEGImageReaderSpi()
/*    */   {
/* 31 */     super("Werner Randelshofer", "1.0", new String[] { "JPEG", "JPG" }, new String[] { "jpg" }, new String[] { "image/jpg" }, "org.monte.media.jpeg.CMYKJPEGImageReader", new Class[] { ImageInputStream.class, InputStream.class, [B.class }, null, false, null, null, null, null, false, null, null, null, null);
/*    */   }
/*    */ 
/*    */   public boolean canDecodeInput(Object source)
/*    */     throws IOException
/*    */   {
/* 54 */     if (source instanceof ImageInputStream) {
/* 55 */       ImageInputStream in = (ImageInputStream)source;
/* 56 */       in.mark();
/*    */ 
/* 58 */       if (in.readShort() != -40) {
/* 59 */         in.reset();
/* 60 */         return false;
/*    */       }
/* 62 */       in.reset();
/* 63 */       return true;
/*    */     }
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   public ImageReader createReaderInstance(Object extension) throws IOException
/*    */   {
/* 70 */     return new CMYKJPEGImageReader(this);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 75 */     return "CMYK JPEG Image Reader";
/*    */   }
/*    */ }

/* Location:           C:\Sankar\Downloads\CMYKDemo.jar
 * Qualified Name:     org.monte.media.jpeg.CMYKJPEGImageReaderSpi
 * JD-Core Version:    0.5.4
 */