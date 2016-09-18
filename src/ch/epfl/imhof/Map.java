package ch.epfl.imhof;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente une carte projetée, composée d'entités géométriques attribuées.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class Map{

    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Construit une carte projetée
     * 
     * @param polyLines
     *            : listes de polylignes attribuées
     * @param polygons
     *            : listes de polygones attribuées
     */
    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections
                .unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * 
     * @return la listes de polylignes attribuées
     */

    public List<Attributed<PolyLine>> polyLines() {
        return this.polyLines;
    }

    /**
     * 
     * @return la listes de polygones attribuées
     */
    public List<Attributed<Polygon>> polygons() {
        return this.polygons;
    }

    /**
     * Bâtisseur de la classe Map
     * 
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     *
     */
    public static class Builder {
        private List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * ajoute une polyligne attribuée à la carte en cours de construction
         * 
         * @param newPolyLine
         *            : polyligne attribuée à ajouter
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }

        /**
         * ajoute un polygone attribuée à la carte en cours de construction
         * 
         * @param newPolyLine
         *            : polygone attribuée à ajouter
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }

        /**
         * construit une carte avec les polylignes et polygones ajoutés jusqu'à
         * présent au bâtisseur
         * 
         * @return un objet Map avec avec les polylignes et polygones ajoutés
         *         jusqu'à présent au bâtisseur
         */
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}
