/**
 * ImageFileFilter.java
 * Trabajo practico OCR de inteligencia artificial (75.23)
 * Grupo: Wadi Jalil Maluf 84780 - Yamil Jalil Maluf 79040
 * 2do cuatrimestre 2011
 */
package neurus;

import java.io.*;

/**
 *Crea un filtro para listar el contenido de un directorio
 */
public class ImageFileFilter implements FileFilter {

    private final String[] okFileExtensions;
    
    public ImageFileFilter(String[] ext){
        okFileExtensions=ext;
    }

    @Override
    public boolean accept(File pathname) {
        for (String extension : okFileExtensions) {
            if (pathname.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
