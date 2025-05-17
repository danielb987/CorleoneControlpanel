package se.bergqvist.turntable;

import com.fazecast.jSerialComm.SerialPortTimeoutException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.bergqvist.serial.SerialPort;

/**
 * Turntable
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class Turntable implements Runnable {


    public interface TurntableListener {

        void info(int speed, boolean direction, int pos, int track, boolean head);

    }



    private final String PORTNAME = "/dev/ttyTurntable";
//    private final String PORTNAME = "/dev/ttyUSB0";

    private final List<TurntableListener> _listeners = new ArrayList<>();
    private final SerialPort _serialPort;
//    private Reader _reader;
    private BufferedReader _reader;
    private PrintWriter _writer;

    public static Turntable get() {
        return GET_INSTANCE.INSTANCE;
    }

    public Turntable() {
        _serialPort = new SerialPort(PORTNAME);

        int MAX = 48000;
        double diameter = 130 * 12 * 25.4 / 160;
        double omkrets = diameter * Math.PI;

        System.out.format("Diameter: %1.2f mm, Omkrets: %1.2f mm, Steg/mm: %1.0f%n", diameter, omkrets, MAX / omkrets);
//        System.exit(0);
    }

    public Turntable init() {
//        _reader = new InputStreamReader(_serialPort.getInputStream());
        _reader = new BufferedReader(new InputStreamReader(_serialPort.getInputStream()));
        _writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_serialPort.getOutputStream())));
        new Thread(this).start();
//        _writer.print("Hello\r");
        _writer.print("!LOCALTION MAX\r");
        _writer.flush();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        _writer.print("!AUTO\r");
        _writer.flush();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        _writer.print("!AUTO 10\r");
        _writer.flush();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
//        _writer.print("!AUTO 100\r");
//        _writer.print("!AUTO 200\r");
//        _writer.print("!AUTO 001\r");
        _writer.flush();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
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
                    if (! line.isBlank()) {
                        return line;
                    }
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
                if (!line.startsWith("#INFO: ")) {
                    System.out.println(line);
                }

                if (line.startsWith("#INFO: ")) {
//                    Pattern pattern = Pattern.compile("^\\#INFO\\: (\\d+) (\\w\\w) (\\d+) (\\w+) (\\w|\\s)");
                    Pattern pattern = Pattern.compile("^\\#INFO\\: (\\d+) (\\w\\w) (\\d+) (\\w+|--) (\\w|\\s)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
//                        for (int i=0; i <= matcher.groupCount(); i++) {
//                            System.out.format("Group %d: %s%n", i, matcher.group(i));
//                        }
                        int speed = Integer.parseInt(matcher.group(1));
                        boolean direction = "CW".equals(matcher.group(2));
                        int pos = Integer.parseInt(matcher.group(3));
                        int track = -1;
                        if (! "--".equals(matcher.group(4))) {
                            track = Integer.parseInt(matcher.group(4));
                        }
                        boolean head = "H".equals(matcher.group(5));
                        int tempTrack = track;
                        java.awt.EventQueue.invokeLater(() -> {
                            for (var l : _listeners) {
                                l.info(speed, direction, pos, tempTrack, head);
                            }
                        });
                    }
                }
            } catch (IOException  e) {
                e.printStackTrace();
                _serialPort.closePort();
                return;
            }
        }
    }

    public void runHome() {
        _writer.format("!RUN HOME\r");
        _writer.flush();
    }

    public void gotoTrack(int track, boolean head) {
        _writer.format("!TRACK %02d %s\r", track, head ? "HEAD" : "TAIL");
        _writer.flush();
    }


    public void gotoPosition(int pos) {
        _writer.format("!RUN %05d\r", pos);
        _writer.flush();
    }

    public void program(int track) {
        _writer.format("!PROGRAM %02d\r", track);
        _writer.flush();
    }


    public void addListener(TurntableListener l) {
        _listeners.add(l);
    }


    private static class GET_INSTANCE {

        private static Turntable INSTANCE = new Turntable().init();

    }

}
