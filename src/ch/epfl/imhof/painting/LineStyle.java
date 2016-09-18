package ch.epfl.imhof.painting;

/**
 * Regroupe tous les paramètres de style utiles au dessin d'une ligne.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class LineStyle {
    private final float width;
    private final Color color;
    private final LineCap lineCap;
    private final LineJoin lineJoin;
    private final float[] alternance;

    /**
     * Enumération représentant les terminaisons de ligne
     * 
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     *
     */
    public enum LineCap {
        Butt, Round, Square
    };

    /**
     * Enumération représentant les jointures de ligne
     * 
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     *
     */
    public enum LineJoin {
        Miter, Round, Bevel
    };

    /**
     * Construit des styles de ligne avec les paramètre passés en argument.
     * 
     * @param width
     *            Largeur du trait.
     * @param color
     *            Couleur du trait.
     * @param lineCap
     *            Terminaison des lignes.
     * @param lineJoin
     *            Jointures des segments.
     * @param alternance
     *            Alternance des section opaques et transparentes, pour le
     *            dessin en traitillés.
     */
    public LineStyle(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float[] alternance) {
        if (width < 0)
            throw new IllegalArgumentException("invalid red component: "
                    + width);
        for (int i = 0; i < alternance.length; i++) {
            if (alternance[i] < 0)
                throw new IllegalArgumentException("invalid red component: "
                        + alternance[i]);
        }
        this.width = width;
        this.color = color;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
        this.alternance = alternance.clone();
    }

    /**
     * 
     * @return La largeur du trait.
     */
    public float getWidth() {
        return width;
    }

    /**
     * 
     * @return La couleur du trait.
     */
    public Color getColor() {
        return color;
    }

    /**
     * 
     * @return La terminaison des lignes.
     */

    public LineCap getLineCap() {
        return lineCap;
    }

    /**
     * 
     * @return La jointures des segments.
     */

    public LineJoin getLineJoin() {
        return lineJoin;
    }

    /**
     * 
     * @return L'alternance des section opaques et transparentes, pour le dessin
     *         en traitillés.
     */
    public float[] getAlternance() {
        return alternance.clone();
    }

    /**
     * 
     * @param width
     *            Largeur du trait.
     * @param c
     *            Couleur du trait.
     */
    public LineStyle(float width, Color c) {
        this(width, c, LineCap.Butt, LineJoin.Miter, new float[0]);
    }

    /**
     * 
     * @param width
     *            Largeur du trait du nouveau style.
     * @return Un style de ligne identique et ayant le paramètre passé en
     *         argument.
     */
    public LineStyle WithWidth(float width) {
        return new LineStyle(width, this.color, this.lineCap, this.lineJoin,
                this.alternance);
    }

    /**
     * 
     * @param color
     *            Couleur du trait.
     * @return Un style de ligne identique et ayant le paramètre passé en
     *         argument.
     */
    public LineStyle WithColor(Color color) {
        return new LineStyle(this.width, color, this.lineCap, this.lineJoin,
                this.alternance);
    }

    /**
     * 
     * @param lineCap
     *            Terminaison des lignes.
     * @return Un style de ligne identique et ayant le paramètre passé en
     *         argument.
     */
    public LineStyle WithLineCap(LineCap lineCap) {
        return new LineStyle(this.width, this.color, lineCap, this.lineJoin,
                this.alternance);
    }

    /**
     * 
     * @param lineJoin
     *            Jointures des segments.
     * @return Un style de ligne identique et ayant le paramètre passé en
     *         argument.
     */
    public LineStyle WithLineJoin(LineJoin lineJoin) {
        return new LineStyle(this.width, this.color, this.lineCap, lineJoin,
                this.alternance);
    }

    /**
     * 
     * @param pattern
     *            Alternance des section opaques et transparentes, pour le
     *            dessin en traitillés.
     * @return Un style de ligne identique et ayant le paramètre passé en
     *         argument.
     */
    public LineStyle WithPattern(float[] pattern) {
        return new LineStyle(this.width, this.color, this.lineCap,
                this.lineJoin, pattern);
    }

}