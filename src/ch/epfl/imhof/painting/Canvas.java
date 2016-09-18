package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
/**
 * Représente une toile
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public interface Canvas {
    /**
     * 
     * @param style Style de ligne utilisé
     * @param polyline Polyligne à dessiner
     */
    public void drawPolyLine(LineStyle style, PolyLine polyline);
   /**
    * 
    * @param color Couleur du polygone
    * @param polygon Polygone à dessiner
    */
    public void drawPolygon(Color color, Polygon polygon);
}
