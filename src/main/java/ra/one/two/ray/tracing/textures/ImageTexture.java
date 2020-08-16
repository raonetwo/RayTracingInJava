package ra.one.two.ray.tracing.textures;

import ra.one.two.ray.tracing.primitives.math.Vec3;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageTexture implements Texture {
    private final BufferedImage image;

    public ImageTexture(final String imagePath) throws IOException {
        image = ImageIO.read(new File(imagePath));
    }

    public ImageTexture(final InputStream imageStream) throws IOException {
        image = ImageIO.read(imageStream);
    }

    @Override
    public Vec3 value(TextureCoordinates textureCoordinates, Vec3 point) {
        if (image == null) {
            return null;
        }
        // Clamp input texture coordinates to [0,1] x [1,0]
        double u = clamp(textureCoordinates.getUTextureCoordinate(), 0.0, 1.0);
        double v = 1.0 - clamp(textureCoordinates.getVTextureCoordinate(), 0.0, 1.0);  // Flip V to image coordinates

        int i = (int) (u * (image.getWidth()));
        int j = (int) (v * (image.getHeight()));

        // Clamp integer mapping, since actual coordinates should be less than 1.0
        if (i >= image.getWidth()) {
            i = image.getWidth() - 1;
        }
        if (j >= image.getHeight()) {
            j = image.getHeight() - 1;
        }

        final Color color = new Color(image.getRGB(i, j));
        final double colorScale = 255.0;
        ;
        return new Vec3(color.getRed(), color.getGreen(), color.getBlue()).scaleDown(colorScale);
    }

    private double clamp(final double value, final double min, final double max) {
        return Math.min(max, Math.max(min, value));
    }
}
