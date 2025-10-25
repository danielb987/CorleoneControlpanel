package se.bergqvist.serial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

/**
 * Serial port
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class FakeSerialPort implements Runnable, SerialPort {

    PipedInputStream _in = new PipedInputStream();
    PipedOutputStream _out = new PipedOutputStream();
    private BufferedReader _reader;
    private PrintWriter _writer;


    public FakeSerialPort() {
        try {
            _reader = new BufferedReader(new InputStreamReader(new PipedInputStream(_out)));
            _writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new PipedOutputStream(_in))));
            new Thread(this).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

     public static void purgeStream(InputStream serialStream) throws IOException {
        int count = serialStream.available();
        while (count > 0) {
            serialStream.skip(count);
            count = serialStream.available();
        }
    }

    @Override
    public InputStream getInputStream() {
        return _in;
    }

    @Override
    public OutputStream getOutputStream() {
        return _out;
    }

    @Override
    public void closePort() {
//        this._serialPort.closePort();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = _reader.readLine();
                if (line.startsWith("!TRACK ")) {
                    String[] parts = line.split(" ");
                    int track = Integer.parseInt(parts[1]);
                    boolean head = "HEAD".equals(parts[2]);
                    System.out.format("Go to track %d. Head: %b%n", track, head);
                    _writer.format("#INFO: 0 CW 0 %d %c%n", track, head ? 'H' : 'T');
                    _writer.flush();
                } else {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
