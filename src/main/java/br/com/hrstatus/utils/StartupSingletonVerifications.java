/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/*
 * @author spolti
 * Classes executeCommand e secureClose retiradas de:
 * http://www.devmedia.com.br/executando-shell-scripts-utilizando-java/26494
 */

@Singleton
@Startup
public class StartupSingletonVerifications {

	Logger log =  Logger.getLogger(StartupSingletonVerifications.class.getCanonicalName());
	static StartupSingletonVerifications shell = new StartupSingletonVerifications();
	
	private enum binaries {
		//ntpdate: utilizado para atualizar a data/hora dos servidores, local e remoto
		//net: utilizado para obter a data/hora de servidores Windows.
		ntpdate, net
	}
	
	@PostConstruct
	public void verifyBinaries() {
		String message = "Verificando binários necessários para execução do HrStatus...";
		String result = null;
		log.info(message);
		
        for (binaries bin : binaries.values()){
        	result = shell.executeCommand("type " + bin.name() + ";  echo $?");
        	
        	if (result.equals("0")){
        		log.info("Binário " + bin.name() + ": OK");
        	}else {
        		log.warning("Binário " + bin.name() + ": Não encontrado, isto pode causar mal funcionamento de algumas funcionaliades do HrStatus.");
        	}
        }
	}
	
	/*
	 * Returna 0 ou 1
	 * 0 -> Binário encontrado
	 * 1 -> Binário não encontrado
	 */
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
            log.severe("Erro ao executar comando shell" + ioe.getMessage());
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
            log.severe("Erro = " + ex.getMessage());
        }
    }
}