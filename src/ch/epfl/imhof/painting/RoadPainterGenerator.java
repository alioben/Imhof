package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Classe générant un peintre des routes
 * 
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
public final class RoadPainterGenerator {

    private RoadPainterGenerator() {
    }

    /**
     * 
     * @param specs
     *            Spécification des routes.
     * @return Un peintre qui dessine les routes avec les specifications passées
     *         en paramètre.
     */
    public static Painter painterForRoads(RoadSpec... specs) {
        List<Painter> painters = new ArrayList<>();
        for (RoadSpec spec : specs)
            painters.add(spec.forInnerBridge());
        for (RoadSpec spec : specs)
            painters.add(spec.forBorderBridges());
        for (RoadSpec spec : specs)
            painters.add(spec.forInnerRoads());
        for (RoadSpec spec : specs)
            painters.add(spec.forBorderRoads());
        for (RoadSpec spec : specs)
            painters.add(spec.forTunnel());
        return painters.stream().reduce(Painter::above).get();
    }

    /**
     * 
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public final static class RoadSpec {
        private final Predicate<Attributed<?>> tag;
        private final float borderWidth, innerWidth;
        private final Color borderColor, innerColor;
        private static final Predicate<Attributed<?>> isBridge = Filters
                .tagged("bridge");
        private static final Predicate<Attributed<?>> isTunnel = Filters
                .tagged("tunnel");
        private static final Predicate<Attributed<?>> isRoad = (isBridge
                .or(isTunnel)).negate();

        /**
         * Construit un peintre de réseau routier
         * 
         * @param tag
         *            Filtre
         * @param widthI
         *            Largeur du trait
         * @param innerC
         *            Couleur du trait
         * @param widthB
         *            Largeur de la bordure
         * @param borderC
         *            Couleur de la bordure
         */
        public RoadSpec(Predicate<Attributed<?>> tag, float widthI,
                Color innerC, float widthB, Color borderC) {
            this.tag = tag;
            this.innerWidth = widthI;
            this.borderWidth = widthB;
            this.innerColor = innerC;
            this.borderColor = borderC;

        }

        /**
         * 
         * @return Un peintre des tunnels.
         */
        public Painter forTunnel() {
            return Painter.line(innerWidth / 2, borderColor, LineCap.Butt,
                    LineJoin.Round, 2 * innerWidth, 2 * innerWidth).when(
                    tag.and(isTunnel));
        }

        /**
         * 
         * @return un peintre de l'interieur des routes
         */
        public Painter forInnerRoads() {
            return Painter.line(innerWidth, innerColor, LineCap.Round, LineJoin.Round)
                    .when(tag.and(isRoad));
        }

        /**
         * 
         * @return un peintre des bordures des routes.
         */
        public Painter forBorderRoads() {
            return Painter.line(innerWidth + 2 * borderWidth, borderColor, LineCap.Round,
                    LineJoin.Round).when(tag.and(isRoad));
        }

        /**
         * 
         * @return un peintre de l'interieur des ponts
         */
        public Painter forInnerBridge() {
            return Painter.line(innerWidth, innerColor, LineCap.Round, LineJoin.Round)
                    .when(tag.and(isBridge));
        }

        /**
         * 
         * @return un peintre des bordures des ponts.
         */
        public Painter forBorderBridges() {
            return Painter.line(innerWidth + 2 * borderWidth, borderColor, LineCap.Butt,
                    LineJoin.Round).when(tag.and(isBridge));
        }
    }
}
