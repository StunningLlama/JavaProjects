/*   1:    */ package org.lwjgl.util.vector;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.nio.FloatBuffer;
/*   5:    */ 
/*   6:    */ public class Vector2f
/*   7:    */   extends Vector
/*   8:    */   implements Serializable, ReadableVector2f, WritableVector2f
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 1L;
/*  11:    */   public float x;
/*  12:    */   public float y;
/*  13:    */   
/*  14:    */   public Vector2f() {}
/*  15:    */   
/*  16:    */   public Vector2f(ReadableVector2f src)
/*  17:    */   {
/*  18: 65 */     set(src);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Vector2f(float x, float y)
/*  22:    */   {
/*  23: 72 */     set(x, y);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void set(float x, float y)
/*  27:    */   {
/*  28: 79 */     this.x = x;
/*  29: 80 */     this.y = y;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Vector2f set(ReadableVector2f src)
/*  33:    */   {
/*  34: 89 */     this.x = src.getX();
/*  35: 90 */     this.y = src.getY();
/*  36: 91 */     return this;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public float lengthSquared()
/*  40:    */   {
/*  41: 98 */     return this.x * this.x + this.y * this.y;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Vector2f translate(float x, float y)
/*  45:    */   {
/*  46:108 */     this.x += x;
/*  47:109 */     this.y += y;
/*  48:110 */     return this;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Vector negate()
/*  52:    */   {
/*  53:118 */     this.x = (-this.x);
/*  54:119 */     this.y = (-this.y);
/*  55:120 */     return this;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Vector2f negate(Vector2f dest)
/*  59:    */   {
/*  60:129 */     if (dest == null) {
/*  61:130 */       dest = new Vector2f();
/*  62:    */     }
/*  63:131 */     dest.x = (-this.x);
/*  64:132 */     dest.y = (-this.y);
/*  65:133 */     return dest;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Vector2f normalise(Vector2f dest)
/*  69:    */   {
/*  70:143 */     float l = length();
/*  71:145 */     if (dest == null) {
/*  72:146 */       dest = new Vector2f(this.x / l, this.y / l);
/*  73:    */     } else {
/*  74:148 */       dest.set(this.x / l, this.y / l);
/*  75:    */     }
/*  76:150 */     return dest;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static float dot(Vector2f left, Vector2f right)
/*  80:    */   {
/*  81:161 */     return left.x * right.x + left.y * right.y;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static float angle(Vector2f a, Vector2f b)
/*  85:    */   {
/*  86:173 */     float dls = dot(a, b) / (a.length() * b.length());
/*  87:174 */     if (dls < -1.0F) {
/*  88:175 */       dls = -1.0F;
/*  89:176 */     } else if (dls > 1.0F) {
/*  90:177 */       dls = 1.0F;
/*  91:    */     }
/*  92:178 */     return (float)Math.acos(dls);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static Vector2f add(Vector2f left, Vector2f right, Vector2f dest)
/*  96:    */   {
/*  97:190 */     if (dest == null) {
/*  98:191 */       return new Vector2f(left.x + right.x, left.y + right.y);
/*  99:    */     }
/* 100:193 */     dest.set(left.x + right.x, left.y + right.y);
/* 101:194 */     return dest;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static Vector2f sub(Vector2f left, Vector2f right, Vector2f dest)
/* 105:    */   {
/* 106:207 */     if (dest == null) {
/* 107:208 */       return new Vector2f(left.x - right.x, left.y - right.y);
/* 108:    */     }
/* 109:210 */     dest.set(left.x - right.x, left.y - right.y);
/* 110:211 */     return dest;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public Vector store(FloatBuffer buf)
/* 114:    */   {
/* 115:221 */     buf.put(this.x);
/* 116:222 */     buf.put(this.y);
/* 117:223 */     return this;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Vector load(FloatBuffer buf)
/* 121:    */   {
/* 122:232 */     this.x = buf.get();
/* 123:233 */     this.y = buf.get();
/* 124:234 */     return this;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public Vector scale(float scale)
/* 128:    */   {
/* 129:242 */     this.x *= scale;
/* 130:243 */     this.y *= scale;
/* 131:    */     
/* 132:245 */     return this;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public String toString()
/* 136:    */   {
/* 137:252 */     StringBuilder sb = new StringBuilder(64);
/* 138:    */     
/* 139:254 */     sb.append("Vector2f[");
/* 140:255 */     sb.append(this.x);
/* 141:256 */     sb.append(", ");
/* 142:257 */     sb.append(this.y);
/* 143:258 */     sb.append(']');
/* 144:259 */     return sb.toString();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public final float getX()
/* 148:    */   {
/* 149:266 */     return this.x;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public final float getY()
/* 153:    */   {
/* 154:273 */     return this.y;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public final void setX(float x)
/* 158:    */   {
/* 159:281 */     this.x = x;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public final void setY(float y)
/* 163:    */   {
/* 164:289 */     this.y = y;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public boolean equals(Object obj)
/* 168:    */   {
/* 169:293 */     if (this == obj) {
/* 170:293 */       return true;
/* 171:    */     }
/* 172:294 */     if (obj == null) {
/* 173:294 */       return false;
/* 174:    */     }
/* 175:295 */     if (getClass() != obj.getClass()) {
/* 176:295 */       return false;
/* 177:    */     }
/* 178:296 */     Vector2f other = (Vector2f)obj;
/* 179:298 */     if ((this.x == other.x) && (this.y == other.y)) {
/* 180:298 */       return true;
/* 181:    */     }
/* 182:300 */     return false;
/* 183:    */   }
/* 184:    */ }


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.Vector2f
 * JD-Core Version:    0.7.0.1
 */