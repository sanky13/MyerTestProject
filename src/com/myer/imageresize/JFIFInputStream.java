/*     */ package org.monte.media.jpeg;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public class JFIFInputStream extends FilterInputStream
/*     */ {
/*  55 */   private final HashSet<Integer> standaloneMarkers = new HashSet();
/*     */ 
/*  60 */   private final HashSet<Integer> doubleSegMarkers = new HashSet();
/*     */   private Segment segment;
/*     */   private boolean markerFound;
/* 105 */   private int marker = -1;
/* 106 */   private long offset = 0L;
/* 107 */   private boolean isStuffed0xff = false;
/*     */   public static final int JUNK_MARKER = -1;
/*     */   public static final int SOI_MARKER = 65496;
/*     */   public static final int EOI_MARKER = 65497;
/*     */   public static final int TEM_MARKER = 65281;
/*     */   public static final int SOS_MARKER = 65498;
/*     */   public static final int APP1_MARKER = 65505;
/*     */   public static final int APP2_MARKER = 65506;
/*     */   public static final int JPG0_MARKER = 65520;
/*     */   public static final int JPG1_MARKER = 65521;
/*     */   public static final int JPG2_MARKER = 65522;
/*     */   public static final int JPG3_MARKER = 65523;
/*     */   public static final int JPG4_MARKER = 65524;
/*     */   public static final int JPG5_MARKER = 65525;
/*     */   public static final int JPG6_MARKER = 65526;
/*     */   public static final int JPG7_MARKER = 65527;
/*     */   public static final int JPG8_MARKER = 65528;
/*     */   public static final int JPG9_MARKER = 65529;
/*     */   public static final int JPGA_MARKER = 65530;
/*     */   public static final int JPGB_MARKER = 65531;
/*     */   public static final int JPGC_MARKER = 65532;
/*     */   public static final int JPGD_MARKER = 65533;
/*     */   public static final int SOF0_MARKER = 65472;
/*     */   public static final int SOF1_MARKER = 65473;
/*     */   public static final int SOF2_MARKER = 65474;
/*     */   public static final int SOF3_MARKER = 65475;
/*     */   public static final int SOF5_MARKER = 65477;
/*     */   public static final int SOF6_MARKER = 65478;
/*     */   public static final int SOF7_MARKER = 65479;
/*     */   public static final int SOF9_MARKER = 65481;
/*     */   public static final int SOFA_MARKER = 65482;
/*     */   public static final int SOFB_MARKER = 65483;
/*     */   public static final int SOFD_MARKER = 65485;
/*     */   public static final int SOFE_MARKER = 65486;
/*     */   public static final int SOFF_MARKER = 65487;
/*     */   public static final int RST0_MARKER = 65488;
/*     */   public static final int RST1_MARKER = 65489;
/*     */   public static final int RST2_MARKER = 65490;
/*     */   public static final int RST3_MARKER = 65491;
/*     */   public static final int RST4_MARKER = 65492;
/*     */   public static final int RST5_MARKER = 65493;
/*     */   public static final int RST6_MARKER = 65494;
/*     */   public static final int RST7_MARKER = 65495;
/*     */ 
/*     */   public JFIFInputStream(File f)
/*     */     throws IOException
/*     */   {
/* 168 */     this(new BufferedInputStream(new FileInputStream(f)));
/*     */   }
/*     */ 
/*     */   public JFIFInputStream(InputStream in) {
/* 172 */     super(in);
/*     */ 
/* 174 */     for (int i = 65488; i <= 65495; ++i) {
/* 175 */       this.standaloneMarkers.add(Integer.valueOf(i));
/*     */     }
/* 177 */     this.standaloneMarkers.add(Integer.valueOf(65496));
/* 178 */     this.standaloneMarkers.add(Integer.valueOf(65497));
/* 179 */     this.standaloneMarkers.add(Integer.valueOf(65281));
/* 180 */     this.standaloneMarkers.add(Integer.valueOf(65520));
/* 181 */     this.standaloneMarkers.add(Integer.valueOf(65521));
/* 182 */     this.standaloneMarkers.add(Integer.valueOf(65522));
/* 183 */     this.standaloneMarkers.add(Integer.valueOf(65523));
/* 184 */     this.standaloneMarkers.add(Integer.valueOf(65524));
/* 185 */     this.standaloneMarkers.add(Integer.valueOf(65525));
/* 186 */     this.standaloneMarkers.add(Integer.valueOf(65526));
/* 187 */     this.standaloneMarkers.add(Integer.valueOf(65527));
/* 188 */     this.standaloneMarkers.add(Integer.valueOf(65528));
/* 189 */     this.standaloneMarkers.add(Integer.valueOf(65529));
/* 190 */     this.standaloneMarkers.add(Integer.valueOf(65530));
/* 191 */     this.standaloneMarkers.add(Integer.valueOf(65531));
/* 192 */     this.standaloneMarkers.add(Integer.valueOf(65532));
/* 193 */     this.standaloneMarkers.add(Integer.valueOf(65533));
/* 194 */     this.standaloneMarkers.add(Integer.valueOf(65535));
/* 195 */     this.doubleSegMarkers.add(Integer.valueOf(65498));
/*     */ 
/* 198 */     this.segment = new Segment(-1, 0L, -1);
/*     */   }
/*     */ 
/*     */   public Segment getSegment()
/*     */     throws IOException
/*     */   {
/* 209 */     return this.segment;
/*     */   }
/*     */ 
/*     */   public Segment getNextSegment()
/*     */     throws IOException
/*     */   {
/* 222 */     if (!this.segment.isEntropyCoded()) {
/* 223 */       this.markerFound = false;
/*     */       do {
/* 225 */         long skipped = this.in.skip(this.segment.length - this.offset + this.segment.offset);
/* 226 */         if (skipped == -1L) {
/* 227 */           this.segment = new Segment(0, this.offset, -1);
/* 228 */           return null;
/*     */         }
/* 230 */         this.offset += skipped;
/* 231 */       }while (this.offset < this.segment.length + this.segment.offset);
/*     */ 
/* 233 */       if (this.doubleSegMarkers.contains(Integer.valueOf(this.segment.marker))) {
/* 234 */         this.segment = new Segment(0, this.offset, -1);
/* 235 */         return this.segment;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 240 */     while (!this.markerFound)
/*     */     {
/*     */       while (true)
/*     */       {
/*     */         int b;
/* 243 */         if (this.isStuffed0xff) {
/* 244 */           int b = 255;
/* 245 */           this.isStuffed0xff = false;
/*     */         } else {
/* 247 */           b = read0();
/*     */         }
/* 249 */         if (b == -1) {
/* 250 */           return null;
/*     */         }
/* 252 */         if (b == 255) {
/* 253 */           this.markerFound = true;
/* 254 */           break;
/*     */         }
/*     */       }
/* 257 */       int b = read0();
/* 258 */       if (b == -1) {
/* 259 */         return null;
/*     */       }
/* 261 */       if (b == 0) {
/* 262 */         this.markerFound = false;
/* 263 */       } else if (b == 255) {
/* 264 */         this.isStuffed0xff = true;
/* 265 */         this.markerFound = false;
/*     */       } else {
/* 267 */         this.marker = (0xFF00 | b);
/*     */       }
/*     */     }
/* 270 */     this.markerFound = false;
/*     */ 
/* 278 */     if (this.standaloneMarkers.contains(Integer.valueOf(this.marker))) {
/* 279 */       this.segment = new Segment(0xFF00 | this.marker, this.offset, -1);
/*     */     } else {
/* 281 */       int length = read0() << 8 | read0();
/* 282 */       if (length < 2) {
/* 283 */         throw new IOException("JFIFInputStream found illegal segment length " + length + " after marker " + Integer.toHexString(this.marker) + " at offset " + this.offset + ".");
/*     */       }
/* 285 */       this.segment = new Segment(0xFF00 | this.marker, this.offset, length - 2);
/*     */     }
/* 287 */     return this.segment;
/*     */   }
/*     */ 
/*     */   public long getStreamPosition() {
/* 291 */     return this.offset;
/*     */   }
/*     */ 
/*     */   private int read0() throws IOException {
/* 295 */     int b = this.in.read();
/* 296 */     if (b != -1) {
/* 297 */       this.offset += 1L;
/*     */     }
/* 299 */     return b;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 321 */     if (this.markerFound)
/* 322 */       return -1;
/*     */     int b;
/*     */     int b;
/* 326 */     if (this.isStuffed0xff) {
/* 327 */       this.isStuffed0xff = false;
/* 328 */       b = 255;
/*     */     } else {
/* 330 */       b = read0();
/*     */     }
/*     */ 
/* 333 */     if ((this.segment.isEntropyCoded()) && 
/* 334 */       (b == 255)) {
/* 335 */       b = read0();
/* 336 */       if (b == 0)
/*     */       {
/* 338 */         return 255;
/* 339 */       }if (b == 255)
/*     */       {
/* 341 */         this.isStuffed0xff = true;
/* 342 */         return 255;
/*     */       }
/* 344 */       this.markerFound = true;
/* 345 */       this.marker = (0xFF00 | b);
/* 346 */       return -1;
/*     */     }
/*     */ 
/* 349 */     return b;
/*     */   }
/*     */ 
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 371 */     if (this.markerFound) {
/* 372 */       return -1;
/*     */     }
/*     */ 
/* 375 */     int count = 0;
/* 376 */     if (this.segment.isEntropyCoded()) {
/* 377 */       for (; ; ++count) { if (count >= len) break label136;
/* 378 */         int data = read();
/* 379 */         if (data == -1) {
/* 380 */           if (count != 0) break label136; return -1;
/*     */         }
/*     */ 
/* 384 */         b[(off + count)] = (byte)data; }
/*     */ 
/*     */     }
/* 387 */     long available = this.segment.length - this.offset + this.segment.offset;
/* 388 */     if (available <= 0L) {
/* 389 */       return -1;
/*     */     }
/* 391 */     if (available < len) {
/* 392 */       len = (int)available;
/*     */     }
/* 394 */     count = this.in.read(b, off, len);
/* 395 */     if (count != -1) {
/* 396 */       this.offset += count;
/*     */     }
/*     */ 
/* 399 */     label136: return count;
/*     */   }
/*     */ 
/*     */   public final void skipFully(long n) throws IOException
/*     */   {
/* 404 */     total = 0L;
/* 405 */     long cur = 0L;
/*     */ 
/* 407 */     label39: if (total >= n)
/*     */       break label39;
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 432 */     if (this.markerFound) {
/* 433 */       return -1L;
/*     */     }
/*     */ 
/* 436 */     long count = 0L;
/* 437 */     if (this.segment.isEntropyCoded()) {
/* 438 */       for (; ; count += 1L) { if (count >= n) break label113;
/* 439 */         int data = read();
/* 440 */         if (data == -1) {
/*     */           break label113;
/*     */         } }
/*     */ 
/*     */     }
/* 445 */     long available = this.segment.length - this.offset + this.segment.offset;
/* 446 */     if (available < n) {
/* 447 */       n = (int)available;
/*     */     }
/* 449 */     count = this.in.skip(n);
/* 450 */     if (count != -1L) {
/* 451 */       this.offset += count;
/*     */     }
/*     */ 
/* 454 */     label113: return count;
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int readlimit)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 501 */     throw new IOException("Reset not supported");
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 519 */     return false;
/*     */   }
/*     */ 
/*     */   public static class Segment
/*     */   {
/*     */     public final int marker;
/*     */     public final long offset;
/*     */     public final int length;
/*     */ 
/*     */     public Segment(int marker, long offset, int length)
/*     */     {
/*  85 */       this.marker = marker;
/*  86 */       this.offset = offset;
/*  87 */       this.length = length;
/*     */     }
/*     */ 
/*     */     public boolean isEntropyCoded() {
/*  91 */       return this.length == -1;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/*  96 */       return "Segment marker=0x" + Integer.toHexString(this.marker) + " offset=" + this.offset + "=0x" + Long.toHexString(this.offset);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Sankar\Downloads\CMYKDemo.jar
 * Qualified Name:     org.monte.media.jpeg.JFIFInputStream
 * JD-Core Version:    0.5.4
 */