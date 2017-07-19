/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.nio.FloatBuffer;
/*   5:    */ 
/*   6:    */ public class Matrix2f
/*   7:    */   extends Matrix
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 1L;
/*  11:    */   public float m00;
/*  12:    */   public float m01;
/*  13:    */   public float m10;
/*  14:    */   public float m11;
/*  15:    */   
/*  16:    */   public Matrix2f()
/*  17:    */   {
/*  18: 56 */     setIdentity();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Matrix2f(Matrix2f src)
/*  22:    */   {
/*  23: 63 */     load(src);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Matrix2f load(Matrix2f src)
/*  27:    */   {
/*  28: 72 */     return load(src, this);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static Matrix2f load(Matrix2f src, Matrix2f dest)
/*  32:    */   {
/*  33: 82 */     if (dest == null) {
/*  34: 83 */       dest = new Matrix2f();
/*  35:    */     }
/*  36: 85 */     dest.m00 = src.m00;
/*  37: 86 */     dest.m01 = src.m01;
/*  38: 87 */     dest.m10 = src.m10;
/*  39: 88 */     dest.m11 = src.m11;
/*  40:    */     
/*  41: 90 */     return dest;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Matrix load(FloatBuffer buf)
/*  45:    */   {
/*  46:102 */     this.m00 = buf.get();
/*  47:103 */     this.m01 = buf.get();
/*  48:104 */     this.m10 = buf.get();
/*  49:105 */     this.m11 = buf.get();
/*  50:    */     
/*  51:107 */     return this;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Matrix loadTranspose(FloatBuffer buf)
/*  55:    */   {
/*  56:119 */     this.m00 = buf.get();
/*  57:120 */     this.m10 = buf.get();
/*  58:121 */     this.m01 = buf.get();
/*  59:122 */     this.m11 = buf.get();
/*  60:    */     
/*  61:124 */     return this;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Matrix store(FloatBuffer buf)
/*  65:    */   {
/*  66:133 */     buf.put(this.m00);
/*  67:134 */     buf.put(this.m01);
/*  68:135 */     buf.put(this.m10);
/*  69:136 */     buf.put(this.m11);
/*  70:137 */     return this;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Matrix storeTranspose(FloatBuffer buf)
/*  74:    */   {
/*  75:146 */     buf.put(this.m00);
/*  76:147 */     buf.put(this.m10);
/*  77:148 */     buf.put(this.m01);
/*  78:149 */     buf.put(this.m11);
/*  79:150 */     return this;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static Matrix2f add(Matrix2f left, Matrix2f right, Matrix2f dest)
/*  83:    */   {
/*  84:163 */     if (dest == null) {
/*  85:164 */       dest = new Matrix2f();
/*  86:    */     }
/*  87:166 */     left.m00 += right.m00;
/*  88:167 */     left.m01 += right.m01;
/*  89:168 */     left.m10 += right.m10;
/*  90:169 */     left.m11 += right.m11;
/*  91:    */     
/*  92:171 */     return dest;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static Matrix2f sub(Matrix2f left, Matrix2f right, Matrix2f dest)
/*  96:    */   {
/*  97:182 */     if (dest == null) {
/*  98:183 */       dest = new Matrix2f();
/*  99:    */     }
/* 100:185 */     left.m00 -= right.m00;
/* 101:186 */     left.m01 -= right.m01;
/* 102:187 */     left.m10 -= right.m10;
/* 103:188 */     left.m11 -= right.m11;
/* 104:    */     
/* 105:190 */     return dest;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static Matrix2f mul(Matrix2f left, Matrix2f right, Matrix2f dest)
/* 109:    */   {
/* 110:201 */     if (dest == null) {
/* 111:202 */       dest = new Matrix2f();
/* 112:    */     }
/* 113:204 */     float m00 = left.m00 * right.m00 + left.m10 * right.m01;
/* 114:205 */     float m01 = left.m01 * right.m00 + left.m11 * right.m01;
/* 115:206 */     float m10 = left.m00 * right.m10 + left.m10 * right.m11;
/* 116:207 */     float m11 = left.m01 * right.m10 + left.m11 * right.m11;
/* 117:    */     
/* 118:209 */     dest.m00 = m00;
/* 119:210 */     dest.m01 = m01;
/* 120:211 */     dest.m10 = m10;
/* 121:212 */     dest.m11 = m11;
/* 122:    */     
/* 123:214 */     return dest;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static Vector2f transform(Matrix2f left, Vector2f right, Vector2f dest)
/* 127:    */   {
/* 128:226 */     if (dest == null) {
/* 129:227 */       dest = new Vector2f();
/* 130:    */     }
/* 131:229 */     float x = left.m00 * right.x + left.m10 * right.y;
/* 132:230 */     float y = left.m01 * right.x + left.m11 * right.y;
/* 133:    */     
/* 134:232 */     dest.x = x;
/* 135:233 */     dest.y = y;
/* 136:    */     
/* 137:235 */     return dest;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public Matrix transpose()
/* 141:    */   {
/* 142:243 */     return transpose(this);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public Matrix2f transpose(Matrix2f dest)
/* 146:    */   {
/* 147:252 */     return transpose(this, dest);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public static Matrix2f transpose(Matrix2f src, Matrix2f dest)
/* 151:    */   {
/* 152:262 */     if (dest == null) {
/* 153:263 */       dest = new Matrix2f();
/* 154:    */     }
/* 155:265 */     float m01 = src.m10;
/* 156:266 */     float m10 = src.m01;
/* 157:    */     
/* 158:268 */     dest.m01 = m01;
/* 159:269 */     dest.m10 = m10;
/* 160:    */     
/* 161:271 */     return dest;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public Matrix invert()
/* 165:    */   {
/* 166:279 */     return invert(this, this);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static Matrix2f invert(Matrix2f src, Matrix2f dest)
/* 170:    */   {
/* 171:293 */     float determinant = src.determinant();
/* 172:294 */     if (determinant != 0.0F)
/* 173:    */     {
/* 174:295 */       if (dest == null) {
/* 175:296 */         dest = new Matrix2f();
/* 176:    */       }
/* 177:297 */       float determinant_inv = 1.0F / determinant;
/* 178:298 */       float t00 = src.m11 * determinant_inv;
/* 179:299 */       float t01 = -src.m01 * determinant_inv;
/* 180:300 */       float t11 = src.m00 * determinant_inv;
/* 181:301 */       float t10 = -src.m10 * determinant_inv;
/* 182:    */       
/* 183:303 */       dest.m00 = t00;
/* 184:304 */       dest.m01 = t01;
/* 185:305 */       dest.m10 = t10;
/* 186:306 */       dest.m11 = t11;
/* 187:307 */       return dest;
/* 188:    */     }
/* 189:309 */     return null;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public String toString()
/* 193:    */   {
/* 194:316 */     StringBuilder buf = new StringBuilder();
/* 195:317 */     buf.append(this.m00).append(' ').append(this.m10).append(' ').append('\n');
/* 196:318 */     buf.append(this.m01).append(' ').append(this.m11).append(' ').append('\n');
/* 197:319 */     return buf.toString();
/* 198:    */   }
/* 199:    */   
/* 200:    */   public Matrix negate()
/* 201:    */   {
/* 202:327 */     return negate(this);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public Matrix2f negate(Matrix2f dest)
/* 206:    */   {
/* 207:336 */     return negate(this, dest);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public static Matrix2f negate(Matrix2f src, Matrix2f dest)
/* 211:    */   {
/* 212:346 */     if (dest == null) {
/* 213:347 */       dest = new Matrix2f();
/* 214:    */     }
/* 215:349 */     dest.m00 = (-src.m00);
/* 216:350 */     dest.m01 = (-src.m01);
/* 217:351 */     dest.m10 = (-src.m10);
/* 218:352 */     dest.m11 = (-src.m11);
/* 219:    */     
/* 220:354 */     return dest;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public Matrix setIdentity()
/* 224:    */   {
/* 225:362 */     return setIdentity(this);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public static Matrix2f setIdentity(Matrix2f src)
/* 229:    */   {
/* 230:371 */     src.m00 = 1.0F;
/* 231:372 */     src.m01 = 0.0F;
/* 232:373 */     src.m10 = 0.0F;
/* 233:374 */     src.m11 = 1.0F;
/* 234:375 */     return src;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public Matrix setZero()
/* 238:    */   {
/* 239:383 */     return setZero(this);
/* 240:    */   }
/* 241:    */   
/* 242:    */   public static Matrix2f setZero(Matrix2f src)
/* 243:    */   {
/* 244:387 */     src.m00 = 0.0F;
/* 245:388 */     src.m01 = 0.0F;
/* 246:389 */     src.m10 = 0.0F;
/* 247:390 */     src.m11 = 0.0F;
/* 248:391 */     return src;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public float determinant()
/* 252:    */   {
/* 253:398 */     return this.m00 * this.m11 - this.m01 * this.m10;
/* 254:    */   }
/* 255:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Matrix2f
 * JD-Core Version:    0.7.0.1
 */