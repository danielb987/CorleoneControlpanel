package se.bergqvist.turntable;

import com.fazecast.jSerialComm.SerialPortTimeoutException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import se.bergqvist.serial.SerialPort;

/**
 * Turntable
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class Turntable implements Runnable {

    private final String PORTNAME = "/dev/ttyUSB0";

    private final SerialPort _serialPort;
//    private Reader _reader;
    private BufferedReader _reader;
    private PrintWriter _writer;

    public static Turntable get() {
        return GET_INSTANCE.INSTANCE;
    }

    public Turntable() {
        _serialPort = new SerialPort(PORTNAME);
    }

    public Turntable init() {
//        _reader = new InputStreamReader(_serialPort.getInputStream());
        _reader = new BufferedReader(new InputStreamReader(_serialPort.getInputStream()));
        _writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_serialPort.getOutputStream())));
        new Thread(this).start();
//        _writer.print("Hello\r");
//        _writer.print("Hello\r");
//        _writer.print("Hello\r");
        System.out.println("Testar att vändskivan är igång");
        return this;
    }


    private final StringBuilder sb = new StringBuilder();

    @SuppressWarnings("SleepWhileInLoop")
    private String readLine() throws IOException {
        while (true) {
            try {
                int ch = _reader.read();
                if (ch == 10) {
                    String line = sb.toString();
                    sb.setLength(0);
                    return line;
                } else if (ch != 10) {
                    sb.append((char)ch);
                }
//                System.out.format("ch: %d, %c%n", ch, ch);
            } catch (SerialPortTimeoutException e) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e2) {
                    // Do nothing
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = readLine();
                System.out.println(line);
            } catch (IOException  e) {
                e.printStackTrace();
                _serialPort.closePort();
                return;
            }
        }
    }


    private static class GET_INSTANCE {

        private static Turntable INSTANCE = new Turntable().init();

    }

}
