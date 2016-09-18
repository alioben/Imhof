package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Représente un filtre.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
public final class Filters {

    private Filters() {
    }

    /**
     * 
     * @param attribute
     *            Nom de l'attribut.
     * @return Un prédicat qui n'est vrai que si la valeur attribuée à laquelle
     *         on l'applique possède un attribut portant ce nom, indépendemment
     *         de sa valeur.
     */
    public static Predicate<Attributed<?>> tagged(String attribute) {
        return attributed -> attributed.hasAttribute(attribute);
    }

    /**
     * 
     * @param attribute
     *            Nom de l'attribut.
     * @param values
     *            Valeurs possibles de l'attribut.
     * @return Un prédicat qui n'est vrai que si la valeur attribuée à laquelle
     *         on l'applique possède un attribut portant le nom donné et si de
     *         plus la valeur associée à cet attribut fait partie de celles
     *         données.
     */
    public static Predicate<Attributed<?>> tagged(String attribute, String... values) {
        return attributed -> {
            if (tagged(attribute).test(attributed)) {
                String attrValue = attributed.attributeValue(attribute);
                for (int i = 0; i < values.length; i++) {
                    if (values[i].equals(attrValue))
                        return true;
                }
            }
            return false;
        };
    }

    /**
     * 
     * @param layer
     *            Numéro de couche.
     * @return Un prédicat qui n'est vrai que lorsqu'on l'applique à une entité
     *         attribuée appartenant cette couche.
     */
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return attributed -> (attributed.attributeValue("layer", 0) == layer);
    }

}
