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

package br.com.ohsnap.hrstatus.utils;

/*
 * @author spolti
 */

public class PassGenerator {

	private String passGenerator; 
	
	private int tam = 12;
	
    private char[] allowedCharacters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',   
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',   
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',   
            'W', 'X', 'Y', 'Z'}; 
    
    public PassGenerator() {        
        this.setPassGenerator(this.gemPass());
    } 
    
    public PassGenerator(int length) {  
        this.setTam(length);  
        this.setPassGenerator(this.gemPass());  
          
    } 
    
    
    public String gemPass() {  
        String tmp = "";  
        for (int i = 0; i < this.getTam(); i++) {  
            int c = (int) Math.floor(Math.random()*this.getAllowedCharacters().length);  
            tmp += this.getAllowedCharacters()[c];  
        }  
        return tmp;  
    } 
    
    private String getPassGenerator() {  
        return passGenerator;
    }  
    
    private void setPassGenerator(String passGenerator) {  
        this.passGenerator = passGenerator; 
    } 
    
    private char[] getAllowedCharacters() {  
        return allowedCharacters;  
    }
    
    public String toString() {   
        return this.getPassGenerator();  
    }
    
    private int getTam() {  
        return tam;  
    }  
  
    private void setTam(int tam) {  
        this.tam = tam;  
    }  
}