package ch.epfl.imhof.projection;
/**
 * Interface de projection 
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public interface Projection {
    
    abstract Point project(PointGeo point);
    abstract PointGeo inverse(Point point);
}
