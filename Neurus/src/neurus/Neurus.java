/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JApplet;
import org.neuroph.contrib.IACNetwork;
import org.neuroph.contrib.imgrec.FractionRgbData;
import org.neuroph.contrib.matrixmlp.MatrixMultiLayerPerceptron;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.BAM;
import org.neuroph.nnet.CompetitiveNetwork;
import org.neuroph.nnet.Hopfield;
import org.neuroph.nnet.Instar;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Outstar;
import org.neuroph.nnet.RbfNetwork;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author ringo
 */
public class Neurus implements Observer{

    /**
     * @param args the command line arguments
     * 
     */
    public static int TAMANIO_ENTRADA=2500;
    public static int ANCHO_IMAGEN=50;
    public static int nroFuente=1;
    
    public Neurus() throws Exception{
         
    }
    
    public void iniciar() throws Exception{
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
        
        ExtractorCaracteres ec=new ExtractorCaracteres();
        ArrayList<BufferedImage> ab=ec.recortar(new File(dir+"test.png"), new File("D:\\facu\\75.23 IA\\trunk\\Neurus\\src\\neurus\\"),ANCHO_IMAGEN,ANCHO_IMAGEN);
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
            System.out.println("Ganador:"+getWinner(networkOutput)+"!!!");
        }
//        long transcurrido=System.currentTimeMillis()-tiempo;
        
        
//        System.out.println("Calculado en "+transcurrido/1000+" segundos");

//            URL url = ClassLoader.getSystemResource("ar/com/mcg/vista/main/resources/" + soundFileName);
//            java.applet.AudioClip clip = JApplet.newAudioClip(url);
//            clip.play();
         URL url= new URL("file:"+dir+"alarma.wav");
            java.applet.AudioClip clip = JApplet.newAudioClip(url);

            clip.play();
            
            Thread.sleep(1000);

        
    }
    public static void main(String[] args) throws IOException, Exception {
        Neurus n=new Neurus();
        n.iniciar();
      
       
    }

    
      public static int getWinner(double[] winers){ 
              
             double max = 0; 
             int winner = 0; 
              
             for (int i = 0; i < winers.length; i++) { 
                
                  if (winers[i] > max){ 
                        
                       winner = i; 
                       max = winers[i]; 
                        
                  } 
                        
                   
                   
          } 
              
             return winner; 
        } 
    
    
    
    public static void agregarEntrenamiento(String url,NeuralNetwork net) throws Exception{
        ExtractorCaracteres ec=new ExtractorCaracteres();
        TrainingSet trainingSet = new TrainingSet(TAMANIO_ENTRADA,12);
        ArrayList<BufferedImage> imgs=ec.recortar(new File(url), new File("D:\\facu\\75.23 IA\\trunk\\Neurus\\src\\neurus\\"), ANCHO_IMAGEN, ANCHO_IMAGEN);
        System.out.println("Pedazos encontrados "+imgs.size());
        if(imgs.size()!=12)
            return;
        for (int i = 0; i <imgs.size(); i++) {
            BufferedImage bufferedImage = imgs.get(i);
            double[] rgbValues=fractionRgbData(bufferedImage);
            double[] resultado=new double[12];
               
            Arrays.fill(resultado, 0);
            resultado[i]=1;         
            
            trainingSet.addElement(new SupervisedTrainingElement(rgbValues, resultado));
        }       
        net.learnInSameThread(trainingSet);
        System.out.println("Entrenado: "+url);
        nroFuente++;
       
    }
    
    public static double[] fractionRgbData(BufferedImage image) throws Exception {
        //ImageSampler.downSampleImage(new Dimension(30,30),
        FractionRgbData imgRgb = new FractionRgbData( image);
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
}
