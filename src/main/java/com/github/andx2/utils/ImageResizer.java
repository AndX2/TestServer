package com.github.andx2.utils;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;

import static com.github.andx2.AppConfig.*;

public class ImageResizer implements Runnable {

    public static final int AVATAR_MAX_SIZE = 100;
    public static final int IMG_MAX_SIZE = 800;

    private Path sourcePath;
    private Path outPath;

    public ImageResizer(String sourcePath, String outPath) {
        this.sourcePath = new File(sourcePath).toPath();
        this.outPath = new File(outPath).toPath();

    }

    public static void resize(Path out, Path file) {
        try {
            String fileName = file.getFileName().toString();
            BufferedImage originalImage = ImageIO.read(file.toFile());
            BufferedImage scaledImage = Scalr.resize(originalImage, IMG_MAX_SIZE);
            ImageIO.write(scaledImage, "jpg", new File(out.toFile().getPath() + File.separator + "s-" + fileName));
            BufferedImage scaledAvatar = Scalr.resize(originalImage, AVATAR_MAX_SIZE);
            ImageIO.write(scaledAvatar, "jpg", new File(out.toFile().getPath() + File.separator + "m-" + fileName));
        } catch (IOException e) {
            System.out.println("resize IOException");
        }
    }


    @Override
    public void run() {
        String ext = ".jpg";
        FilenameFilter filter = new MyFileNameFilter(ext);
        File outDir = new File(PATH_IMAGES);
        outDir.mkdir();

        while (true) {
            try {
                Thread.currentThread().sleep(1000);
                File source = sourcePath.toFile();
                File[] files = source.listFiles(filter);
                for (File file : files) {
                    System.out.println("resize image: " + file.getName());
                    resize(outDir.toPath(), file.toPath());
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static class MyFileNameFilter implements FilenameFilter {

        private String ext;

        public MyFileNameFilter(String ext) {
            this.ext = ext.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(ext);
        }
    }

    public static void main(String[] args) {
        ImageResizer resizer = new ImageResizer(PATH_UPLOAD, PATH_IMAGES);
        new Thread(resizer).start();
    }
}
