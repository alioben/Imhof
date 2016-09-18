package ch.epfl.imhof.projection;
/**
 * Interface de projection 
 *
 * @author Ben Lalah Ali 
 * @author Alami Idrissi Ali 
 */

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public interface Projection {
    
    abstract Point project(PointGeo point);
    abstract PointGeo inverse(Point point);
}
