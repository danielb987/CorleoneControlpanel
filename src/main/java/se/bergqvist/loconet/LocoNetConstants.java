package se.bergqvist.loconet;

/**
 * LocoNet constants.
 *
 * @author Daniel Bergqvist
 */
public class LocoNetConstants {

    /* Various bit masks */
    public final static int OPC_SW_ACK_CLOSED = 0x20;  /* command switch closed/open bit   */
    public final static int OPC_SW_ACK_OUTPUT = 0x10;  /* command switch output on/off bit */
    public final static int OPC_INPUT_REP_CB = 0x40;  /* control bit, reserved otherwise      */
    public final static int OPC_INPUT_REP_SW = 0x20;  /* input is switch input, aux otherwise */
    public final static int OPC_INPUT_REP_HI = 0x10;  /* input is HI, LO otherwise            */
    public final static int OPC_SW_REP_SW = 0x20;  /* switch input, aux input otherwise    */
    public final static int OPC_SW_REP_HI = 0x10;  /* input is HI, LO otherwise            */
    public final static int OPC_SW_REP_CLOSED = 0x20;  /* 'Closed' line is ON, OFF otherwise   */
    public final static int OPC_SW_REP_THROWN = 0x10;  /* 'Thrown' line is ON, OFF otherwise   */
    public final static int OPC_SW_REP_INPUTS = 0x40;  /* sensor inputs, outputs otherwise     */
    public final static int OPC_SW_REQ_DIR = 0x20;  /* switch direction - closed/thrown     */
    public final static int OPC_SW_REQ_OUT = 0x10;  /* output On/Off                        */

    /* LocoNet opcodes */
    public final static int OPC_GPBUSY = 0x81;
    public final static int OPC_GPOFF = 0x82;
    public final static int OPC_GPON = 0x83;
    public final static int OPC_IDLE = 0x85;
    public final static int OPC_SW_REQ = 0xb0;
    public final static int OPC_SW_REP = 0xb1;
    public final static int OPC_INPUT_REP = 0xb2;
    public final static int OPC_UNKNOWN = 0xb3;
    public final static int OPC_LONG_ACK = 0xb4;
    public final static int OPC_SW_STATE = 0xbc;
    public final static int OPC_SW_ACK = 0xbd;
    public final static int OPC_PEER_XFER = 0xe5;
    public final static int OPC_MASK = 0x7f;  /* mask for acknowledge opcodes */


    /**
     * Encode LocoNet Opcode as a string
     *
     * @param opcode  a LocoNet opcode value
     * @return string containing the opcode "name"
     */
    public final static String OPC_NAME(int opcode) {
        return switch (opcode) {
            case OPC_GPBUSY -> "OPC_GPBUSY";
            case OPC_GPOFF -> "OPC_GPOFF";
            case OPC_GPON -> "OPC_GPON";
            case OPC_IDLE -> "OPC_IDLE";
            case OPC_SW_REQ -> "OPC_SW_REQ";
            case OPC_SW_REP -> "OPC_SW_REP";
            case OPC_INPUT_REP -> "OPC_INPUT_REP";
            case OPC_UNKNOWN -> "OPC_UNKNOWN";
            case OPC_LONG_ACK -> "OPC_LONG_ACK";
            case OPC_SW_STATE -> "OPC_SW_STATE";
            case OPC_SW_ACK -> "OPC_SW_ACK";
            case OPC_PEER_XFER -> "OPC_PEER_XFER";
            default -> "<unknown>";
        };
    }

}
