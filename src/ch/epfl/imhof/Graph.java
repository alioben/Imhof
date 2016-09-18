package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Représente un graphe non orienté.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
public final class Graph<N> {
    private final Map<N, Set<N>> neighbours;

    /**
     * Construit un graphe non orienté avec la table d'adjacence donnée.
     * 
     * @param neighbours
     *            : Table d'adjacence du graphe.
     */
    public Graph(Map<N, Set<N>> neighbours) {
        Map<N, Set<N>> newNeighbours = new HashMap<>();
        for (Entry<N, Set<N>> entry : neighbours.entrySet())
            newNeighbours.put(entry.getKey(), Collections
                    .unmodifiableSet(new HashSet<N>(entry.getValue())));
        this.neighbours = Collections.unmodifiableMap(newNeighbours);
    }

    /**
     * 
     * @return L'ensemble des nœuds du graphe.
     */
    public Set<N> nodes() {
        return this.neighbours.keySet();
    }

    /**
     * 
     * @param node
     *            : Noeud dont on veut le voisin.
     * @exception IllegalArgumentException
     *                : Si le nœud donné ne fait pas partie du graphe.
     * @return L'ensemble des nœuds voisins du nœud donné.
     */
    public Set<N> neighborsOf(N node) {
        if (neighbours.containsKey(node))
            return neighbours.get(node);
        else
            throw new IllegalArgumentException();
    }

    /**
     * Batisseur d'un Graph.
     *
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public final static class Builder<N> {
        private Map<N, Set<N>> neighboursBuilder;

        public Builder() {
            this.neighboursBuilder = new HashMap<N, Set<N>>();
        }

        /**
         * Ajoute le nœud donné au graphe en cours de construction, s'il n'en
         * faisait pas déjà partie.
         * 
         * @param n
         *            : Noeud a ajouter.
         */
        public void addNode(N node) {
            if (!neighboursBuilder.containsKey(node))
                neighboursBuilder.put(node, new HashSet<N>());
        }

        /**
         * Ajoute une arête entre les deux nœuds donnés au graphe en cours de
         * construction.
         */
        public void addEdge(N node1, N node2) {
            if (!neighboursBuilder.containsKey(node1)
                    || !neighboursBuilder.containsKey(node2))
                throw new IllegalArgumentException();
            neighboursBuilder.get(node1).add(node2);
            neighboursBuilder.get(node2).add(node1);
        }

        /**
         * 
         * @return Le graphe composé des nœuds et arêtes ajoutés jusqu'à présent
         *         au bâtisseur.
         */
        public Graph<N> build() {
            return new Graph<N>(neighboursBuilder);
        }
    }
}
