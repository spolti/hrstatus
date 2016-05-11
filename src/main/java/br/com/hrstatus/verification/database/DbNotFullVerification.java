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

package br.com.hrstatus.verification.database;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.action.databases.helper.IllegalVendorException;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.dao.BancoDadosInterface;
import br.com.hrstatus.model.BancoDados;
import br.com.hrstatus.resrources.ResourcesManagement;
import br.com.hrstatus.utils.UserInfo;
import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.verification.Verification;
import br.com.hrstatus.verification.helper.VerificationHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
 * @author spolti
 */

@Resource
public class DbNotFullVerification extends VerificationHelper{

    Logger log = Logger.getLogger(DbNotFullVerification.class.getCanonicalName());

    @Autowired
    private Result result;
    @Autowired
    private BancoDadosInterface dbDAO;
    @Autowired
    private ResourcesManagement resource;
    @Autowired
    private Verification verification;
    private UserInfo userInfo = new UserInfo();
    private DateUtils dt = new DateUtils();


    @Get("/database/startDataBaseVerification/notFullDBVerification")
    public void startNotFullDataBaseVerification() throws ClassNotFoundException, SQLException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IllegalVendorException {


        // Inserting HTML title in the result
        result.include("title", "Hr Status Home");
        log.info("[ " + userInfo.getLoggedUsername() + " ] URI called: /database/startDataBaseVerification/notFullDBVerification");
        log.info("[ " + userInfo.getLoggedUsername() + " ] Initializing a notFullDBVerification verification.");

        final List<BancoDados> listNOKbeforeVerification = this.dbDAO.getdataBasesNOK();
        final List<BancoDados> listdb = new ArrayList<BancoDados>();
        if (!resource.islocked("notFullDBVerification")) {
            resource.lockRecurso("notFullDBVerification");
            verification.databaseVerification(this.dbDAO.getdataBasesNOK());

            for (BancoDados db : listNOKbeforeVerification) {
                listdb.add(this.dbDAO.getDataBaseByID(db.getId()));
            }

            result.include("class", "activeBanco");
            result.include("bancoDados", listdb).forwardTo(HomeController.class).home("");
            resource.releaseLock("notFullDBVerification");
        } else {
            result.include("class", "activeBanco");
            result.include("info", "O recurso notFullDBVerification está locado, aguarde o término da mesma").forwardTo(HomeController.class).home("");
        }
    }
}