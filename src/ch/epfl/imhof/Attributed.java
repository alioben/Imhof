package ch.epfl.imhof;

/**
 * Représente une entité de type T dotée d'attributs.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */

public final class Attributed<T>{

    private final T value;
    private final Attributes attributes;

    /**
     * Construit une valeur attribuée dont la valeur et les attributs sont ceux
     * donnés.
     * 
     * @param value
     *            : valeur à laquelle les attributs sont attachés
     * @param attributes
     *            :les attributs attachés à la valeur.
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * 
     * @return La valeur à laquelle les attributs sont attachés.
     */
    public T value() {
        return this.value;
    }

    /**
     * 
     * @return Les attributs attachés à la valeur.
     */
    public Attributes attributes() {
        return this.attributes;
    }

    /**
     * 
     * @param attributeName
     *            : Nom de l'attribut.
     * @return Vrai si et seulement si les attributs incluent celui dont le nom
     *         est passé en argument.
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * 
     * @param attributeName
     *            : Nom de l'attribut.
     * @return La valeur associée à l'attribut donné, ou null si celui-ci
     *         n'existe pas.
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * 
     * @param attributeName
     *            : Nom de l'attribut.
     * @param defaultValue
     *            : Valur par défaut.
     * @return La valeur associée à l'attribut donné, ou la valeur par défaut
     *         donnée si celui-ci n'existe pas.
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * 
     * @param attributeName
     *            : Nom de l'attribut.
     * @param defaultValue
     *            : Valeur par defaut.
     * @return La valeur entière associée à l'attribut donné, ou la valeur par
     *         défaut si celui-ci n'existe pas ou si la valeur qui lui est
     *         associée n'est pas un entier valide.
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

}
