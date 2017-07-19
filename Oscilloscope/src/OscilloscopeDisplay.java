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
import java.nio.IntBuffer;
import java.util.Arrays;

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

public class OscilloscopeDisplay {

	public int DWidth = 1470;
	public int DHeight = 900;
	
	public float decay = 0.9f;
	public float pstr = 0.2f;
	
	private long window;
	public void run_ogl() {
		ogl_init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void ogl_init() {
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(DWidth, DHeight, "Oscilloscope Output!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
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
	
	int numlines = 10000;
	public float[] vertices = new float[4*numlines + 8];
	int vertices_offset = 8;
	public float[] colors = new float[8*numlines + 16];
	int colors_offset = 16;
	
	int element_offset = 4;

	int index = 0;
	private void loop() {

		double lastTime = glfwGetTime();
		int nbFrames = 0;
	     // Measure speed
	     
		for (int i = 0; i < numlines; i++) {
			colors[i*4+0] = 0.0f;
			colors[i*4+1] = 0.0f;
			colors[i*4+2] = 1.0f;
			colors[i*4+3] = 0.0f;
		}
		

		for (int i = 0; i < 8; i++) {
			colors[i*4+0] = 0.0f;
			colors[i*4+1] = 0.0f;
			colors[i*4+2] = 0.0f;
			colors[i*4+3] = 0.0f;
		}
		
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
		
		int vsId = this.loadShader("/main.vsh", GL20.GL_VERTEX_SHADER);
		int fsId = this.loadShader("/main.fsh", GL20.GL_FRAGMENT_SHADER);
		int pId = GL20.glCreateProgram();
		GL20.glAttachShader(pId, vsId);
		GL20.glAttachShader(pId, fsId);
		
		GL20.glBindAttribLocation(pId, 0, "in_Position");
		GL20.glBindAttribLocation(pId, 1, "in_Color");
		
		GL20.glLinkProgram(pId);
		GL20.glValidateProgram(pId);
		glfwSwapInterval(1);
		
		int tex_ID = GL20.glGetUniformLocation(pId, "prev_frame");
		int res_ID = GL20.glGetUniformLocation(pId, "resolution");
		int time_ID = GL20.glGetUniformLocation(pId, "time");
		int decay_ID = GL20.glGetUniformLocation(pId, "decay");
		int pstr_ID = GL20.glGetUniformLocation(pId, "pstr");
		
		
		

		int vsId2 = this.loadShader("/blit.vsh", GL20.GL_VERTEX_SHADER);
		int fsId2 = this.loadShader("/blit.fsh", GL20.GL_FRAGMENT_SHADER);
		int pId2 = GL20.glCreateProgram();
		GL20.glAttachShader(pId2, vsId2);
		GL20.glAttachShader(pId2, fsId2);
		GL20.glBindAttribLocation(pId2, 0, "in_Position");
		GL20.glLinkProgram(pId2);
		GL20.glValidateProgram(pId2);
		int tex_ID2 = GL20.glGetUniformLocation(pId2, "prev_frame");
		int res_ID2 = GL20.glGetUniformLocation(pId2, "resolution");
		
		
		
	//	FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertices.length);
	//	vBuffer.put(vertices);
	//	vBuffer.flip();
	//	FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
	//	colorsBuffer.put(colors);
	//	colorsBuffer.flip();
		
		
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		 
		// Create a new Vertex Buffer Object in memory and select it (bind)
		// A VBO is a collection of Vectors which in this case resemble the location of each vertex.
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STREAM_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		 
		int vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colors, GL15.GL_STREAM_DRAW);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		int framebuffer1 = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer1);
		int texture1 = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, DWidth, DHeight, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		int depthrenderbuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthrenderbuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL_DEPTH_COMPONENT, DWidth, DHeight);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthrenderbuffer);
		
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture1, 0);
		//GL20.glDrawBuffers(new int[] {GL30.GL_COLOR_ATTACHMENT0});
		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
			throw new RuntimeException ("Something went wrong with the framebuffer");
		

		GL30.glBindVertexArray(0);
		int vaoId2 = GL30.glGenVertexArrays();

		int vboId2 = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId2);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, new float[] {-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f}, GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		

		int framebuffer2 = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer2);
		int texture2 = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture2);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, DWidth, DHeight, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture2, 0);
		
		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
			throw new RuntimeException ("Something went wrong with the framebuffer");
		

		GL30.glBindVertexArray(0);
		
		while ( !glfwWindowShouldClose(window) ) {
			
		     double currentTime = glfwGetTime();
		     nbFrames++;
		     
		     if ( currentTime - lastTime >= 1.0 ){ // If last prinf() was more than 1 sec ago
		         // printf and reset timer
		        System.out.println((nbFrames) + " FPS");
		         nbFrames = 0;
		         lastTime += 1.0;
		     }
		     
			int tmpindex = index;
			index = -1;

			
			GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
			GL30.glBindVertexArray(vaoId);
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer1);
			GL11.glViewport(0, 0, DWidth, DHeight);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			GL20.glUseProgram(pId);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture2);
		    GL20.glUniform1i(tex_ID, 0);
		    GL20.glUniform2fv(res_ID, new float[] {DWidth, DHeight});
		    GL20.glUniform1f(time_ID, ((float)(currentTime-lastTime)));
		    GL20.glUniform1f(decay_ID, decay);
		    GL20.glUniform1f(pstr_ID, pstr);
		    
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices);
		//	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
			GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, colors);
		//	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
			GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glLineWidth(3f);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, element_offset);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if (tmpindex > 0)
				GL11.glDrawArrays(GL11.GL_LINES, element_offset, tmpindex/2);
			GL11.glDisable(GL11.GL_BLEND);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
			GL20.glUseProgram(0);

			
			GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
			GL30.glBindVertexArray(vaoId2);
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer2);
			GL11.glViewport(0, 0, DWidth, DHeight);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			GL20.glUseProgram(pId2);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture1);
			GL20.glUniform1i(tex_ID2, 0);
			GL20.glUniform2fv(res_ID2, new float[] {DWidth, DHeight});

			GL20.glEnableVertexAttribArray(0);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
			GL20.glUseProgram(0);
		    

			GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
			GL11.glViewport(0, 0, DWidth, DHeight);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture1);
			
			
			GL45.glBlitNamedFramebuffer(framebuffer2, 0, 0, 0, DWidth, DHeight, 0, 0, DWidth, DHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
			
			index = 0;
			Arrays.fill(vertices, 0);
			vertices[0] = -1.0f;
			vertices[1] = -1.0f;
			vertices[2] = -1.0f;
			vertices[3] = 1.0f;
			vertices[4] = 1.0f;
			vertices[5] = 1.0f;
			vertices[6] = 1.0f;
			vertices[7] = -1.0f;

			glfwSwapBuffers(window);
			
			glfwPollEvents();
		}
		
	}
}
