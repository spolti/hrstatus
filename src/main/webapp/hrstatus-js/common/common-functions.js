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

