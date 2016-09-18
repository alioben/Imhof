package ch.epfl.imhof;

/**
 * Représente un ensemble d'attributs et la valeur qui leur est associée.
 *
 * @author Ben Lalah Ali 
 * @author Alami Idrissi Ali
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class Attributes {

    private final Map<String, String> attributes;

    /**
     * Construit un ensemble d'attributs avec les paires clef/valeur présentes
     * dans la table associative donnée.
     * 
     * @param attributes
     *            : un ensemble d'attributs avec les paires clef/valeur
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections
                .unmodifiableMap(new HashMap<String, String>(attributes));
    }

    /**
     * 
     * @return Vrai si et seulement si l'ensemble d'attributs est vide.
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * 
     * @param key
     *            : clef donnée.
     * @return Vrai si l'ensemble d'attributs contient la clef donnée.
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * 
     * @param key
     *            : clef donnée.
     * @return La valeur associée à la clef donnée, ou null si la clef n'existe
     *         pas.
     */
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * 
     * @param key
     *            : clef donnée.
     * @param defaultValue
     *            : valeur par defaut donnée.
     * @return La valeur associée à la clef donnée, ou la valeur par défaut
     *         donnée si aucune valeur ne lui est associée.
     */
    public String get(String key, String defaultValue) {
        String attribute = attributes.get(key);
        return (attribute == null) ? defaultValue : attribute;
    }

    /**
     * 
     * @param key
     *            : clef donnée.
     * @param defaultValue
     *            : valeur par défaut donnée.
     * @return L'entier associé à la clef donnée, ou la valeur par défaut donnée
     *         si aucune valeur ne lui est associée, ou si cette valeur n'est
     *         pas un entier valide.
     */
    public int get(String key, int defaultValue) {
        String attribute = attributes.get(key);
        return ((attribute == null || !isInteger(attribute)) ? defaultValue
                : Integer.parseInt(attribute));
    }

    /**
     * 
     * @param keysToKeep
     *            : Ensemble de clefs.
     * @return Une version filtrée des attributs ne contenant que ceux dont le
     *         nom figure dans l'ensemble passé.
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<String, String> entry : attributes.entrySet()) {
            if (keysToKeep.contains(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return new Attributes(map);
    }
    
    /**
     * Batisseur de la classe Attributes.
     *
     */
    public static final class Builder {
        private Map<String, String> attributesBuilder;

        /**
         * Ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs
         * en cours de construction. Si un attribut de même nom avait déjà été
         * ajouté précédemment à l'ensemble, sa valeur est remplacée par celle
         * donnée.
         * 
         * @param key
         *            : La clef de l'entrée.
         * @param value
         *            : La valeur de l'entrée.
         */
        public Builder() {
            attributesBuilder = new HashMap<>();
        }

        /**
         * Ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs
         * en cours de construction. Si un attribut de même nom avait déjà été
         * ajouté précédemment à l'ensemble, sa valeur est remplacée par celle
         * donnée.
         * 
         * @param key
         *            : Clef de l'attribut.
         * @param value
         *            : Valeur de l'attribut.
         */
        public void put(String key, String value) {
            attributesBuilder.put(key, value);
        }

        /**
         * Construit un ensemble d'attributs contenant les associations
         * clef/valeur ajoutées jusqu'à présent.
         * 
         * @return Un ensemble d'attributs contenant les associations
         *         clef/valeur ajoutées jusqu'à présent.
         */
        public Attributes build() {
            return new Attributes(attributesBuilder);
        }
    }
    
    private boolean isInteger(String numberString) {
        try {
            Integer.parseInt(numberString);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

}
