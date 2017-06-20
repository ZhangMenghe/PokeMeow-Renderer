package main.java.org.cytoscape.pokemeow.internal.SampleUsage;
import com.jogamp.opengl.GL2ES2;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.jogamp.opengl.GL2ES2.*;
import static com.jogamp.opengl.GL2ES2.GL_INFO_LOG_LENGTH;

public class ShaderManager {

    public static final ShaderManager INSTANCE = new ShaderManager();

    private static final Map<String, Integer> extensions = new HashMap<>();

    static {
        extensions.put("vert", GL_VERTEX_SHADER);
        extensions.put("frag", GL_FRAGMENT_SHADER);
    }

    private final Map<String, Integer> programs = new HashMap<>();

    private static final int COMPILE = 0;
    private static final int LINK = 1;
    private final int[] status = new int[2];

    private ShaderManager() {
    }

    public int buildProgram(GL2ES2 gl, String name) {
        if (programs.containsKey(name)) {
            return programs.get(name);
        }
        final List<Integer> shaders = new ArrayList<>(5);
        for (Entry<String, Integer> entry : extensions.entrySet()) {
            final String extension = entry.getKey();
            final int type = entry.getValue();
            final String path = "shader/" + name + "." + extension;
            String code = readResourceAsString(path);

            if (code != null) {
                shaders.add(compileShader(gl, type, code));
            }
            else{
                System.out.println("Fail to load shader");
            }
        }

        final int program = linkProgram(gl, shaders);
        programs.put(name, program);
        return program;
    }

    private int compileShader(GL2ES2 gl, int type, String code) {
        final int shader = gl.glCreateShader(type);
        if (shader == 0) {

            return 0;
        }
        gl.glShaderSource(shader, 1, new String[]{code}, new int[]{code.length()}, 0);
        gl.glCompileShader(shader);
        gl.glGetShaderiv(shader, GL_COMPILE_STATUS, status, COMPILE);
        if (status[COMPILE] == 0) {

            gl.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    private int linkProgram(GL2ES2 gl, List<Integer> shaders) {
        final int program = gl.glCreateProgram();
        if (program == 0) {

        }
        for (int shader : shaders) {
            gl.glAttachShader(program, shader);
        }
        gl.glLinkProgram(program);
        gl.glGetProgramiv(program, GL_LINK_STATUS, status, LINK);
        if (status[LINK] == 0) {

            gl.glDeleteProgram(program);
            return 0;
        }
        return program;
    }

    private String getProgramInfoLog(GL2ES2 gl, int program) {
        final int[] bufSize = new int[1];
        gl.glGetProgramiv(program, GL_INFO_LOG_LENGTH, bufSize, 0);
        if (bufSize[0] == 0) {
            return "no program info log";
        }
        int[] length = new int[1];
        byte[] infoLog = new byte[bufSize[0]];
        gl.glGetProgramInfoLog(program, bufSize[0], length, 0, infoLog, 0);
        return new String(infoLog, 0, length[0]);
    }

    private String getShaderInfoLog(GL2ES2 gl, int shader) {
        final int[] bufSize = new int[1];
        gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, bufSize, 0);
        if (bufSize[0] == 0) {
            return "no shader info log";
        }
        final int[] length = new int[1];
        final byte[] infoLog = new byte[bufSize[0]];
        gl.glGetShaderInfoLog(shader, bufSize[0], length, 0, infoLog, 0);
        return new String(infoLog, 0, length[0]);
    }

    private static String readResourceAsString(String name) {
        try (final InputStream stream = ShaderManager.class.getResourceAsStream(name)) {
            if (stream == null) {
                return null;
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
