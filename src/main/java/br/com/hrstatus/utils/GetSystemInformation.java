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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

/*
 * @author spolti
 */

@Service
public class GetSystemInformation {

	Logger log =  Logger.getLogger(GetSystemInformation.class.getCanonicalName());
	
	public List<String> SystemInformation () {
		
		ArrayList<String> result = new ArrayList<String>();
		result.add(System.getProperty("java.runtime.version"));
		result.add(System.getProperty("java.vendor"));
		result.add(System.getProperty("java.vm.name"));
		result.add(System.getProperty("os.name") + "-" + System.getProperty("os.version"));
		
		return result;
	}
}