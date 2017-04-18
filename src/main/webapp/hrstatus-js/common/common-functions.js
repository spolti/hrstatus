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
/**
 * Created by fspolti on 12/1/16.
 */

/*function to handle the html5 required inputs to show its alert
 * Note that each form which uses html5 validations should have this line:
 * <input id="submit_handle" type="submit" style="display: none"/>
 */
function submitform() {
    $('#submit_handle').click();
    return false;
}

/*
 * call this function where the switch buttons are being in use.
 */
function initializeSwitchButton() {
    jQuery(".bootstrap-switch").bootstrapSwitch();
}

/*
 * This function validate if the password provided in the forms are equals
 */
function validatePassword() {
    var pass2 = document.getElementById("verifyPassword").value;
    var pass1 = document.getElementById("password").value;
    if (pass1 != pass2)
        document.getElementById("verifyPassword").setCustomValidity("As senhas digitadas não são iguais");
    else
        document.getElementById("verifyPassword").setCustomValidity('');
}

/*
 * Retrieve parameter from url, original code retrieved from stackoverflow.com
 */
function getParameterByName(name) {
    var regexS = "[\\?&]" + name + "=([^&#]*)",
        regex = new RegExp(regexS),
        results = regex.exec(window.location.search);
    if (results == null) {
        return "";
    } else {
        return decodeURIComponent(results[1].replace(/\+/g, " "));
    }
}

/*
 * Update the form on updating and creating new OS
 * Hide/show not supported fields according the selected OS.
 */
function setPort() {
    if (document.getElementById('type').value == 'WINDOWS') {
        document.getElementById('port').setAttribute('value', '23');
        document.getElementById('logDir').style.visibility = "hidden";
        document.getElementById('suCommand').style.visibility = "hidden";
    } else {
        document.getElementById('port').setAttribute('value', '22');
        document.getElementById('logDir').style.visibility = "visible";
        document.getElementById('suCommand').style.visibility = "visible";
    }
}

/*
 * Update the form on updating and creating new Database
 * Hide/show not supported fields according the selected Database.
 * The queries configured here are only for information purposes, user can not update it.
 */
function setDatabaseConfig() {
    if (document.getElementById('vendor').value == 'MYSQL') {
        document.getElementById('port').value = '3306';
        document.getElementById('queryDate').value = 'SELECT NOW() AS date;';
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'ORACLE') {
        document.getElementById('port').value = '1501';
        document.getElementById('queryDate').value = 'select sysdate from dual';
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'POSTGRESQL') {
        document.getElementById('port').value = '5432';
        document.getElementById('queryDate').value = 'SELECT now();';
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'DB2') {
        document.getElementById('port').value = '50000';
        document.getElementById('queryDate').value = "select VARCHAR_FORMAT(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MM:SS') FROM SYSIBM.SYSDUMMY1";
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'SQLSERVER') {
        document.getElementById('port').value = '1433';
        document.getElementById('queryDate').value = 'SELECT GETDATE();';
        document.getElementById('db_name').style.visibility = "visible";

    } else if (document.getElementById('vendor').value == 'MONGODB') {
        document.getElementById('port').value = '27017';
        document.getElementById('queryDate').value = '3306';
        document.getElementById('db_name').style.visibility = "hidden";
    }
    document.getElementById('queryDate').readOnly = true;
}

/*
 * Functions responsible for the setup.jsp page.
 */
function sendEmailTest() {
    var x = document.getElementById('testMailJndi');
    x.setAttribute('value', document.getElementById('mailJndi').value);
    $('#sendTestEmail').modal('show');
}

function addDest() {
    if ($('#configuration')[0].checkValidity()) {
        text = document.getElementById("addDestinatario").value;
        $('#destinatarios').append('<option value="' + text + '" selected>' + text + '</option>');
        $('#destinatarios').attr("size", (parseInt($("#destinatarios").attr("size")) + 1));
        $('#destinatarios').selectpicker('refresh');
    } else {
        submitform();
    }
}

function removeMail() {
    var x = document.getElementById("destinatarios");
    x.remove(x.selectedIndex);
    $('#destinatarios').attr("size", (parseInt($("#destinatarios").attr("size")) + 1));
    $('#destinatarios').selectpicker('refresh');
}
/*
 * End of setup.jsp funcitons
 */