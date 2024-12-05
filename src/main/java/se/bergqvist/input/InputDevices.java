package se.bergqvist.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The list of input devices.
 *
 * @author Daniel Bergqvist
 */
public class InputDevices {

    public Set<Path> getInputDevices() {

        Set<Path> devices = new HashSet<>();

        try {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("/dev/input"))) {
                for (Path path : stream) {
                    String filename = path.toString();
//                    System.out.println(filename);
                    if (!Files.isDirectory(path) && filename.startsWith("/dev/input/event")) {
//                        System.out.println(path.getFileName());
                        if (callUdevadm(filename)) {
                            devices.add(path);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return devices;
    }

    private boolean callUdevadm(String inputDevice) {

        List<String> output = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("udevadm", "info", inputDevice);

        boolean isTouchScreen = false;
        String devPath = null;

	try {

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
                if (line.equals("E: ID_INPUT_TOUCHSCREEN=1")) {
                    isTouchScreen = true;
                }
                if (line.startsWith("E: DEVPATH=")) {
                    devPath = "/sys" + line.substring("E: DEVPATH=".length());
                }
//                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
//                System.out.println("Success!");
//                System.out.println(output);
            } else {
                //abnormal...
                throw new IOException("Exited with error: "+Integer.toString(exitVal));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
	}

        return isTouchScreen;
    }

}
