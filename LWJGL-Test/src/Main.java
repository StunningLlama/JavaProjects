import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
/*import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;*/

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Main
{
	   // Entry point for the application
    public static void main(String[] args) {
        new Main();
    }
     
    // Setup variables
    private final String WINDOW_TITLE = "The Quad: Moving";
    private final int WIDTH = 1600;
    private final int HEIGHT = 900;
    private final double PI = 3.14159265358979323846;
    // Quad variables
    private int vaoId = 0;
    private int vboId = 0;
    public static float[] vertices = null;
    public static float[] colors = null;
    // Shader variables
    private int pId = 0;
    // Texture variables
    private int[] texIds = new int[] {0, 0};
    // Moving variables
    private int projectionMatrixLocation = 0;
    private int viewMatrixLocation = 0;
    private int modelMatrixLocation = 0;
    private Matrix4f projectionMatrix = null;
    private Matrix4f viewMatrix = null;
    private Matrix4f modelMatrix = null;
    private FloatBuffer matrix44Buffer = null;
    static int vertexCount;
    Camera c;
    ByteBuffer pixels;
    //private static UnicodeFont font;
    public Main() {
        // Initialize OpenGL (Display)
        this.setupOpenGL();
        this.setupQuad();
        this.setupShaders();
        this.setupTextures();
        this.setupMatrices();
        
        Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
       // font = new UnicodeFont(awtFont);
        
        
       c = new Camera(0.0f, 0.0f, 0.0f, this);
        c.setPitch(0.0f);
        c.setYaw(0.0f);
        c.setRoll(0.0f);
        while (!Display.isCloseRequested()) {
            // Do a single loop (logic/render)
            //c.lookThrough();
            this.loopCycle();
            // Force a maximum FPS of about 60
            Display.sync(60);
            // Let the CPU synchronize with the GPU if GPU is tagging behind
            Display.update();
        }
         
        // Destroy OpenGL (Display)
        this.destroyOpenGL();
    }
 

    /** Buffers hold sound data. */
    IntBuffer buffer = BufferUtils.createIntBuffer(1);
   
    /** Sources are points emitting sound. */
    IntBuffer source = BufferUtils.createIntBuffer(1);
	/** Position of the source sound. */
	FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
	 
	/** Velocity of the source sound. */
	FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
	 
	/** Position of the listener. */
	FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
	 
	/** Velocity of the listener. */
	FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
	 
	/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	FloatBuffer listenerOri =
	    BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });
	int loadALData() {
	    // Load wav data into a buffer.
	    AL10.alGenBuffers(buffer);
	 
	    if(AL10.alGetError() != AL10.AL_NO_ERROR)
	      return AL10.AL_FALSE;
	 
	    //Loads the wave file from your file system
	    BufferedInputStream fin = null;
	    try {
	      fin = new BufferedInputStream(new FileInputStream("F:/Shader/sound.wav"));
	    } catch (java.io.FileNotFoundException ex) {
	      ex.printStackTrace();
	      return AL10.AL_FALSE;
	    }
	    WaveData waveFile = WaveData.create(fin);
	    try {
	      fin.close();
	    } catch (java.io.IOException ex) {
	    }
	 
	    //Loads the wave file from this class's package in your classpath

	    AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
 
	    waveFile.dispose();
	 
	    // Bind the buffer with the source.
	    AL10.alGenSources(source);
	 
	    if (AL10.alGetError() != AL10.AL_NO_ERROR)
	      return AL10.AL_FALSE;
	 
	    AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
	    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
	    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
	    //AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
	    //AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );
	 
	    // Do another error check and return.
	    if (AL10.alGetError() == AL10.AL_NO_ERROR)
	      return AL10.AL_TRUE;
	 
	    return AL10.AL_FALSE;
	  }
    
    void setListenerValues() {
    	// AL10.alListener(AL10.AL_POSITION,    listenerPos);
    	//  AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
    	 // AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
    	}
    
    private void setupMatrices() {
        // Setup projection matrix
        projectionMatrix = new Matrix4f();
        float fieldOfView = 80f;
        float aspectRatio = (float)WIDTH / (float)HEIGHT;
        float near_plane = 0.1f;
        float far_plane = 100f;
         
        float y_scale = this.coTangent(this.degreesToRadians(fieldOfView / 2f));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far_plane - near_plane;
         
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
                projectionMatrix.m33 = 0;
         
        // Setup view matrix
        viewMatrix = new Matrix4f();
         
        // Setup model matrix
        modelMatrix = new Matrix4f();
         
        // Create a FloatBuffer with the proper size to store our matrices later
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
    }
 
    private void setupTextures() {
        texIds[0] = this.loadPNGTexture("F:/Shader/a.png", GL13.GL_TEXTURE0);
        texIds[1] = this.loadPNGTexture("F:/Shader/b.png", GL13.GL_TEXTURE0);
         
        this.exitOnGLError("setupTexture");
    }
    private void setupOpenGL() {
        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
             
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create(pixelFormat, contextAtrributes);
             
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
         

             
        this.loadALData();
        setListenerValues();
        
        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
         
        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        this.exitOnGLError("setupOpenGL");
    }
    public static int[] indices;
    int vbocId;
    private void setupQuad() {

    	// Vertices, the order is not important. XYZW instead of XYZ
        ModelDemo.setUpDisplayLists();
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
        FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorsBuffer.put(colors);
        colorsBuffer.flip();
        // OpenGL expects to draw vertices in counter clockwise order by default
        // Create a new Vertex Array Object in memory and select it (bind)
     // Create a new Vertex Array Object in memory and select it (bind)
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
         
        // Create a new Vertex Buffer Object in memory and select it (bind) - VERTICES
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        // Create a new VBO for the indices and select it (bind) - COLORS
        vbocId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
         
        // Create a new VBO for the indices and select it (bind) - INDICES
         
        // Set the default quad rotation, scale and position values
         
        this.exitOnGLError("setupQuad");
    }
     
    private void setupShaders() {       
        // Load the vertex shader
        int vsId = this.loadShader("F:/Shader/color.vsh", GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        int fsId = this.loadShader("F:/Shader/color.fsh", GL20.GL_FRAGMENT_SHADER);
         
        // Create a new shader program that links both shaders
        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);
 
        // Position information will be attribute 0
        GL20.glBindAttribLocation(pId, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(pId, 1, "in_Color");
        // Textute information will be attribute 2
        GL20.glBindAttribLocation(pId, 2, "in_TextureCoord");
 
        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);
 
        // Get matrices uniform locations
        projectionMatrixLocation = GL20.glGetUniformLocation(pId,"projectionMatrix");
        viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
        modelMatrixLocation = GL20.glGetUniformLocation(pId, "modelMatrix");
 
        this.exitOnGLError("setupShaders");
    }
     
    private void logicCycle() {
        //-- Input processing
        //-- Update matrices
        // Reset view and model matrices
        c.respondToKey();
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
         
        // Translate camera
        // Scale, translate and rotate model
        //Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
        //Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
        Matrix4f.rotate(this.degreesToRadians(c.rot.z), new Vector3f(0, 0, 1), 
                viewMatrix, viewMatrix);
        Matrix4f.rotate(this.degreesToRadians(c.rot.y), new Vector3f(1, 0, 0), 
       		 viewMatrix, viewMatrix);
        Matrix4f.rotate(this.degreesToRadians(c.rot.x), new Vector3f(0, 1, 0), 
          		 viewMatrix, viewMatrix);
        Matrix4f.translate(c.pos, viewMatrix, viewMatrix);
         
        // Upload matrices to the uniform variables
        GL20.glUseProgram(pId);
         
        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
         
        GL20.glUseProgram(0);
        
        this.exitOnGLError("logicCycle");
    }
     
    private void renderCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        
        GL20.glUseProgram(pId);
        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        

        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
         
        this.exitOnGLError("renderCycle");
    }
     
    private void loopCycle() {
        // Update logic
        this.logicCycle();
        // Update rendered frame
        this.renderCycle();
         
        this.exitOnGLError("loopCycle");
    }
     
    private void destroyOpenGL() {  
        // Delete the texturevoid killALData() {
    	  AL10.alDeleteSources(source);
    	  AL10.alDeleteBuffers(buffer);
        GL11.glDeleteTextures(texIds[0]);
        GL11.glDeleteTextures(texIds[1]);
         
        // Delete the shaders
        GL20.glUseProgram(0);
        GL20.glDeleteProgram(pId);
         
        // Select the VAO
        GL30.glBindVertexArray(vaoId);
         
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
         
        // Delete the vertex VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboId);
         
        // Delete the index VBO
         
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
         
        this.exitOnGLError("destroyOpenGL");
         
        Display.destroy();
    }
     
    private int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;
         
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
         
        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
         
        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
         
        this.exitOnGLError("loadShader");
         
        return shaderID;
    }
     
    private int loadPNGTexture(String filename, int textureUnit) {
        ByteBuffer buf = null;
        int tWidth = 0;
        int tHeight = 0;
         
        try {
            // Open the PNG file as an InputStream
            InputStream in = new FileInputStream(filename);
            // Link the PNG decoder to this stream
            PNGDecoder decoder = new PNGDecoder(in);
             
            // Get the width and height of the texture
            tWidth = decoder.getWidth();
            tHeight = decoder.getHeight();
             
             
            // Decode the PNG file in a ByteBuffer
            buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
            buf.flip();
             
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
         
        // Create a new texture object in memory and bind it
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
         
        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
         
        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0, 
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
         
        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
         
        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                GL11.GL_LINEAR_MIPMAP_LINEAR);
         
        this.exitOnGLError("loadPNGTexture");
         
        return texId;
    }
     
    private float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }
     
    private float degreesToRadians(float degrees) {
        return degrees * (float)(PI / 180d);
    }
     
    private void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
         
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
             
            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }
}

