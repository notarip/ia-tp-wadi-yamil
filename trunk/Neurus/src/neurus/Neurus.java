/**
 * Neurus.java
 * Trabajo practico OCR de inteligencia artificial (75.23)
 * Grupo: Wadi Jalil Maluf 84780 - Yamil Jalil Maluf 79040
 * 2do cuatrimestre 2011
 */
package neurus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Neurus {
    
    public static int TAMANIO_CAPA_INT = 108; //tamaño de la capa intermedia
    public static int TAMANIO_SALIDA = 12; //tamaño de la capa de salida
    public static double ERROR =0.01 ; //Error para el entrenamiento de la red
    public static int ANCHO_IMAGEN = 30; //Ancho de la imagen para alimentar la red
    private static int ALTO_IMAGEN=60; //Alto de la imagen para alimentar la red
    public static int TAMANIO_ENTRADA = ANCHO_IMAGEN * ALTO_IMAGEN; //tamaño de entrada de la red
    
    public Neurus() throws Exception {
    }

    public static void main(String[] args) throws IOException, Exception {
                
        boolean reconocer = false;
        boolean entrenar = false;
        boolean guardarRed = false;
        boolean cargarRed = false;
        String baseDir = System.getProperty("user.dir");
        String archivoAReconocer = null;
        String dirEntrenamiento = null;
        String archivoDatosRed = "datosRed.dat";
        ArrayList<String> archivosEntrenamiento = new ArrayList<String>();
        RedNeuronal red = null;

        if (args.length == 0) {
            System.out.println("Se deben ingresar parametros.");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-r")) {
                reconocer = true;
                if ((i + 1) < args.length) {
                    archivoAReconocer = args[i + 1];
                } else {
                    System.out.println("Error en el parametro -r. Falta especificar archivo?");
                    return;
                }
            }
            if (args[i].equals("-e")) {
                entrenar = true;
                int j = i + 1;
                while (j < args.length) {
                    archivosEntrenamiento.add(args[j]);
                    j++;
                }
            }
            if (args[i].equals("-ed")) {
                entrenar = true;
                if ((i + 1) < args.length) {
                    dirEntrenamiento = args[i + 1];
                } else {
                    System.out.println("Error en el parametro -ed. Falta especificar directorio?");
                    return;
                }
            }
            if (args[i].equals("-s")) {
                guardarRed = true;
            }
            if (args[i].equals("-c")) {
                cargarRed = true;
            }
        }
        
        if (cargarRed) {
            File datos = new File(baseDir + File.separator + archivoDatosRed);
            if (datos.exists()) {
                red = RedNeuronal.cargarRed(datos.getPath());
                System.out.println("Se cargaron los datos guardados en la red");
            } else {
                System.out.println("No se pudo cargar la red. No existen datos guardados.");
            }
        } else {
            red = new RedNeuronal(TAMANIO_ENTRADA,ERROR, TAMANIO_CAPA_INT, TAMANIO_SALIDA, ANCHO_IMAGEN,ALTO_IMAGEN);
        }

        if (entrenar) {
            //Se entrena la red con los archivos pasados por linea de comando
            if (archivosEntrenamiento.size() > 0) {
                for (String nombreArchivo : archivosEntrenamiento) {
                    File archivo = new File(baseDir + File.separator + nombreArchivo);
                    if (archivo.exists()) {
                        System.out.println("Entrenando la red con el archivo " + nombreArchivo);
                        red.lanzarEntrenamientoImagen(archivo);
                        System.out.println("Finalizo el entrenamiento con el archivo " + nombreArchivo);
                    } else {
                        System.out.println("El archivo " + baseDir + File.separator + nombreArchivo + " no existe.");
                        System.out.println("Se corta la ejecucion del programa.");
                        return;
                    }
                }
            }
            
            //Se entrena la red con los archivos indicados en el directorio
            if (dirEntrenamiento != null) {
                File dirEnt = new File(baseDir + File.separator + dirEntrenamiento);
                String[] ext = {"jpg", "png"}; //filtro para las extensiones de los archivos a cargar 
                File[] listaArchivos = dirEnt.listFiles(new ImageFileFilter(ext));

                for (int i = 0; i < listaArchivos.length; i++) {
                    System.out.println("Entrenando la red con el archivo " + listaArchivos[i].getName());
                    red.lanzarEntrenamientoImagen(listaArchivos[i]);
                }
            }

            if (guardarRed) {
                red.guardar(baseDir + File.separator + archivoDatosRed);
                System.out.println("Se guardaron los datos de la red en el archivo " + archivoDatosRed);
            }
        }

        if (reconocer) {
            File archImagen = new File(baseDir + File.separator + archivoAReconocer);
            if (archImagen.exists()) {
                String resultado = red.reconocerImagen(archImagen);
                System.out.println("Se reconocio el archivo " + archivoAReconocer);
                System.out.println("Resultado: " + resultado);
                FileWriter outFile = new FileWriter(baseDir + File.separator + "salida.txt");
                PrintWriter out = new PrintWriter(outFile);
                out.println("Resultado: " + resultado);
                out.close();
            } else {
                System.out.println("No se puede abrir el archivo " + archImagen.getPath());
                System.out.println("El archivo no existe.");
            }
        }
        System.out.println("Finalizada la ejecucion del programa");
    }
}
