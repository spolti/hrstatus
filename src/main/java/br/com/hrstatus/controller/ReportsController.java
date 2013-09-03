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

package br.com.hrstatus.controller;

/*
 * @author spolti
 */

import java.io.ByteArrayInputStream;
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

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.Iteracoes;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.utils.UserInfo;

@Resource
public class ReportsController {

	private Iteracoes iteracoesDAO;
	private BancoDadosInterface bancoDadosDAO;
	private HttpServletResponse response;
	UserInfo userInfo = new UserInfo();

	public ReportsController(Iteracoes iteracoesDAO,
			HttpServletResponse response, BancoDadosInterface bancoDadosDAO) {
		this.iteracoesDAO = iteracoesDAO;
		this.response = response;
		this.bancoDadosDAO = bancoDadosDAO;
	}

	
	@Get
	@Path("/reports/reportFull")

	@SuppressWarnings("all")
	public InputStream fullReport() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportFull");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportFull.jasper"));

		List<Servidores> listServers = this.iteracoesDAO.listServers();
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
	@SuppressWarnings("all")
	public InputStream serversOK() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/serversOK");

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
	@SuppressWarnings("all")
	public InputStream reportServersNOK() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportServersNOK");
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
	@SuppressWarnings("all")
	public InputStream soLinux() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOLinux");
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
	@SuppressWarnings("all")
	public InputStream reportSOWindows() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOWindows");
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
	@SuppressWarnings("all")
	public InputStream reportSOUnix() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass()).info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOUnix");
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
	@SuppressWarnings("all")
	public InputStream reportSOOthers() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass())
				.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOOthers");
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
	
	@Get
	@Path("/reports/reportDataBaseFull")
	@SuppressWarnings("all")
	public InputStream reportDataBaseFull() throws FileNotFoundException, JRException {
		Logger.getLogger(getClass())
				.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBaseFull");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class
				.getResourceAsStream("/jasper/reportDataBaseFull.jasper"));

		List<BancoDados> listDataBases = this.bancoDadosDAO.listDataBases();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
				listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,
					parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=reportDataBaseFull.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			Logger.getLogger(getClass()).error(e.getMessage());
		}
		return null;
	}
}
