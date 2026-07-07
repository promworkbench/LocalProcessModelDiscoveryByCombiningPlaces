package org.processmining.lpms.io.readers;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.lpms.model.AcceptingPetriNetLPM;
import org.processmining.lpms.model.LPM;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.base.FullPnmlElementFactory;
import org.processmining.plugins.pnml.base.Pnml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LPMsFromPNMLsInZipReader implements LPMsReader {
    private final String fileName;

    public LPMsFromPNMLsInZipReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Collection<LPM> getLPMs() {
        Collection<LPM> lpms = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(fileName)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".pnml")) {
                    try (InputStream stream = zipFile.getInputStream(entry)) {
                        AcceptingPetriNet apn = parsePnml(stream, entry.getName());
                        lpms.add(new AcceptingPetriNetLPM(apn));
                    }
                }
            }
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException("Failed to read LPMs from zip: " + fileName, e);
        }
        return lpms;
    }

    private AcceptingPetriNet parsePnml(InputStream input, String name) throws XmlPullParserException, IOException {
        FullPnmlElementFactory pnmlFactory = new FullPnmlElementFactory();
        Petrinet net = PetrinetFactory.newPetrinet(name);
        Marking marking = new Marking();
        GraphLayoutConnection layout = new GraphLayoutConnection(net);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, null);

        Pnml pnml = new Pnml();
        synchronized (pnmlFactory) {
            Pnml.setFactory(pnmlFactory);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.START_TAG) {
                eventType = xpp.next();
            }
            if (xpp.getName().equals("pnml")) {
                pnml.importElement(xpp, pnml);
            } else {
                pnml.log("pnml", xpp.getLineNumber(), "Expected pnml");
            }
            pnml.convertToNet(net, marking, layout);
        }

        return new AcceptingPetriNetImpl(net, marking);
    }
}
