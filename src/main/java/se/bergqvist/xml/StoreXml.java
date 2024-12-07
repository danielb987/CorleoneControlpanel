package se.bergqvist.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import se.bergqvist.controlpanel.ControlPanel;
import se.bergqvist.log.Logger;

/**
 *
 * @author Daniel Bergqvist
 */
public class StoreXml {

    private final String schemaVersion = "-1-0";

    public void store(File file) {
        try {
            Element root = createXmlTree();
            root.setAttribute("noNamespaceSchemaLocation",
                    "schema/controlpanel" + schemaVersion + ".xsd",
                    org.jdom2.Namespace.getNamespace("xsi",
                            "http://www.w3.org/2001/XMLSchema-instance"));
            Document doc = newDocument(root);
            writeXML(file, doc);
        } catch (java.io.FileNotFoundException ex) {
            LOG.error("FileNotFound error writing file: " + ex.getLocalizedMessage());
        } catch (java.io.IOException ex) {
            LOG.error("IO error writing file: " + ex.getLocalizedMessage());
        }
    }

    private Document newDocument(Element root) {
        Document doc = new Document(root);
        return doc;
    }

    private void writeXML(File file, Document doc) throws IOException, FileNotFoundException {
        // ensure parent directory exists
//        if (file.getParent() != null) {
//            FileUtil.createDirectory(file.getParent());
//        }
        // write the result to selected file
        try (FileOutputStream o = new FileOutputStream(file)) {
            XMLOutputter fmt = new XMLOutputter();
            fmt.setFormat(Format.getPrettyFormat()
                    .setLineSeparator(System.getProperty("line.separator"))
                    .setTextMode(Format.TextMode.TRIM_FULL_WHITE));
            fmt.output(doc, o);
            o.flush();
        }
    }

    private Element createXmlTree() {
        Element root = new Element("Controlpanel");
        Element configuration = new Element("Configuration");
        root.addContent(configuration);
        root.addContent(ControlPanel.get().getXml());
        Element views = new Element("Views");
        root.addContent(views);
        return root;
    }


    private static final Logger LOG = new Logger(StoreXml.class);

}
