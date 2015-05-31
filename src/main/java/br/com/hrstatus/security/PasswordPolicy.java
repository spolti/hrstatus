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

package br.com.hrstatus.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author spolti
 */

public class PasswordPolicy {

	static int minSize = 8;
	static int maxSize = 16;
	static int minRep = 3;
	static int minSpecChar = 1;
	static int minUpCase = 1;
	static int minNum = 3;
	static char[] specChars = { '@', '#', '$', '(', ')', '&', '*' };
	static char[] alphabetic = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W',
			'V', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '@', '#', '$', '(', ')', '&', '*' };

	public static HashMap<String, String> verifyPassComplexity(String password) {

		// Criando tabela para armazenar os resultados.
		HashMap<String, String> result = new HashMap<String, String>();

		// Transformando a String password em um array de caracteres
		char passVar[] = password.toCharArray();

		if (passVar.length < minSize) {
			result.put("PassSize", "false");
			result.put("PassSizeMsg", "O tamanho mínimo da senha é " + minSize + " caracteres, o tamanho da senha digitada foi " + passVar.length);

		} else if (passVar.length > maxSize) {
			result.put("PassSize", "false");
			result.put("PassSizeMsg", "O tamanho máximo da senha é " + maxSize + " caracteres, o tamanho da senha digitada foi " + passVar.length);

		} else if ((passVar.length > minSize) && (passVar.length < maxSize)) {
			// Começam as Demais validações
			// verificando caracteres repetidos
			String rep = "";
			for (int i = 0; i < alphabetic.length; i++) {
				rep = PasswordPolicy.rep(alphabetic[i] + "", minRep);
				if (password.contains(rep)) {
					result.put("PassMinRep", "false");
					result.put("PassMinRepMsg","Não é permitido repetir o mesmo caracter por mais de " + minRep + " vezes. A sentença " + rep + " está inválida.");
				}
			}
			//Próxima validação

		}

		return result;
	}

	public static String rep(String chr, int freq) {
		String[] arr = new String[freq];
		Arrays.fill(arr, chr);
		@SuppressWarnings("rawtypes")
		List l = Arrays.asList(arr);
		String result = "";
		for (int i = 0; i < l.size(); i++) {
			result = (result + "" + l.get(i));
		}
		return result;
	}

	public static void main(String args[]) {

		List<String> passVal = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		map = verifyPassComplexity("123456789");

		Object[] valueMap = map.keySet().toArray();

		for (int i = 0; i < valueMap.length; i++) {
			if (map.get(valueMap[i]).equals("false")) {
				//System.out.println(map.get(valueMap[i + 1]));
				passVal.add(map.get(valueMap[i + 1]));
			}
		}

		for (int j = 0; j < passVal.size(); j++) {
			System.out.println(passVal.get(j));
		}

	}
}