/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.nio.FloatBuffer;
/*   5:    */ 
/*   6:    */ public class Vector3f
/*   7:    */   extends Vector
/*   8:    */   implements Serializable, ReadableVector3f, WritableVector3f
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 1L;
/*  11:    */   public float x;
/*  12:    */   public float y;
/*  13:    */   public float z;
/*  14:    */   
/*  15:    */   public Vector3f() {}
/*  16:    */   
/*  17:    */   public Vector3f(ReadableVector3f src)
/*  18:    */   {
/*  19: 65 */     set(src);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Vector3f(float x, float y, float z)
/*  23:    */   {
/*  24: 72 */     set(x, y, z);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void set(float x, float y)
/*  28:    */   {
/*  29: 79 */     this.x = x;
/*  30: 80 */     this.y = y;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void set(float x, float y, float z)
/*  34:    */   {
/*  35: 87 */     this.x = x;
/*  36: 88 */     this.y = y;
/*  37: 89 */     this.z = z;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Vector3f set(ReadableVector3f src)
/*  41:    */   {
/*  42: 98 */     this.x = src.getX();
/*  43: 99 */     this.y = src.getY();
/*  44:100 */     this.z = src.getZ();
/*  45:101 */     return this;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public float lengthSquared()
/*  49:    */   {
/*  50:108 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Vector3f translate(float x, float y, float z)
/*  54:    */   {
/*  55:118 */     this.x += x;
/*  56:119 */     this.y += y;
/*  57:120 */     this.z += z;
/*  58:121 */     return this;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest)
/*  62:    */   {
/*  63:133 */     if (dest == null) {
/*  64:134 */       return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
/*  65:    */     }
/*  66:136 */     dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
/*  67:137 */     return dest;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest)
/*  71:    */   {
/*  72:150 */     if (dest == null) {
/*  73:151 */       return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
/*  74:    */     }
/*  75:153 */     dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
/*  76:154 */     return dest;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest)
/*  80:    */   {
/*  81:172 */     if (dest == null) {
/*  82:173 */       dest = new Vector3f();
/*  83:    */     }
/*  84:175 */     dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
/*  85:    */     
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:181 */     return dest;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public Vector negate()
/*  94:    */   {
/*  95:191 */     this.x = (-this.x);
/*  96:192 */     this.y = (-this.y);
/*  97:193 */     this.z = (-this.z);
/*  98:194 */     return this;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public Vector3f negate(Vector3f dest)
/* 102:    */   {
/* 103:203 */     if (dest == null) {
/* 104:204 */       dest = new Vector3f();
/* 105:    */     }
/* 106:205 */     dest.x = (-this.x);
/* 107:206 */     dest.y = (-this.y);
/* 108:207 */     dest.z = (-this.z);
/* 109:208 */     return dest;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public Vector3f normalise(Vector3f dest)
/* 113:    */   {
/* 114:218 */     float l = length();
/* 115:220 */     if (dest == null) {
/* 116:221 */       dest = new Vector3f(this.x / l, this.y / l, this.z / l);
/* 117:    */     } else {
/* 118:223 */       dest.set(this.x / l, this.y / l, this.z / l);
/* 119:    */     }
/* 120:225 */     return dest;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static float dot(Vector3f left, Vector3f right)
/* 124:    */   {
/* 125:236 */     return left.x * right.x + left.y * right.y + left.z * right.z;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public static float angle(Vector3f a, Vector3f b)
/* 129:    */   {
/* 130:246 */     float dls = dot(a, b) / (a.length() * b.length());
/* 131:247 */     if (dls < -1.0F) {
/* 132:248 */       dls = -1.0F;
/* 133:249 */     } else if (dls > 1.0F) {
/* 134:250 */       dls = 1.0F;
/* 135:    */     }
/* 136:251 */     return (float)Math.acos(dls);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public Vector load(FloatBuffer buf)
/* 140:    */   {
/* 141:258 */     this.x = buf.get();
/* 142:259 */     this.y = buf.get();
/* 143:260 */     this.z = buf.get();
/* 144:261 */     return this;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public Vector scale(float scale)
/* 148:    */   {
/* 149:269 */     this.x *= scale;
/* 150:270 */     this.y *= scale;
/* 151:271 */     this.z *= scale;
/* 152:    */     
/* 153:273 */     return this;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public Vector store(FloatBuffer buf)
/* 157:    */   {
/* 158:282 */     buf.put(this.x);
/* 159:283 */     buf.put(this.y);
/* 160:284 */     buf.put(this.z);
/* 161:    */     
/* 162:286 */     return this;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public String toString()
/* 166:    */   {
/* 167:293 */     StringBuilder sb = new StringBuilder(64);
/* 168:    */     
/* 169:295 */     sb.append("Vector3f[");
/* 170:296 */     sb.append(this.x);
/* 171:297 */     sb.append(", ");
/* 172:298 */     sb.append(this.y);
/* 173:299 */     sb.append(", ");
/* 174:300 */     sb.append(this.z);
/* 175:301 */     sb.append(']');
/* 176:302 */     return sb.toString();
/* 177:    */   }
/* 178:    */   
/* 179:    */   public final float getX()
/* 180:    */   {
/* 181:309 */     return this.x;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public final float getY()
/* 185:    */   {
/* 186:316 */     return this.y;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public final void setX(float x)
/* 190:    */   {
/* 191:324 */     this.x = x;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public final void setY(float y)
/* 195:    */   {
/* 196:332 */     this.y = y;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setZ(float z)
/* 200:    */   {
/* 201:340 */     this.z = z;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public float getZ()
/* 205:    */   {
/* 206:347 */     return this.z;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public boolean equals(Object obj)
/* 210:    */   {
/* 211:351 */     if (this == obj) {
/* 212:351 */       return true;
/* 213:    */     }
/* 214:352 */     if (obj == null) {
/* 215:352 */       return false;
/* 216:    */     }
/* 217:353 */     if (getClass() != obj.getClass()) {
/* 218:353 */       return false;
/* 219:    */     }
/* 220:354 */     Vector3f other = (Vector3f)obj;
/* 221:356 */     if ((this.x == other.x) && (this.y == other.y) && (this.z == other.z)) {
/* 222:356 */       return true;
/* 223:    */     }
/* 224:358 */     return false;
/* 225:    */   }
/* 226:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Vector3f
 * JD-Core Version:    0.7.0.1
 */