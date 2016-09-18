package ch.epfl.imhof.painting;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public class PaintingTest {
    @Test
    public void PaintingMethodTest() {
        OSMMap map;
        try {

            Painter swissPainter = SwissPainter.painter();

            map = OSMMapReader.readOSMFile(
                    getClass().getResource("/lausanne.osm.gz").getFile(), true);
            Projection projection = new CH1903Projection();
            Map mapT = new OSMToGeoTransformer(projection).transform(map); // La
                                                                           // toile
            Point bl = new Point(532510, 150590);
            Point tr = new Point(539570, 155260);
            Java2DCanvas canvas = new Java2DCanvas(bl, tr, 1600, 1060, 150,
                    Color.WHITE);

            // Dessin de la carte et stockage dans un fichier
            swissPainter.drawMap(mapT, canvas);
            ImageIO.write(canvas.image(), "png", new File("loz.png"));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
