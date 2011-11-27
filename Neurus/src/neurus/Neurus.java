/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import org.neuroph.contrib.imgrec.FractionRgbData;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;

/**
 *
 * @author ringo
 */
public class Neurus implements Observer{

    /**
     * @param args the command line arguments
     * 
     */
    
    
    public Neurus() throws Exception{
         
    }
    
    public void iniciar() throws Exception{
         int maxIterations = 1000;
        NeuralNetwork neuralNet = new MultiLayerPerceptron(225,450,225,120,12);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.08);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.01);//0-1
       // ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        
        
         // enable batch if using MomentumBackpropagation
       
        
        neuralNet.getLearningRule().addObserver(this);
        
        if( neuralNet.getLearningRule() instanceof MomentumBackpropagation ) 
            ((MomentumBackpropagation)neuralNet.getLearningRule()).setBatchMode(true);
        
        
         String dir = System.getProperty("user.dir").concat("/src/neurus/");
        agregarEntrenamiento(dir+"fuente1.png", neuralNet);
        //agregarEntrenamiento(dir+"fuente2.png", neuralNet);
//        agregarEntrenamiento(dir+"fuente3.png", neuralNet);
        
        
      
        TrainingSet testSet = new TrainingSet();
        
        ExtractorCaracteres ec=new ExtractorCaracteres();
        ArrayList<BufferedImage> ab=ec.recortar(new File(dir+"test.png"), new File("D:\\facu\\75.23 IA\\trunk\\Neurus\\src\\neurus\\"),15,15);
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
        TrainingSet trainingSet = new TrainingSet(225,12);
        ArrayList<BufferedImage> imgs=ec.recortar(new File(url), new File("D:\\facu\\75.23 IA\\trunk\\Neurus\\src\\neurus\\"), 15, 15);
        
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
//        net.save("redIntentoAmano");
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
          System.out.println( "Training, Network Epoch " + rule.getCurrentIteration() + ", Error:" + rule.getTotalNetworkError());
    }
}
