package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import ch.epfl.imhof.dem.DigitalElevationModel;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Programme principal.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class Main {

    public static final double INCH_TO_METER = 0.0254;

    /**
     * 
     * @param args
     *            Paramètre nécessaire au lancement du programme dans l'ordre
     *            qui suit: Chemin de la carte - Chemin du fichier HGT -
     *            Longitude du point bas gauche - Latitude du point bas gauche -
     *            Longitude du point haut droit - Latitude du point haut droit -
     *            Resolution de l'image - Chemin du fichier image de sortie
     * @throws Exception Lève une exception si les arguments ne sont pas valides
     */
    public static void main(String[] args) throws Exception {
        
        // Vérifier s'il y a les huit argument requis
        if(args.length != 8)
            throw new IllegalArgumentException("Invalid number of arguments !");
        
        // Extraction des paramètres
        String cheminCarte = args[0];
        String cheminHGT = args[1];
        PointGeo bl = new PointGeo(Math.toRadians(Double.parseDouble(args[2])),
                Math.toRadians(Double.parseDouble(args[3])));
        PointGeo tr = new PointGeo(Math.toRadians(Double.parseDouble(args[4])),
                Math.toRadians(Double.parseDouble(args[5])));
        int resolution = Integer.parseInt(args[6]);
        String output = args[7];
        
        // Declaration des paramètres
        Projection projection = new CH1903Projection();
        Point projectedBl = projection.project(bl);
        Point projectedTr = projection.project(tr);
        double resolutionPpM = resolution / INCH_TO_METER;
        int height = (int) Math.round((resolutionPpM * (1 / 25000d)
                * (tr.latitude() - bl.latitude()) * Earth.RADIUS));
        int width = (int) Math
                .round((((projectedTr.x() - projectedBl.x()) / (projectedTr.y() - projectedBl
                        .y())) * height));

        // Extraction de la carte
        OSMMap osmMap;

        // Déclaration d'un modèle numérique du terrain
        try (DigitalElevationModel hgtModel = new HGTDigitalElevationModel(
                new File(cheminHGT))) {

            // Déclaration du peintre
            Painter swissPainter = SwissPainter.painter();

            // Lecture de la carte
            osmMap = OSMMapReader.readOSMFile(cheminCarte, true);
            Map map = new OSMToGeoTransformer(projection).transform(osmMap);

            // Déclaration de la toile
            Java2DCanvas canvas = new Java2DCanvas(projectedBl, projectedTr,
                    width, height, resolution, Color.WHITE);

            // Dessin de la carte
            swissPainter.drawMap(map, canvas);

            // Creation d'une image de relief
            ReliefShader shader = new ReliefShader(projection, hgtModel,
                    new Vector3(-1, 1, 1));
            BufferedImage relief = shader.shadedRelief(projectedBl,
                    projectedTr, width, height, 1.7 * (resolutionPpM / 1000));

            // Assemblage du relief et de la carte
            BufferedImage finalImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Color currentColor;
            for (int x = 0; x < canvas.image().getWidth(); x++) {
                for (int y = 0; y < canvas.image().getHeight(); y++) {
                    currentColor = Color.multiply(
                            Color.rgb(canvas.image().getRGB(x, y)),
                            Color.rgb(relief.getRGB(x, y)));
                    finalImage.setRGB(x, y, Color.toJavaColor(currentColor).getRGB());
                }
            }

            // Stockage de la carte dans le fichier de sortie
            ImageIO.write(finalImage, "png", new File(output));
        }
    }
}
