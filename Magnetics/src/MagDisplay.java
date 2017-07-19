import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MagDisplay {
	
	Camera cam;
	
	public int DWidth = 1470;
	public int DHeight = 900;
	
	public float decay = 0.9f;
	public float pstr = 0.2f;
	
	Physics physics;
	
	private long window;
	public void run_ogl() {
		this.setupMatrices();
		ogl_init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	

    private Matrix4f projectionMatrix = null;
    public Matrix4f viewMatrix = null;
    public Matrix4f viewMatrix2 = new Matrix4f();
    private Matrix4f modelMatrix = null;
    private FloatBuffer matrix44Buffer = null;

    private int projectionMatrixLocation = 0;
    private int viewMatrixLocation = 0;
    private int modelMatrixLocation = 0;
    private int projectionMatrixLocation2 = 0;
    private int viewMatrixLocation2 = 0;
    private int modelMatrixLocation2 = 0;
    private int lightdirection = 0;
    
    private void logicCycle() {
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
         
        //Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
        //Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
        Matrix4f.rotate((float)Math.toRadians(cam.roll), new Vector3f(0, 0, 1), 
                viewMatrix, viewMatrix);
        Matrix4f.rotate((float)Math.toRadians(cam.pitch), new Vector3f(1, 0, 0), 
        		 viewMatrix, viewMatrix);
        Matrix4f.rotate((float)Math.toRadians(cam.yaw), new Vector3f(0, 1, 0), 
         		 viewMatrix, viewMatrix);
        Matrix4f.load(viewMatrix, viewMatrix2);
     //   System.out.println(viewMatrix2.toString());
        Matrix4f.translate(new Vector3f(cam.y, cam.z, cam.x), viewMatrix, viewMatrix);
        GL20.glUseProgram(0);
//        this.exitOnGLError("logicCycle");
    }

    private float degreesToRadians(float degrees) {
        return degrees * (float)(Math.PI / 180d);
    }
    
    private float coTangent(float input) {
    	return (float)(Math.cos(input)/Math.sin(input));
    }
    
    private static int WIDTH = 1600;
    private static int HEIGHT = 900;
    
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
    
	public void ogl_init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		window = glfwCreateWindow(DWidth, DHeight, "Oscilloscope Output!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			cam.handleKeyPress(window, key, scancode, action, mods);
		});
		
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
    	cam = new Camera(window, this);
    	GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    	physics = new Physics(cam);
	}
	
	public int loadShader(String filename, int type) {
	    StringBuilder shaderSource = new StringBuilder();
	    int shaderID = 0;
	     
	    try {
	    	InputStream in = getClass().getResourceAsStream(filename);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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
	    if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0)
        {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 1000));
            System.err.println("Could not compile shader");
            System.exit(1);
        }

	    return shaderID;
	}

	int numcoilparts;
	public float[] coilvertices;
	public float[] coilcolors;
	public boolean willupdatecoil = false;

	int numfieldlines = 0;
	public float[] fieldvertices;
	public float[] fieldcolors;
	public boolean willupdatefield = false;
	
	int numareaparts = 0;
	public float[] areavertices;
	public float[] areacolors;
	public float[] areanormals;
	public boolean willupdatearea = false;
	
	public static final int SIZE = 2000;
	public static final int SIZE2 = 20000;
	
	private void loop() {

		double lastTime = glfwGetTime();
		int nbFrames = 0;
	     // Measure speed
	    
		coilvertices = new float[4*SIZE];
		coilcolors = new float[4*SIZE];
		fieldvertices = new float[4*SIZE2];
		fieldcolors = new float[4*SIZE2];
		areavertices = new float[4*SIZE];
		areacolors = new float[4*SIZE];
		areanormals = new float[3*SIZE];
		
		for (int i = 0; i < 4*SIZE; i++) {
			coilvertices[i] = 0;
			coilcolors[i] = 0;
			areavertices[i] = 0;
			areacolors[i] = 0;
		}

		for (int i = 0; i < 3*SIZE; i++) {
			areanormals[i] = 0;
		}
	
		for (int i = 0; i < 4*SIZE2; i++) {
			fieldvertices[i] = 0;
			fieldcolors[i] = 0;
		}
	
		updatecoil();
		performphysics();
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GL11.glOrtho(0, DWidth, 0, DHeight, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		int vsId, fsId, pId;
		{
			vsId = this.loadShader("/main.vsh", GL20.GL_VERTEX_SHADER);
			fsId = this.loadShader("/main.fsh", GL20.GL_FRAGMENT_SHADER);
			pId = GL20.glCreateProgram();
			GL20.glAttachShader(pId, vsId);
			GL20.glAttachShader(pId, fsId);

			GL20.glBindAttribLocation(pId, 0, "in_Position");
			GL20.glBindAttribLocation(pId, 1, "in_Color");

			GL20.glLinkProgram(pId);
			GL20.glValidateProgram(pId);

			glfwSwapInterval(1);

			projectionMatrixLocation = GL20.glGetUniformLocation(pId,"projectionMatrix");
			viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
			modelMatrixLocation = GL20.glGetUniformLocation(pId, "modelMatrix");
		}
		

		int vsId2, fsId2, pId2;
		{
			vsId2 = this.loadShader("/surface.vsh", GL20.GL_VERTEX_SHADER);
			fsId2 = this.loadShader("/surface.fsh", GL20.GL_FRAGMENT_SHADER);
			pId2 = GL20.glCreateProgram();
			GL20.glAttachShader(pId2, vsId2);
			GL20.glAttachShader(pId2, fsId2);

			GL20.glBindAttribLocation(pId2, 0, "in_Position");
			GL20.glBindAttribLocation(pId2, 1, "in_Color");
			GL20.glBindAttribLocation(pId2, 2, "in_Normal");

			GL20.glLinkProgram(pId2);
			GL20.glValidateProgram(pId2);

			glfwSwapInterval(1);

			projectionMatrixLocation2 = GL20.glGetUniformLocation(pId2,"projectionMatrix");
			viewMatrixLocation2 = GL20.glGetUniformLocation(pId2, "viewMatrix");
			modelMatrixLocation2 = GL20.glGetUniformLocation(pId2, "modelMatrix");
			lightdirection = GL20.glGetUniformLocation(pId2, "light_direction");
		}
		
		
        int CvaoId, CvboId, CvbocId;
        {
        	CvaoId = GL30.glGenVertexArrays();
        	GL30.glBindVertexArray(CvaoId);

        	CvboId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CvboId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, coilvertices, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        	CvbocId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CvbocId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, coilcolors, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
        GL30.glBindVertexArray(0);

        int FvaoId, FvboId, FvbocId;
        {
        	FvaoId = GL30.glGenVertexArrays();
        	GL30.glBindVertexArray(FvaoId);

        	FvboId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, FvboId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fieldvertices, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        	FvbocId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, FvbocId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fieldcolors, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
        GL30.glBindVertexArray(0);
        
        int AvaoId, AvboId, AvbocId, AvbonId;
        {
        	AvaoId = GL30.glGenVertexArrays();
        	GL30.glBindVertexArray(AvaoId);

        	AvboId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, AvboId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, areavertices, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        	AvbocId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, AvbocId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, areacolors, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        	
        	AvbonId = GL15.glGenBuffers();
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, AvbonId);
        	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, areanormals, GL15.GL_STREAM_DRAW);
        	GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
        GL30.glBindVertexArray(0);
		while ( !glfwWindowShouldClose(window) ) {
			logicCycle();
		     double currentTime = glfwGetTime();
		     nbFrames++;
		     
		     if ( currentTime - lastTime >= 1.0 ){ // If last prinf() was more than 1 sec ago
		         // printf and reset timer
		//        System.out.println((nbFrames) + " FPS");
		         nbFrames = 0;
		         lastTime += 1.0;
		     }
		     
//			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer1);
			GL11.glViewport(0, 0, DWidth, DHeight);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			GL20.glUseProgram(pId);

	        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
	        GL20.glUniformMatrix4fv(projectionMatrixLocation, false, matrix44Buffer);
	        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
	        GL20.glUniformMatrix4fv(viewMatrixLocation, false, matrix44Buffer);
	        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
	        GL20.glUniformMatrix4fv(modelMatrixLocation, false, matrix44Buffer);
	        
	        GL30.glBindVertexArray(CvaoId);
	        {
				if (willupdatecoil) {
					updatecoil();
					willupdatecoil = false;
					
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CvboId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, coilvertices);
		        	GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);

		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CvbocId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, coilcolors);
		        	GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				}

	        	GL20.glEnableVertexAttribArray(0);
	        	GL20.glEnableVertexAttribArray(1);
	        	GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, numcoilparts);
	        	//	GL11.glDrawArrays(GL11.GL_LINES, 0, ind);
	        	GL20.glDisableVertexAttribArray(0);
	        	GL20.glDisableVertexAttribArray(1);
	        }
	        GL30.glBindVertexArray(0);

	        GL30.glBindVertexArray(FvaoId);
	        {
				if (willupdatefield) {
					performphysics();
					willupdatefield = false;
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, FvboId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, fieldvertices);
		        	GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);

		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, FvbocId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, fieldcolors);
		        	GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				}

	        	GL20.glEnableVertexAttribArray(0);
	        	GL20.glEnableVertexAttribArray(1);
	        	GL11.glDrawArrays(GL11.GL_LINES, 0, numfieldlines);
	        	GL20.glDisableVertexAttribArray(0);
	        	GL20.glDisableVertexAttribArray(1);
	        }
			GL30.glBindVertexArray(0);
			GL20.glUseProgram(0);

			GL20.glUseProgram(pId2);

	        projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
	        GL20.glUniformMatrix4fv(projectionMatrixLocation2, false, matrix44Buffer);
	        viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
	        GL20.glUniformMatrix4fv(viewMatrixLocation2, false, matrix44Buffer);
	        modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
	        GL20.glUniformMatrix4fv(modelMatrixLocation2, false, matrix44Buffer);
	        

	        double x = Math.sin(Math.toRadians(-cam.yaw)) * Math.cos(Math.toRadians(cam.pitch));
	        double y = Math.sin(Math.toRadians(cam.pitch));
	        double z = Math.cos(Math.toRadians(-cam.yaw)) * Math.cos(Math.toRadians(cam.pitch));
	        GL20.glUniform3f(lightdirection, (float)x, (float)y, (float)z);
	       // GL20.glUniform3f(lightdirection, cam.y, cam.z, cam.x);
	        		
	        GL30.glBindVertexArray(AvaoId);
	        {
				if (willupdatearea) {
					numareaparts = physics.newtriangle(areavertices, areacolors, areanormals, numareaparts);
					willupdatearea = false;
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, AvboId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, areavertices);
		        	GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);

		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, AvbocId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, areacolors);
		        	GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		        	
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, AvbonId);
		        	GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, areanormals);
		        	GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
		        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
				}

				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				
	        	GL20.glEnableVertexAttribArray(0);
	        	GL20.glEnableVertexAttribArray(1);
	        	GL20.glEnableVertexAttribArray(2);
	        	GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, numareaparts);
	        	GL20.glDisableVertexAttribArray(0);
	        	GL20.glDisableVertexAttribArray(1);
	        	GL20.glDisableVertexAttribArray(2);
	        	

				GL11.glDisable(GL11.GL_BLEND);
	        }
			GL30.glBindVertexArray(0);
			

			glfwSwapBuffers(window);
			
			glfwPollEvents();
			cam.update();
		}
	}
	
	private void updatecoil() {
		numcoilparts = physics.fillCoilData(coilvertices, coilcolors);
	}

	private void performphysics() {
		if (true) {
			return;
		} else
		{
			numfieldlines = 0;
			for (double x = -2; x <= 2; x += 0.2) {
				for (double y = -2; y <= 2; y += 0.2) {
					for (double z = -2; z <= 2; z += 0.2) {
						Vec3 force = physics.getBfield(x, y, z, 1.0);
						force = force.unitvector().mul(0.1);
						addFieldVec(force, new Vec3(x, y, z));
					}
				}
			}
		}
	}
	
	
	public void addFieldVec(Vec3 force, Vec3 pos) {
		fieldvertices[numfieldlines*4 + 0] = (float) pos.x;
		fieldvertices[numfieldlines*4 + 1] = (float) pos.y;
		fieldvertices[numfieldlines*4 + 2] = (float) pos.z;
		fieldvertices[numfieldlines*4 + 3] = 1;
		fieldcolors[numfieldlines*4 + 0] = 0f;
		fieldcolors[numfieldlines*4 + 1] = 1f;
		fieldcolors[numfieldlines*4 + 2] = 1f;
		fieldcolors[numfieldlines*4 + 3] = 1f;
		numfieldlines++;
		fieldvertices[numfieldlines*4 + 0] = (float) pos.x + (float)force.x;
		fieldvertices[numfieldlines*4 + 1] = (float) pos.y + (float)force.y;
		fieldvertices[numfieldlines*4 + 2] = (float) pos.z + (float)force.z;
		fieldvertices[numfieldlines*4 + 3] = 1;
		fieldcolors[numfieldlines*4 + 0] = 0.3f;
		fieldcolors[numfieldlines*4 + 1] = 0.3f;
		fieldcolors[numfieldlines*4 + 2] = 0.3f;
		fieldcolors[numfieldlines*4 + 3] = 1f;
		numfieldlines++;
	}
}
