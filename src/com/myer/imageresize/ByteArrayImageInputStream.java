/*     */ package org.monte.media.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ public class ByteArrayImageInputStream extends ImageInputStreamImpl2
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int count;
/*     */   private final int arrayOffset;
/*     */ 
/*     */   public ByteArrayImageInputStream(byte[] buf)
/*     */   {
/*  56 */     this(buf, ByteOrder.BIG_ENDIAN);
/*     */   }
/*     */ 
/*     */   public ByteArrayImageInputStream(byte[] buf, ByteOrder byteOrder) {
/*  60 */     this(buf, 0, buf.length, byteOrder);
/*     */   }
/*     */ 
/*     */   public ByteArrayImageInputStream(byte[] buf, int offset, int length, ByteOrder byteOrder) {
/*  64 */     this.buf = buf;
/*  65 */     this.streamPos = offset;
/*  66 */     this.count = Math.min(offset + length, buf.length);
/*  67 */     this.arrayOffset = offset;
/*  68 */     this.byteOrder = byteOrder;
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */   {
/*  86 */     flushBits();
/*  87 */     return (this.streamPos < this.count) ? this.buf[(int)(this.streamPos++)] & 0xFF : -1;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] b, int off, int len)
/*     */   {
/* 121 */     flushBits();
/* 122 */     if (b == null)
/* 123 */       throw new NullPointerException();
/* 124 */     if ((off < 0) || (len < 0) || (len > b.length - off)) {
/* 125 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 127 */     if (this.streamPos >= this.count) {
/* 128 */       return -1;
/*     */     }
/* 130 */     if (this.streamPos + len > this.count) {
/* 131 */       len = (int)(this.count - this.streamPos);
/*     */     }
/* 133 */     if (len <= 0) {
/* 134 */       return 0;
/*     */     }
/* 136 */     System.arraycopy(this.buf, (int)this.streamPos, b, off, len);
/* 137 */     this.streamPos += len;
/* 138 */     return len;
/*     */   }
/*     */ 
/*     */   public synchronized long skip(long n)
/*     */   {
/* 154 */     if (this.streamPos + n > this.count) {
/* 155 */       n = this.count - this.streamPos;
/*     */     }
/* 157 */     if (n < 0L) {
/* 158 */       return 0L;
/*     */     }
/* 160 */     this.streamPos += n;
/* 161 */     return n;
/*     */   }
/*     */ 
/*     */   public synchronized int available()
/*     */   {
/* 175 */     return (int)(this.count - this.streamPos);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */   }
/*     */ 
/*     */   public long getStreamPosition()
/*     */     throws IOException
/*     */   {
/* 193 */     checkClosed();
/* 194 */     return this.streamPos - this.arrayOffset;
/*     */   }
/*     */ 
/*     */   public void seek(long pos) throws IOException {
/* 198 */     checkClosed();
/* 199 */     flushBits();
/*     */ 
/* 202 */     if (pos < this.flushedPos) {
/* 203 */       throw new IndexOutOfBoundsException("pos < flushedPos!");
/*     */     }
/*     */ 
/* 206 */     this.streamPos = (pos + this.arrayOffset);
/*     */   }
/*     */ 
/*     */   private void flushBits() {
/* 210 */     this.bitOffset = 0;
/*     */   }
/*     */ 
/*     */   public long length() {
/* 214 */     return this.count - this.arrayOffset;
/*     */   }
/*     */ }

/* Location:           C:\Sankar\Downloads\CMYKDemo.jar
 * Qualified Name:     org.monte.media.io.ByteArrayImageInputStream
 * JD-Core Version:    0.5.4
 */