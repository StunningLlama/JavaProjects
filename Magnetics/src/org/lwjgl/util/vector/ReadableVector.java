package org.lwjgl.util.vector;

import java.nio.FloatBuffer;

public abstract interface ReadableVector
{
  public abstract float length();
  
  public abstract float lengthSquared();
  
  public abstract Vector store(FloatBuffer paramFloatBuffer);
}


/* Location:           F:\lwjgl-2.9.1\jar\lwjgl_util.jar
 * Qualified Name:     org.lwjgl.util.vector.ReadableVector
 * JD-Core Version:    0.7.0.1
 */