package se.bergqvist.loconet;

import java.util.Arrays;

/**
 * A LocoNet packet.
 *
 * @author Daniel Bergqvist
 */
public class LocoNetPacket {

    private final int _buffer[];

    public static LocoNetPacket getThrowTurnoutLnPacket(boolean throwTurnout) {
        return null;
    }

    public LocoNetPacket(int buffer[]) {
        this._buffer = Arrays.copyOf(buffer, buffer.length);;
    }

}
