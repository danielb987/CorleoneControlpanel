package se.bergqvist.serial;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serial port
 * @author Daniel Bergqvist (C) 2025
 */
public interface SerialPort {

    public InputStream getInputStream();

    public OutputStream getOutputStream();

    public void closePort();

}
