/*     */ package org.monte.media.jpeg;
/*     */ 
/*     */ import com.sun.imageio.plugins.jpeg.JPEGImageReader;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.color.ICC_ColorSpace;
/*     */ import java.awt.color.ICC_Profile;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorConvertOp;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageReadParam;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ import javax.imageio.spi.ImageReaderSpi;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*     */ import org.monte.media.io.ByteArrayImageInputStream;
/*     */ import org.monte.media.io.ImageInputStreamAdapter;
/*     */ 
/*     */ public class CMYKJPEGImageReader extends ImageReader
/*     */ {
/*  36 */   private boolean isYCCKInversed = true;
/*  37 */   private static DirectColorModel RGB = new DirectColorModel(24, 16711680, 65280, 255, 0);
/*     */   private BufferedImage image;
/*     */   private static final int SCALEBITS = 16;
/*     */   private static final int MAXJSAMPLE = 255;
/*     */   private static final int CENTERJSAMPLE = 128;
/*     */   private static final int ONE_HALF = 32768;
/* 564 */   private static final int[] Cr_r_tab = new int[256];
/* 565 */   private static final int[] Cb_b_tab = new int[256];
/* 566 */   private static final int[] Cr_g_tab = new int[256];
/* 567 */   private static final int[] Cb_g_tab = new int[256];
/*     */ 
/*     */   public CMYKJPEGImageReader(ImageReaderSpi originatingProvider)
/*     */   {
/*  42 */     super(originatingProvider);
/*     */   }
/*     */ 
/*     */   public int getNumImages(boolean allowSearch) throws IOException
/*     */   {
/*  47 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getWidth(int imageIndex) throws IOException
/*     */   {
/*  52 */     readHeader();
/*  53 */     return this.image.getWidth();
/*     */   }
/*     */ 
/*     */   public int getHeight(int imageIndex) throws IOException
/*     */   {
/*  58 */     readHeader();
/*  59 */     return this.image.getHeight();
/*     */   }
/*     */ 
/*     */   public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException
/*     */   {
/*  64 */     readHeader();
/*  65 */     LinkedList l = new LinkedList();
/*  66 */     l.add(new ImageTypeSpecifier(RGB, RGB.createCompatibleSampleModel(this.image.getWidth(), this.image.getHeight())));
/*  67 */     return l.iterator();
/*     */   }
/*     */ 
/*     */   public IIOMetadata getStreamMetadata() throws IOException
/*     */   {
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   public IIOMetadata getImageMetadata(int imageIndex) throws IOException
/*     */   {
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException
/*     */   {
/*  82 */     if (imageIndex > 0) {
/*  83 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  85 */     readHeader();
/*  86 */     return this.image;
/*     */   }
/*     */ 
/*     */   private void readHeader()
/*     */     throws IOException
/*     */   {
/*  93 */     if (this.image != null)
/*     */       return;
/*  95 */     ImageInputStream iis = null;
/*  96 */     Object in = getInput();
/*     */ 
/* 102 */     if (in instanceof byte[])
/* 103 */       iis = new ByteArrayImageInputStream((byte[])(byte[])in);
/* 104 */     else if (in instanceof ImageInputStream)
/* 105 */       iis = (ImageInputStream)in;
/* 106 */     else if (in instanceof InputStream)
/* 107 */       iis = new MemoryCacheImageInputStream((InputStream)in);
/*     */     else {
/* 109 */       throw new IOException("Can't handle input of type " + in);
/*     */     }
/* 111 */     this.image = read(iis, this.isYCCKInversed);
/*     */   }
/*     */ 
/*     */   public boolean isYCCKInversed()
/*     */   {
/* 119 */     return this.isYCCKInversed;
/*     */   }
/*     */ 
/*     */   public void setYCCKInversed(boolean newValue)
/*     */   {
/* 126 */     this.isYCCKInversed = newValue;
/*     */   }
/*     */ 
/*     */   public static BufferedImage read(ImageInputStream in, boolean inverseYCCKColors) throws IOException
/*     */   {
/* 131 */     in.seek(0L);
/*     */ 
/* 137 */     int samplePrecision = 0;
/* 138 */     int numberOfLines = 0;
/* 139 */     int numberOfSamplesPerLine = 0;
/* 140 */     int numberOfComponentsInFrame = 0;
/* 141 */     int app14AdobeColorTransform = 0;
/* 142 */     ByteArrayOutputStream app2ICCProfile = new ByteArrayOutputStream();
/*     */ 
/* 145 */     JFIFInputStream fifi = new JFIFInputStream(new ImageInputStreamAdapter(in));
/* 146 */     for (JFIFInputStream.Segment seg = fifi.getNextSegment(); seg != null; seg = fifi.getNextSegment()) {
/* 147 */       if (((65472 <= seg.marker) && (seg.marker <= 65475)) || ((65477 <= seg.marker) && (seg.marker <= 65479)) || ((65481 <= seg.marker) && (seg.marker <= 65483)) || ((65485 <= seg.marker) && (seg.marker <= 65487)))
/*     */       {
/* 152 */         DataInputStream dis = new DataInputStream(fifi);
/* 153 */         samplePrecision = dis.readUnsignedByte();
/* 154 */         numberOfLines = dis.readUnsignedShort();
/* 155 */         numberOfSamplesPerLine = dis.readUnsignedShort();
/* 156 */         numberOfComponentsInFrame = dis.readUnsignedByte();
/*     */ 
/* 161 */         break;
/*     */       }
/* 163 */       if (seg.marker == 65506)
/*     */       {
/* 165 */         if (seg.length >= 26) {
/* 166 */           DataInputStream dis = new DataInputStream(fifi);
/*     */ 
/* 168 */           if ((dis.readLong() == 5279137264856878918L) && (dis.readInt() == 1229735168))
/*     */           {
/* 170 */             dis.skipBytes(2);
/*     */ 
/* 174 */             byte[] b = new byte[512];
/* 175 */             for (int count = dis.read(b); count != -1; count = dis.read(b))
/* 176 */               app2ICCProfile.write(b, 0, count);
/*     */           }
/*     */         }
/*     */       } else {
/* 180 */         if ((seg.marker != 65518) || 
/* 182 */           (seg.length != 12)) continue;
/* 183 */         DataInputStream dis = new DataInputStream(fifi);
/*     */ 
/* 185 */         if ((dis.readInt() == 1097101154L) && (dis.readUnsignedShort() == 25856)) {
/* 186 */           int version = dis.readUnsignedByte();
/* 187 */           int app14Flags0 = dis.readUnsignedShort();
/* 188 */           int app14Flags1 = dis.readUnsignedShort();
/* 189 */           app14AdobeColorTransform = dis.readUnsignedByte();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 197 */     BufferedImage img = null;
/* 198 */     if (numberOfComponentsInFrame != 4)
/*     */     {
/* 200 */       in.seek(0L);
/* 201 */       img = readImageFromYUVorGray(in);
/* 202 */     } else if (numberOfComponentsInFrame == 4)
/*     */     {
/* 205 */       ICC_Profile profile = null;
/* 206 */       if (app2ICCProfile.size() > 0) {
/*     */         try {
/* 208 */           profile = ICC_Profile.getInstance(new ByteArrayInputStream(app2ICCProfile.toByteArray()));
/*     */         }
/*     */         catch (Throwable ex) {
/* 211 */           ex.printStackTrace();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 216 */       if (profile == null) {
/* 217 */         profile = ICC_Profile.getInstance(CMYKJPEGImageReader.class.getResourceAsStream("Generic CMYK Profile.icc"));
/*     */       }
/* 219 */       switch (app14AdobeColorTransform)
/*     */       {
/*     */       case 0:
/*     */       default:
/* 223 */         in.seek(0L);
/* 224 */         img = readRGBAImageFromRGBA(new ImageInputStreamAdapter(in), profile);
/* 225 */         break;
/*     */       case 1:
/* 227 */         throw new IOException("YCbCr not supported");
/*     */       case 2:
/* 232 */         in.seek(0L);
/* 233 */         if (inverseYCCKColors) {
/* 234 */           img = readRGBImageFromInvertedYCCK(new ImageInputStreamAdapter(in), profile); break label571:
/*     */         }
/* 236 */         img = readRGBImageFromYCCK(new ImageInputStreamAdapter(in), profile);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 242 */     label571: return img;
/*     */   }
/*     */ 
/*     */   private static ImageReader createNativeJPEGReader() {
/* 246 */     return new JPEGImageReader(new CMYKJPEGImageReaderSpi());
/*     */   }
/*     */ 
/*     */   public static BufferedImage readRGBImageFromCMYK(InputStream in, ICC_Profile cmykProfile)
/*     */     throws IOException
/*     */   {
/* 279 */     ImageInputStream inputStream = null;
/* 280 */     ImageReader reader = createNativeJPEGReader();
/* 281 */     inputStream = (in instanceof ImageInputStream) ? (ImageInputStream)in : ImageIO.createImageInputStream(in);
/* 282 */     reader.setInput(inputStream);
/* 283 */     Raster raster = reader.readRaster(0, null);
/* 284 */     BufferedImage image = createRGBImageFromCMYK(raster, cmykProfile);
/* 285 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage readRGBAImageFromRGBA(InputStream in, ICC_Profile rgbaProfile)
/*     */     throws IOException
/*     */   {
/* 305 */     ImageInputStream inputStream = null;
/* 306 */     ImageReader reader = createNativeJPEGReader();
/* 307 */     inputStream = (in instanceof ImageInputStream) ? (ImageInputStream)in : ImageIO.createImageInputStream(in);
/* 308 */     reader.setInput(inputStream);
/* 309 */     Raster raster = reader.readRaster(0, null);
/* 310 */     BufferedImage image = createRGBAImageFromRGBA(raster, rgbaProfile);
/* 311 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage readRGBImageFromYCCK(InputStream in, ICC_Profile cmykProfile)
/*     */     throws IOException
/*     */   {
/* 331 */     ImageInputStream inputStream = null;
/* 332 */     ImageReader reader = createNativeJPEGReader();
/* 333 */     inputStream = (in instanceof ImageInputStream) ? (ImageInputStream)in : ImageIO.createImageInputStream(in);
/* 334 */     reader.setInput(inputStream);
/* 335 */     Raster raster = reader.readRaster(0, null);
/* 336 */     BufferedImage image = createRGBImageFromYCCK(raster, cmykProfile);
/* 337 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage readRGBImageFromInvertedYCCK(InputStream in, ICC_Profile cmykProfile)
/*     */     throws IOException
/*     */   {
/* 357 */     ImageInputStream inputStream = null;
/* 358 */     ImageReader reader = createNativeJPEGReader();
/* 359 */     inputStream = (in instanceof ImageInputStream) ? (ImageInputStream)in : ImageIO.createImageInputStream(in);
/* 360 */     reader.setInput(inputStream);
/* 361 */     Raster raster = reader.readRaster(0, null);
/* 362 */     raster = convertInvertedYCCKToCMYK(raster);
/* 363 */     BufferedImage image = createRGBImageFromCMYK(raster, cmykProfile);
/* 364 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage createRGBImageFromYCCK(Raster ycckRaster, ICC_Profile cmykProfile)
/*     */   {
/*     */     BufferedImage image;
/*     */     BufferedImage image;
/* 379 */     if (cmykProfile != null) {
/* 380 */       ycckRaster = convertYCCKtoCMYK(ycckRaster);
/* 381 */       image = createRGBImageFromCMYK(ycckRaster, cmykProfile);
/*     */     } else {
/* 383 */       int w = ycckRaster.getWidth(); int h = ycckRaster.getHeight();
/* 384 */       int[] rgb = new int[w * h];
/* 385 */       int[] Y = ycckRaster.getSamples(0, 0, w, h, 0, (int[])null);
/* 386 */       int[] Cb = ycckRaster.getSamples(0, 0, w, h, 1, (int[])null);
/* 387 */       int[] Cr = ycckRaster.getSamples(0, 0, w, h, 2, (int[])null);
/* 388 */       int[] K = ycckRaster.getSamples(0, 0, w, h, 3, (int[])null);
/*     */ 
/* 392 */       int i = 0; for (int imax = Y.length; i < imax; ++i) {
/* 393 */         float k = K[i]; float y = Y[i]; float cb = Cb[i]; float cr = Cr[i];
/* 394 */         float vr = y + 1.402F * (cr - 128.0F) - k;
/* 395 */         float vg = y - 0.34414F * (cb - 128.0F) - 0.71414F * (cr - 128.0F) - k;
/* 396 */         float vb = y + 1.772F * (cb - 128.0F) - k;
/* 397 */         rgb[i] = ((0xFF & ((vr > 255.0F) ? 'ÿ' : (vr < 0.0F) ? 0 : (int)(vr + 0.5F))) << 16 | (0xFF & ((vg > 255.0F) ? 'ÿ' : (vg < 0.0F) ? 0 : (int)(vg + 0.5F))) << 8 | 0xFF & ((vb > 255.0F) ? 'ÿ' : (vb < 0.0F) ? 0 : (int)(vb + 0.5F)));
/*     */       }
/*     */ 
/* 402 */       Raster rgbRaster = Raster.createPackedRaster(new DataBufferInt(rgb, rgb.length), w, h, w, new int[] { 16711680, 65280, 255 }, null);
/*     */ 
/* 405 */       ColorSpace cs = ColorSpace.getInstance(1000);
/* 406 */       ColorModel cm = new DirectColorModel(cs, 24, 16711680, 65280, 255, 0, false, 3);
/*     */ 
/* 408 */       image = new BufferedImage(cm, (WritableRaster)rgbRaster, true, null);
/*     */     }
/* 410 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage createRGBImageFromInvertedYCCK(Raster ycckRaster, ICC_Profile cmykProfile)
/*     */   {
/*     */     BufferedImage image;
/*     */     BufferedImage image;
/* 424 */     if (cmykProfile != null) {
/* 425 */       ycckRaster = convertInvertedYCCKToCMYK(ycckRaster);
/* 426 */       image = createRGBImageFromCMYK(ycckRaster, cmykProfile);
/*     */     } else {
/* 428 */       int w = ycckRaster.getWidth(); int h = ycckRaster.getHeight();
/* 429 */       int[] rgb = new int[w * h];
/*     */ 
/* 433 */       int[] Y = ycckRaster.getSamples(0, 0, w, h, 0, (int[])null);
/* 434 */       int[] Cb = ycckRaster.getSamples(0, 0, w, h, 1, (int[])null);
/* 435 */       int[] Cr = ycckRaster.getSamples(0, 0, w, h, 2, (int[])null);
/* 436 */       int[] K = ycckRaster.getSamples(0, 0, w, h, 3, (int[])null);
/*     */ 
/* 438 */       int i = 0; for (int imax = Y.length; i < imax; ++i) {
/* 439 */         float k = 255 - K[i]; float y = 255 - Y[i]; float cb = 255 - Cb[i]; float cr = 255 - Cr[i];
/* 440 */         float vr = y + 1.402F * (cr - 128.0F) - k;
/* 441 */         float vg = y - 0.34414F * (cb - 128.0F) - 0.71414F * (cr - 128.0F) - k;
/* 442 */         float vb = y + 1.772F * (cb - 128.0F) - k;
/* 443 */         rgb[i] = ((0xFF & ((vr > 255.0F) ? 'ÿ' : (vr < 0.0F) ? 0 : (int)(vr + 0.5F))) << 16 | (0xFF & ((vg > 255.0F) ? 'ÿ' : (vg < 0.0F) ? 0 : (int)(vg + 0.5F))) << 8 | 0xFF & ((vb > 255.0F) ? 'ÿ' : (vb < 0.0F) ? 0 : (int)(vb + 0.5F)));
/*     */       }
/*     */ 
/* 448 */       Raster rgbRaster = Raster.createPackedRaster(new DataBufferInt(rgb, rgb.length), w, h, w, new int[] { 16711680, 65280, 255 }, null);
/*     */ 
/* 451 */       ColorSpace cs = ColorSpace.getInstance(1000);
/* 452 */       ColorModel cm = new DirectColorModel(cs, 24, 16711680, 65280, 255, 0, false, 3);
/*     */ 
/* 454 */       image = new BufferedImage(cm, (WritableRaster)rgbRaster, true, null);
/*     */     }
/* 456 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage createRGBImageFromCMYK(Raster cmykRaster, ICC_Profile cmykProfile)
/*     */   {
/* 473 */     int w = cmykRaster.getWidth();
/* 474 */     int h = cmykRaster.getHeight();
/*     */     BufferedImage image;
/* 476 */     if (cmykProfile != null) {
/* 477 */       ColorSpace cmykCS = new ICC_ColorSpace(cmykProfile);
/* 478 */       BufferedImage image = new BufferedImage(w, h, 1);
/*     */ 
/* 480 */       WritableRaster rgbRaster = image.getRaster();
/* 481 */       ColorSpace rgbCS = image.getColorModel().getColorSpace();
/* 482 */       ColorConvertOp cmykToRgb = new ColorConvertOp(cmykCS, rgbCS, null);
/* 483 */       cmykToRgb.filter(cmykRaster, rgbRaster);
/*     */     }
/*     */     else {
/* 486 */       int[] rgb = new int[w * h];
/*     */ 
/* 488 */       int[] C = cmykRaster.getSamples(0, 0, w, h, 0, (int[])null);
/* 489 */       int[] M = cmykRaster.getSamples(0, 0, w, h, 1, (int[])null);
/* 490 */       int[] Y = cmykRaster.getSamples(0, 0, w, h, 2, (int[])null);
/* 491 */       int[] K = cmykRaster.getSamples(0, 0, w, h, 3, (int[])null);
/*     */ 
/* 493 */       int i = 0; for (int imax = C.length; i < imax; ++i) {
/* 494 */         int k = K[i];
/* 495 */         rgb[i] = (255 - Math.min(255, C[i] + k) << 16 | 255 - Math.min(255, M[i] + k) << 8 | 255 - Math.min(255, Y[i] + k));
/*     */       }
/*     */ 
/* 500 */       Raster rgbRaster = Raster.createPackedRaster(new DataBufferInt(rgb, rgb.length), w, h, w, new int[] { 16711680, 65280, 255 }, null);
/*     */ 
/* 503 */       ColorSpace cs = ColorSpace.getInstance(1000);
/* 504 */       ColorModel cm = new DirectColorModel(cs, 24, 16711680, 65280, 255, 0, false, 3);
/* 505 */       image = new BufferedImage(cm, (WritableRaster)rgbRaster, true, null);
/*     */     }
/* 507 */     return image;
/*     */   }
/*     */ 
/*     */   public static BufferedImage createRGBAImageFromRGBA(Raster rgbaRaster, ICC_Profile rgbaProfile)
/*     */   {
/* 524 */     int w = rgbaRaster.getWidth();
/* 525 */     int h = rgbaRaster.getHeight();
/*     */ 
/* 528 */     rgbaProfile = null;
/*     */     BufferedImage image;
/* 529 */     if (rgbaProfile != null) {
/* 530 */       ColorSpace rgbaCS = new ICC_ColorSpace(rgbaProfile);
/* 531 */       BufferedImage image = new BufferedImage(w, h, 1);
/*     */ 
/* 533 */       WritableRaster rgbRaster = image.getRaster();
/* 534 */       ColorSpace rgbCS = image.getColorModel().getColorSpace();
/* 535 */       ColorConvertOp cmykToRgb = new ColorConvertOp(rgbaCS, rgbCS, null);
/* 536 */       cmykToRgb.filter(rgbaRaster, rgbRaster);
/*     */     }
/*     */     else {
/* 539 */       int[] rgb = new int[w * h];
/*     */ 
/* 541 */       int[] R = rgbaRaster.getSamples(0, 0, w, h, 0, (int[])null);
/* 542 */       int[] G = rgbaRaster.getSamples(0, 0, w, h, 1, (int[])null);
/* 543 */       int[] B = rgbaRaster.getSamples(0, 0, w, h, 2, (int[])null);
/* 544 */       int[] A = rgbaRaster.getSamples(0, 0, w, h, 3, (int[])null);
/*     */ 
/* 546 */       int i = 0; for (int imax = R.length; i < imax; ++i) {
/* 547 */         rgb[i] = (A[i] << 24 | R[i] << 16 | G[i] << 8 | B[i]);
/*     */       }
/*     */ 
/* 550 */       Raster rgbRaster = Raster.createPackedRaster(new DataBufferInt(rgb, rgb.length), w, h, w, new int[] { 16711680, 65280, 255, -16777216 }, null);
/*     */ 
/* 553 */       ColorSpace cs = ColorSpace.getInstance(1000);
/* 554 */       ColorModel cm = new DirectColorModel(cs, 32, 16711680, 65280, 255, -16777216, false, 3);
/* 555 */       image = new BufferedImage(cm, (WritableRaster)rgbRaster, true, null);
/*     */     }
/* 557 */     return image;
/*     */   }
/*     */ 
/*     */   private static synchronized void buildYCCtoRGBtable()
/*     */   {
/* 573 */     if (Cr_r_tab[0] == 0) {
/* 574 */       int i = 0; for (int x = -128; i <= 255; ++x)
/*     */       {
/* 578 */         Cr_r_tab[i] = ((int)(91881.971999999994D * x + 32768.0D) >> 16);
/*     */ 
/* 580 */         Cb_b_tab[i] = ((int)(116130.292D * x + 32768.0D) >> 16);
/*     */ 
/* 582 */         Cr_g_tab[i] = (-46802 * x);
/*     */ 
/* 585 */         Cb_g_tab[i] = (-22554 * x + 32768);
/*     */ 
/* 574 */         ++i;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Raster convertInvertedYCCKToCMYK(Raster ycckRaster)
/*     */   {
/* 597 */     buildYCCtoRGBtable();
/*     */ 
/* 599 */     int w = ycckRaster.getWidth(); int h = ycckRaster.getHeight();
/* 600 */     int[] ycckY = ycckRaster.getSamples(0, 0, w, h, 0, (int[])null);
/* 601 */     int[] ycckCb = ycckRaster.getSamples(0, 0, w, h, 1, (int[])null);
/* 602 */     int[] ycckCr = ycckRaster.getSamples(0, 0, w, h, 2, (int[])null);
/* 603 */     int[] ycckK = ycckRaster.getSamples(0, 0, w, h, 3, (int[])null);
/* 604 */     int[] cmyk = new int[ycckY.length];
/*     */ 
/* 606 */     for (int i = 0; i < ycckY.length; ++i) {
/* 607 */       int y = 255 - ycckY[i];
/* 608 */       int cb = 255 - ycckCb[i];
/* 609 */       int cr = 255 - ycckCr[i];
/*     */ 
/* 612 */       int cmykC = 255 - (y + Cr_r_tab[cr]);
/* 613 */       int cmykM = 255 - (y + (Cb_g_tab[cb] + Cr_g_tab[cr] >> 16));
/*     */ 
/* 616 */       int cmykY = 255 - (y + Cb_b_tab[cb]);
/*     */ 
/* 618 */       cmyk[i] = (((cmykC > 255) ? 'ÿ' : (cmykC < 0) ? 0 : cmykC) << 24 | ((cmykM > 255) ? 'ÿ' : (cmykM < 0) ? 0 : cmykM) << 16 | ((cmykY > 255) ? 'ÿ' : (cmykY < 0) ? 0 : cmykY) << 8 | 255 - ycckK[i]);
/*     */     }
/*     */ 
/* 624 */     Raster cmykRaster = Raster.createPackedRaster(new DataBufferInt(cmyk, cmyk.length), w, h, w, new int[] { -16777216, 16711680, 65280, 255 }, null);
/*     */ 
/* 627 */     return cmykRaster;
/*     */   }
/*     */ 
/*     */   private static Raster convertYCCKtoCMYK(Raster ycckRaster)
/*     */   {
/* 632 */     buildYCCtoRGBtable();
/*     */ 
/* 634 */     int w = ycckRaster.getWidth(); int h = ycckRaster.getHeight();
/* 635 */     int[] ycckY = ycckRaster.getSamples(0, 0, w, h, 0, (int[])null);
/* 636 */     int[] ycckCb = ycckRaster.getSamples(0, 0, w, h, 1, (int[])null);
/* 637 */     int[] ycckCr = ycckRaster.getSamples(0, 0, w, h, 2, (int[])null);
/* 638 */     int[] ycckK = ycckRaster.getSamples(0, 0, w, h, 3, (int[])null);
/*     */ 
/* 640 */     int[] cmyk = new int[ycckY.length];
/*     */ 
/* 642 */     for (int i = 0; i < ycckY.length; ++i) {
/* 643 */       int y = ycckY[i];
/* 644 */       int cb = ycckCb[i];
/* 645 */       int cr = ycckCr[i];
/*     */ 
/* 648 */       int cmykC = 255 - (y + Cr_r_tab[cr]);
/* 649 */       int cmykM = 255 - (y + (Cb_g_tab[cb] + Cr_g_tab[cr] >> 16));
/*     */ 
/* 652 */       int cmykY = 255 - (y + Cb_b_tab[cb]);
/*     */ 
/* 654 */       cmyk[i] = (((cmykC > 255) ? 'ÿ' : (cmykC < 0) ? 0 : cmykC) << 24 | ((cmykM > 255) ? 'ÿ' : (cmykM < 0) ? 0 : cmykM) << 16 | ((cmykY > 255) ? 'ÿ' : (cmykY < 0) ? 0 : cmykY) << 8 | ycckK[i]);
/*     */     }
/*     */ 
/* 660 */     return Raster.createPackedRaster(new DataBufferInt(cmyk, cmyk.length), w, h, w, new int[] { -16777216, 16711680, 65280, 255 }, null);
/*     */   }
/*     */ 
/*     */   public static BufferedImage readImageFromYUVorGray(ImageInputStream in)
/*     */     throws IOException
/*     */   {
/* 679 */     ImageReader r = createNativeJPEGReader();
/* 680 */     r.setInput(in);
/* 681 */     BufferedImage img = r.read(0);
/* 682 */     return img;
/*     */   }
/*     */ }

/* Location:           C:\Sankar\Downloads\CMYKDemo.jar
 * Qualified Name:     org.monte.media.jpeg.CMYKJPEGImageReader
 * JD-Core Version:    0.5.4
 */