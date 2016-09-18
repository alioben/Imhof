package ch.epfl.imhof.painting;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente un toile en deux dimensions
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
public final class Java2DCanvas implements Canvas {

    private final Graphics2D graphics;
    private final BufferedImage image;
    private Function<Point, Point> rebasing;

    /**
     * Construction d'une toile à deux dimensions
     * 
     * @param bl
     *            Point bas gauche en coordonnées du plan
     * @param tr
     *            Point haut droit en coordonnées du plan
     * @param width
     *            Largeur en pixels de la toile
     * @param height
     *            Hauteur en pixels de la toile
     * @param resolution
     *            résolution de la toile
     * @param backgroundColor
     *            Couleur du fond de la toile
     */
    public Java2DCanvas(Point bl, Point tr, int width, int height,
            int resolution, Color backgroundColor) {
        this.image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        this.graphics = this.image.createGraphics();
        this.rebasing = Point.alignedCoordinateChange(bl, new Point(0, height
                / (resolution / 72d)), tr, new Point(
                width / (resolution / 72d), 0));
        graphics.setColor(Color.toJavaColor(backgroundColor));
        graphics.fillRect(0, 0, width, height);
        graphics.scale(resolution / 72d, resolution / 72d);
        graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    }

    @Override
    public void drawPolyLine(LineStyle style, PolyLine polyline) {
        BasicStroke stroke = new BasicStroke(style.getWidth(), style
                .getLineCap().ordinal(), style.getLineJoin().ordinal(), 10f,
                (style.getAlternance().length == 0) ? null : style
                        .getAlternance(), 0f);
        graphics.setStroke(stroke);
        graphics.setColor(Color.toJavaColor(style.getColor()));
        Path2D line = drawShape(polyline, polyline.isClosed());
        graphics.draw(line);
    }

    @Override
    public void drawPolygon(Color color, Polygon polygon) {
        // Dessine l'enveloppe
        graphics.setColor(Color.toJavaColor(color));
        Path2D shell = drawShape(polygon.shell(), polygon.shell().isClosed());
        Area area = new Area(shell);
        // Dessine les trous
        for (ClosedPolyLine hole : polygon.holes())
            area.subtract(new Area(drawShape(hole, hole.isClosed())));
        graphics.fill(area);
    }

    /**
     * 
     * @param shape
     *            polyligne à dessiner
     * @param close
     *            indique si le chemin est fermé
     * @return chemin en deux dimensions représentant le polyligne donné en
     *         paramètre
     */
    private Path2D drawShape(PolyLine shape, boolean close) {
        Path2D path = new Path2D.Double();
        Iterator<Point> iterator = shape.points().iterator();
        Point initialPoint = rebasing.apply(iterator.next());
        path.moveTo(initialPoint.x(), initialPoint.y());
        while (iterator.hasNext()) {
            Point point = rebasing.apply(iterator.next());
            path.lineTo(point.x(), point.y());
        }
        if (close)
            path.closePath();
        return path;
    }

    public BufferedImage image() {
        return image;
    }

}
