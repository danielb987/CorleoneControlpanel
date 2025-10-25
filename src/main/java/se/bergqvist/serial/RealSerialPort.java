package se.bergqvist.serial;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Serial port
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class RealSerialPort implements SerialPort {

    private final com.fazecast.jSerialComm.SerialPort _serialPort;

    public RealSerialPort(String portname) {
        for (String port : getActualPortNames()) {
            System.out.format("Port: %s%n", port);
        }
//        System.exit(0);

        com.fazecast.jSerialComm.SerialPort serialPort;
        try {
            serialPort = com.fazecast.jSerialComm.SerialPort.getCommPort(portname);
            serialPort.openPort();
//            serialPort.setComPortTimeouts(com.fazecast.jSerialComm.SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
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

    @Override
    public InputStream getInputStream() {
        return this._serialPort.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return this._serialPort.getOutputStream();
    }

    @Override
    public void closePort() {
        this._serialPort.closePort();
    }



    public static List<String> getActualPortNames() {
        // first, check that the comm package can be opened and ports seen
        List<String> portNames = new ArrayList<>();
        com.fazecast.jSerialComm.SerialPort[] portIDs = com.fazecast.jSerialComm.SerialPort.getCommPorts();
        // find the names of suitable ports
        for (com.fazecast.jSerialComm.SerialPort portID : portIDs) {
            portNames.add(portID.getSystemPortName());
        }
        // On Linux and Mac, try to find symlinks and to use the system property purejavacomm.portnamepattern
        if (SystemType.isLinux() || SystemType.isMacOSX()) {
            File[] files = new File("/dev").listFiles();
            if (files != null ) {
                // Find symlinks linked to real ports
                Set<String> symlinkPorts = Stream.of(files).filter(file -> !file.isDirectory()
                        && portNames.contains(getSymlinkTarget(file))
                        && !portNames.contains(file.getName())).map(File::getName).collect(Collectors.toSet());
                portNames.addAll(symlinkPorts);
                System.out.format("Adding symlink port %s%n", symlinkPorts);

                // Let the user add additional serial ports
                String portnamePattern = System.getProperty("purejavacomm.portnamepattern");
                if (portnamePattern != null) {
                    Pattern pattern = Pattern.compile(portnamePattern);
                    Set<String> ports = Stream.of(files).filter(file -> !file.isDirectory()
                            && pattern.matcher(file.getName()).matches()
                            && !portNames.contains(file.getName())).map(File::getName).collect(Collectors.toSet());
                    portNames.addAll(ports);
                    System.out.format("Adding user-specified ports %s matching pattern %s%n", ports, portnamePattern);
                }
            }
        }
        return portNames;
    }

    private static String getSymlinkTarget(File symlink) {
        try {
            // Path.toRealPath() follows a symlink
            return symlink.toPath().toRealPath().toFile().getName();
        } catch (IOException e) {
            return null;
        }
    }



    public class SystemType {

        static final public int MACCLASSIC = 1; // no longer supported - latest JVM is 1.1.8
        static final public int MACOSX = 2;
        static final public int WINDOWS = 4;
        static final public int LINUX = 5;
        static final public int OS2 = 6;
        static final public int UNIX = 7;

        static int type = 0;
        static boolean isSet = false;

        static String osName;

        /**
         * Get the integer constant for the OS. Useful in switch statements.
         *
         * @return Type as an integer
         */
        public static int getType() {
            setType();
            return type;
        }

        /**
         * The os.name property
         *
         * @return OS name
         */
        public static String getOSName() {
            setType();
            return osName;
        }

        /**
         * Convenience method to determine if OS is Mac OS X. Useful if an exception
         * needs to be made for Mac OS X.
         *
         * @return true if on Mac OS X.
         */
        public static boolean isMacOSX() {
            setType();
            return (type == MACOSX);
        }

        /**
         * Convenience method to determine if OS is Linux. Useful if an exception
         * needs to be made for Linux.
         *
         * @return true if on Linux
         */
        public static boolean isLinux() {
            setType();
            return (type == LINUX);
        }

        /**
         * Convenience method to determine if OS is Microsoft Windows. Useful if an
         * exception needs to be made for Microsoft Windows.
         *
         * @return true if on Microsoft Windows
         */
        public static boolean isWindows() {
            setType();
            return (type == WINDOWS);
        }

        /**
         * Convenience method to determine if OS is OS/2. Useful if an exception
         * needs to be made for OS/2.
         *
         * @return true if on OS/2
         */
        public static boolean isOS2() {
            setType();
            return (type == OS2);
        }

        /**
         * Convenience method to determine if OS is Unix. Useful if an exception
         * needs to be made for Unix.
         *
         * @return true if on Unix
         */
        public static boolean isUnix() {
            setType();
            return (type == UNIX);
        }

        static void setType() {
            if (isSet) {
                return;
            }
            isSet = true;

            osName = System.getProperty("os.name");
            String lowerCaseName = osName.toLowerCase();

            if (lowerCaseName.contains("os x")) { // Prefered test per http://developer.apple.com/library/mac/#technotes/tn2002/tn2110.html
                // Mac OS X
                type = MACOSX;
            } else if (lowerCaseName.contains("linux")) {
                // Linux
                type = LINUX;
            } else if (lowerCaseName.contains("os/2")) {
                // OS/2
                type = OS2;
            } else if (lowerCaseName.contains("windows")) {  // usually a suffix indicates flavor
                // Windows
                type = WINDOWS;
            } else if (lowerCaseName.contains("nix") || lowerCaseName.contains("nux") || lowerCaseName.contains("aix") || lowerCaseName.contains("solaris")) {
                // Unix
                type = UNIX;
            } else {
                // No match
                type = 0;
                System.out.format("Could not determine system type from os.name=/%s/%n", osName);
            }
        }

    }

}
