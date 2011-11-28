/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImagenTest;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import neurus.ExtractorCaracteres;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Atlhon
 */
public class ExtractorCaracteresTest {

    private static String dir;
    private static File salidaDir;
    private static int ancho = 75;
    private static int alto = 75;

    public ExtractorCaracteresTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        dir = System.getProperty("user.dir").concat("/test/archivos/");
        salidaDir = new File(dir + "salida");
        ancho = 75;
        alto = 75;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void recortarEnMemoriaTest() throws IOException {

        ArrayList<BufferedImage> listaChar;
        ExtractorCaracteres extractor = new ExtractorCaracteres();

        File imageFile = new File(dir + "entrada/test.png");
        listaChar = extractor.recortar(imageFile, null, alto, ancho);
      
        assertEquals(1, listaChar.size());

    }
     @Test
     public void recortarEnMemoriaTestYGuardar() throws IOException {

        ArrayList<BufferedImage> listaChar;
        ExtractorCaracteres extractor = new ExtractorCaracteres();

        File imageFile = new File(dir + "entrada/test.png");
        listaChar = extractor.recortar(imageFile, salidaDir, alto, ancho);
      
        assertEquals(1, listaChar.size());

    }
}
