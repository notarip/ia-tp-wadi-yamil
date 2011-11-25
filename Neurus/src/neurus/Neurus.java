/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.imageio.ImageIO;
import org.neuroph.contrib.imgrec.FractionRgbData;
import org.neuroph.contrib.imgrec.ImageRecognitionSample;
import org.neuroph.contrib.imgrec.ImageSampler;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Hopfield;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author ringo
 */
public class Neurus {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        

        int maxIterations = 800;
        NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, 2,4,1);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.01);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.01);//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        
        
         // enable batch if using MomentumBackpropagation
        if( neuralNet.getLearningRule() instanceof MomentumBackpropagation )
            ((MomentumBackpropagation)neuralNet.getLearningRule()).setBatchMode(true);
        
        TrainingSet trainingSet = new TrainingSet(2,1);

        String imagePath = "D:\\Dropbox\\facu\\75.23 Inteligencia Artificial\\Tp Red Neuronal";
        for (int i = 1; i <=3 ; i++) {
            BufferedImage bi = ImageIO.read(new File(imagePath + "\\" + i + ".png"));
            //trainingSet.addElement(new SupervisedTrainingElement(fractionRgbData(bi), new double[]{i/10}));
            trainingSet.addElement(new SupervisedTrainingElement(new double[]{i/10,i/10}, new double[]{i/10}));
            //trainingSet.addElement(new SupervisedTrainingElement(new double[]{i}, new double[]{i}));
            
        }
        
        neuralNet.learnInSameThread(trainingSet);
        
      
        TrainingSet testSet = new TrainingSet();
        BufferedImage bi = ImageIO.read(new File(imagePath + "\\test.png"));
        a=1;
        //testSet.addElement(new TrainingElement(fractionRgbData(bi)));
        testSet.addElement(new TrainingElement(new double[]{3/10,3/10}));

        for (TrainingElement testElement : testSet.trainingElements()) {
            neuralNet.setInput(testElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }

       
    }

    static int a=0;
    public static double[] fractionRgbData(BufferedImage image) throws Exception {
        FractionRgbData imgRgb = new FractionRgbData(ImageSampler.downSampleImage(new Dimension(15, 15), image));
        double input[];
        
        
            input = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(imgRgb.getFlattenedRgbValues());
           
            System.out.println( Arrays.toString(input));
            for (int i = 0; i < input.length; i++) {
            input[i]=a;
         
            }
            a++;
        return input;
    }
}
