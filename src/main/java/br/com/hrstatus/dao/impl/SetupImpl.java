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

package br.com.hrstatus.dao.impl;

import br.com.hrstatus.dao.SetupInterface;
import br.com.hrstatus.model.Setup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class SetupImpl implements SetupInterface {

    @PersistenceContext(unitName = "hrstatus")
    protected EntityManager em;


    @Override
    public Setup loadConfiguration() {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Setup> criteriaQuery = builder.createQuery(Setup.class);
        Root<Setup> setupRoot = criteriaQuery.from(Setup.class);
        criteriaQuery.select(setupRoot);
        Query query = em.createQuery(criteriaQuery);
        return (Setup) query.getSingleResult();
    }

    @Override
    public String mailSession() {
        Query q = em.createQuery("SELECT e.mailSession from Setup e");
        return String.valueOf(q.getSingleResult());
    }

    @Override
    public String mailFrom() {
        Query q = em.createQuery("SELECT e.mailFrom from Setup e");
        return String.valueOf(q.getSingleResult());
    }


}