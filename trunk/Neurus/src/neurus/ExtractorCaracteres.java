/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.javaocr.ocrPlugins.CharacterExtractor;
import net.sourceforge.javaocr.scanner.DocumentScanner;
import net.sourceforge.javaocr.scanner.PixelImage;

/**
 *
 * @author Atlhon
 */
public class ExtractorCaracteres extends CharacterExtractor {

    private ArrayList<BufferedImage> listaCaracteres;
    private int num = 0;
    private DocumentScanner documentScanner = new DocumentScanner();
    private File outputDir = null;
    private File inputImage = null;
    private int std_width;
    private int std_height;

    public ExtractorCaracteres() {

        listaCaracteres = new ArrayList<BufferedImage>();


    }
    /*
     *Si dirSalida es null las imagenes no se guardan en el disco pero si en memoria
     */

    public ArrayList<BufferedImage> recortar(File imagenEntrada, File dirSalida, int std_ancho, int std_alto) {

        try {
            this.std_width = std_ancho;
            this.std_height = std_alto;
            this.inputImage = imagenEntrada;
            this.outputDir = dirSalida;
            Image img = ImageIO.read(imagenEntrada);
            PixelImage pixelImage = new PixelImage(img);
            pixelImage.toGrayScale(true);
            pixelImage.filter();
            documentScanner.scan(pixelImage, this, 0, 0, pixelImage.width, pixelImage.height);

        } catch (IOException ex) {
            Logger.getLogger(CharacterExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCaracteres;
    }

    @Override
    public void processChar(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2) {
        try {
            int areaW = x2 - x1;
            int areaH = y2 - y1;

            //Extract the character
            BufferedImage characterImage = ImageIO.read(inputImage);
            characterImage = characterImage.getSubimage(x1, y1, areaW, areaH);

            //Scale image so that both the height and width are less than std size
            if (characterImage.getWidth() > std_width) {
                //Make image always std_width wide
                double scaleAmount = (double) std_width / (double) characterImage.getWidth();
                AffineTransform tx = new AffineTransform();
                tx.scale(scaleAmount, scaleAmount);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                characterImage = op.filter(characterImage, null);
            }

            if (characterImage.getHeight() > std_height) {
                //Make image always std_height tall
                double scaleAmount = (double) std_height / (double) characterImage.getHeight();
                AffineTransform tx = new AffineTransform();
                tx.scale(scaleAmount, scaleAmount);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                characterImage = op.filter(characterImage, null);
            }

            //Paint the scaled image on a white background
            BufferedImage normalizedImage = new BufferedImage(std_width, std_height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = normalizedImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, std_width, std_height);

            //Center scaled image on new canvas
            int x_offset = (std_width - characterImage.getWidth()) / 2;
            int y_offset = (std_height - characterImage.getHeight()) / 2;

            g.drawImage(characterImage, x_offset, y_offset, null);
            g.dispose();

            this.listaCaracteres.add(convertirAByN(normalizedImage));

            if (outputDir != null) {
                //Save new image to file
                File outputfile = new File(outputDir + File.separator + "caracter_" + num + ".png");
                ImageIO.write(convertirAByN(normalizedImage), "png", outputfile);
            }
            num++;

        } catch (Exception ex) {
            Logger.getLogger(CharacterExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BufferedImage convertirAByN(BufferedImage imagen) throws IOException {

        BufferedImage imagenBN = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        imagenBN.getGraphics().drawImage(imagen, 0, 0, null);
        return imagenBN;


    }
}
