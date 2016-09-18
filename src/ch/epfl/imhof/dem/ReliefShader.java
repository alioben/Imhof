package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe dessinant un relief ombré et coloré.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class ReliefShader {
    private final Projection projection;
    private final DigitalElevationModel model;
    private final Vector3 light;

    /**
     * 
     * @param projection
     *            Projection utilisée.
     * @param model
     *            Modèle d'élévation du terrain.
     * @param light
     *            Vecteur pointant dans la direction de la source lumineuse.
     */
    public ReliefShader(Projection projection, DigitalElevationModel model,
            Vector3 light) {
        this.projection = projection;
        this.model = model;
        this.light = light;
    }

    /**
     * 
     * @param bl
     *            Point bas gauche en coordonnées du plan.
     * @param tr
     *            Point haut droit en coordonnées du plan.
     * @param width
     *            Largeur en pixels de l'image à dessiner.
     * @param height
     *            Hauteur en pixels de l'image à dessiner.
     * @param radius
     *            Rayon de floutage.
     * @return Une image représentant un relief ombré et coloré.
     */
    public BufferedImage shadedRelief(Point bl, Point tr, int width,
            int height, double radius) {
        // Traite les deux cas ou le rayon est nul ou non nul
        if (radius == 0) {
            // Fonction de changement de coordonnées
            Function<Point, Point> rebasing = Point.alignedCoordinateChange(
                    new Point(0, height), bl, new Point(width, 0), tr);
            return ombrage(width, height, rebasing);
        } else {
            float[] data = gaussianBlur(radius);
            int limit = (int) Math.ceil(data.length / 2);
            // Fonction de changement de coordonnées
            Function<Point, Point> rebasing = Point.alignedCoordinateChange(
                    new Point(limit, height + limit), bl, new Point(width
                            + limit, limit), tr);
            BufferedImage image = ombrage(width + 2 * limit,
                    height + 2 * limit, rebasing);
            return blur(image, data).getSubimage(limit, limit, width,
                    height);
        }
    }

    /**
     * 
     * @param width
     *            Largeur de l'image produite
     * @param height
     *            Longueur de l'image produite
     * @param rebasing
     *            Fonction de changement de coordonnées
     * @return Un relief ombré brut
     */
    private BufferedImage ombrage(int width, int height,
            Function<Point, Point> rebasing) {
        BufferedImage ombrage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                PointGeo point = projection.inverse(rebasing.apply(new Point(x,
                        y)));
                Vector3 vector = model.normalAt(point).normalized();
                double cos = vector.scalarProduct(light.normalized());
                double r = 0.5 * (cos + 1), b = 0.5 * (0.7 * cos + 1);
                ombrage.setRGB(x, y, Color.toJavaColor(Color.rgb(r, r, b))
                        .getRGB());
            }
        }
        return ombrage;
    }

    /**
     * 
     * @param radius
     *            Rayon de floutage
     * @return Le noyau du flou gaussien (unidimensionnel) sous forme d'un
     *         tableau
     */
    private float[] gaussianBlur(double radius) {
        double sigma = radius / 3;
        int matrixSize = (int) (2 * Math.ceil(radius) + 1);
        double normal = 0;
        float[] kernel = new float[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            int x = i - (int) Math.ceil(matrixSize / 2);
            kernel[i] = (float) Math.exp(-(x * x) / (2 * sigma * sigma));
            normal += kernel[i];
        }
        for (int i = 0; i < matrixSize; i++) {
            kernel[i] *= (1 / normal);
        }
        return kernel;
    }

    /**
     * 
     * @param ombre
     *            Relief ombré brut
     * @param data
     *            Noyau du flou gaussien (unidimensionnel)
     * @return Image floutée
     */
    private BufferedImage blur(BufferedImage ombre, float[] data) {
        Kernel kernelV = new Kernel(1, data.length, data);
        Kernel kernelH = new Kernel(data.length, 1, data);
        ConvolveOp convolveH = new ConvolveOp(kernelV, ConvolveOp.EDGE_NO_OP,
                null);
        ConvolveOp convolveV = new ConvolveOp(kernelH, ConvolveOp.EDGE_NO_OP,
                null);
        return convolveV.filter(convolveH.filter(ombre, null), null);
    }
}
