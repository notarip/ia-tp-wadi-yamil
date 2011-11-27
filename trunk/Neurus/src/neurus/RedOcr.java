/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neurus;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author asus
 */
public class RedOcr extends BackPropagation{

    @Override
    public void learn(TrainingSet trainingSet) {
        super.learn(trainingSet);
        trainingSet.iterator();
        
        
    }
  
    
}
