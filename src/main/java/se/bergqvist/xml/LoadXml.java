package se.bergqvist.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaders;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import se.bergqvist.config.Config;
import se.bergqvist.controlpanel.ControlPanel;
import se.bergqvist.log.Logger;

/**
 *
 * @author Daniel Bergqvist
 */
public class LoadXml {

    public void load(File file) {
        try {
            readXml(file);
        } catch (java.io.FileNotFoundException ex3) {
            LOG.error("FileNotFound error reading file: " + ex3.getLocalizedMessage());
        } catch (IOException ex) {
            LOG.error("IO error reading file: " + ex.getLocalizedMessage());
        } catch (JDOMException ex) {
            LOG.error("JDom exception reading file: " + ex.getLocalizedMessage());
        }
    }

    private void readXml(File file) throws JDOMException, IOException {
        Element root = getRoot(file);
        System.out.format("root: %s: %s%n", root.getName(), root.getClass().getName());
        Element configuration = root.getChild("Configuration");
        Config.get().loadXml(configuration);
        Element controlpanel = root.getChild("Controlpanel");
        ControlPanel.get().loadXml(controlpanel);
        Element views = root.getChild("Views");
    }

    protected Element getRoot(File file) throws JDOMException, IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
//            LOG.error("getRoot from stream");

//            processingInstructionHRef = null;
//            processingInstructionType = null;

            SAXBuilder builder = getBuilder();
            Document doc = builder.build(new BufferedInputStream(stream));
//            doc = processInstructions(doc);  // handle any process instructions
            // find root
            return doc.getRootElement();
        }
    }

    public static SAXBuilder getBuilder() {
        SAXBuilder builder = new SAXBuilder(XMLReaders.XSDVALIDATING);

        // control Schema validation
        builder.setFeature("http://apache.org/xml/features/validation/schema", true);
        builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);

        return builder;
    }


    private static final Logger LOG = new Logger(LoadXml.class);

}
