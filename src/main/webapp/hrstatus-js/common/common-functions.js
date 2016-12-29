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
* This function validate if the password provided in the forms are equals
*/
window.onload = function () {
    document.getElementById("password").onchange = validatePassword;
    document.getElementById("verifyPassword").onchange = validatePassword;
}
function validatePassword() {
    var pass2 = document.getElementById("verifyPassword").value;
    var pass1 = document.getElementById("password").value;
    if (pass1 != pass2)
        document.getElementById("verifyPassword").setCustomValidity("As senhas digitadas não são iguais");
    else
        document.getElementById("verifyPassword").setCustomValidity('');
}

/*
* Retrieve parameter from url, original code retrieved from stackoverflow
*/
function getParameterByName( name ){
    var regexS = "[\\?&]"+name+"=([^&#]*)",
        regex = new RegExp( regexS ),
        results = regex.exec( window.location.search );
    if( results == null ){
        return "";
    } else{
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
 */
function setDatabaseConfig() {

    if (document.getElementById('vendor').value == 'MYSQL') {
        document.getElementById('port').setAttribute('value', '3306');
        document.getElementById('queryDate').setAttribute('value', 'SELECT NOW() AS date;');
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'ORACLE'){
        document.getElementById('port').setAttribute('value', '1501');
        document.getElementById('queryDate').setAttribute('value', 'select sysdate from dual');
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'POSTGRESQL'){
        document.getElementById('port').setAttribute('value', '5432');
        document.getElementById('queryDate').setAttribute('value', 'SELECT now();');
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'DB2'){
        document.getElementById('port').setAttribute('value', '50000');
        document.getElementById('queryDate').setAttribute('value', "select VARCHAR_FORMAT(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MM:SS') FROM SYSIBM.SYSDUMMY1");
        document.getElementById('db_name').style.visibility = "hidden";

    } else if (document.getElementById('vendor').value == 'SQLSERVER'){
        document.getElementById('port').setAttribute('value', '1433');
        document.getElementById('queryDate').setAttribute('value', 'SELECT GETDATE();');
        document.getElementById('db_name').style.visibility = "visible";

    } else if (document.getElementById('vendor').value == 'MONGODB'){
        document.getElementById('port').setAttribute('value', '27017');
        document.getElementById('queryDate').setAttribute('value', '3306');
        document.getElementById('db_name').style.visibility = "hidden";
    }
}

