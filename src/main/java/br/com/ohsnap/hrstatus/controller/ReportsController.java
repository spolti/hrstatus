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

package br.com.ohsnap.hrstatus.controller;

/*
 * @author spolti
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.ohsnap.hrstatus.dao.Iteracoes;
import br.com.ohsnap.hrstatus.model.Servidores;

@Resource
public class ReportsController {

	private Iteracoes iteracoesDAO;
	private HttpServletResponse response;

	public ReportsController(Iteracoes iteracoesDAO,
			HttpServletResponse response) {
		this.iteracoesDAO = iteracoesDAO;
		this.response = response;
	}

	@Get
	@Path("/reports/reportFull")
	public InputStream fullReport() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("URI Called: /reports/reportFull");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportFull.jasper"));

		List<Servidores> listServers = iteracoesDAO.listServers();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();

		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportFull.pdf");
			return new ByteArrayInputStream(bytes);
		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;

	}

	@Get
	@Path("/reports/reportServersOK")
	public InputStream serversOK() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("URI Called: /reports/serversOK");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportServersOK.jasper"));
		List<Servidores> listServers = iteracoesDAO.getServersOK();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportServersOK.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}

		return null;
	}

	@Get
	@Path("/reports/reportServersNOK")
	public InputStream reportServersNOK() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info(
				"URI Called: /reports/reportServersNOK");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportServersNOK.jasper"));
		List<Servidores> listServers = iteracoesDAO.getServersNOK();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportServersNOK.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}

	@Get
	@Path("/reports/reportSOLinux")
	public InputStream soLinux() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("URI Called: /reports/reportSOLinux");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportSOLinux.jasper"));
		List<Servidores> listServers = iteracoesDAO.getSOLinux();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportSOLinux.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}

	@Get
	@Path("/reports/reportSOWindows")
	public InputStream reportSOWindows() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info(
				"URI Called: /reports/reportSOWindows");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportSOWindows.jasper"));
		List<Servidores> listServers = iteracoesDAO.getSOWindows();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportSOWindows.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}

	@Get
	@Path("/reports/reportSOUnix")
	public InputStream reportSOUnix() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("URI Called: /reports/reportSOUnix");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportSOUnix.jasper"));
		List<Servidores> listServers = iteracoesDAO.getSOUnix();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportSOUnix.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}

	@Get
	@Path("/reports/reportSOOthers")
	public InputStream reportSOOthers() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass())
				.info("URI Called: /reports/reportSOOthers");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportSOOthers.jasper"));

		List<Servidores> listServers = iteracoesDAO.getSOOthers();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportSOOthers.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}
}
