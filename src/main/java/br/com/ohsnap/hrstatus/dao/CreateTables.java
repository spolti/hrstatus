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

package br.com.ohsnap.hrstatus.dao;

/*
 * @author spolti
 */

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import br.com.ohsnap.hrstatus.model.Lock;
import br.com.ohsnap.hrstatus.model.Users;

public class CreateTables {
	public static void main(String[] args) {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		
//		cfg.addAnnotatedClass(Servidores.class);
//		SchemaExport se = new SchemaExport(cfg);
//		se.create(true, true);

		cfg.addAnnotatedClass(Users.class);
		SchemaExport se1 = new SchemaExport(cfg);
		se1.create(true, true);
		
//		cfg.addAnnotatedClass(Authorities.class);
//		SchemaExport se1 = new SchemaExport(cfg);
//		se1.create(true, true);
	
//		cfg.addAnnotatedClass(Configurations.class);
//		SchemaExport se1 = new SchemaExport(cfg);
//		se1.create(true, true);

//		cfg.addAnnotatedClass(PassExpire.class);
//		SchemaExport se1 = new SchemaExport(cfg);
//		se1.create(true, true);
		
//		cfg.addAnnotatedClass(Lock.class);
//		SchemaExport se = new SchemaExport(cfg);
//		se.create(true, true);
		
	}
}
