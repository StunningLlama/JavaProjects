/*  1:   */ package org.lwjgl.util.vector;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.nio.FloatBuffer;
/*  5:   */ 
/*  6:   */ public abstract class Vector
/*  7:   */   implements Serializable, ReadableVector
/*  8:   */ {
/*  9:   */   public final float length()
/* 10:   */   {
/* 11:58 */     return (float)Math.sqrt(lengthSquared());
/* 12:   */   }
/* 13:   */   
/* 14:   */   public abstract float lengthSquared();
/* 15:   */   
/* 16:   */   public abstract Vector load(FloatBuffer paramFloatBuffer);
/* 17:   */   
/* 18:   */   public abstract Vector negate();
/* 19:   */   
/* 20:   */   public final Vector normalise()
/* 21:   */   {
/* 22:86 */     float len = length();
/* 23:87 */     if (len != 0.0F)
/* 24:   */     {
/* 25:88 */       float l = 1.0F / len;
/* 26:89 */       return scale(l);
/* 27:   */     }
/* 28:91 */     throw new IllegalStateException("Zero length vector");
/* 29:   */   }
/* 30:   */   
/* 31:   */   public abstract Vector store(FloatBuffer paramFloatBuffer);
/* 32:   */   
/* 33:   */   public abstract Vector scale(float paramFloat);
/* 34:   */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Vector
 * JD-Core Version:    0.7.0.1
 */