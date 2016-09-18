package ch.epfl.imhof.painting;

/**
 * Représente une couleur.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class Color {
    private final double r, g, b;
    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    /**
     * Construit une couleur avec les quantités de couleurs passées en
     * paramètre.
     * 
     * @param r
     *            Composante rouge.
     * @param g
     *            Composante verte.
     * @param b
     *            Composante bleue.
     */
    private Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Construit une couleur grise dont les trois composantes sont égales à la
     * valeur entrée en paramètre.
     * 
     * @param proportion
     *            Proportion de gris comprise entre 0 et 1 (inclus).
     * @return Une couleur grise dont les trois composantes sont égales à la
     *         valeur entrée en paramètre.
     */
    public static Color gray(double proportion) {
        return rgb(proportion, proportion, proportion);
    }

    /**
     * 
     * @param r
     *            Composante rouge de la couleur.
     * @param g
     *            Composante verte de la couleur.
     * @param b
     *            Composante bleue de la couleur.
     * @exception IllegalArgumentException
     *                Lève une IllegalArgumentException si les composantes
     *                entrées en paramètre ne sont pas incluses dans
     *                l'intervalle [0,1] (nclus).
     * @return Une couleur dont les trois composantes sont égales aux valeurs
     *         entrées en paramètre.
     */
    public static Color rgb(double r, double g, double b) {
        if (!(0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (!(0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (!(0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);
        return new Color(r, g, b);
    }

    /**
     * 
     * @param i
     *            Entier dans lequel sont empaquetées les trois composantes de
     *            la couleur.
     * @return Une couleur dont les trois composantes sont extraites de l'entier
     *         entré en paramètre.
     */
    public static Color rgb(int i) {
        int r = (i >> 16) & 0xFF;
        int g = (i >> 8) & 0xFF;
        int b = i & 0xFF;
        return rgb((double) r / 255d, (double) g / 255d, (double) b / 255d);
    }

    /**
     * 
     * @param c1
     *            Première couleur à multiplier.
     * @param c2
     *            Deuxième couleur à multiplier.
     * @return Une couleur dont les composantes sont le produit des composantes
     *         des couleurs entrées en paramètre.
     */
    public static Color multiply(Color c1, Color c2) {
        return new Color(c1.r * c2.r, c1.g * c2.g, c1.b * c2.b);
    }

    /**
     * 
     * @return La composante rouge de la couleur.
     */
    public double getRed() {
        return r;
    }

    /**
     * 
     * @return La composante verte de la couleur.
     */
    public double getGreen() {
        return g;
    }

    /**
     * 
     * @return La composante bleue de la couleur.
     */
    public double getBlue() {
        return b;
    }

    /**
     * 
     * @param c
     *            Couleur à convertir.
     * @return Une couleur de java ayant les mêmes composantes que cette
     *         couleur.
     */
    public static java.awt.Color toJavaColor(Color c) {
        return (new java.awt.Color((float) c.r, (float) c.g, (float) c.b));
    }

}
