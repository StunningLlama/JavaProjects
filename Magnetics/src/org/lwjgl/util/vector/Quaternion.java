/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.nio.FloatBuffer;
/*   4:    */ 
/*   5:    */ public class Quaternion
/*   6:    */   extends Vector
/*   7:    */   implements ReadableVector4f
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 1L;
/*  10:    */   public float x;
/*  11:    */   public float y;
/*  12:    */   public float z;
/*  13:    */   public float w;
/*  14:    */   
/*  15:    */   public Quaternion()
/*  16:    */   {
/*  17: 55 */     setIdentity();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Quaternion(ReadableVector4f src)
/*  21:    */   {
/*  22: 64 */     set(src);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Quaternion(float x, float y, float z, float w)
/*  26:    */   {
/*  27: 72 */     set(x, y, z, w);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void set(float x, float y)
/*  31:    */   {
/*  32: 81 */     this.x = x;
/*  33: 82 */     this.y = y;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void set(float x, float y, float z)
/*  37:    */   {
/*  38: 91 */     this.x = x;
/*  39: 92 */     this.y = y;
/*  40: 93 */     this.z = z;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void set(float x, float y, float z, float w)
/*  44:    */   {
/*  45:103 */     this.x = x;
/*  46:104 */     this.y = y;
/*  47:105 */     this.z = z;
/*  48:106 */     this.w = w;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Quaternion set(ReadableVector4f src)
/*  52:    */   {
/*  53:117 */     this.x = src.getX();
/*  54:118 */     this.y = src.getY();
/*  55:119 */     this.z = src.getZ();
/*  56:120 */     this.w = src.getW();
/*  57:121 */     return this;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Quaternion setIdentity()
/*  61:    */   {
/*  62:129 */     return setIdentity(this);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static Quaternion setIdentity(Quaternion q)
/*  66:    */   {
/*  67:138 */     q.x = 0.0F;
/*  68:139 */     q.y = 0.0F;
/*  69:140 */     q.z = 0.0F;
/*  70:141 */     q.w = 1.0F;
/*  71:142 */     return q;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public float lengthSquared()
/*  75:    */   {
/*  76:149 */     return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static Quaternion normalise(Quaternion src, Quaternion dest)
/*  80:    */   {
/*  81:163 */     float inv_l = 1.0F / src.length();
/*  82:165 */     if (dest == null) {
/*  83:166 */       dest = new Quaternion();
/*  84:    */     }
/*  85:168 */     dest.set(src.x * inv_l, src.y * inv_l, src.z * inv_l, src.w * inv_l);
/*  86:    */     
/*  87:170 */     return dest;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Quaternion normalise(Quaternion dest)
/*  91:    */   {
/*  92:182 */     return normalise(this, dest);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static float dot(Quaternion left, Quaternion right)
/*  96:    */   {
/*  97:195 */     return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Quaternion negate(Quaternion dest)
/* 101:    */   {
/* 102:207 */     return negate(this, dest);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static Quaternion negate(Quaternion src, Quaternion dest)
/* 106:    */   {
/* 107:220 */     if (dest == null) {
/* 108:221 */       dest = new Quaternion();
/* 109:    */     }
/* 110:223 */     dest.x = (-src.x);
/* 111:224 */     dest.y = (-src.y);
/* 112:225 */     dest.z = (-src.z);
/* 113:226 */     dest.w = src.w;
/* 114:    */     
/* 115:228 */     return dest;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public Vector negate()
/* 119:    */   {
/* 120:235 */     return negate(this, this);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public Vector load(FloatBuffer buf)
/* 124:    */   {
/* 125:242 */     this.x = buf.get();
/* 126:243 */     this.y = buf.get();
/* 127:244 */     this.z = buf.get();
/* 128:245 */     this.w = buf.get();
/* 129:246 */     return this;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Vector scale(float scale)
/* 133:    */   {
/* 134:255 */     return scale(scale, this, this);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static Quaternion scale(float scale, Quaternion src, Quaternion dest)
/* 138:    */   {
/* 139:266 */     if (dest == null) {
/* 140:267 */       dest = new Quaternion();
/* 141:    */     }
/* 142:268 */     src.x *= scale;
/* 143:269 */     src.y *= scale;
/* 144:270 */     src.z *= scale;
/* 145:271 */     src.w *= scale;
/* 146:272 */     return dest;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public Vector store(FloatBuffer buf)
/* 150:    */   {
/* 151:279 */     buf.put(this.x);
/* 152:280 */     buf.put(this.y);
/* 153:281 */     buf.put(this.z);
/* 154:282 */     buf.put(this.w);
/* 155:    */     
/* 156:284 */     return this;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public final float getX()
/* 160:    */   {
/* 161:291 */     return this.x;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public final float getY()
/* 165:    */   {
/* 166:298 */     return this.y;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public final void setX(float x)
/* 170:    */   {
/* 171:307 */     this.x = x;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public final void setY(float y)
/* 175:    */   {
/* 176:316 */     this.y = y;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setZ(float z)
/* 180:    */   {
/* 181:325 */     this.z = z;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public float getZ()
/* 185:    */   {
/* 186:334 */     return this.z;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setW(float w)
/* 190:    */   {
/* 191:343 */     this.w = w;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public float getW()
/* 195:    */   {
/* 196:352 */     return this.w;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public String toString()
/* 200:    */   {
/* 201:356 */     return "Quaternion: " + this.x + " " + this.y + " " + this.z + " " + this.w;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static Quaternion mul(Quaternion left, Quaternion right, Quaternion dest)
/* 205:    */   {
/* 206:371 */     if (dest == null) {
/* 207:372 */       dest = new Quaternion();
/* 208:    */     }
/* 209:373 */     dest.set(left.x * right.w + left.w * right.x + left.y * right.z - left.z * right.y, left.y * right.w + left.w * right.y + left.z * right.x - left.x * right.z, left.z * right.w + left.w * right.z + left.x * right.y - left.y * right.x, left.w * right.w - left.x * right.x - left.y * right.y - left.z * right.z);
/* 210:    */     
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:379 */     return dest;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public static Quaternion mulInverse(Quaternion left, Quaternion right, Quaternion dest)
/* 219:    */   {
/* 220:395 */     float n = right.lengthSquared();
/* 221:    */     
/* 222:397 */     n = n == 0.0D ? n : 1.0F / n;
/* 223:399 */     if (dest == null) {
/* 224:400 */       dest = new Quaternion();
/* 225:    */     }
/* 226:401 */     dest.set((left.x * right.w - left.w * right.x - left.y * right.z + left.z * right.y) * n, (left.y * right.w - left.w * right.y - left.z * right.x + left.x * right.z) * n, (left.z * right.w - left.w * right.z - left.x * right.y + left.y * right.x) * n, (left.w * right.w + left.x * right.x + left.y * right.y + left.z * right.z) * n);
/* 227:    */     
/* 228:    */ 
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:    */ 
/* 233:    */ 
/* 234:    */ 
/* 235:    */ 
/* 236:    */ 
/* 237:412 */     return dest;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public final void setFromAxisAngle(Vector4f a1)
/* 241:    */   {
/* 242:423 */     this.x = a1.x;
/* 243:424 */     this.y = a1.y;
/* 244:425 */     this.z = a1.z;
/* 245:426 */     float n = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/* 246:    */     
/* 247:428 */     float s = (float)(Math.sin(0.5D * a1.w) / n);
/* 248:429 */     this.x *= s;
/* 249:430 */     this.y *= s;
/* 250:431 */     this.z *= s;
/* 251:432 */     this.w = ((float)Math.cos(0.5D * a1.w));
/* 252:    */   }
/* 253:    */   
/* 254:    */   public final Quaternion setFromMatrix(Matrix4f m)
/* 255:    */   {
/* 256:444 */     return setFromMatrix(m, this);
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static Quaternion setFromMatrix(Matrix4f m, Quaternion q)
/* 260:    */   {
/* 261:458 */     return q.setFromMat(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
/* 262:    */   }
/* 263:    */   
/* 264:    */   public final Quaternion setFromMatrix(Matrix3f m)
/* 265:    */   {
/* 266:470 */     return setFromMatrix(m, this);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public static Quaternion setFromMatrix(Matrix3f m, Quaternion q)
/* 270:    */   {
/* 271:484 */     return q.setFromMat(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
/* 272:    */   }
/* 273:    */   
/* 274:    */   private Quaternion setFromMat(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22)
/* 275:    */   {
/* 276:495 */     float tr = m00 + m11 + m22;
/* 277:496 */     if (tr >= 0.0D)
/* 278:    */     {
/* 279:497 */       float s = (float)Math.sqrt(tr + 1.0D);
/* 280:498 */       this.w = (s * 0.5F);
/* 281:499 */       s = 0.5F / s;
/* 282:500 */       this.x = ((m21 - m12) * s);
/* 283:501 */       this.y = ((m02 - m20) * s);
/* 284:502 */       this.z = ((m10 - m01) * s);
/* 285:    */     }
/* 286:    */     else
/* 287:    */     {
/* 288:504 */       float max = Math.max(Math.max(m00, m11), m22);
/* 289:505 */       if (max == m00)
/* 290:    */       {
/* 291:506 */         float s = (float)Math.sqrt(m00 - (m11 + m22) + 1.0D);
/* 292:507 */         this.x = (s * 0.5F);
/* 293:508 */         s = 0.5F / s;
/* 294:509 */         this.y = ((m01 + m10) * s);
/* 295:510 */         this.z = ((m20 + m02) * s);
/* 296:511 */         this.w = ((m21 - m12) * s);
/* 297:    */       }
/* 298:512 */       else if (max == m11)
/* 299:    */       {
/* 300:513 */         float s = (float)Math.sqrt(m11 - (m22 + m00) + 1.0D);
/* 301:514 */         this.y = (s * 0.5F);
/* 302:515 */         s = 0.5F / s;
/* 303:516 */         this.z = ((m12 + m21) * s);
/* 304:517 */         this.x = ((m01 + m10) * s);
/* 305:518 */         this.w = ((m02 - m20) * s);
/* 306:    */       }
/* 307:    */       else
/* 308:    */       {
/* 309:520 */         float s = (float)Math.sqrt(m22 - (m00 + m11) + 1.0D);
/* 310:521 */         this.z = (s * 0.5F);
/* 311:522 */         s = 0.5F / s;
/* 312:523 */         this.x = ((m20 + m02) * s);
/* 313:524 */         this.y = ((m12 + m21) * s);
/* 314:525 */         this.w = ((m10 - m01) * s);
/* 315:    */       }
/* 316:    */     }
/* 317:528 */     return this;
/* 318:    */   }
/* 319:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Quaternion
 * JD-Core Version:    0.7.0.1
 */