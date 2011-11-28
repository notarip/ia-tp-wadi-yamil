/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.io.*;

/**
 *
 * @author Atlhon
 */
public class ImageFileFilter implements FileFilter {

    private final String[] okFileExtensions =
            new String[]{"jpg", "png"};

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
