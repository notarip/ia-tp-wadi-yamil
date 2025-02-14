/**
 * RedNeuronal.java
 * Trabajo practico OCR de inteligencia artificial (75.23)
 * Grupo: Wadi Jalil Maluf 84780 - Yamil Jalil Maluf 79040
 * 2do cuatrimestre 2011
 */
package neurus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

public class RedNeuronal implements Observer, Serializable {

    private transient NeuralNetwork neuralNet;
    private int maxIterations;
    private double maxError;
    private int tEntrada;
    private int tSalida;
    private int anchoImagen;
    private int altoImagen;

    public RedNeuronal(int tEntrada, double error, int tCapa_intermedia, int tSalida, int ancho, int alto) {
        this.tEntrada = tEntrada;
        this.tSalida = tSalida;
        neuralNet = new MultiLayerPerceptron(tEntrada, tCapa_intermedia, tSalida);
        maxIterations = 1000; //Valor por defecto
        setMaximoError(maxError);
        if (neuralNet.getLearningRule() instanceof MomentumBackpropagation) {
            ((MomentumBackpropagation) neuralNet.getLearningRule()).setBatchMode(true);
        }
        anchoImagen = ancho;
        altoImagen = alto;
        addObserver(this);
    }

    static RedNeuronal cargarRed(String archivo) {
        RedNeuronal red = null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(archivo);
            in = new ObjectInputStream(fis);
            red = (RedNeuronal) in.readObject();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        NeuralNetwork net = NeuralNetwork.load(archivo + ".red");
        red.neuralNet = net;
        return red;
    }

    public void guardar(String archivo) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(archivo);
            out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
            neuralNet.save(archivo + ".red");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Se agrega una imagen que contiene los 12 digitos a reconocer. 
     * Si no se pudo dividir correctamente la imagen en 12 partes se devuelve false
     */
    public boolean lanzarEntrenamientoImagen(File fuente) throws Exception {

        ExtractorCaracteres ec = new ExtractorCaracteres();
        TrainingSet trainingSet = new TrainingSet(this.tEntrada, this.tSalida);
        ArrayList<BufferedImage> imgs = ec.recortar(fuente, null, anchoImagen, altoImagen);
        if (imgs.size() != 12) {
            System.out.println("La imagen pasada para entrenamiento " + fuente.getName() + " no se pudo interpretar correctamente");
            return false;
        }
        //A cada caracter extraido se le asigna su valor para generar el conjunto
        //de entrenamiento
        for (int i = 0; i < imgs.size(); i++) {
            BufferedImage bufferedImage = imgs.get(i);
            double[] rgbValues = transformarRGBtoInputRed(bufferedImage);
            double[] resultado = new double[12];
            Arrays.fill(resultado, 0);
            resultado[i] = 1;
            trainingSet.addElement(new SupervisedTrainingElement(rgbValues, resultado));
        }
        this.neuralNet.learnInSameThread(trainingSet);
        return true;
    }

    public boolean isEntrenamientoStopped() {
        return neuralNet.getLearningRule().isStopped();
    }

    public String reconocerImagen(File imagen) throws Exception {

        StringBuilder resultado = new StringBuilder();
        TrainingSet testSet = new TrainingSet();
        ExtractorCaracteres extractor = new ExtractorCaracteres();
        ArrayList<BufferedImage> listaCaracteres = extractor.recortar(imagen, null, anchoImagen, altoImagen);

        //Se agregan todos los caracteres encontrados en la imagen al conjunto para ser reconocios
        for (BufferedImage image : listaCaracteres) {
            testSet.addElement(new TrainingElement(transformarRGBtoInputRed(image)));
        }
        //Para cada elemento agregado al conjunto se efectua el reconocimiento
        for (TrainingElement testElement : testSet.trainingElements()) {
            neuralNet.setInput(testElement.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            String ch;
            int salida = interpretarSalida(networkOutput);
            //Se mapea la salida con el valor que le corresponde
            if (salida == 10) {
                ch = ".";
            } else {
                if (salida == 11) {
                    ch = ",";
                } else {
                    ch = String.valueOf(salida);
                }
            }
            resultado.append(ch);
        }
        return resultado.toString();
    }

    /**
     * Encuentra en la salida de la red el resultado mas probale
     * del reconocimiento. Para ello se encuetra el maximo 
     * valor de la salida
     * @param salida es el resultado arrojado por la red neuronal
     * @return 
     */
    private int interpretarSalida(double[] salida) {
        double max = 0;
        int resultado = 0;
        for (int i = 0; i < salida.length; i++) {
            if (salida[i] > max) {
                resultado = i;
                max = salida[i];
            }
        }
        return resultado;
    }

    /*
     * Devuelve un array de double equivalente al contenido de una imagen
     */
    private double[] transformarRGBtoInputRed(BufferedImage image) throws Exception {
        FractionRgbData imgRgb = new FractionRgbData(image);
        double input[];
        input = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(imgRgb.getFlattenedRgbValues());
        return input;
    }

    public final void setMaximoError(double er) {
        ((LMS) neuralNet.getLearningRule()).setMaxError(er);
        this.maxError = er;
    }

    public void addObserver(Observer ob) {
        neuralNet.getLearningRule().addObserver(ob);
    }

    @Override
    public void update(Observable o, Object arg) {
        SupervisedLearning rule = (SupervisedLearning) o;
        if (rule != null)
        {
            System.out.println("Training, Network Epoch " + rule.getCurrentIteration() + ", Error:" + rule.getTotalNetworkError());
        }
    }

    public int getAltoImagen() {
        return altoImagen;
    }

    public void setAltoImagen(int altoImagen) {
        this.altoImagen = altoImagen;
    }

    public int getAnchoImagen() {
        return anchoImagen;
    }

    public void setAnchoImagen(int anchoImagen) {
        this.anchoImagen = anchoImagen;
    }

    public double getMaxError() {
        return maxError;
    }

    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }
}
