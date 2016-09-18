package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Représente un modèle numérique du terrain lié à un fichier HGT.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {
    private final FileInputStream stream;
    private final long length;
    private ShortBuffer altitudes;
    private final int sizeBuffer;
    private final double longitude, latitude, angularResolution, dimension;

    /**
     * Construit un modèle numérique du terrain à partir du fichier HGT entré en
     * paramètre.
     * 
     * @param file
     *            le fichier HGT contenant le modèle numérique du terrain.
     * @throws IOException
     *             Signale la présence d'une erreur durant la lecture du
     *             fichier.
     * @throws IllegalArgumentException
     *             Lève une exception si le fichier HGT n'est pas valide.
     */
    public HGTDigitalElevationModel(File file) throws IOException {
        String name = file.getName();

        // Calcul de la dimension du fichier
        length = file.length();
        dimension = Math.sqrt(length / 2d);

        // Vérifie si le nom est le même du fichier HGT
        if (!name
                .matches("(N|S)(0[0-9]|[0-8][0-9]|90)(E|W)(0[0-9]{2}|1[0-7][0-9]|180)\\.hgt")
                || dimension != (int) dimension)
            throw new IllegalArgumentException("invalid file name: " + name);

        sizeBuffer = Math
                .toIntExact(Math.round((Math.sqrt(file.length() / 2)))) - 1;
        angularResolution = Math.toRadians(1d / sizeBuffer);

        // Extraction de la latitude et de la longitude du nom de fichier
        latitude = Math.toRadians(((name.charAt(0) == 'N') ? 1 : -1)
                * Integer.parseInt(name.substring(1, 3)));
        longitude = Math.toRadians(((name.charAt(3) == 'E') ? 1 : -1)
                * Integer.parseInt(name.substring(4, 7)));

        // Lecture du fichier HGT grace à un shortBuffer
        stream = new FileInputStream(file);
        altitudes = stream.getChannel().map(MapMode.READ_ONLY, 0, length)
                .asShortBuffer();
    }

    /**
     * @throws IllegalArgumentException
     *             Lève une exception si le point pour lequel la normale est
     *             demandé ne fait pas partie de la zone couverte par le MNT.
     */
    @Override
    public Vector3 normalAt(PointGeo point) {
        // Longitude et Latitude du point entré en paramètre
        double thisLatitude = point.latitude();
        double thisLongitude = point.longitude();

        // Vérifie si le point appartient à la zone
        if (!(thisLatitude >= latitude
                && thisLatitude <= latitude + Math.toRadians(1)
                && thisLongitude >= longitude && thisLongitude <= longitude
                + Math.toRadians(1)))
            throw new IllegalArgumentException("invalid argument: ("
                    + thisLongitude + "," + thisLatitude + ") ,(" + longitude
                    + "," + latitude + ")");

        // Extraction des positions du point
        int x = (int) ((thisLongitude - longitude) / angularResolution);
        int y = (int) ((thisLatitude - latitude) / angularResolution);

        // Construire le vecteur normale tridimensionnel 
        double s = Earth.RADIUS * angularResolution;
        return new Vector3(
                0.5

                        * s
                        * (getIndex(x, y) - getIndex(x + 1, y)
                                + getIndex(x, y + 1) - getIndex(x + 1, y + 1)),
                0.5

                        * s
                        * (getIndex(x, y) + getIndex(x + 1, y)
                                - getIndex(x, y + 1) - getIndex(x + 1, y + 1)),
                s * s);
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        altitudes = null;
        stream.close();
    }

    /**
     * 
     * @param x
     *            Colonne du point dans le tableau des altitudes
     * @param y
     *            Ligne du point dans le tableau des altitudes
     * @return l'element à la position donnée
     */
    private short getIndex(int x, int y) {
        return altitudes.get((sizeBuffer - y) * (sizeBuffer + 1) + x);
    }
}
