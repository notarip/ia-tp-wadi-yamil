/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImagenTest;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
public class GenerarCaracteresTest {

    private static String dir;
    private static File salidaDir;

    public GenerarCaracteresTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        dir = System.getProperty("user.dir").concat("/test/archivos/");
        salidaDir = new File(dir + "salida/");
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
    public void generarTest() throws IOException {

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts(); // Get the fonts

        String sampleText = "0 1 2 3 4 5 6 7 8 9 . ,";
        int j;
        for (int i = 0; i < fonts.length; i++) {
            //create the font you wish to use
            j = i * 5;
            if (j < fonts.length) {
                Font font = new Font(fonts[20 + i].getName(), Font.PLAIN, 50);

                //create the FontRenderContext object which helps us to measure the text
                FontRenderContext frc = new FontRenderContext(null, true, true);

                //get the height and width of the text
                Rectangle2D bounds = font.getStringBounds(sampleText, frc);
                int w = (int) bounds.getWidth();
                int h = (int) bounds.getHeight();
                w = w + 40;

                //create a BufferedImage object
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                //calling createGraphics() to get the Graphics2D
                Graphics2D g = image.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                //set color and other parameters
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, w, h);
                g.setColor(Color.BLACK);
                g.setFont(font);


                g.drawString(sampleText, (float) bounds.getX() + 20, (float) -bounds.getY());

                //releasing resources
                g.dispose();

                //creating the file
                File outputfile = new File(salidaDir + "fuente" + (20 + i) + ".png");
                ImageIO.write(image, "png", outputfile);
            }
        }
    }
}