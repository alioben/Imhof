package ch.epfl.imhof;

import org.junit.Test;

public class MainTest {

    @Test
    public void lausanneTest() throws Exception {
        String[] args = {
                "/home/benlalah/workspace/imHof/ressources/lausanne.osm.gz",
                "/home/benlalah/workspace/imHof/ressources/N46E006.hgt",
                "6.5594", "46.5032", "6.6508", "46.5459", "300", "lausanne.png" };

        Main.main(args);
    }

    @Test
    public void interlakenTest() throws Exception {

        String[] args = {
                "/home/benlalah/workspace/imHof/ressources/interlaken.osm.gz",
                "/home/benlalah/workspace/imHof/ressources/N46E007.hgt",
                "7.8122", "46.6645", "7.9049", "46.7061", "300",
                "interlaken.png" };

        Main.main(args);
    }

    @Test
    public void berneTest() throws Exception {
        String[] args = { "/home/benlalah/workspace/imHof/ressources/berne.osm.gz", "/home/benlalah/workspace/imHof/ressources/N46E007.hgt", "7.3912", "46.9322",
                "7.4841", "46.9742", "300", "berne.png" };
        Main.main(args);
    }
}
