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
    private int _masterAddress;
    private boolean _inverted;

    @Override
    public int getAddress() {
        return _address;
    }

    @Override
    public void setAddress(int address) {
        this._address = address;
    }

    @Override
    public int getMasterAddress() {
        return _masterAddress;
    }

    @Override
    public void setMasterAddress(int address) {
        this._masterAddress = address;
    }

    @Override
    public boolean isInverted() {
        return _inverted;
    }

    @Override
    public void setInverted(boolean inverted) {
        this._inverted = inverted;
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
        if (_masterAddress != 0) {
            icon.setAttribute("MasterAddress", Integer.toString(_masterAddress));
        }
        if (_inverted) {
            icon.setAttribute("Inverted", "yes");
        }
        return icon;
    }

    @Override
    public void loadXml(Element iconData) {
        String addr = iconData.getAttributeValue("Address");
        if (addr != null) {
            _address = Integer.parseInt(addr);
        }
        String masterAddr = iconData.getAttributeValue("MasterAddress");
        if (masterAddr != null) {
            _masterAddress = Integer.parseInt(masterAddr);
        }
        String invertedElem = iconData.getAttributeValue("Inverted");
        if (invertedElem != null) {
            _inverted = invertedElem.equals("yes");
        }
        System.out.format("Address: %d%n", _address);
    }

}
