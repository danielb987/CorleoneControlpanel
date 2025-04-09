package se.bergqvist.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serial port
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class SerialPort {

    private final String PORTNAME = "/dev/usb1";

    private final com.fazecast.jSerialComm.SerialPort _serialPort;

    public SerialPort() {
        com.fazecast.jSerialComm.SerialPort serialPort;
        try {
            serialPort = com.fazecast.jSerialComm.SerialPort.getCommPort(PORTNAME);
            serialPort.openPort();
            serialPort.setComPortTimeouts(com.fazecast.jSerialComm.SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
            serialPort.setNumDataBits(8);
            serialPort.setNumStopBits(1);
            serialPort.setParity(com.fazecast.jSerialComm.SerialPort.NO_PARITY);
            serialPort.setBaudRate(9600);
            serialPort.setRTS();
            serialPort.setDTR();
            serialPort.setFlowControl(com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_DISABLED);
            purgeStream(serialPort.getInputStream());
        } catch (java.io.IOException | com.fazecast.jSerialComm.SerialPortInvalidPortException ex) {
            throw new RuntimeException("Cannot open serial port", ex);
        }
        this._serialPort = serialPort;
    }

     public static void purgeStream(InputStream serialStream) throws IOException {
        int count = serialStream.available();
        while (count > 0) {
            serialStream.skip(count);
            count = serialStream.available();
        }
    }

    public InputStream getInputStream() {
        return this._serialPort.getInputStream();
    }

    public OutputStream getOutputStream() {
        return this._serialPort.getOutputStream();
    }

    public void closePort() {
        this._serialPort.closePort();
    }

}
