package br.com.hrstatus.action.os;

import java.util.logging.Logger;

/**
 * Created by fspolti on 3/3/16.
 */
public abstract class CommandExecutionHelper {

    private final static Logger log = Logger.getLogger(CommandExecutionHelper.class.getName());

    @SuppressWarnings("unchecked")
    public static class MyLogger implements com.jcraft.jsch.Logger {
        @SuppressWarnings("rawtypes")
        static java.util.Hashtable name = new java.util.Hashtable();
        static {
            name.put(new Integer(DEBUG), "DEBUG: ");
            name.put(new Integer(INFO), "INFO: ");
            name.put(new Integer(WARN), "WARN: ");
            name.put(new Integer(ERROR), "ERROR: ");
            name.put(new Integer(FATAL), "FATAL: ");
        }

        public boolean isEnabled(int level) {
            return true;
        }

        public void log(int level, String message) {
            System.out.print(name.get(new Integer(level)));
            log.fine(message);
        }
    }

   /*
    * Returns true if the host is localhost or 127.0.0.1
    */
    public static boolean isLocalhost (String host) {
        return true ? host.equals("localhost") || host.equals("127.0.0.1") : false;
    }
}
