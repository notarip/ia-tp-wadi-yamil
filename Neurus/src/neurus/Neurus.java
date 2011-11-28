/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;

/**
 *
 * @author ringo
 */
public class Neurus {

    public static int TAMANIO_ENTRADA = 2500;
    public static int TAMANIO_CAPA_INT = 108;
    public static int TAMANIO_SALIDA = 12;
    public static int ANCHO_IMAGEN = 50; //Se supone cuadrada
//    public static int nroFuente=1;

    public Neurus() throws Exception {
    }

    /**
     * @param args the command line arguments
     * 
     */
    public static void main(String[] args) throws IOException, Exception {
        //    Neurus n = new Neurus();
        //      n.iniciar();
        String baseDir = System.getProperty("user.dir");
        boolean reconocer = false;
        boolean entrenar = false;
        String archivoAReconocer = null;
        ArrayList<String> archivosEntrenamiento = new ArrayList<String>();
        String dirEntrenamiento = null;
        boolean guardarRed = false;
        String archivoDatosRed = "datosRed.dat";
        boolean cargarRed = false;
        RedNeuronal red=null;

        if (args.length == 0) {
            System.out.println("Se deben ingresar parametros.");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-r")) {
                reconocer = true;
                if ((i + 1) < args.length) {
                    archivoAReconocer = args[i + 1];
                }else{
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
                }else{
                    System.out.println("Error en el parametro -ed. Falta especificar directorio?");
                    return;
                }

            }
            if (args[i].equals("-s")) {
                guardarRed = true;
//                archivoDatosRed = args[i + 1];
            }
            if (args[i].equals("-c")) {
                cargarRed = true;
            }
        }

        if (cargarRed) {
            File datos = new File(baseDir + File.separator + archivoDatosRed);
            if (datos.exists()) {
                red=RedNeuronal.cargarRed(datos.getPath());
                
                System.out.println("Se cargaron los datos guardados en la red");
            } else {
                System.out.println("No se pudo cargar la red. No habìa datos guardados.");
            }
        }else{
            red = new RedNeuronal(TAMANIO_ENTRADA, TAMANIO_CAPA_INT, TAMANIO_SALIDA, ANCHO_IMAGEN);
        }

        

