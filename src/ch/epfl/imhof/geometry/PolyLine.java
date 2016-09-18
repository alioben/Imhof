package ch.epfl.imhof.geometry;

/**
 * Classe abstraite des PolyLines
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PolyLine{

    private final List<Point> points;

    /**
     * @throws IllegalArgumentException
     *             : si la liste des points est nulle
     * @param points
     *            : Liste des points sommets du PolyLine (polygone mathematique)
     */
    public PolyLine(List<Point> points) {
        if (points == null || points.size() == 0)
            throw new IllegalArgumentException();
        else
            this.points = Collections.unmodifiableList(new ArrayList<>(
                    points));
    }

    /**
     * 
     * @return Si le PolyLine est fermé ou ouvert
     */
    public abstract boolean isClosed();

    /**
     * @return La liste des points sommets du PolyLine
     */
    public List<Point> points() {
        return this.points;
    }

    /**
     * @return Le premier point sommet de la liste
     */
    public Point firstPoint() {
        Point fp = points.get(0);
        return fp;
    }

    /**
     * Batit un PolyLine modifiable
     * 
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public final static class Builder {

        private List<Point> points;

        /**
         * Construit un PolyLine vide
         */
        public Builder() {
            points = new ArrayList<Point>();
        }

        /**
         * Ajoute newPoint à la liste des points sommets du PolyLine
         * 
         * @param newPoint
         *            : Point à ajouter dans la liste des points
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * @return Un PolyLine ouvert de type OpenPolyLine
         */
        public OpenPolyLine buildOpen() {
            OpenPolyLine openPolyLine = new OpenPolyLine(points);
            return openPolyLine;
        }

        /**
         * @return Un PolyLine fermé de type ClosedPolyLine
         */
        public ClosedPolyLine buildClosed() {
            ClosedPolyLine closedPolyLine = new ClosedPolyLine(points);
            return closedPolyLine;
        }
    }
}
