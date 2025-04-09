package se.bergqvist.turntable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import se.bergqvist.serial.SerialPort;

/**
 * Turntable
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class Turntable implements Runnable {

    private final SerialPort _serialPort;
    private BufferedReader _reader;
    private PrintWriter _writer;

    public Turntable() {
        _serialPort = new SerialPort();
    }

    public void init() {
        _reader = new BufferedReader(new InputStreamReader(_serialPort.getInputStream()));
        _writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_serialPort.getOutputStream())));
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = _reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        _serialPort.closePort();
    }

}
