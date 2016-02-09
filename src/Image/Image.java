package Image;

import java.awt.*;
import java.util.Arrays;

import static Utils.KohonenStaticFields.*;

/**
 * Created by Aga on 2016-02-01.
 */
public class Image {
    private final Color[][] imageAsColors;
    private final int width;
    private final int height;
    private final int buffersWide;
    private final int buffersHigh;

    public Image(Color[][] imageAsColors, int width, int height, int buffersWide, int buffersHigh) {
        this.imageAsColors = imageAsColors;
        this.width = width;
        this.height = height;
        this.buffersWide = buffersWide;
        this.buffersHigh = buffersHigh;
    }

    public Color[][] getImageAsColors() {
        return imageAsColors;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBuffersWide() {
        return buffersWide;
    }

    public int getBuffersHigh() {
        return buffersHigh;
    }

    public Color[][] getBufferAt(int x, int y) {
        Color[][] buffer = new Color[BUFFER_SIZE][BUFFER_SIZE];
        int b_i=0;
        for(int i=BUFFER_SIZE*x; i<BUFFER_SIZE*x+BUFFER_SIZE; i++){
            int b_j=0;
            for(int j=BUFFER_SIZE*y; j<BUFFER_SIZE*y+BUFFER_SIZE; j++){
                buffer[b_i][b_j] = imageAsColors[i][j];
                b_j++;
            }
            b_i++;
        }
        return buffer;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageAsColors=" + Arrays.toString(imageAsColors) +
                ", width=" + width +
                ", height=" + height +
                ", buffersWide=" + buffersWide +
                ", buffersHigh=" + buffersHigh +
                '}';
    }
}
