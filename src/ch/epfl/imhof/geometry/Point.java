package ch.epfl.imhof.geometry;


import java.util.function.Function;

/**
 * Construit un Point avec des coordonnées cartsiennes.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */

final public class Point{

    private final double x, y;

    /**
     * @param x
     *            : coordonnée x du point
     * @param y
     *            : coordonnée y du point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return La coordonée x du point
     */
    public double x() {
        return x;
    }

    /**
     * @return La coordonée y du point
     */
    public double y() {
        return y;
    }

    /**
     * 
     * @param pointDepartA
     *            Point 1 dans la base de départ.
     * @param pointArriveeA
     *            Point 1 dans la base d'arrivée.
     * @param pointDepartB
     *            Point 2 dans la base de départ.
     * @param pointArriveeB
     *            Point 2 dans la base d'arrivée.
     * @return La fonction responsable du changement de base caractérisé par les
     *         quatres points passés en arguments.
     */
    public static Function<Point, Point> alignedCoordinateChange(
            Point pointDepartA, Point pointArriveeA, Point pointDepartB,
            Point pointArriveeB) {
        double detX = 1 / ((pointDepartA.x) - (pointDepartB.x));
        double detY = 1 / ((pointDepartA.y) - (pointDepartB.y));
        double[] xAffine = {
                detX * (pointArriveeA.x - pointArriveeB.x),
                detX
                        * (pointDepartA.x * pointArriveeB.x - pointDepartB.x
                                * pointArriveeA.x) };
        double[] yAffine = {
                detY * (pointArriveeA.y - pointArriveeB.y),
                detY
                        * (pointDepartA.y * pointArriveeB.y - pointDepartB.y
                                * pointArriveeA.y) };
        return (point) -> {
            double newX = point.x * xAffine[0] + xAffine[1];
            double newY = point.y * yAffine[0] + yAffine[1];
            return new Point(newX, newY);
        };
    }
}
