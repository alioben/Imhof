package ch.epfl.imhof.osm;

/**
 * Construit une entité OSM.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */

import ch.epfl.imhof.Attributes;

public abstract class OSMEntity {

    private final Attributes attributes;
    private final long id;

    /**
     * Construit une entité OSM dotée de l'identifiant unique et des attributs
     * donnés.
     * 
     * @param id
     *            : Identifiant de l'entité OSM.
     * @param attributes
     *            : Attributs de l'entité OSM.
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * 
     * @return Identifiant de l'entité.
     */
    public long id() {
        return id;
    }

    /**
     * 
     * @return Attributs de l'entité.
     */
    public Attributes attributes() {
        return this.attributes;
    }

    /**
     * 
     * @param key
     *            : Attribut recherché.
     * @return Vrai si et seulement si l'entité possède l'attribut passé en
     *         argument.
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }
    
    /**
     * 
     * @param key
     *            : Clef de la valeur en question.
     * @return La valeur de l'attribut donné, ou null si celui-ci n'existe pas.
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }
    
 
    /**
     * Construit un batisseur pour une entité OSM.
     *
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public abstract static class Builder {
        private Attributes.Builder attributesBuilder;
        private long id;
        private boolean isIncomplete;

        /**
         * Construit un bâtisseur pour une entité OSM identifiée par l'entier
         * donné.
         * 
         * @param id
         *            : Identifiant de l'entité en construction.
         */
        public Builder(long id) {
            this.id = id;
            isIncomplete = false;
            attributesBuilder = new Attributes.Builder();
        }
        
        
        /**
         * Ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs
         * de l'entité en cours de construction. Si un attribut de même nom
         * avait déjà été ajouté précédemment, sa valeur est remplacée par celle
         * donnée.
         * 
         * @param key
         *            : Clef de l'attribut.
         * @param value
         *            : Valeur de l'attribut.
         */
        public void setAttribute(String key, String value) {
            attributesBuilder.put(key, value);
        }

        /**
         * Déclare que l'entité en cours de construction est incomplète.
         */
        public void setIncomplete() {
            isIncomplete = true;
        }

        /**
         * 
         * @return Vrai si et seulement si l'entité en cours de construction est
         *         incomplète.
         */
        public boolean isIncomplete() {
            return isIncomplete;
        }
        
        protected Attributes getAttributes(){
            return attributesBuilder.build();
        }
        
        protected long id(){
            return id;
        }
    }
}
