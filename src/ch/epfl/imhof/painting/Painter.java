package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Interface Représentant un peintre
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public interface Painter {
    public void drawMap(Map map, Canvas canvas);

    /**
     * 
     * @param backgroundColor
     *            Couleur des polygones
     * @return Un peintre des polygones
     */
    public static Painter polygon(Color backgroundColor) {
        return (map, canvas) -> {
            List<Attributed<Polygon>> polygons = map.polygons();
            for (Attributed<Polygon> polygon : polygons)
                canvas.drawPolygon(backgroundColor, polygon.value());
        };
    }

    /**
     * 
     * @param style
     *            Style de ligne à utiliser
     * @return Un peintre de lignes
     */
    public static Painter line(LineStyle style) {
        return (map, canvas) -> {
            for (Attributed<PolyLine> polyline : map.polyLines())
                canvas.drawPolyLine(style, polyline.value());
        };
    }

    /**
     * 
     * @param width
     *            Largeur des lignes
     * @param color
     *            Couleurs des Lignes
     * @param lineCap
     *            Terminaison des Lignes
     * @param lineJoin
     *            Jointure des segments
     * @param alternance
     *            des section opaques et transparentes, pour le dessin en
     *            traitillés.
     * @return Un peintre de lignes
     */
    public static Painter line(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... alternance) {
        LineStyle style = new LineStyle(width, color, lineCap, lineJoin,
                alternance);
        return line(style);
    }

    /**
     * 
     * @param width
     *            Largeurs des lignes
     * @param color
     *            Couleur des lignes
     * @return Un peintre de lignes
     */
    public static Painter line(float width, Color color) {
        LineStyle style = new LineStyle(width, color);
        return line(style);
    }

    /**
     * 
     * @param style
     *            Sytle de ligne utilisé
     * @return Un peintre de bordures
     */
    public static Painter outline(LineStyle style) {
        return (map, canvas) -> {
            ClosedPolyLine shell;
            List<ClosedPolyLine> holes;
            for (Attributed<Polygon> polygon : map.polygons()) {
                shell = polygon.value().shell();
                holes = polygon.value().holes();
                canvas.drawPolyLine(style, shell);
                for (PolyLine hole : holes)
                    canvas.drawPolyLine(style, hole);
            }
        };
    }

    /**
     * 
     * @param width
     *            Largeur des bordures
     * @param color
     *            Couleurs des bordures
     * @param lineCap
     *            Terminaison des bordures
     * @param lineJoin
     *            Jointure des segments
     * @param alternance
     *            des section opaques et transparentes, pour le dessin en
     *            traitillés.
     * @return Un peintre de bordures
     */
    public static Painter outline(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... alternance) {
        LineStyle style = new LineStyle(width, color, lineCap, lineJoin,
                alternance);
        return outline(style);
    }

    /**
     * 
     * @param width
     *            Largeurs des lignes
     * @param color
     *            Couleur des lignes
     * @return Un peintre de bordures
     */
    public static Painter outline(float width, Color color) {
        LineStyle style = new LineStyle(width, color);
        return outline(style);
    }

    /**
     * 
     * @param predicate
     *            prédicat
     * @return un peintre qui dessine les éléments de la carte vérifiant le
     *         prédicat passé en paramètre
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (map, canvas) -> {
            List<Attributed<Polygon>> filtredPolygons = new ArrayList<>(
                    map.polygons());
            List<Attributed<PolyLine>> filtredPolyLines = new ArrayList<>(
                    map.polyLines());
            filtredPolygons.removeIf(x -> !predicate.test(x));
            filtredPolyLines.removeIf(x -> !predicate.test(x));
            this.drawMap(new Map(filtredPolyLines, filtredPolygons), canvas);
        };

    }

    /**
     * 
     * @param painter
     *            peintre à superposer
     * @return un peintre qui superpose ce peintre et celui passé en paramètre
     */
    public default Painter above(Painter painter) {
        return (map, canvas) -> {
            painter.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    /**
     * 
     * @return un peintre qui dessine une carte selon 10 niveaux
     */
    public default Painter layered() {
        List<Painter> painters = new ArrayList<>();
        for (int i = 5; i >= -5; i--)
            painters.add(this.when(Filters.onLayer(i)));
        return painters.stream().reduce(Painter::above).get();
    }
}