class VertexData {
    // Vertex data
    private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
    private float[] rgba = new float[] {1f, 1f, 1f, 1f};
    private float[] st = new float[] {0f, 0f};
     
    // The amount of bytes an element has
    public static final int elementBytes = 4;
     
    // Elements per parameter
    public static final int positionElementCount = 4;
    public static final int colorElementCount = 4;
    public static final int textureElementCount = 2;
     
    // Bytes per parameter
    public static final int positionBytesCount = positionElementCount * elementBytes;
    public static final int colorByteCount = colorElementCount * elementBytes;
    public static final int textureByteCount = textureElementCount * elementBytes;
     
    // Byte offsets per parameter
    public static final int positionByteOffset = 0;
    public static final int colorByteOffset = positionByteOffset + positionBytesCount;
    public static final int textureByteOffset = colorByteOffset + colorByteCount;
     
    // The amount of elements that a vertex has
    public static final int elementCount = positionElementCount + 
            colorElementCount + textureElementCount;    
    // The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
    public static final int stride = positionBytesCount + colorByteCount + 
            textureByteCount;
     
    // Setters
    public void setXYZ(float x, float y, float z) {
        this.setXYZW(x, y, z, 1f);
    }
     
    public void setRGB(float r, float g, float b) {
        this.setRGBA(r, g, b, 1f);
    }
     
