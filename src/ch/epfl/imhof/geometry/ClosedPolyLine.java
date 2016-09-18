package ch.epfl.imhof.geometry;

/**
 * Construit un PolyLine fermé.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */

import java.io.Serializable;
import java.util.List;

public final class ClosedPolyLine extends PolyLine implements Serializable{

    private static final long serialVersionUID = 3678455933898290274L;

    /**
     * Construit un PolyLine fermé
     * 
     * @param points
     *            : Liste des points sommets
     * @throws IllegalArgumentException
     *             : si la liste des points est vide
     */
    public ClosedPolyLine(List<Point> points){
        super(points);
    }

    /**
     * @return Vrai car le PolyLine est fermé
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * @return L'aire positive du PolyLine fermé
     */
    public double area() {
        List<Point> points = super.points();
        double area = 0;
        for (int i = 0; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            Point nextPoint = points.get(Math.floorMod(i + 1, points.size()));
            Point previousPoint = points.get(Math.floorMod(i - 1, points.size()));
            area += currentPoint.x() * (nextPoint.y() - previousPoint.y());
        }
        return Math.abs(area / 2);
    }

    /**
     * Verifie si le PolyLine contient le point rentré en paramètre;
     * 
     * @param p
     *            : point en question
     * @return Si le PolyLine contient ou pas le point rentré en paramètre
     */
    public boolean containsPoint(Point p) {
        List<Point> points = super.points();
        int index = 0;
        for (int i = 0; i < points.size(); i++) {
            Point point1 = points.get(i);
            Point point2 = points.get((i + 1) % points.size());
            if (point1.y() <= p.y()) {
                if (point2.y() > p.y() && isLeftSide(point1, point2, p))
                    index++;
            } else {
                if (point2.y() <= p.y() && isLeftSide(point2, point1, p))
                    index--;
            }
        }
        return (index != 0);
    }

    private boolean isLeftSide(Point point1, Point point2, Point point) {
        return (point1.x() - point.x()) * (point2.y() - point.y()) > (point2.x() - point.x())
                * (point1.y() - point.y());
    }
    
}
