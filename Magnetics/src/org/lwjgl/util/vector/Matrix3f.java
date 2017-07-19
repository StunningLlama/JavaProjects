/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.nio.FloatBuffer;
/*   5:    */ 
/*   6:    */ public class Matrix3f
/*   7:    */   extends Matrix
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 1L;
/*  11:    */   public float m00;
/*  12:    */   public float m01;
/*  13:    */   public float m02;
/*  14:    */   public float m10;
/*  15:    */   public float m11;
/*  16:    */   public float m12;
/*  17:    */   public float m20;
/*  18:    */   public float m21;
/*  19:    */   public float m22;
/*  20:    */   
/*  21:    */   public Matrix3f()
/*  22:    */   {
/*  23: 65 */     setIdentity();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Matrix3f load(Matrix3f src)
/*  27:    */   {
/*  28: 74 */     return load(src, this);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static Matrix3f load(Matrix3f src, Matrix3f dest)
/*  32:    */   {
/*  33: 84 */     if (dest == null) {
/*  34: 85 */       dest = new Matrix3f();
/*  35:    */     }
/*  36: 87 */     dest.m00 = src.m00;
/*  37: 88 */     dest.m10 = src.m10;
/*  38: 89 */     dest.m20 = src.m20;
/*  39: 90 */     dest.m01 = src.m01;
/*  40: 91 */     dest.m11 = src.m11;
/*  41: 92 */     dest.m21 = src.m21;
/*  42: 93 */     dest.m02 = src.m02;
/*  43: 94 */     dest.m12 = src.m12;
/*  44: 95 */     dest.m22 = src.m22;
/*  45:    */     
/*  46: 97 */     return dest;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Matrix load(FloatBuffer buf)
/*  50:    */   {
/*  51:109 */     this.m00 = buf.get();
/*  52:110 */     this.m01 = buf.get();
/*  53:111 */     this.m02 = buf.get();
/*  54:112 */     this.m10 = buf.get();
/*  55:113 */     this.m11 = buf.get();
/*  56:114 */     this.m12 = buf.get();
/*  57:115 */     this.m20 = buf.get();
/*  58:116 */     this.m21 = buf.get();
/*  59:117 */     this.m22 = buf.get();
/*  60:    */     
/*  61:119 */     return this;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Matrix loadTranspose(FloatBuffer buf)
/*  65:    */   {
/*  66:131 */     this.m00 = buf.get();
/*  67:132 */     this.m10 = buf.get();
/*  68:133 */     this.m20 = buf.get();
/*  69:134 */     this.m01 = buf.get();
/*  70:135 */     this.m11 = buf.get();
/*  71:136 */     this.m21 = buf.get();
/*  72:137 */     this.m02 = buf.get();
/*  73:138 */     this.m12 = buf.get();
/*  74:139 */     this.m22 = buf.get();
/*  75:    */     
/*  76:141 */     return this;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Matrix store(FloatBuffer buf)
/*  80:    */   {
/*  81:150 */     buf.put(this.m00);
/*  82:151 */     buf.put(this.m01);
/*  83:152 */     buf.put(this.m02);
/*  84:153 */     buf.put(this.m10);
/*  85:154 */     buf.put(this.m11);
/*  86:155 */     buf.put(this.m12);
/*  87:156 */     buf.put(this.m20);
/*  88:157 */     buf.put(this.m21);
/*  89:158 */     buf.put(this.m22);
/*  90:159 */     return this;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public Matrix storeTranspose(FloatBuffer buf)
/*  94:    */   {
/*  95:168 */     buf.put(this.m00);
/*  96:169 */     buf.put(this.m10);
/*  97:170 */     buf.put(this.m20);
/*  98:171 */     buf.put(this.m01);
/*  99:172 */     buf.put(this.m11);
/* 100:173 */     buf.put(this.m21);
/* 101:174 */     buf.put(this.m02);
/* 102:175 */     buf.put(this.m12);
/* 103:176 */     buf.put(this.m22);
/* 104:177 */     return this;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static Matrix3f add(Matrix3f left, Matrix3f right, Matrix3f dest)
/* 108:    */   {
/* 109:188 */     if (dest == null) {
/* 110:189 */       dest = new Matrix3f();
/* 111:    */     }
/* 112:191 */     left.m00 += right.m00;
/* 113:192 */     left.m01 += right.m01;
/* 114:193 */     left.m02 += right.m02;
/* 115:194 */     left.m10 += right.m10;
/* 116:195 */     left.m11 += right.m11;
/* 117:196 */     left.m12 += right.m12;
/* 118:197 */     left.m20 += right.m20;
/* 119:198 */     left.m21 += right.m21;
/* 120:199 */     left.m22 += right.m22;
/* 121:    */     
/* 122:201 */     return dest;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static Matrix3f sub(Matrix3f left, Matrix3f right, Matrix3f dest)
/* 126:    */   {
/* 127:212 */     if (dest == null) {
/* 128:213 */       dest = new Matrix3f();
/* 129:    */     }
/* 130:215 */     left.m00 -= right.m00;
/* 131:216 */     left.m01 -= right.m01;
/* 132:217 */     left.m02 -= right.m02;
/* 133:218 */     left.m10 -= right.m10;
/* 134:219 */     left.m11 -= right.m11;
/* 135:220 */     left.m12 -= right.m12;
/* 136:221 */     left.m20 -= right.m20;
/* 137:222 */     left.m21 -= right.m21;
/* 138:223 */     left.m22 -= right.m22;
/* 139:    */     
/* 140:225 */     return dest;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static Matrix3f mul(Matrix3f left, Matrix3f right, Matrix3f dest)
/* 144:    */   {
/* 145:236 */     if (dest == null) {
/* 146:237 */       dest = new Matrix3f();
/* 147:    */     }
/* 148:239 */     float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02;
/* 149:    */     
/* 150:241 */     float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02;
/* 151:    */     
/* 152:243 */     float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02;
/* 153:    */     
/* 154:245 */     float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12;
/* 155:    */     
/* 156:247 */     float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12;
/* 157:    */     
/* 158:249 */     float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12;
/* 159:    */     
/* 160:251 */     float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22;
/* 161:    */     
/* 162:253 */     float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22;
/* 163:    */     
/* 164:255 */     float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22;
/* 165:    */     
/* 166:    */ 
/* 167:258 */     dest.m00 = m00;
/* 168:259 */     dest.m01 = m01;
/* 169:260 */     dest.m02 = m02;
/* 170:261 */     dest.m10 = m10;
/* 171:262 */     dest.m11 = m11;
/* 172:263 */     dest.m12 = m12;
/* 173:264 */     dest.m20 = m20;
/* 174:265 */     dest.m21 = m21;
/* 175:266 */     dest.m22 = m22;
/* 176:    */     
/* 177:268 */     return dest;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public static Vector3f transform(Matrix3f left, Vector3f right, Vector3f dest)
/* 181:    */   {
/* 182:280 */     if (dest == null) {
/* 183:281 */       dest = new Vector3f();
/* 184:    */     }
/* 185:283 */     float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z;
/* 186:284 */     float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z;
/* 187:285 */     float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z;
/* 188:    */     
/* 189:287 */     dest.x = x;
/* 190:288 */     dest.y = y;
/* 191:289 */     dest.z = z;
/* 192:    */     
/* 193:291 */     return dest;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public Matrix transpose()
/* 197:    */   {
/* 198:299 */     return transpose(this, this);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public Matrix3f transpose(Matrix3f dest)
/* 202:    */   {
/* 203:308 */     return transpose(this, dest);
/* 204:    */   }
/* 205:    */   
/* 206:    */   public static Matrix3f transpose(Matrix3f src, Matrix3f dest)
/* 207:    */   {
/* 208:318 */     if (dest == null) {
/* 209:319 */       dest = new Matrix3f();
/* 210:    */     }
/* 211:320 */     float m00 = src.m00;
/* 212:321 */     float m01 = src.m10;
/* 213:322 */     float m02 = src.m20;
/* 214:323 */     float m10 = src.m01;
/* 215:324 */     float m11 = src.m11;
/* 216:325 */     float m12 = src.m21;
/* 217:326 */     float m20 = src.m02;
/* 218:327 */     float m21 = src.m12;
/* 219:328 */     float m22 = src.m22;
/* 220:    */     
/* 221:330 */     dest.m00 = m00;
/* 222:331 */     dest.m01 = m01;
/* 223:332 */     dest.m02 = m02;
/* 224:333 */     dest.m10 = m10;
/* 225:334 */     dest.m11 = m11;
/* 226:335 */     dest.m12 = m12;
/* 227:336 */     dest.m20 = m20;
/* 228:337 */     dest.m21 = m21;
/* 229:338 */     dest.m22 = m22;
/* 230:339 */     return dest;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public float determinant()
/* 234:    */   {
/* 235:346 */     float f = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
/* 236:    */     
/* 237:    */ 
/* 238:    */ 
/* 239:350 */     return f;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public String toString()
/* 243:    */   {
/* 244:357 */     StringBuilder buf = new StringBuilder();
/* 245:358 */     buf.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append('\n');
/* 246:359 */     buf.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append('\n');
/* 247:360 */     buf.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append('\n');
/* 248:361 */     return buf.toString();
/* 249:    */   }
/* 250:    */   
/* 251:    */   public Matrix invert()
/* 252:    */   {
/* 253:369 */     return invert(this, this);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public static Matrix3f invert(Matrix3f src, Matrix3f dest)
/* 257:    */   {
/* 258:379 */     float determinant = src.determinant();
/* 259:381 */     if (determinant != 0.0F)
/* 260:    */     {
/* 261:382 */       if (dest == null) {
/* 262:383 */         dest = new Matrix3f();
/* 263:    */       }
/* 264:392 */       float determinant_inv = 1.0F / determinant;
/* 265:    */       
/* 266:    */ 
/* 267:395 */       float t00 = src.m11 * src.m22 - src.m12 * src.m21;
/* 268:396 */       float t01 = -src.m10 * src.m22 + src.m12 * src.m20;
/* 269:397 */       float t02 = src.m10 * src.m21 - src.m11 * src.m20;
/* 270:398 */       float t10 = -src.m01 * src.m22 + src.m02 * src.m21;
/* 271:399 */       float t11 = src.m00 * src.m22 - src.m02 * src.m20;
/* 272:400 */       float t12 = -src.m00 * src.m21 + src.m01 * src.m20;
/* 273:401 */       float t20 = src.m01 * src.m12 - src.m02 * src.m11;
/* 274:402 */       float t21 = -src.m00 * src.m12 + src.m02 * src.m10;
/* 275:403 */       float t22 = src.m00 * src.m11 - src.m01 * src.m10;
/* 276:    */       
/* 277:405 */       dest.m00 = (t00 * determinant_inv);
/* 278:406 */       dest.m11 = (t11 * determinant_inv);
/* 279:407 */       dest.m22 = (t22 * determinant_inv);
/* 280:408 */       dest.m01 = (t10 * determinant_inv);
/* 281:409 */       dest.m10 = (t01 * determinant_inv);
/* 282:410 */       dest.m20 = (t02 * determinant_inv);
/* 283:411 */       dest.m02 = (t20 * determinant_inv);
/* 284:412 */       dest.m12 = (t21 * determinant_inv);
/* 285:413 */       dest.m21 = (t12 * determinant_inv);
/* 286:414 */       return dest;
/* 287:    */     }
/* 288:416 */     return null;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public Matrix negate()
/* 292:    */   {
/* 293:425 */     return negate(this);
/* 294:    */   }
/* 295:    */   
/* 296:    */   public Matrix3f negate(Matrix3f dest)
/* 297:    */   {
/* 298:434 */     return negate(this, dest);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public static Matrix3f negate(Matrix3f src, Matrix3f dest)
/* 302:    */   {
/* 303:444 */     if (dest == null) {
/* 304:445 */       dest = new Matrix3f();
/* 305:    */     }
/* 306:447 */     dest.m00 = (-src.m00);
/* 307:448 */     dest.m01 = (-src.m02);
/* 308:449 */     dest.m02 = (-src.m01);
/* 309:450 */     dest.m10 = (-src.m10);
/* 310:451 */     dest.m11 = (-src.m12);
/* 311:452 */     dest.m12 = (-src.m11);
/* 312:453 */     dest.m20 = (-src.m20);
/* 313:454 */     dest.m21 = (-src.m22);
/* 314:455 */     dest.m22 = (-src.m21);
/* 315:456 */     return dest;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public Matrix setIdentity()
/* 319:    */   {
/* 320:464 */     return setIdentity(this);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static Matrix3f setIdentity(Matrix3f m)
/* 324:    */   {
/* 325:473 */     m.m00 = 1.0F;
/* 326:474 */     m.m01 = 0.0F;
/* 327:475 */     m.m02 = 0.0F;
/* 328:476 */     m.m10 = 0.0F;
/* 329:477 */     m.m11 = 1.0F;
/* 330:478 */     m.m12 = 0.0F;
/* 331:479 */     m.m20 = 0.0F;
/* 332:480 */     m.m21 = 0.0F;
/* 333:481 */     m.m22 = 1.0F;
/* 334:482 */     return m;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public Matrix setZero()
/* 338:    */   {
/* 339:490 */     return setZero(this);
/* 340:    */   }
/* 341:    */   
/* 342:    */   public static Matrix3f setZero(Matrix3f m)
/* 343:    */   {
/* 344:499 */     m.m00 = 0.0F;
/* 345:500 */     m.m01 = 0.0F;
/* 346:501 */     m.m02 = 0.0F;
/* 347:502 */     m.m10 = 0.0F;
/* 348:503 */     m.m11 = 0.0F;
/* 349:504 */     m.m12 = 0.0F;
/* 350:505 */     m.m20 = 0.0F;
/* 351:506 */     m.m21 = 0.0F;
/* 352:507 */     m.m22 = 0.0F;
/* 353:508 */     return m;
/* 354:    */   }
/* 355:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Matrix3f
 * JD-Core Version:    0.7.0.1
 */