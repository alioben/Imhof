package ch.epfl.imhof;

/**
 * Un point à la surface de la Terre, en coordonnées sphériques.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */

final public class PointGeo {

    private final double longitude, latitude;

    /**
     * Construit un point avec la latitude et la longitude données.
     *
     * @param longitude:
     *            la longitude du point, en radians
     * @param latitude:
     *            la latitude du point, en radians
     * @throws IllegalArgumentException:
     *             si la longitude est invalide, c-à-d hors de l'intervalle [-π;
     *             π]
     * @throws IllegalArgumentException:
     *             si la latitude est invalide, c-à-d hors de l'intervalle
     *             [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {
        if (!(longitude >= -Math.PI && longitude <= Math.PI))
            throw new IllegalArgumentException();
        else
            this.longitude = longitude;
        if (!(latitude >= -Math.PI / 2 && latitude <= Math.PI / 2))
            throw new IllegalArgumentException();
        else
            this.latitude = latitude;
    }

    /**
     * @return La longitude du point.
     */
    public double longitude() {
        return longitude;
    }

    /**
     * @return La latitude du point.
     */
    public double latitude() {
        return latitude;
    }

}
