/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.nio.FloatBuffer;
/*   5:    */ 
/*   6:    */ public class Vector4f
/*   7:    */   extends Vector
/*   8:    */   implements Serializable, ReadableVector4f, WritableVector4f
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 1L;
/*  11:    */   public float x;
/*  12:    */   public float y;
/*  13:    */   public float z;
/*  14:    */   public float w;
/*  15:    */   
/*  16:    */   public Vector4f() {}
/*  17:    */   
/*  18:    */   public Vector4f(ReadableVector4f src)
/*  19:    */   {
/*  20: 65 */     set(src);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Vector4f(float x, float y, float z, float w)
/*  24:    */   {
/*  25: 72 */     set(x, y, z, w);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void set(float x, float y)
/*  29:    */   {
/*  30: 79 */     this.x = x;
/*  31: 80 */     this.y = y;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void set(float x, float y, float z)
/*  35:    */   {
/*  36: 87 */     this.x = x;
/*  37: 88 */     this.y = y;
/*  38: 89 */     this.z = z;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void set(float x, float y, float z, float w)
/*  42:    */   {
/*  43: 96 */     this.x = x;
/*  44: 97 */     this.y = y;
/*  45: 98 */     this.z = z;
/*  46: 99 */     this.w = w;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Vector4f set(ReadableVector4f src)
/*  50:    */   {
/*  51:108 */     this.x = src.getX();
/*  52:109 */     this.y = src.getY();
/*  53:110 */     this.z = src.getZ();
/*  54:111 */     this.w = src.getW();
/*  55:112 */     return this;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public float lengthSquared()
/*  59:    */   {
/*  60:119 */     return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Vector4f translate(float x, float y, float z, float w)
/*  64:    */   {
/*  65:129 */     this.x += x;
/*  66:130 */     this.y += y;
/*  67:131 */     this.z += z;
/*  68:132 */     this.w += w;
/*  69:133 */     return this;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static Vector4f add(Vector4f left, Vector4f right, Vector4f dest)
/*  73:    */   {
/*  74:145 */     if (dest == null) {
/*  75:146 */       return new Vector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
/*  76:    */     }
/*  77:148 */     dest.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
/*  78:149 */     return dest;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static Vector4f sub(Vector4f left, Vector4f right, Vector4f dest)
/*  82:    */   {
/*  83:162 */     if (dest == null) {
/*  84:163 */       return new Vector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
/*  85:    */     }
/*  86:165 */     dest.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
/*  87:166 */     return dest;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Vector negate()
/*  91:    */   {
/*  92:176 */     this.x = (-this.x);
/*  93:177 */     this.y = (-this.y);
/*  94:178 */     this.z = (-this.z);
/*  95:179 */     this.w = (-this.w);
/*  96:180 */     return this;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Vector4f negate(Vector4f dest)
/* 100:    */   {
/* 101:189 */     if (dest == null) {
/* 102:190 */       dest = new Vector4f();
/* 103:    */     }
/* 104:191 */     dest.x = (-this.x);
/* 105:192 */     dest.y = (-this.y);
/* 106:193 */     dest.z = (-this.z);
/* 107:194 */     dest.w = (-this.w);
/* 108:195 */     return dest;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public Vector4f normalise(Vector4f dest)
/* 112:    */   {
/* 113:205 */     float l = length();
/* 114:207 */     if (dest == null) {
/* 115:208 */       dest = new Vector4f(this.x / l, this.y / l, this.z / l, this.w / l);
/* 116:    */     } else {
/* 117:210 */       dest.set(this.x / l, this.y / l, this.z / l, this.w / l);
/* 118:    */     }
/* 119:212 */     return dest;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static float dot(Vector4f left, Vector4f right)
/* 123:    */   {
/* 124:223 */     return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static float angle(Vector4f a, Vector4f b)
/* 128:    */   {
/* 129:233 */     float dls = dot(a, b) / (a.length() * b.length());
/* 130:234 */     if (dls < -1.0F) {
/* 131:235 */       dls = -1.0F;
/* 132:236 */     } else if (dls > 1.0F) {
/* 133:237 */       dls = 1.0F;
/* 134:    */     }
/* 135:238 */     return (float)Math.acos(dls);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public Vector load(FloatBuffer buf)
/* 139:    */   {
/* 140:245 */     this.x = buf.get();
/* 141:246 */     this.y = buf.get();
/* 142:247 */     this.z = buf.get();
/* 143:248 */     this.w = buf.get();
/* 144:249 */     return this;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public Vector scale(float scale)
/* 148:    */   {
/* 149:256 */     this.x *= scale;
/* 150:257 */     this.y *= scale;
/* 151:258 */     this.z *= scale;
/* 152:259 */     this.w *= scale;
/* 153:260 */     return this;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public Vector store(FloatBuffer buf)
/* 157:    */   {
/* 158:268 */     buf.put(this.x);
/* 159:269 */     buf.put(this.y);
/* 160:270 */     buf.put(this.z);
/* 161:271 */     buf.put(this.w);
/* 162:    */     
/* 163:273 */     return this;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public String toString()
/* 167:    */   {
/* 168:277 */     return "Vector4f: " + this.x + " " + this.y + " " + this.z + " " + this.w;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public final float getX()
/* 172:    */   {
/* 173:284 */     return this.x;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public final float getY()
/* 177:    */   {
/* 178:291 */     return this.y;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public final void setX(float x)
/* 182:    */   {
/* 183:299 */     this.x = x;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public final void setY(float y)
/* 187:    */   {
/* 188:307 */     this.y = y;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public void setZ(float z)
/* 192:    */   {
/* 193:315 */     this.z = z;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public float getZ()
/* 197:    */   {
/* 198:323 */     return this.z;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setW(float w)
/* 202:    */   {
/* 203:331 */     this.w = w;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public float getW()
/* 207:    */   {
/* 208:338 */     return this.w;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public boolean equals(Object obj)
/* 212:    */   {
/* 213:342 */     if (this == obj) {
/* 214:342 */       return true;
/* 215:    */     }
/* 216:343 */     if (obj == null) {
/* 217:343 */       return false;
/* 218:    */     }
/* 219:344 */     if (getClass() != obj.getClass()) {
/* 220:344 */       return false;
/* 221:    */     }
/* 222:345 */     Vector4f other = (Vector4f)obj;
/* 223:347 */     if ((this.x == other.x) && (this.y == other.y) && (this.z == other.z) && (this.w == other.w)) {
/* 224:347 */       return true;
/* 225:    */     }
/* 226:349 */     return false;
/* 227:    */   }
/* 228:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Vector4f
 * JD-Core Version:    0.7.0.1
 */