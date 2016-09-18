package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Interface représentant un modèle numérique du terrain.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public interface DigitalElevationModel extends AutoCloseable{
    
    /**
     * @param point Point en coordonnées WGS 84 
     * @return Le vecteur normal à la Terre au point entré en paramètre.
     */
    public Vector3 normalAt(PointGeo point);
}
