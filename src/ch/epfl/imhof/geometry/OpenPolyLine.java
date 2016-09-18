package ch.epfl.imhof.geometry;

import java.io.Serializable;
/**
 * Construit un PolyLine ouvert.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
import java.util.List;

public final class OpenPolyLine extends PolyLine implements Serializable{

    private static final long serialVersionUID = 4215798506057348123L;

    /**
     * Construit un PolyLine fermé
     * 
     * @param points
     *            : Liste des points sommets
     * @throws IllegalArgumentException
     *             : si la liste des points est vide
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * @return Faux car le PolyLine est ouvert
     */
    @Override
    public boolean isClosed() {
        return false;
    }

}
