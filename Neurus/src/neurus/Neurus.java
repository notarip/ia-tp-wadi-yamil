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
import org.neuroph.contrib.imgrec.ColorMode;
import org.neuroph.contrib.imgrec.FractionRgbData;
import org.neuroph.contrib.imgrec.ImageRecognitionHelper;
import org.neuroph.contrib.imgrec.ImageRecognitionSample;
import org.neuroph.contrib.imgrec.ImageSampler;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;

/**
 *
 * @author ringo
 */
public class Neurus {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Time stamp N1:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));

        int maxIterations = 10000;
        NeuralNetwork neuralNet = new MultiLayerPerceptron(4, 9, 1);
        ((LMS) neuralNet.getLearningRule()).setMaxError(0.001);//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(0.7);//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        TrainingSet trainingSet = new TrainingSet();

        String imagePath = args[0];
        for (int i = 0; i < args.length; i++) {
            BufferedImage bi = ImageIO.read(new File(imagePath + "\\" + i + ".png"));
            trainingSet.addElement(new SupervisedTrainingElement(fractionRgbData(bi), new double[i]));
        }

        neuralNet.learnInSameThread(trainingSet);
        System.out.println("Time stamp N2:" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:MM").format(new Date()));

        TrainingSet testSet = new TrainingSet();
      //  testSet.addElement(new TrainingElement(new double[]{4223.0D / daxmax, 4259.0D / daxmax, 4203.0D / daxmax, 3989.0D / daxmax}));

        for (TrainingElement testElement : testSet.trainingElements()) {
            neuralNet.setInput(testElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            System.out.print("Input: " + Arrays.toString(testElement.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }

    }

    public static double[] fractionRgbData(BufferedImage image) {
        FractionRgbData imgRgb = new FractionRgbData(ImageSampler.downSampleImage(new Dimension(75, 75), image));
        double input[];

        
            input = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(imgRgb.getFlattenedRgbValues());
        return input;
    }
}
