package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.util.IOUtil;
import sun.misc.IOUtils;
import sun.rmi.runtime.Log;
import java.io.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.lang.*;
import java.util.StringTokenizer;

/**
 * Created by ZhangMenghe on 2017/7/13.
 */
public class buildJarUtilTest {
        private static final String LIB_BIN = "lib-bin/";

        private final static String GLUEGEN = "gluegen-rt";
        private final static String JOGL = "jogl_desktop";

        /**
         * When packaged into JAR extracts DLLs, places these into
         */
        public static void loadFromJar() {
            // we need to put both DLLs to temp dir
            String path = "AC_" + new Date().getTime();
            loadLib(path, GLUEGEN);
            loadLib(path, JOGL);
        }

        /**
         * Puts library to temp dir and loads to memory
         */
        private static void loadLib(String path, String name) {
            name = name + ".dll";
            try {
                String libpath = System.getProperty("java.library.path");
                if (libpath == null || libpath.length() == 0) {
                    throw new RuntimeException("java.library.path is null");
                }
                String mpath = null;
                StringTokenizer st = new StringTokenizer(libpath, System
                        .getProperty("path.separator"));
                if (st.hasMoreElements()) {
                    mpath = st.nextToken();
                } else {
                    throw new RuntimeException("can not split library path:"
                            + libpath);
                }
                // have to use a stream
                //InputStream in = new FileInputStream("f://lib-bin//" + name);
                InputStream in = Demo.class.getResourceAsStream(LIB_BIN + name);
                // always write to different location
                File fileOut = new File(mpath + "/" + name);//System.getProperty("java.io.tmpdir") + "/" + path +'/'+ LIB_BIN + name);//
                System.out.println("Writing dll to: " + fileOut.getAbsolutePath());
                OutputStream out = new FileOutputStream(fileOut);
                IOUtil.copyStream2Stream(in,out,in.available());
                in.close();
                out.close();
                System.load(fileOut.toString());
            } catch (Exception e) {
                System.out.println("Failed to load required DLL");
            }
        }
//    public static void main( String[] args ) {
//        System.out.println("Loading DLL");
//        try {
//            System.loadLibrary(GLUEGEN);
//            System.out.println("DLL is loaded from memory");
//        } catch (UnsatisfiedLinkError e) {
//            loadFromJar();
//        }
//    }
}
