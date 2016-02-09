import Activation.LinearActivationFunction;
import Neuron.Neuron;
import Image.Image;
import Image.ImageUtilities;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.KohonenStaticFields.*;

/**
 * Created by Aga on 2016-02-01.
 */
public class KohonenTest {

    List<Neuron> neurons;
    Image inputImage;

    @Test
    public void run(){
    neurons = new ArrayList<>();

        for (int i = 0; i <  NUMBER_OF_NEURONS; i++) {
            Neuron n = new Neuron();
            n.setActivationFunction(new LinearActivationFunction());

            //set random weights
            double[] weights = new double[BUFFER_SIZE*BUFFER_SIZE*COLOURS];
            for (int wi = 0; wi < weights.length; wi++) {
                weights[wi] = (Math.random() - 0.5)*2;
            }
            n.setWeights(0.0, weights);
            n.setPreviousWeights(weights);
            neurons.add(n);
        }

        try{
            File imageFile = new File(PATHNAME + FILENAME);
            System.out.println("Total Space: "+  imageFile.getTotalSpace());
            if (!imageFile.exists()) {
                throw new RuntimeException("ERROR: missing input file");
            }
            inputImage = ImageUtilities.createPaddedImage(imageFile);
        }catch(IOException ioe){

        }

        for(int epochNum=0; epochNum<MAX_EPOCHS; epochNum++) {

            for (int i = 0; i < inputImage.getBuffersWide(); i++) {
                for (int j = 0; j < inputImage.getBuffersHigh(); j++) {

                    Color[][] buffer = inputImage.getBufferAt(i, j);

                    List<Double> normalisedBuffer = ImageUtilities.getNormalisedBuffer(buffer);

                    double[] input = normalisedBuffer.stream().mapToDouble($ -> $).toArray();
                    for (Neuron neuron : neurons) {
                        neuron.setInputs(input);
                        neuron.calculateOutput();
                    }

                    Neuron bestFittingNeuron = neurons.get(0);
                    for (Neuron neuron : neurons) {
                        if (neuron.getAnswer() > bestFittingNeuron.getAnswer()) {
                            bestFittingNeuron = neuron;
                        }
                    }

                    double[] weights = bestFittingNeuron.getWeights();
                    for (int k = 0; k < input.length; k++) {
                        double delta = (input[k] - weights[k]) * TEACHING_FACTOR;
                        if(Double.isNaN(delta)){
                            throw new IllegalArgumentException();
                        }
                        weights[k] += delta;
                    }

                }
            }

        }

        Map<Dimension, Neuron> codedImage = new HashMap<>(inputImage.getBuffersWide() * inputImage.getBuffersHigh());

        for (int i = 0; i < inputImage.getBuffersWide(); i++) {
            for (int j = 0; j < inputImage.getBuffersHigh(); j++) {
                Color[][] buffer = inputImage.getBufferAt(i, j);

                List<Double> normalisedBuffer = ImageUtilities.getNormalisedBuffer(buffer);
                double[] input = normalisedBuffer.stream().mapToDouble($ -> $).toArray();

                int bestAnswerNeuronIndex;
                double bestAnswer;

                for (Neuron neuron : neurons) {
                    neuron.setInputs(input);
                    neuron.calculateOutput();
                }

                bestAnswerNeuronIndex = 0;
                bestAnswer = neurons.get(0).getAnswer();

                for (int k = 0; k < neurons.size(); k++) {
                    double answer = neurons.get(k).getAnswer();
//                    System.out.println("Answer["+k+"]:"+answer);
                    if(answer>bestAnswer){
                        bestAnswer=answer;
                        bestAnswerNeuronIndex = k;
                    }
                }

                codedImage.put(new Dimension(i,j), neurons.get(bestAnswerNeuronIndex));
            }
        }

        BufferedImage img = new BufferedImage(BUFFER_SIZE * inputImage.getBuffersWide(), BUFFER_SIZE * inputImage.getBuffersHigh(), BufferedImage.TYPE_3BYTE_BGR);
        System.out.println("Zapisuję obraz");
        ImageUtilities.writeNeuralImage(codedImage, img);
        try {
            ImageIO.write(img, "png", new File(PATHNAME + FILENAME.substring(0, FILENAME.length() - 4) + "_tmp.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Obraz zapisano");

    }


    public void loadFile(){

    }


}
