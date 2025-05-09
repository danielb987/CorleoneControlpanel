package se.bergqvist.loconet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * LocoNet TCP client.
 *
 * The LocoNet over TCP protocol is described here:
 * https://loconetovertcp.sourceforge.net/Protocol/LoconetOverTcp.html
 *
 * @author Daniel Bergqvist
 */
public class LocoNetTcpClient {

    private static final LocoNetTcpClient INSTANCE = new LocoNetTcpClient().init();

    private PrintWriter out;
    private BufferedReader in;

    public static LocoNetTcpClient get() {
        return INSTANCE;
    }

    private LocoNetTcpClient init() {
        try {
            // create a socket to connect to the server running on localhost at port number 1234
            Socket socket = new Socket("localhost", 1234);

            // Setup output stream to send data to the server
            out = new PrintWriter(socket.getOutputStream(), true);

            // Setup input stream to receive data from the server
//            in = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new Receiver()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    void send(int[] message) {
        StringBuilder sb = new StringBuilder();
        sb.append("SEND");
        for (int value : message) {
            sb.append(String.format("%02X", value));
        }
        sb.append("\n");
        out.print(sb.toString());
    }

    private class Receiver implements Runnable {

        byte[] buffer = new byte[256];

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(in.readLine());
                }
/*
                    int count = in.read(buffer);
                    StringBuilder sb = new StringBuilder();
                    for (int i=0; i < count; i++) {
                        if (i > 0) sb.append(',');
                        sb.append(buffer[i]);
//                        sb.append(String.format("%02X", buffer[i]));
//                        sb.append(String.format("%02X", ((int)buffer[i]) & 0xFF));
                    }
                    System.out.println(sb.toString());
                }
*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