        if (entrenar) {

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
            if (dirEntrenamiento != null) {
                File dirEnt = new File(baseDir + File.separator + dirEntrenamiento);
                File[] listaArchivos = dirEnt.listFiles(new ImageFileFilter());

                for (int i = 0; i < listaArchivos.length; i++) {
                    System.out.println("Entrenando la red con el archivo " + listaArchivos[i].getName());
                    red.lanzarEntrenamientoImagen(listaArchivos[i]);
                    System.out.println("Finalizo el entrenamiento con el archivo " + listaArchivos[i].getName());
                }
            }

            if (guardarRed) {
                red.guardar(baseDir+File.separator+archivoDatosRed);
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

    }
    /**
    public void iniciar() throws Exception {
    int maxIterations = 1000;
    ////NeuralNetwork.load("redIntentoAmano")
    NeuralNetwork neuralNet = new MultiLayerPerceptron(TAMANIO_ENTRADA,108,12);
    
    //         NeuralNetwork neuralNet =new SupervisedHebbianNetwork(TAMANIO_ENTRADA,  12);//MatrixMultiLayerPerceptron(new MultiLayerPerceptron(TransferFunctionType.RAMP,TAMANIO_ENTRADA,125,12));
    
    //NeuralNetwork neuralNet = new MultiLayerPerceptron(225,350,12);
    ((LMS) neuralNet.getLearningRule()).setMaxError(0.1);//0-1
    //        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.00001);//0-1
    // ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
    
    
    // enable batch if using MomentumBackpropagation
    
    
    neuralNet.getLearningRule().addObserver(this);
    
    if( neuralNet.getLearningRule() instanceof MomentumBackpropagation ) 
    ((MomentumBackpropagation)neuralNet.getLearningRule()).setBatchMode(true);
    
    
    String dir = System.getProperty("user.dir").concat("/src/neurus/");
    for (int i = 1; i < 69; i++) {
    String adir=dir+"50/fuente ("+i+").png";
    agregarEntrenamiento(adir, neuralNet);   
    }
    //         agregarEntrenamiento(dir+"50/wadi1.png", neuralNet);
    //         agregarEntrenamiento(dir+"50/wadi2.png", neuralNet);
    //         agregarEntrenamiento(dir+"50/wadi3.png", neuralNet);
    //         agregarEntrenamiento(dir+"50/wadi4.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente1.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente2.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente3.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente4.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente5.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente6.png", neuralNet);
    //                agregarEntrenamiento(dir+"fuente7.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente8.png", neuralNet);
    //        agregarEntrenamiento(dir+"fuente9.png", neuralNet);
    //         agregarEntrenamiento(dir+"wadi1.png", neuralNet);
    //         agregarEntrenamiento(dir+"wadi2.png", neuralNet);
    //         agregarEntrenamiento(dir+"wadi3.png", neuralNet);
    //         agregarEntrenamiento(dir+"wadi4.png", neuralNet);
    //         agregarEntrenamiento(dir+"wadi5.png", neuralNet);
    
    
    //       neuralNet.save("redIntentoAmano");
    TrainingSet testSet = new TrainingSet();
    
    ExtractorCaracteres ec = new ExtractorCaracteres();
    ArrayList<BufferedImage> ab = ec.recortar(new File(dir + "test.png"), new File("D:\\facu\\75.23 IA\\trunk\\Neurus\\src\\neurus\\"), ANCHO_IMAGEN, ANCHO_IMAGEN);
    testSet.addElement(new TrainingElement(fractionRgbData(ab.get(0))));
    
    //        do {            
    //            Thread.sleep(5*1000);
    //        } while (!neuralNet.getLearningRule().isStopped());
    //        
    for (TrainingElement testElement : testSet.trainingElements()) {
    neuralNet.setInput(testElement.getInput());
    neuralNet.calculate();
    double[] networkOutput = neuralNet.getOutput();
    
    System.out.println(" Output: " + Arrays.toString(networkOutput));
    System.out.println("Ganador:" + getWinner(networkOutput) + "!!!");
    
    //            File arch=new File("");
    //            
    //            URL url= arch.toURI().toURL();
    
    URL url = new URL("file:" + dir + "alarma.wav");
    //            URL url = ClassLoader.getSystemResource("/src/neurus/alarma.wav");
    java.applet.AudioClip clip = JApplet.newAudioClip(url);
    
    clip.play();
    }
    //        long transcurrido=System.currentTimeMillis()-tiempo;
    
    
    //        System.out.println("Calculado en "+transcurrido/1000+" segundos");
    
    //            URL url = ClassLoader.getSystemResource("ar/com/mcg/vista/main/resources/" + soundFileName);
    //            java.applet.AudioClip clip = JApplet.newAudioClip(url);
    //            clip.play();
    //         URL url= new URL("file:"+dir+"alarma.wav");
    //            java.applet.AudioClip clip = JApplet.newAudioClip(url);
    //
    //            clip.play();
    
    Thread.sleep(1000);
    
    
    }
     */
    /**  
    public static int getWinner(double[] winers) {
    
    double max = 0;
    int winner = 0;
    
    for (int i = 0; i < winers.length; i++) {
    
    if (winers[i] > max) {
    
    winner = i;
    max = winers[i];
    
    }
    
    
    
    }
    
    return winner;
    }
    
    //    
    //      public static int getWinner(double[] winers){ 
    //              
    //             double max = 0; 
    //             int winner = 0; 
    //              
    //             for (int i = 0; i < winers.length; i++) { 
    //                
    //                  if (winers[i] > max){ 
    //                        
    //                       winner = i; 
    //                       max = winers[i]; 
    //                        
    //                  } 
    //                        
    //                   
    //                   
    //          } 
    //              
    //             return winner; 
    //        } 
    
    
    
    public static void agregarEntrenamiento(String url,NeuralNetwork net) throws Exception{
    ExtractorCaracteres ec=new ExtractorCaracteres();
    TrainingSet trainingSet = new TrainingSet(TAMANIO_ENTRADA,12);
    ArrayList<BufferedImage> imgs=ec.recortar(new File(url), new File("D:\\facu\\75.23 IA\\trunk\\Neurus\\src\\neurus\\"), ANCHO_IMAGEN, ANCHO_IMAGEN);
    System.out.println("Pedazos encontrados "+imgs.size());
    if(imgs.size()!=12)
    return;
    for (int i = 0; i <imgs.size(); i++) {
    BufferedImage bufferedImage = imgs.get(i);
    double[] rgbValues = fractionRgbData(bufferedImage);
    double[] resultado = new double[12];
    
    Arrays.fill(resultado, 0);
    resultado[i] = 1;
    
    trainingSet.addElement(new SupervisedTrainingElement(rgbValues, resultado));
    }
    net.learnInSameThread(trainingSet);
    System.out.println("Entrenado: " + url);
    nroFuente++;
    
    }
    
    public static double[] fractionRgbData(BufferedImage image) throws Exception {
    //ImageSampler.downSampleImage(new Dimension(30,30),
    FractionRgbData imgRgb = new FractionRgbData(image);
    double input[];
    
    
    input = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(imgRgb.getFlattenedRgbValues());
    
    //            System.out.println("Tamanio imput"+input.length);
    //input = imgRgb.getFlattenedRgbValues();
    // System.out.println("Tamanio imput"+input.length);
    //            System.out.println( Arrays.toString(input));
    //Arrays.toString(input);
    
    return input;
    }
    
    @Override
    public void update(Observable o, Object arg) {
    SupervisedLearning rule = (SupervisedLearning)o; 
    if(rule!=null)
    System.out.println( nroFuente+"-Training, Network Epoch " + rule.getCurrentIteration() + ", Error:" + rule.getTotalNetworkError());
    }
     **/
}