    public void setST(float s, float t) {
        this.st = new float[] {s, t};
    }
     
    public void setXYZW(float x, float y, float z, float w) {
        this.xyzw = new float[] {x, y, z, w};
    }
     
    public void setRGBA(float r, float g, float b, float a) {
        this.rgba = new float[] {r, g, b, 1f};
    }
     
    // Getters  
    public float[] getElements() {
        float[] out = new float[VertexData.elementCount];
        int i = 0;
         
        // Insert XYZW elements
        out[i++] = this.xyzw[0];
        out[i++] = this.xyzw[1];
        out[i++] = this.xyzw[2];
        out[i++] = this.xyzw[3];
        // Insert RGBA elements
        out[i++] = this.rgba[0];
        out[i++] = this.rgba[1];
        out[i++] = this.rgba[2];
        out[i++] = this.rgba[3];
        // Insert ST elements
        out[i++] = this.st[0];
        out[i++] = this.st[1];
         
        return out;
    }
     
    public float[] getXYZW() {
        return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3]};
    }
     
    public float[] getXYZ() {
        return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2]};
    }
     
    public float[] getRGBA() {
        return new float[] {this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3]};
    }
     
    public float[] getRGB() {
        return new float[] {this.rgba[0], this.rgba[1], this.rgba[2]};
    }
     
    public float[] getST() {
        return new float[] {this.st[0], this.st[1]};
    }
}

class Camera
{
	Vector3f pos;
	Vector3f rot;
	Vector3f vel;
	Main m;
	
	public Camera(float x, float y, float z, Main a)
	{
		pos = new Vector3f(x, y, z);
		rot = new Vector3f(0f, 0f ,0f);
		vel = new Vector3f(0f, 0f ,0f);
		m = a;
		Mouse.setGrabbed(true);
	}
	
	public void setXYZ(float x, float y, float z)
	{
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}

	public void setYaw(float x)	{rot.x = x;}
	public void setPitch(float y) { rot.y = y; }
	public void setRoll(float z) {rot.z = z;}
	public void incYaw(float x)	{rot.x += x;}
	public void incPitch(float y) {rot.y += y;}
	public void incRoll(float z) {rot.z += z;}
	
