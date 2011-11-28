/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ImagenTest;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.jws.soap.SOAPBinding.Use;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.javaocr.ocrPlugins.CharacterExtractor;
import net.sourceforge.javaocr.scanner.PixelImage;

/**
 *
 * @author Atlhon
 */
public class ManejoImagenTest {

    private static String dir;
    private static File salidaDir;
    private static int ancho = 75;
    private static int alto = 75;

    public ManejoImagenTest() {
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
    public void extraerCaracter() {

        File imageFile = new File(dir + "entrada/test2.png");
        CharacterExtractor extractor = new CharacterExtractor();
        extractor.slice(imageFile, salidaDir, ancho, alto);
        assertTrue(true);
    }

    @Test
    public void toGrayScale() throws IOException {
        /* File imageFile = new File(dir + "entrada/test1.png");
        Image img = ImageIO.read(imageFile);
        PixelImage pixelImage = new PixelImage(img);
        //        pixelImage.toGrayScale(true);
        pixelImage.filter();
        BufferedImage normalizedImage = new BufferedImage(pixelImage.width, pixelImage.height, BufferedImage.TYPE_4BYTE_ABGR);
        WritableRaster raster = (WritableRaster) normalizedImage.getData();
        raster.setPixels(0, 0, pixelImage.width, pixelImage.height, pixelImage.pixels);
        File outputfile = new File(dir + "salida/imagen filtrada.png");
        ImageIO.write(normalizedImage, "png", outputfile);*/

        BufferedImage image = ImageIO.read(new File(dir + "entrada/test1.png"));

        RescaleOp rescaleOp = new RescaleOp(1.2f, 30, null);
        rescaleOp.filter(image, image);  // Source and destination are the same.
        File fi = new File(dir + "salida/imagen filtrada.png");
        ImageIO.write(image, "png", fi);
        /*
        try
        {
        //colored image path
        BufferedImage image = ImageIO.read(new  
        File(dir + "entrada/test1.png"));
        
        //getting width and height of image
        double image_width = image.getWidth();
        double image_height = image.getHeight();
        
        BufferedImage bimg = null;
        BufferedImage img = image;
        
        //drawing a new image      
        bimg = new BufferedImage((int)image_width, (int)image_height,
        BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D gg = bimg.createGraphics();
        gg.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
        
        //saving black and white image onto drive
        //                             String temp = "blackAndwhite.jpeg";
        File fi = new File(dir + "salida/imagen filtrada.png");
        ImageIO.write(bimg, "png", fi);
        assertTrue(true);
        }
        catch (Exception e)
        {
        System.out.println(e);
        }*/
    }

    @Test
    public void convertirAByN() throws IOException {

        BufferedImage image_to_save = ImageIO.read(new File(dir + "entrada/test.png"));

        BufferedImage image_to_save2 = new BufferedImage(image_to_save.getWidth(), image_to_save.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        image_to_save2.getGraphics().drawImage(image_to_save, 0, 0, null);
        image_to_save = image_to_save2;

        File fi = new File(dir + "salida/imagen en b y n.png");
        ImageIO.write(image_to_save2, "png", fi);
    }
}
