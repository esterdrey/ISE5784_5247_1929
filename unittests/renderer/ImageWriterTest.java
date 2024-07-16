package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static java.awt.Color.*;


class ImageWriterTest {

    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter("yellow", 800, 500);
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                Color color = i % 50 == 0 || j % 50 == 0 ? new Color(RED) : new Color(YELLOW);
                imageWriter.writePixel(i, j, color);
            }
        }

        imageWriter.writeToImage();
    }
}