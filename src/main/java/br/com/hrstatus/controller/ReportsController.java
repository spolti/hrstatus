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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.dao.ServersInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class ReportsController {

	Logger log =  Logger.getLogger(ReportsController.class.getCanonicalName());
	
	@Autowired
	private ServersInterface iteracoesDAO;
	@Autowired
	private BancoDadosInterface bancoDadosDAO;
	@Autowired
	private HttpServletResponse response;
	UserInfo userInfo = new UserInfo();


	@SuppressWarnings("all")
	@Path("/reports/reportFull")
	public InputStream fullReport() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportFull");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportFull.jasper"));

		List<Servidores> listServers = this.iteracoesDAO.listServers();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();

		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportFull.pdf");
			return new ByteArrayInputStream(bytes);
		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	@Path("/reports/reportServersOK")
	@SuppressWarnings("all")
	public InputStream serversOK() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/serversOK");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportServersOK.jasper"));
		List<Servidores> listServers = iteracoesDAO.getServersOK();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportServersOK.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	@Path("/reports/reportServersNOK")
	@SuppressWarnings("all")
	public InputStream reportServersNOK() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportServersNOK");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportServersNOK.jasper"));
		List<Servidores> listServers = iteracoesDAO.getServersNOK();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportServersNOK.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	@Path("/reports/reportSOLinux")
	@SuppressWarnings("all")
	public InputStream soLinux() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOLinux");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportSOLinux.jasper"));
		List<Servidores> listServers = iteracoesDAO.getSOLinux();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportSOLinux.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	@Path("/reports/reportSOWindows")
	@SuppressWarnings("all")
	public InputStream reportSOWindows() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOWindows");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportSOWindows.jasper"));
		List<Servidores> listServers = iteracoesDAO.getSOWindows();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportSOWindows.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	@Path("/reports/reportSOUnix")
	@SuppressWarnings("all")
	public InputStream reportSOUnix() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOUnix");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportSOUnix.jasper"));
		List<Servidores> listServers = iteracoesDAO.getSOUnix();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportSOUnix.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	@Path("/reports/reportSOOthers")
	@SuppressWarnings("all")
	public InputStream reportSOOthers() throws FileNotFoundException, JRException {
		log
				.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportSOOthers");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportSOOthers.jasper"));

		List<Servidores> listServers = iteracoesDAO.getSOOthers();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listServers, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportSOOthers.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	@Path("/reports/reportDataBaseFull")
	@SuppressWarnings("all")
	public InputStream reportDataBaseFull() throws FileNotFoundException, JRException {
		log
				.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBaseFull");
		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportDataBaseFull.jasper"));

		List<BancoDados> listDataBases = this.bancoDadosDAO.listDataBases();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportDataBaseFull.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	@Path("/reports/reportDataBaseOK")
	@SuppressWarnings("all")
	public InputStream reportDataBaseOK() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBaseOK");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportDataBaseOK.jasper"));
		List<BancoDados> listDataBases = this.bancoDadosDAO.getdataBasesOK();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportDataBaseOK.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	@Path("/reports/reportDataBaseNOK")
	@SuppressWarnings("all")
	public InputStream reportDataBaseNOK() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBaseNOK");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportDataBaseNOK.jasper"));
		List<BancoDados> listDataBases = this.bancoDadosDAO.getdataBasesNOK();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportDataBaseNOK.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	@Path("/reports/reportDataBaseMysql")
	@SuppressWarnings("all")
	public InputStream reportDataBaseMysql() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBaseMysql");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportDataBaseMysql.jasper"));
		List<BancoDados> listDataBases = this.bancoDadosDAO.getVendorMysql();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportDataBaseMysql.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	@Path("/reports/reportDataBaseOracle")
	@SuppressWarnings("all")
	public InputStream reportDataBaseOracle() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBaseOracle");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportDataBaseOracle.jasper"));
		List<BancoDados> listDataBases = this.bancoDadosDAO.getVendorOracle();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportDataBaseOracle.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	@Path("/reports/reportDataBasePostgre")
	@SuppressWarnings("all")
	public InputStream reportDataBasePostgre() throws FileNotFoundException, JRException {
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /reports/reportDataBasePostgre");

		JasperReport jasperFile = (JasperReport) JRLoader.loadObject(ReportsController.class.getResourceAsStream("/jasper/reportDataBasePostgre.jasper"));
		List<BancoDados> listDataBases = this.bancoDadosDAO.getVendorPostgre();
		JasperReport jasperStream = jasperFile;
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listDataBases, false);
		Map parametros = new HashMap();
		try {
			byte[] bytes = JasperRunManager.runReportToPdf(jasperStream,parametros, ds);
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=reportDataBasePostgre.pdf");
			return new ByteArrayInputStream(bytes);

		} catch (JRException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
}