package br.com.hrstatus.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ExecuteOSCommand {
	
	Logger log =  Logger.getLogger(ExecuteOSCommand.class.getCanonicalName());
	
    public String executeCommand(final String command) {
        
        final ArrayList<String> commands = new ArrayList<String>();
        String retorno = null;
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(command);
        
        BufferedReader br = null;        
        
        try {                        
            final ProcessBuilder p = new ProcessBuilder(commands);
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            
            String line;            
            while((line = br.readLine()) != null) {
                retorno = line;
            }
            
        } catch (IOException ioe) {
            log.severe("Error executing shell command: " + ioe.getMessage());
        } finally {
            secureClose(br);
        }
        
        return retorno;
    }

    
    private void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            log.severe("Error: " + ex.getMessage());
        }
    }
}