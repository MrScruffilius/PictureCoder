import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PicCoder {
    private static final Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws IOException {

        String desicion = "0";

        System.out.println("1 - Encode text to .png");
        System.out.println("2 - Decode .png to text");
        System.out.println("PLEASE PRESS 1 OR 2:");
        while (!desicion.equals("1") && !desicion.equals("2")) {


            desicion = sc.next().toLowerCase();
            if (desicion.equals("2")) {
                String path;

                System.out.println("WRITE A FILENAME:");
                while (true) {
                    path = sc.next();
                    if (Files.isReadable(Path.of(path + ".png"))) {
                        path = (path + ".png");
                        break;
                    }else{
                        System.out.println("File not found!");
                    }

                }


                File file = new File(path);


                BufferedImage img = ImageIO.read(file);
                int width = img.getWidth();
                int height = img.getHeight();
                List<Byte> list = new ArrayList<>();
                boolean isDone = false;
                Raster raster = img.getData();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {


                        int[] rgb = new int[5];
                        raster.getPixel(x, y, rgb);


                        if (rgb[0] != 0) {
                            list.add((byte) rgb[0]);
                        } else isDone = true;
                        if (rgb[1] != 0) {
                            list.add((byte) rgb[1]);
                        } else isDone = true;
                        if (rgb[2] != 0) {
                            list.add((byte) rgb[2]);
                        } else isDone = true;

                        if (isDone) break;
                    }
                    if (isDone) break;
                }
                byte[] bytes = new byte[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    bytes[i] = list.get(i);
                }

                System.out.println(new String(bytes, StandardCharsets.UTF_8));

            } else if (desicion.equals("1")) {
                System.out.println("PLEASE WRITE TEXT:");

                String text = sc.next() + sc.nextLine();

                byte[] bytes = text.getBytes(StandardCharsets.UTF_8);


                int size = (int) Math.ceil(Math.sqrt((float) bytes.length / 3));
                if (size == 0) size = 1;


                int[][] pixel2 = new int[size][size];

                int pointer = 0;
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {

                        int r = 0;
                        int g = 0;
                        int b = 0;

                        if (pointer != bytes.length) {
                            r = bytes[pointer];
                            pointer++;
                        }
                        if (pointer != bytes.length) {
                            g = bytes[pointer];
                            pointer++;
                        }
                        if (pointer != bytes.length) {
                            b = bytes[pointer];
                            pointer++;
                        }


                        pixel2[x][y] = (r << 16) ^ (g << 8) ^ (b);
                    }
                    if (pointer == bytes.length) break;
                }


                BufferedImage theImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

                for (int i = 0; i < pixel2.length; i++) {
                    for (int j = 0; j < pixel2[0].length; j++) {

                        theImage.setRGB(i, j, pixel2[i][j]);
                    }
                }

                System.out.println("PLEASE DEFINE FILENAME:");
                String outputName = sc.next();
                File outputfile = new File(outputName + ".png");
                try {
                    ImageIO.write(theImage, "png", outputfile);
                } catch (IOException ignored) {
                }
            }
        }
    }

}
