package ch.epfl.imhof.geometry;

/**
 * Construit un Polygon avec ou sans trous.
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Polygon{

    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Construit un Polygon avec les trous et l'enveloppe passés en paramètre
     * 
     * @param shell
     *            : l'enveloppe du polygone
     * @param holes
     *            : les trous du polygone
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections
                .unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }

    /**
     * Construit un Polygon sans trous, avec l'enveloppe passés en paramètre
     * 
     * @param shell
     *            : l'enveloppe du polygone
     */
    public Polygon(ClosedPolyLine shell) {
        this.shell = shell;
        this.holes = Collections
                .unmodifiableList(new ArrayList<ClosedPolyLine>());
    }

    /**
     * @return L'enveloppe du Polygon
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * @return La liste des trous du Polygon
     */
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
