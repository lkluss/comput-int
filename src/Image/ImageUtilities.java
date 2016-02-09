package Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import Neuron.Neuron;

import static Utils.KohonenStaticFields.*;

/**
 * Created by Aga on 2016-02-01.
 */
public final class ImageUtilities {

    public static Image createPaddedImage(File input) throws IOException {
        BufferedImage imageToCompress = ImageIO.read(input);
        int width = imageToCompress.getWidth();
        int height = imageToCompress.getHeight();

        int buffersWide = (int) Math.ceil(width / BUFFER_SIZE * 1.0)+1;
        int buffersHigh = (int) Math.ceil(height / BUFFER_SIZE * 1.0)+1;

        Color[][] imageAsColor = new Color[buffersWide*BUFFER_SIZE][buffersHigh*BUFFER_SIZE];

        //most of these will be fed directly to the gc after the next loop executes
        for(int i=0; i<buffersWide*BUFFER_SIZE; i++) {
            for (int j = 0; j < buffersHigh * BUFFER_SIZE; j++) {
                imageAsColor[i][j] = new Color(0,0,0);
            }
        }

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                imageAsColor[i][j] = new Color(imageToCompress.getRGB(i, j));
            }
        }

        return new Image(imageAsColor, width, height, buffersWide, buffersHigh);
    }

    public static java.util.List<Double> getNormalisedBuffer(Color[][] buffer) {
        java.util.List<Double> result = new ArrayList<>();

        for(int i=0; i<BUFFER_SIZE; i++){
            for(int j=0; j<BUFFER_SIZE; j++){
                if(COLOURS==3){
                    result.add((double) buffer[i][j].getRed());
                    result.add((double) buffer[i][j].getGreen());
                    result.add((double) buffer[i][j].getBlue());
                } else if (COLOURS==1){
                    result.add((double) buffer[i][j].getRed() + (double) buffer[i][j].getGreen() + (double) buffer[i][j].getBlue());
                }
            }
        }

        Double sqrtSum = Math.sqrt(result.stream().reduce((double) 0, (a, b) -> a + b * b));

        if(sqrtSum!=0) {
            result = result.stream().map($ -> $ / sqrtSum).collect(Collectors.toList());
        }
        return result;
    }

    public static void writeNeuralImage(Map<Dimension, Neuron> codedImage, BufferedImage img) {
        for (Map.Entry<Dimension, Neuron> e : codedImage.entrySet()) {
            Neuron neuron = e.getValue();
            int bufferXIndex = (int) e.getKey().getWidth();
            int bufferYIndex = (int) e.getKey().getHeight();

            double[] weights = neuron.getWeights();
            double[] data;
            if(COLOURS==1){
                data = Arrays.stream(weights).map(value -> mapToPixelScale(value)).toArray();
            } else if(COLOURS==3){
                data = new double[3*BUFFER_SIZE*BUFFER_SIZE];
                int dataIndex = 0;
                for (int i = 0; i < weights.length; i+=3) {
                    data[dataIndex]= mapToPixelScale(weights[i+2]);
                    data[dataIndex+1]= mapToPixelScale(weights[i+1]);
                    data[dataIndex+2]= mapToPixelScale(weights[i]);

                    dataIndex+=3;
                }
            }

            Double max = Arrays.stream(data).max().getAsDouble();
            Double min = Arrays.stream(data).min().getAsDouble();

            int[] ints = Arrays.stream(data).map($ -> (($ - min) / (max - min))*255).mapToInt($ -> (int) $).toArray();

            if(COLOURS==1){
                for (int i = 0; i < ints.length; i++) {
                    img.setRGB(
                            BUFFER_SIZE*bufferXIndex + i % BUFFER_SIZE,
                            BUFFER_SIZE*bufferYIndex + (int) Math.floor(i/BUFFER_SIZE),
                            new Color(ints[i],ints[i],ints[i]).getRGB()
                    );
                }
            } else if(COLOURS==3){
                for (int i = 0; i < ints.length; i+=3) {
                    img.setRGB(
                            BUFFER_SIZE*bufferXIndex + i % BUFFER_SIZE,
                            BUFFER_SIZE*bufferYIndex + (int) Math.floor(i/(3*BUFFER_SIZE)),
                            new Color(ints[i+2],ints[i+1],ints[i+1]).getRGB()
                    );
                }
            }
        }
    }

    private static double mapToPixelScale(double value) {
        return Math.abs(value*255);
    }
}