	public static float UPS = 0.005f;
	public static float mouseSensitivity = 0.1f;
	public static float gravity = -0.002f;
	public static float ground = 0.5f;
	
	private float clamp(float min, float val, float max)
	{
		if (val < min) return min;
		if (val > max) return max;
		return val;
	}
	
	public void VelTick()
	{
		pos.x += vel.x;
		pos.z += vel.z;
		if (!inAir || flying)
		{
			vel.x /= 1.2f;
			vel.z /= 1.2f;
		}
		pos.y += vel.y;
		if (!flying)
			vel.y -= gravity;
		else
			vel.y /= 1.2;
		if (pos.y > ground)
		{
			inAir = false;
			pos.y = ground;
			vel.y = 0f;
		}
		else if (pos.y < ground)
		{
			inAir = true;
		}
	}
	
	boolean inAir = false;
	boolean flying = false;
	
	float[] xyzprev = {0f,0f,0f};
	public static float ROOT_TWO_OVER_TWO=0.707106781f;//18654752440084436210485f
	public void respondToKey()
	{
		VelTick();
		int dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        int dy = Mouse.getDY();
 
        //controll camera yaw from x movement fromt the mouse
        this.incYaw(dx * mouseSensitivity);
        if (rot.x < -180f) rot.x = 180 + (rot.x + 180);
        if (rot.x > 180f) rot.x = -180 + (rot.x - 180);
        //controll camera pitch from y movement fromt the mouse
        this.setPitch(clamp(-90, rot.y + dy * -mouseSensitivity, 90));
        
        
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
        	this.incRoll(0.5f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E))//move backwards
        {
        	this.incRoll(-0.5f);
        }
       // this.setRoll(rot.x*rot.y/90f);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
        	if (Mouse.isGrabbed())
        		Mouse.setGrabbed(false);
        } else if (!Mouse.isGrabbed()) Mouse.setGrabbed(true);
        float AirMul = 1.0f;
        float PotatoMul = 1.0f;
        if ((Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_S))&&(Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_D)))
        	PotatoMul = ROOT_TWO_OVER_TWO;
        if (inAir) AirMul = 0.1f;
        if (flying)
        	AirMul = 3.0f;
        if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
        {
			//AL10.alSourcePlay(m.source.get(0));
			vel.x -= UPS * AirMul * PotatoMul * (float)Math.sin(Math.toRadians(rot.x));
			vel.z += UPS * AirMul * PotatoMul * (float)Math.cos(Math.toRadians(rot.x));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
        {
        	vel.x += UPS * AirMul * PotatoMul * (float)Math.sin(Math.toRadians(rot.x));
        	vel.z -= UPS * AirMul * PotatoMul * (float)Math.cos(Math.toRadians(rot.x));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
        {
        	vel.x -= UPS * AirMul * PotatoMul * (float)Math.sin(Math.toRadians(rot.x-90));
        	vel.z += UPS * AirMul * PotatoMul * (float)Math.cos(Math.toRadians(rot.x-90));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right
        {
        	vel.x -= UPS * AirMul * PotatoMul * (float)Math.sin(Math.toRadians(rot.x+90));
        	vel.z += UPS * AirMul * PotatoMul * (float)Math.cos(Math.toRadians(rot.x+90));
        }
        if (flying && Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
        	vel.y -= 0.7f;
        }
        if (flying && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
        	vel.y += 0.7f;
        }
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.getEventKeyState() && !inAir && !flying)
            {
            	vel.y = -0.03f;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_F && Keyboard.getEventKeyState())
            {
            	flying = !flying;
            }
        }
        vel.x = clamp(-1.0f, vel.x, 1.0f);
        vel.z = clamp(-1.0f, vel.z, 1.0f);
        if (flying)
            vel.y = clamp(-0.1f, vel.y, 0.1f);
        
        if (((pos.x-xyzprev[0])*(pos.x-xyzprev[0])+
        		(pos.y-xyzprev[1])*(pos.y-xyzprev[1])+
        		(pos.z-xyzprev[2])*(pos.z-xyzprev[2])
        		)>0.7f*0.7f)
        {
        	if (!inAir)
        		AL10.alSourcePlay(m.source.get(0));
			xyzprev[0] = pos.x;
			xyzprev[1] = pos.y;
			xyzprev[2] = pos.z;
        }
	}
	
  
}