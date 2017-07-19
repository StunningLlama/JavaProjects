/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.nio.FloatBuffer;
/*   5:    */ 
/*   6:    */ public class Matrix4f
/*   7:    */   extends Matrix
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 1L;
/*  11:    */   public float m00;
/*  12:    */   public float m01;
/*  13:    */   public float m02;
/*  14:    */   public float m03;
/*  15:    */   public float m10;
/*  16:    */   public float m11;
/*  17:    */   public float m12;
/*  18:    */   public float m13;
/*  19:    */   public float m20;
/*  20:    */   public float m21;
/*  21:    */   public float m22;
/*  22:    */   public float m23;
/*  23:    */   public float m30;
/*  24:    */   public float m31;
/*  25:    */   public float m32;
/*  26:    */   public float m33;
/*  27:    */   
/*  28:    */   public Matrix4f()
/*  29:    */   {
/*  30: 52 */     setIdentity();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Matrix4f(Matrix4f src)
/*  34:    */   {
/*  35: 57 */     load(src);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String toString()
/*  39:    */   {
/*  40: 64 */     StringBuilder buf = new StringBuilder();
/*  41: 65 */     buf.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append(this.m30).append('\n');
/*  42: 66 */     buf.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append(this.m31).append('\n');
/*  43: 67 */     buf.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append(this.m32).append('\n');
/*  44: 68 */     buf.append(this.m03).append(' ').append(this.m13).append(' ').append(this.m23).append(' ').append(this.m33).append('\n');
/*  45: 69 */     return buf.toString();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Matrix setIdentity()
/*  49:    */   {
/*  50: 77 */     return setIdentity(this);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static Matrix4f setIdentity(Matrix4f m)
/*  54:    */   {
/*  55: 86 */     m.m00 = 1.0F;
/*  56: 87 */     m.m01 = 0.0F;
/*  57: 88 */     m.m02 = 0.0F;
/*  58: 89 */     m.m03 = 0.0F;
/*  59: 90 */     m.m10 = 0.0F;
/*  60: 91 */     m.m11 = 1.0F;
/*  61: 92 */     m.m12 = 0.0F;
/*  62: 93 */     m.m13 = 0.0F;
/*  63: 94 */     m.m20 = 0.0F;
/*  64: 95 */     m.m21 = 0.0F;
/*  65: 96 */     m.m22 = 1.0F;
/*  66: 97 */     m.m23 = 0.0F;
/*  67: 98 */     m.m30 = 0.0F;
/*  68: 99 */     m.m31 = 0.0F;
/*  69:100 */     m.m32 = 0.0F;
/*  70:101 */     m.m33 = 1.0F;
/*  71:    */     
/*  72:103 */     return m;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Matrix setZero()
/*  76:    */   {
/*  77:111 */     return setZero(this);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static Matrix4f setZero(Matrix4f m)
/*  81:    */   {
/*  82:120 */     m.m00 = 0.0F;
/*  83:121 */     m.m01 = 0.0F;
/*  84:122 */     m.m02 = 0.0F;
/*  85:123 */     m.m03 = 0.0F;
/*  86:124 */     m.m10 = 0.0F;
/*  87:125 */     m.m11 = 0.0F;
/*  88:126 */     m.m12 = 0.0F;
/*  89:127 */     m.m13 = 0.0F;
/*  90:128 */     m.m20 = 0.0F;
/*  91:129 */     m.m21 = 0.0F;
/*  92:130 */     m.m22 = 0.0F;
/*  93:131 */     m.m23 = 0.0F;
/*  94:132 */     m.m30 = 0.0F;
/*  95:133 */     m.m31 = 0.0F;
/*  96:134 */     m.m32 = 0.0F;
/*  97:135 */     m.m33 = 0.0F;
/*  98:    */     
/*  99:137 */     return m;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public Matrix4f load(Matrix4f src)
/* 103:    */   {
/* 104:146 */     return load(src, this);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static Matrix4f load(Matrix4f src, Matrix4f dest)
/* 108:    */   {
/* 109:156 */     if (dest == null) {
/* 110:157 */       dest = new Matrix4f();
/* 111:    */     }
/* 112:158 */     dest.m00 = src.m00;
/* 113:159 */     dest.m01 = src.m01;
/* 114:160 */     dest.m02 = src.m02;
/* 115:161 */     dest.m03 = src.m03;
/* 116:162 */     dest.m10 = src.m10;
/* 117:163 */     dest.m11 = src.m11;
/* 118:164 */     dest.m12 = src.m12;
/* 119:165 */     dest.m13 = src.m13;
/* 120:166 */     dest.m20 = src.m20;
/* 121:167 */     dest.m21 = src.m21;
/* 122:168 */     dest.m22 = src.m22;
/* 123:169 */     dest.m23 = src.m23;
/* 124:170 */     dest.m30 = src.m30;
/* 125:171 */     dest.m31 = src.m31;
/* 126:172 */     dest.m32 = src.m32;
/* 127:173 */     dest.m33 = src.m33;
/* 128:    */     
/* 129:175 */     return dest;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Matrix load(FloatBuffer buf)
/* 133:    */   {
/* 134:187 */     this.m00 = buf.get();
/* 135:188 */     this.m01 = buf.get();
/* 136:189 */     this.m02 = buf.get();
/* 137:190 */     this.m03 = buf.get();
/* 138:191 */     this.m10 = buf.get();
/* 139:192 */     this.m11 = buf.get();
/* 140:193 */     this.m12 = buf.get();
/* 141:194 */     this.m13 = buf.get();
/* 142:195 */     this.m20 = buf.get();
/* 143:196 */     this.m21 = buf.get();
/* 144:197 */     this.m22 = buf.get();
/* 145:198 */     this.m23 = buf.get();
/* 146:199 */     this.m30 = buf.get();
/* 147:200 */     this.m31 = buf.get();
/* 148:201 */     this.m32 = buf.get();
/* 149:202 */     this.m33 = buf.get();
/* 150:    */     
/* 151:204 */     return this;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public Matrix loadTranspose(FloatBuffer buf)
/* 155:    */   {
/* 156:216 */     this.m00 = buf.get();
/* 157:217 */     this.m10 = buf.get();
/* 158:218 */     this.m20 = buf.get();
/* 159:219 */     this.m30 = buf.get();
/* 160:220 */     this.m01 = buf.get();
/* 161:221 */     this.m11 = buf.get();
/* 162:222 */     this.m21 = buf.get();
/* 163:223 */     this.m31 = buf.get();
/* 164:224 */     this.m02 = buf.get();
/* 165:225 */     this.m12 = buf.get();
/* 166:226 */     this.m22 = buf.get();
/* 167:227 */     this.m32 = buf.get();
/* 168:228 */     this.m03 = buf.get();
/* 169:229 */     this.m13 = buf.get();
/* 170:230 */     this.m23 = buf.get();
/* 171:231 */     this.m33 = buf.get();
/* 172:    */     
/* 173:233 */     return this;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public Matrix store(FloatBuffer buf)
/* 177:    */   {
/* 178:242 */     buf.put(this.m00);
/* 179:243 */     buf.put(this.m01);
/* 180:244 */     buf.put(this.m02);
/* 181:245 */     buf.put(this.m03);
/* 182:246 */     buf.put(this.m10);
/* 183:247 */     buf.put(this.m11);
/* 184:248 */     buf.put(this.m12);
/* 185:249 */     buf.put(this.m13);
/* 186:250 */     buf.put(this.m20);
/* 187:251 */     buf.put(this.m21);
/* 188:252 */     buf.put(this.m22);
/* 189:253 */     buf.put(this.m23);
/* 190:254 */     buf.put(this.m30);
/* 191:255 */     buf.put(this.m31);
/* 192:256 */     buf.put(this.m32);
/* 193:257 */     buf.put(this.m33);
/* 194:258 */     return this;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public Matrix storeTranspose(FloatBuffer buf)
/* 198:    */   {
/* 199:267 */     buf.put(this.m00);
/* 200:268 */     buf.put(this.m10);
/* 201:269 */     buf.put(this.m20);
/* 202:270 */     buf.put(this.m30);
/* 203:271 */     buf.put(this.m01);
/* 204:272 */     buf.put(this.m11);
/* 205:273 */     buf.put(this.m21);
/* 206:274 */     buf.put(this.m31);
/* 207:275 */     buf.put(this.m02);
/* 208:276 */     buf.put(this.m12);
/* 209:277 */     buf.put(this.m22);
/* 210:278 */     buf.put(this.m32);
/* 211:279 */     buf.put(this.m03);
/* 212:280 */     buf.put(this.m13);
/* 213:281 */     buf.put(this.m23);
/* 214:282 */     buf.put(this.m33);
/* 215:283 */     return this;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public Matrix store3f(FloatBuffer buf)
/* 219:    */   {
/* 220:292 */     buf.put(this.m00);
/* 221:293 */     buf.put(this.m01);
/* 222:294 */     buf.put(this.m02);
/* 223:295 */     buf.put(this.m10);
/* 224:296 */     buf.put(this.m11);
/* 225:297 */     buf.put(this.m12);
/* 226:298 */     buf.put(this.m20);
/* 227:299 */     buf.put(this.m21);
/* 228:300 */     buf.put(this.m22);
/* 229:301 */     return this;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public static Matrix4f add(Matrix4f left, Matrix4f right, Matrix4f dest)
/* 233:    */   {
/* 234:312 */     if (dest == null) {
/* 235:313 */       dest = new Matrix4f();
/* 236:    */     }
/* 237:315 */     left.m00 += right.m00;
/* 238:316 */     left.m01 += right.m01;
/* 239:317 */     left.m02 += right.m02;
/* 240:318 */     left.m03 += right.m03;
/* 241:319 */     left.m10 += right.m10;
/* 242:320 */     left.m11 += right.m11;
/* 243:321 */     left.m12 += right.m12;
/* 244:322 */     left.m13 += right.m13;
/* 245:323 */     left.m20 += right.m20;
/* 246:324 */     left.m21 += right.m21;
/* 247:325 */     left.m22 += right.m22;
/* 248:326 */     left.m23 += right.m23;
/* 249:327 */     left.m30 += right.m30;
/* 250:328 */     left.m31 += right.m31;
/* 251:329 */     left.m32 += right.m32;
/* 252:330 */     left.m33 += right.m33;
/* 253:    */     
/* 254:332 */     return dest;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public static Matrix4f sub(Matrix4f left, Matrix4f right, Matrix4f dest)
/* 258:    */   {
/* 259:343 */     if (dest == null) {
/* 260:344 */       dest = new Matrix4f();
/* 261:    */     }
/* 262:346 */     left.m00 -= right.m00;
/* 263:347 */     left.m01 -= right.m01;
/* 264:348 */     left.m02 -= right.m02;
/* 265:349 */     left.m03 -= right.m03;
/* 266:350 */     left.m10 -= right.m10;
/* 267:351 */     left.m11 -= right.m11;
/* 268:352 */     left.m12 -= right.m12;
/* 269:353 */     left.m13 -= right.m13;
/* 270:354 */     left.m20 -= right.m20;
/* 271:355 */     left.m21 -= right.m21;
/* 272:356 */     left.m22 -= right.m22;
/* 273:357 */     left.m23 -= right.m23;
/* 274:358 */     left.m30 -= right.m30;
/* 275:359 */     left.m31 -= right.m31;
/* 276:360 */     left.m32 -= right.m32;
/* 277:361 */     left.m33 -= right.m33;
/* 278:    */     
/* 279:363 */     return dest;
/* 280:    */   }
/* 281:    */   
/* 282:    */   public static Matrix4f mul(Matrix4f left, Matrix4f right, Matrix4f dest)
/* 283:    */   {
/* 284:374 */     if (dest == null) {
/* 285:375 */       dest = new Matrix4f();
/* 286:    */     }
/* 287:377 */     float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
/* 288:378 */     float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
/* 289:379 */     float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
/* 290:380 */     float m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
/* 291:381 */     float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
/* 292:382 */     float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
/* 293:383 */     float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
/* 294:384 */     float m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
/* 295:385 */     float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
/* 296:386 */     float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
/* 297:387 */     float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
/* 298:388 */     float m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
/* 299:389 */     float m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
/* 300:390 */     float m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
/* 301:391 */     float m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
/* 302:392 */     float m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;
/* 303:    */     
/* 304:394 */     dest.m00 = m00;
/* 305:395 */     dest.m01 = m01;
/* 306:396 */     dest.m02 = m02;
/* 307:397 */     dest.m03 = m03;
/* 308:398 */     dest.m10 = m10;
/* 309:399 */     dest.m11 = m11;
/* 310:400 */     dest.m12 = m12;
/* 311:401 */     dest.m13 = m13;
/* 312:402 */     dest.m20 = m20;
/* 313:403 */     dest.m21 = m21;
/* 314:404 */     dest.m22 = m22;
/* 315:405 */     dest.m23 = m23;
/* 316:406 */     dest.m30 = m30;
/* 317:407 */     dest.m31 = m31;
/* 318:408 */     dest.m32 = m32;
/* 319:409 */     dest.m33 = m33;
/* 320:    */     
/* 321:411 */     return dest;
/* 322:    */   }
/* 323:    */   
/* 324:    */   public static Vector4f transform(Matrix4f left, Vector4f right, Vector4f dest)
/* 325:    */   {
/* 326:423 */     if (dest == null) {
/* 327:424 */       dest = new Vector4f();
/* 328:    */     }
/* 329:426 */     float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
/* 330:427 */     float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
/* 331:428 */     float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
/* 332:429 */     float w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;
/* 333:    */     
/* 334:431 */     dest.x = x;
/* 335:432 */     dest.y = y;
/* 336:433 */     dest.z = z;
/* 337:434 */     dest.w = w;
/* 338:    */     
/* 339:436 */     return dest;
/* 340:    */   }
/* 341:    */   
/* 342:    */   public Matrix transpose()
/* 343:    */   {
/* 344:444 */     return transpose(this);
/* 345:    */   }
/* 346:    */   
/* 347:    */   public Matrix4f translate(Vector2f vec)
/* 348:    */   {
/* 349:453 */     return translate(vec, this);
/* 350:    */   }
/* 351:    */   
/* 352:    */   public Matrix4f translate(Vector3f vec)
/* 353:    */   {
/* 354:462 */     return translate(vec, this);
/* 355:    */   }
/* 356:    */   
/* 357:    */   public Matrix4f scale(Vector3f vec)
/* 358:    */   {
/* 359:471 */     return scale(vec, this, this);
/* 360:    */   }
/* 361:    */   
/* 362:    */   public static Matrix4f scale(Vector3f vec, Matrix4f src, Matrix4f dest)
/* 363:    */   {
/* 364:482 */     if (dest == null) {
/* 365:483 */       dest = new Matrix4f();
/* 366:    */     }
/* 367:484 */     src.m00 *= vec.x;
/* 368:485 */     src.m01 *= vec.x;
/* 369:486 */     src.m02 *= vec.x;
/* 370:487 */     src.m03 *= vec.x;
/* 371:488 */     src.m10 *= vec.y;
/* 372:489 */     src.m11 *= vec.y;
/* 373:490 */     src.m12 *= vec.y;
/* 374:491 */     src.m13 *= vec.y;
/* 375:492 */     src.m20 *= vec.z;
/* 376:493 */     src.m21 *= vec.z;
/* 377:494 */     src.m22 *= vec.z;
/* 378:495 */     src.m23 *= vec.z;
/* 379:496 */     return dest;
/* 380:    */   }
/* 381:    */   
/* 382:    */   public Matrix4f rotate(float angle, Vector3f axis)
/* 383:    */   {
/* 384:506 */     return rotate(angle, axis, this);
/* 385:    */   }
/* 386:    */   
/* 387:    */   public Matrix4f rotate(float angle, Vector3f axis, Matrix4f dest)
/* 388:    */   {
/* 389:517 */     return rotate(angle, axis, this, dest);
/* 390:    */   }
/* 391:    */   
/* 392:    */   public static Matrix4f rotate(float angle, Vector3f axis, Matrix4f src, Matrix4f dest)
/* 393:    */   {
/* 394:530 */     if (dest == null) {
/* 395:531 */       dest = new Matrix4f();
/* 396:    */     }
/* 397:532 */     float c = (float)Math.cos(angle);
/* 398:533 */     float s = (float)Math.sin(angle);
/* 399:534 */     float oneminusc = 1.0F - c;
/* 400:535 */     float xy = axis.x * axis.y;
/* 401:536 */     float yz = axis.y * axis.z;
/* 402:537 */     float xz = axis.x * axis.z;
/* 403:538 */     float xs = axis.x * s;
/* 404:539 */     float ys = axis.y * s;
/* 405:540 */     float zs = axis.z * s;
/* 406:    */     
/* 407:542 */     float f00 = axis.x * axis.x * oneminusc + c;
/* 408:543 */     float f01 = xy * oneminusc + zs;
/* 409:544 */     float f02 = xz * oneminusc - ys;
/* 410:    */     
/* 411:546 */     float f10 = xy * oneminusc - zs;
/* 412:547 */     float f11 = axis.y * axis.y * oneminusc + c;
/* 413:548 */     float f12 = yz * oneminusc + xs;
/* 414:    */     
/* 415:550 */     float f20 = xz * oneminusc + ys;
/* 416:551 */     float f21 = yz * oneminusc - xs;
/* 417:552 */     float f22 = axis.z * axis.z * oneminusc + c;
/* 418:    */     
/* 419:554 */     float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
/* 420:555 */     float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
/* 421:556 */     float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
/* 422:557 */     float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
/* 423:558 */     float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
/* 424:559 */     float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
/* 425:560 */     float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
/* 426:561 */     float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
/* 427:562 */     dest.m20 = (src.m00 * f20 + src.m10 * f21 + src.m20 * f22);
/* 428:563 */     dest.m21 = (src.m01 * f20 + src.m11 * f21 + src.m21 * f22);
/* 429:564 */     dest.m22 = (src.m02 * f20 + src.m12 * f21 + src.m22 * f22);
/* 430:565 */     dest.m23 = (src.m03 * f20 + src.m13 * f21 + src.m23 * f22);
/* 431:566 */     dest.m00 = t00;
/* 432:567 */     dest.m01 = t01;
/* 433:568 */     dest.m02 = t02;
/* 434:569 */     dest.m03 = t03;
/* 435:570 */     dest.m10 = t10;
/* 436:571 */     dest.m11 = t11;
/* 437:572 */     dest.m12 = t12;
/* 438:573 */     dest.m13 = t13;
/* 439:574 */     return dest;
/* 440:    */   }
/* 441:    */   
/* 442:    */   public Matrix4f translate(Vector3f vec, Matrix4f dest)
/* 443:    */   {
/* 444:584 */     return translate(vec, this, dest);
/* 445:    */   }
/* 446:    */   
/* 447:    */   public static Matrix4f translate(Vector3f vec, Matrix4f src, Matrix4f dest)
/* 448:    */   {
/* 449:595 */     if (dest == null) {
/* 450:596 */       dest = new Matrix4f();
/* 451:    */     }
/* 452:598 */     dest.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
/* 453:599 */     dest.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
/* 454:600 */     dest.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
/* 455:601 */     dest.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;
/* 456:    */     
/* 457:603 */     return dest;
/* 458:    */   }
/* 459:    */   
/* 460:    */   public Matrix4f translate(Vector2f vec, Matrix4f dest)
/* 461:    */   {
/* 462:613 */     return translate(vec, this, dest);
/* 463:    */   }
/* 464:    */   
/* 465:    */   public static Matrix4f translate(Vector2f vec, Matrix4f src, Matrix4f dest)
/* 466:    */   {
/* 467:624 */     if (dest == null) {
/* 468:625 */       dest = new Matrix4f();
/* 469:    */     }
/* 470:627 */     dest.m30 += src.m00 * vec.x + src.m10 * vec.y;
/* 471:628 */     dest.m31 += src.m01 * vec.x + src.m11 * vec.y;
/* 472:629 */     dest.m32 += src.m02 * vec.x + src.m12 * vec.y;
/* 473:630 */     dest.m33 += src.m03 * vec.x + src.m13 * vec.y;
/* 474:    */     
/* 475:632 */     return dest;
/* 476:    */   }
/* 477:    */   
/* 478:    */   public Matrix4f transpose(Matrix4f dest)
/* 479:    */   {
/* 480:641 */     return transpose(this, dest);
/* 481:    */   }
/* 482:    */   
/* 483:    */   public static Matrix4f transpose(Matrix4f src, Matrix4f dest)
/* 484:    */   {
/* 485:651 */     if (dest == null) {
/* 486:652 */       dest = new Matrix4f();
/* 487:    */     }
/* 488:653 */     float m00 = src.m00;
/* 489:654 */     float m01 = src.m10;
/* 490:655 */     float m02 = src.m20;
/* 491:656 */     float m03 = src.m30;
/* 492:657 */     float m10 = src.m01;
/* 493:658 */     float m11 = src.m11;
/* 494:659 */     float m12 = src.m21;
/* 495:660 */     float m13 = src.m31;
/* 496:661 */     float m20 = src.m02;
/* 497:662 */     float m21 = src.m12;
/* 498:663 */     float m22 = src.m22;
/* 499:664 */     float m23 = src.m32;
/* 500:665 */     float m30 = src.m03;
/* 501:666 */     float m31 = src.m13;
/* 502:667 */     float m32 = src.m23;
/* 503:668 */     float m33 = src.m33;
/* 504:    */     
/* 505:670 */     dest.m00 = m00;
/* 506:671 */     dest.m01 = m01;
/* 507:672 */     dest.m02 = m02;
/* 508:673 */     dest.m03 = m03;
/* 509:674 */     dest.m10 = m10;
/* 510:675 */     dest.m11 = m11;
/* 511:676 */     dest.m12 = m12;
/* 512:677 */     dest.m13 = m13;
/* 513:678 */     dest.m20 = m20;
/* 514:679 */     dest.m21 = m21;
/* 515:680 */     dest.m22 = m22;
/* 516:681 */     dest.m23 = m23;
/* 517:682 */     dest.m30 = m30;
/* 518:683 */     dest.m31 = m31;
/* 519:684 */     dest.m32 = m32;
/* 520:685 */     dest.m33 = m33;
/* 521:    */     
/* 522:687 */     return dest;
/* 523:    */   }
/* 524:    */   
/* 525:    */   public float determinant()
/* 526:    */   {
/* 527:694 */     float f = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
/* 528:    */     
/* 529:    */ 
/* 530:    */ 
/* 531:    */ 
/* 532:    */ 
/* 533:700 */     f -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
/* 534:    */     
/* 535:    */ 
/* 536:    */ 
/* 537:    */ 
/* 538:705 */     f += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
/* 539:    */     
/* 540:    */ 
/* 541:    */ 
/* 542:    */ 
/* 543:710 */     f -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
/* 544:    */     
/* 545:    */ 
/* 546:    */ 
/* 547:    */ 
/* 548:715 */     return f;
/* 549:    */   }
/* 550:    */   
/* 551:    */   private static float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22)
/* 552:    */   {
/* 553:727 */     return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
/* 554:    */   }
/* 555:    */   
/* 556:    */   public Matrix invert()
/* 557:    */   {
/* 558:737 */     return invert(this, this);
/* 559:    */   }
/* 560:    */   
/* 561:    */   public static Matrix4f invert(Matrix4f src, Matrix4f dest)
/* 562:    */   {
/* 563:747 */     float determinant = src.determinant();
/* 564:749 */     if (determinant != 0.0F)
/* 565:    */     {
/* 566:756 */       if (dest == null) {
/* 567:757 */         dest = new Matrix4f();
/* 568:    */       }
/* 569:758 */       float determinant_inv = 1.0F / determinant;
/* 570:    */       
/* 571:    */ 
/* 572:761 */       float t00 = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
/* 573:762 */       float t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
/* 574:763 */       float t02 = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
/* 575:764 */       float t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
/* 576:    */       
/* 577:766 */       float t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
/* 578:767 */       float t11 = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
/* 579:768 */       float t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
/* 580:769 */       float t13 = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
/* 581:    */       
/* 582:771 */       float t20 = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
/* 583:772 */       float t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
/* 584:773 */       float t22 = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
/* 585:774 */       float t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
/* 586:    */       
/* 587:776 */       float t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
/* 588:777 */       float t31 = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
/* 589:778 */       float t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
/* 590:779 */       float t33 = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);
/* 591:    */       
/* 592:    */ 
/* 593:782 */       dest.m00 = (t00 * determinant_inv);
/* 594:783 */       dest.m11 = (t11 * determinant_inv);
/* 595:784 */       dest.m22 = (t22 * determinant_inv);
/* 596:785 */       dest.m33 = (t33 * determinant_inv);
/* 597:786 */       dest.m01 = (t10 * determinant_inv);
/* 598:787 */       dest.m10 = (t01 * determinant_inv);
/* 599:788 */       dest.m20 = (t02 * determinant_inv);
/* 600:789 */       dest.m02 = (t20 * determinant_inv);
/* 601:790 */       dest.m12 = (t21 * determinant_inv);
/* 602:791 */       dest.m21 = (t12 * determinant_inv);
/* 603:792 */       dest.m03 = (t30 * determinant_inv);
/* 604:793 */       dest.m30 = (t03 * determinant_inv);
/* 605:794 */       dest.m13 = (t31 * determinant_inv);
/* 606:795 */       dest.m31 = (t13 * determinant_inv);
/* 607:796 */       dest.m32 = (t23 * determinant_inv);
/* 608:797 */       dest.m23 = (t32 * determinant_inv);
/* 609:798 */       return dest;
/* 610:    */     }
/* 611:800 */     return null;
/* 612:    */   }
/* 613:    */   
/* 614:    */   public Matrix negate()
/* 615:    */   {
/* 616:808 */     return negate(this);
/* 617:    */   }
/* 618:    */   
/* 619:    */   public Matrix4f negate(Matrix4f dest)
/* 620:    */   {
/* 621:817 */     return negate(this, dest);
/* 622:    */   }
/* 623:    */   
/* 624:    */   public static Matrix4f negate(Matrix4f src, Matrix4f dest)
/* 625:    */   {
/* 626:827 */     if (dest == null) {
/* 627:828 */       dest = new Matrix4f();
/* 628:    */     }
/* 629:830 */     dest.m00 = (-src.m00);
/* 630:831 */     dest.m01 = (-src.m01);
/* 631:832 */     dest.m02 = (-src.m02);
/* 632:833 */     dest.m03 = (-src.m03);
/* 633:834 */     dest.m10 = (-src.m10);
/* 634:835 */     dest.m11 = (-src.m11);
/* 635:836 */     dest.m12 = (-src.m12);
/* 636:837 */     dest.m13 = (-src.m13);
/* 637:838 */     dest.m20 = (-src.m20);
/* 638:839 */     dest.m21 = (-src.m21);
/* 639:840 */     dest.m22 = (-src.m22);
/* 640:841 */     dest.m23 = (-src.m23);
/* 641:842 */     dest.m30 = (-src.m30);
/* 642:843 */     dest.m31 = (-src.m31);
/* 643:844 */     dest.m32 = (-src.m32);
/* 644:845 */     dest.m33 = (-src.m33);
/* 645:    */     
/* 646:847 */     return dest;
/* 647:    */   }
/* 648:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Matrix4f
 * JD-Core Version:    0.7.0.1
 */