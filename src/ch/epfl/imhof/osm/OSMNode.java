package ch.epfl.imhof.osm;

/**
 * Construit un noeud OSM.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

public final class OSMNode extends OSMEntity {
    private final PointGeo position;

    /**
     * Construit un nœud OSM avec l'identifiant, la position et les attributs
     * donnés.
     * 
     * @param id
     *            : Identifiant du noeud.
     * @param position
     *            : Position du noeud.
     * @param attributes
     *            : Attributs du noeud.
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * 
     * @return La position du noeud.
     */
    public PointGeo position() {
        return this.position;
    }

    /**
     * Batisseur d'un noeud.
     *
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public static final class Builder extends OSMEntity.Builder {
        private PointGeo position;

        /**
         * Construit un bâtisseur pour un nœud ayant l'identifiant et la
         * position donnés.
         * 
         * @param id
         *            : Identifiant du noeud.
         * @param position
         *            : Position du noeud.
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * Construit un nœud OSM avec l'identifiant et la position passés au
         * constructeur, et les éventuels attributs ajoutés jusqu'ici au
         * bâtisseur.
         * 
         * @return Un noeud OSM avec les parametres spécifiés dans le
         *         constructeur.
         * @throws IllegalStateException
         *             : Lève l'exception si le nœud en cours de construction
         *             est incomplet.
         */
        public OSMNode build() {
            if ((super.isIncomplete()))
                throw new IllegalStateException();
            else
                return new OSMNode(super.id(), position, super.getAttributes());
        }
    }
}
