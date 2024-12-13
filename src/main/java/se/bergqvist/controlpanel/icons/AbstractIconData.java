package se.bergqvist.controlpanel.icons;

import java.util.List;
import org.jdom2.Element;

/**
 * Abstract class of the IconData.
 *
 * @author Daniel Bergqvist
 */
public abstract class AbstractIconData implements IconData {

    private int _address;

    @Override
    public int getAddress() {
        return _address;
    }

    @Override
    public void setAddress(int address) {
        this._address = address;
    }

    @Override
    public Element getXml(int x, int y) {
        Icon i = getIcon();
        Element icon = new Element("Icon");
        icon.setAttribute("x", Integer.toString(x));
        icon.setAttribute("y", Integer.toString(y));
        icon.setAttribute("type", i.getType().name());
        icon.setAttribute("bits", Integer.toString(i.getBits()));

        if (_address != 0) {
            icon.setAttribute("Address", Integer.toString(_address));
        }
//        iconData.addContent(new Element("DevPath").addContent(sc._devPath));
        return icon;
    }

    @Override
    public void loadXml(Element iconData) {
        String addr = iconData.getAttributeValue("Address");
        if (addr != null) {
            _address = Integer.parseInt(addr);
        }
        System.out.format("Address: %d%n", _address);
    }

}
