package ch.epfl.imhof.osm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

/**
 * construire une carte OpenStreetMap à partir de données stockées dans un
 * fichier au format OSM.
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */

public final class OSMMapReader {

    /**
     * 
     * @param fileName
     *            : Nom du fichier OSM.
     * @param unGZip
     *            : Vrai si le fichier compressé en format GZip.
     * @throws SAXException
     *             : Lève l'exception en cas d'erreur dans le format du fichier
     *             XML contenant la carte.
     * @throws IOException
     *             : Lève l'exception IOException en cas d'autre erreur
     *             d'entrée/sortie, p.ex. si le fichier n'existe pas.
     * @return La carte OSM contenue dans le fichier de nom donné, en le
     *         décompressant avec gzip.
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {
        XMLReader factory = null;
        Handler handler = null;
        factory = XMLReaderFactory.createXMLReader();
        handler = new Handler();
        factory.setContentHandler(handler);
        try(InputStream fileStream = readFile(fileName, unGZip)){
            factory.parse(new InputSource(fileStream));
        }
        return handler.getMap();
    }

    private OSMMapReader() {
    }

    private static InputStream readFile(String fileName, boolean unGZip)
            throws IOException {
        InputStream fileInputStream = new FileInputStream(fileName);
        if (unGZip) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    fileInputStream);
            return new BufferedInputStream(gzipInputStream);
        } else
            return new BufferedInputStream(fileInputStream);
    }

    private static class Handler extends DefaultHandler {
        private OSMMap.Builder mapBuilder;
        private OSMWay.Builder wayBuilder;
        private OSMNode.Builder nodeBuilder;
        private OSMRelation.Builder relationBuilder;

        @Override
        public void startDocument() {
            mapBuilder = new OSMMap.Builder();
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            switch (qName) {
            case "way":
                if (!wayBuilder.isIncomplete())
                    mapBuilder.addWay(wayBuilder.build());
                break;
            case "node":
                if (!nodeBuilder.isIncomplete())
                    mapBuilder.addNode(nodeBuilder.build());
                break;
            case "relation":
                if (!relationBuilder.isIncomplete())
                    mapBuilder.addRelation(relationBuilder.build());
                break;
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes atts) throws SAXException {
            switch (qName) {
            case "node":
                long id = Long.parseLong(atts.getValue("id"));
                double longitude = Math.toRadians(Double.parseDouble(atts
                        .getValue("lon"))),
                latitude = Math.toRadians(Double.parseDouble(atts
                        .getValue("lat")));
                PointGeo point = new PointGeo(longitude, latitude);
                nodeBuilder = new OSMNode.Builder(id, point);
                wayBuilder = null;
                relationBuilder = null;
                break;
            case "way":
                long idWay = Long.parseLong(atts.getValue("id"));
                wayBuilder = new OSMWay.Builder(idWay);
                relationBuilder = null;
                nodeBuilder = null;
                break;
            case "nd":
                OSMNode node = mapBuilder.nodeForId(Long.parseLong(atts
                        .getValue("ref")));
                if (node != null)
                    wayBuilder.addNode(node);
                else
                    wayBuilder.setIncomplete();
                break;
            case "tag":
                String key = atts.getValue("k");
                String value = atts.getValue("v");
                if (wayBuilder != null)
                    wayBuilder.setAttribute(key, value);
                else if (relationBuilder != null)
                    relationBuilder.setAttribute(key, value);
                else
                    nodeBuilder.setAttribute(key, value);
                break;
            case "relation":
                long idRelation = Long.parseLong(atts.getValue("id"));
                relationBuilder = new OSMRelation.Builder(idRelation);
                nodeBuilder = null;
                wayBuilder = null;
                break;
            case "member":
                long memberReference = Long.parseLong(atts.getValue("ref"));
                String type = atts.getValue("type");
                String role = atts.getValue("role");
                OSMEntity member = null;
                Type memberType = null;
                switch (type) {
                case "node":
                    member = mapBuilder.nodeForId(memberReference);
                    memberType = Type.NODE;
                    break;
                case "way":
                    member = mapBuilder.wayForId(memberReference);
                    memberType = Type.WAY;
                    break;
                case "relation":
                    member = mapBuilder.relationForId(memberReference);
                    memberType = Type.RELATION;
                    break;
                }
                if (member != null) {
                    relationBuilder.addMember(memberType, role, member);
                } else {
                    relationBuilder.setIncomplete();
                }
                break;
            }

        }

        public OSMMap getMap() {
            return mapBuilder.build();
        }

    }

}
