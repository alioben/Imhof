package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.projection.Projection;

/**
 * Représente un convertisseur de données OSM en carte.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */

public final class OSMToGeoTransformer {
    private final Projection projection;
    private final static Set<String> ATTRIBUTES_POLYGONE = new HashSet<>(
            Arrays.asList("building", "landuse", "layer", "leisure", "natural",
                    "waterway"));
    private final static Set<String> ATTRIBUTES_POLYLINE = new HashSet<>(
            Arrays.asList("bridge", "highway", "layer", "man_made", "railway",
                    "tunnel", "waterway"));
    private final static Set<String> IS_AREA_ATTRIBUTES = new HashSet<String>(
            Arrays.asList("aeroway", "amenity", "building", "harbour",
                    "historic", "landuse", "leisure", "man_made", "military",
                    "natural", "office", "place", "power", "public_transport",
                    "shop", "sport", "tourism", "water", "waterway", "wetland"));

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection
     * donnée.
     * 
     * @param projection
     *            : Projection à utiliser pour projeter les points.
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * Construit un convertisseur OSM en géométrie
     * 
     * @param map
     *            Map de type OSM.
     * @return Une Map géometrique convertie.
     */
    public Map transform(OSMMap map) {
        Map.Builder mapBuilder = new Map.Builder();

        // Conversion des chemins en PolyLigne et Polygon
        for (OSMWay way : map.ways()) {
            // Déclaration des variables
            PolyLine.Builder polyLineBuilder = new PolyLine.Builder();

            // Vérifier s'il a les bon attributs
            Attributes attributesPolygone = way.attributes().keepOnlyKeys(
                    ATTRIBUTES_POLYGONE);
            Attributes attributesPolyLine = way.attributes().keepOnlyKeys(
                    ATTRIBUTES_POLYLINE);

            boolean isSurface = isSurface(way);
            
            // Ajoute les noeuds au Builder
            for (OSMNode node : way.nonRepeatingNodes())
                polyLineBuilder.addPoint(projection.project(node.position()));
            
            // Ajouter l'element au builder
            if (isSurface) {
                if (way.isClosed() && !attributesPolygone.isEmpty())
                    mapBuilder
                            .addPolygon(new Attributed<>(new Polygon(
                                    polyLineBuilder.buildClosed()),
                                    attributesPolygone));
            } else {
                if (way.isClosed() && !attributesPolyLine.isEmpty()) {
                    mapBuilder.addPolyLine(new Attributed<PolyLine>(
                            polyLineBuilder.buildClosed(), attributesPolyLine));
                } else if (!way.isClosed() && !attributesPolyLine.isEmpty()) {
                    mapBuilder.addPolyLine(new Attributed<PolyLine>(
                            polyLineBuilder.buildOpen(), attributesPolyLine));
                }
            }
        }

        // Conversion des relations en multipolygones
        for (OSMRelation relation : map.relations()) {
            Attributes attributesRelation = relation.attributes().keepOnlyKeys(
                    ATTRIBUTES_POLYGONE);
            if (!attributesRelation.isEmpty()) {
                List<Attributed<Polygon>> polygons = assemblePolygon(relation,
                        attributesRelation);
                //TODO: Refaire avec un forEach
                for (Attributed<Polygon> polygon : polygons) {
                    mapBuilder.addPolygon(polygon);
                }
            }
        }

        return mapBuilder.build();
    }

    private boolean isSurface(OSMWay way) {
        String area = way.attributes().get("area");
        if (way.isClosed()) {
            if (area != null
                    && (area.equals("true") || area.equals("yes") || area
                            .equals("1")))
                return true;
            else {
                for (String attribute : IS_AREA_ATTRIBUTES) {
                    if (way.attributes().contains(attribute))
                        return true;
                }
            }
        }
        return false;
    }

    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        // Déclaration des variables
        List<ClosedPolyLine> rings = new ArrayList<>();
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<>();
        Graph<OSMNode> nodeGraph;

        // Extraction des membres chemins ayant le role cherché + Construction
        // du graph
        for (Member member : relation.members()) {
            if (member.type().equals(Type.WAY) && member.role().equals(role)) {
                OSMWay way = (OSMWay) member.member();
                OSMNode previousNode = null;
                for (OSMNode currentNode : way.nodes()) {
                    graphBuilder.addNode(currentNode);
                    if (previousNode != null)
                        graphBuilder.addEdge(currentNode, previousNode);
                    previousNode = currentNode;
                }
            }
        }
        nodeGraph = graphBuilder.build();

        // Vérification si chaque noeud a deux voisin
        for (OSMNode node : nodeGraph.nodes()) {
            if (nodeGraph.neighborsOf(node).size() != 2)
                return rings;
        }

        // Extraction des anneaux
        Set<OSMNode> graphNodes = nodeGraph.nodes();
        Set<OSMNode> visitedNodes = new HashSet<>();
        for (OSMNode firstNode : graphNodes) {
            if (!visitedNodes.contains(firstNode)) {
                OSMNode currentNode = firstNode;
                PolyLine.Builder polylineBuilder = new PolyLine.Builder();
                boolean finishedRing = false;
                while (!finishedRing) {
                    Set<OSMNode> neighbors = nodeGraph.neighborsOf(currentNode);
                    OSMNode neighbor = null;
                    for (OSMNode node : neighbors)
                        if (!visitedNodes.contains(node))
                            neighbor = node;
                    if (neighbor != null) {
                        currentNode = neighbor;
                        polylineBuilder.addPoint(projection.project(currentNode
                                .position()));
                        visitedNodes.add(currentNode);
                    } else
                        finishedRing = true;
                }
                rings.add(polylineBuilder.buildClosed());
                polylineBuilder = new PolyLine.Builder();
            }
        }
        return rings;
    }

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {

        // Déclaration des liste des inners et outers
        List<ClosedPolyLine> inners = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outers = ringsForRole(relation, "outer");

        // Trie de la liste des outers par ordre croissant
        outers.sort(new ComparatorClosedP());

        // Liste des resultats à retouner
        List<Attributed<Polygon>> result = new ArrayList<>();

        java.util.Map<ClosedPolyLine, List<ClosedPolyLine>> polygonsBuilder = new HashMap<>();

        // Associer à chaque Outer les Inners qui vont avec
        for (ClosedPolyLine outer : outers) {
            polygonsBuilder.put(outer, new ArrayList<ClosedPolyLine>());
            Iterator<ClosedPolyLine> iterator = inners.iterator();
            while (iterator.hasNext()) {
                ClosedPolyLine inner = iterator.next();
                boolean isIn = true;
                for (Point point : inner.points()) {
                    if (!outer.containsPoint(point))
                        isIn = false;
                }
                if (isIn) {
                    polygonsBuilder.get(outer).add(inner);
                    iterator.remove();
                }
            }
        }

        // Ajout des Polygone dans la liste à retourner
        for (Entry<ClosedPolyLine, List<ClosedPolyLine>> polygonBuilder : polygonsBuilder
                .entrySet()) {
            result.add(new Attributed<>(new Polygon(polygonBuilder.getKey(),
                    polygonBuilder.getValue()), attributes));
        }

        return result;
    }

    private class ComparatorClosedP implements Comparator<ClosedPolyLine> {
        @Override
        public int compare(ClosedPolyLine closedPolyLine1, ClosedPolyLine closedPolyLine2) {
            double area1 = closedPolyLine1.area();
            double area2 = closedPolyLine2.area();

            if (area1 == area2)
                return 0;
            else {
                if (area1 < area2)
                    return -1;
                else
                    return 1;
            }
        }
    }

}
