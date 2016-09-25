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

package br.com.hrstatus.rest.resources;

import br.com.hrstatus.model.OperatingSystem;
import br.com.hrstatus.model.support.VerificationStatus;
import br.com.hrstatus.repository.Repository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
@Path("resource/operating-system")
@Produces("application/json")
@Transactional
public class OperatingSystemRestRepository {

    private Logger log = Logger.getLogger(OperatingSystemRestRepository.class.getName());

    @Inject
    private Repository repository;

    /*
    * Load the Operating Systems resources
    */
    @GET
    @Path("load")
    public void load(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/admin/resource/operating-system.jsp").forward(request, response);
    }

    /*
    * Register a new Operating System
    */
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_JSON)
    public void newOperationgSystem(OperatingSystem operatingSystem, @Context HttpServletRequest request,
                                    @Context HttpServletResponse response) throws ServletException, IOException {

        log.info("hostname " + operatingSystem.getHostname());
        log.info("address " + operatingSystem.getAddress());
        log.info("bootstrapSelectType " + operatingSystem.getType());
        log.info("port " + operatingSystem.getPort());
        log.info("username " + operatingSystem.getUsername());
        log.info("password " + operatingSystem.getPassword());
        log.info("logDir " + operatingSystem.getLogDir());
        log.info("suCommand " + operatingSystem.getSuCommand());
        log.info("verify " + operatingSystem.isToVerify());
        operatingSystem.setStatus(VerificationStatus.NOT_VERIFIED);
        repository.save(operatingSystem);
        request.getRequestDispatcher("/admin/resource/operating-system.jsp").forward(request, response);
    }

    /*
    * Register a new Operating System
    */
    @GET
    @Path("get")
    public void get() {
//
//        repository.load().stream().forEach(e ->{
//            log.info("hostname " + e.getHostname());
//
//            log.info("address " + e.getAddress());
//
//            log.info("bootstrapSelectType " + e.getType());
//
//            log.info("port " + e.getPort());
//
//            log.info("username " + e.getUsername());
//
//            log.info("username " + e.getPassword());
//
//            log.info("logDir " + e.getLogDir());
//
//            log.info("suCommand " + e.getSuCommand());
//
//            log.info("verify " + e.isToVerify());
//
//            log.info("status " + e.getStatus());
//        });

    }

}