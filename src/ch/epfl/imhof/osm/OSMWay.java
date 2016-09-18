package ch.epfl.imhof.osm;

/**
 * Représente un chemin OSM.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

public final class OSMWay extends OSMEntity {
    private final List<OSMNode> nodes;

    /**
     * Construit un chemin étant donnés son identifiant unique, ses nœuds et ses
     * attributs.
     * 
     * @param id
     *            : Identifiant unique du chemin.
     * @param nodes
     *            : Liste des noeuds du chemin.
     * @param attributes
     *            :Attributs du chemin.
     * @throws IllegalArgumentException
     *             : si la liste de nœuds possède moins de deux éléments.
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) {
        super(id, attributes);
        if (nodes.size() < 2)
            throw new IllegalArgumentException();
        this.nodes = Collections
                .unmodifiableList(new ArrayList<OSMNode>(nodes));
    }

    /**
     * 
     * @return Le nombre de nœuds du chemin
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * 
     * @return La liste des nœuds du chemin
     */
    public List<OSMNode> nodes() {
        return this.nodes;
    }

    /**
     * 
     * @return La liste des nœuds du chemin sans le dernier si celui-ci est
     *         identique au premier.
     */
    public List<OSMNode> nonRepeatingNodes() {
        return (isClosed()) ? nodes.subList(0, nodes.size() - 1) : this.nodes;
    }

    /**
     * 
     * @return Le premier nœud du chemin
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * 
     * @return Le dernier nœud du chemin
     */
    public OSMNode lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    /**
     * 
     * @return Vrai si et seulement si le chemin est fermé.
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    /**
     * Construit un bâtisseur de la classe OSMWay.
     * 
     * @author
     *
     */

    public static final class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes;

        /**
         * Construit un bâtisseur pour un chemin ayant l'identifiant donné.
         * 
         * @param id
         *            : Identifiant du chemin.
         */
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<OSMNode>();
        }

        /**
         * Ajoute un nœud à (la fin) des nœuds du chemin en cours de
         * construction.
         * 
         * @param newNode
         *            : Noeud à ajouter.
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * Construit et retourne le chemin ayant les nœuds et les attributs
         * ajoutés jusqu'à présent.
         * 
         * @throws IllegalStateException
         *             : si le chemin en cours de construction est incomplet.
         * @return Le chemin construit.
         */
        public OSMWay build() {
            if (this.isIncomplete())
                throw new IllegalStateException();
            else
                return new OSMWay(super.id(), nodes, super.getAttributes());
        }

        public boolean isIncomplete() {
            return (nodes.size() < 2 || super.isIncomplete());
        }
    }

}
