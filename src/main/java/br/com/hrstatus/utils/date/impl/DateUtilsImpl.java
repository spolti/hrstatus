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

package br.com.hrstatus.utils.date.impl;

import br.com.hrstatus.utils.date.DateUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DateUtilsImpl implements DateUtils {

    // todo, o tratamento de data tem que ser feito quando extraída do servidor, assim evitamos código extra para validar datas de diferentes tipos

    /*
    * Supported formats:
    *   Mon Mar 04 16:09:05 BRT 2013
    *   Seg Mar 04 16:09:05 BRT 2013
    *   Mon Mar 04 16:08:49 2013
    *   Seg Mar 04 16:08:49 2013
    *   segunda-feira, 4 de março de 2013 16h13min27s BRT
    *   Wed Mar 01 00:00:00 GMT-03:00 1950
    *   2015-05-25 12:23:02
    */
    private static final List<String> patterns = Arrays.asList(
            "EEE MMM dd HH:mm:ss z yyyy",
            "EEE MMM dd HH:mm:ss yyyy",
            "dd HH:mm:ss yyyy",
            "EEE dd HH:mm:ss z yyyy",
            "E MMM dd HH:mm:ss yyyy",
            "EEE MMM dd HH:mm:ss z yyyy",
            "yyyy-MM-dd HH:mm:ss"
    );

    public static String format(String date) {

        String result = "not parsed";

        for (String pattern : patterns) {
            //System.out.println("testando pattern " +  pattern + " em " + date);
            try {
                ok(date);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDateTime.parse(date, formatter).toString();

            } catch (DateTimeParseException e) {

                result = e.getMessage();
            }
        }
        return result;

    }

    private static String ok(String date) {

        final String ptBr = "(Dom|Seg|Ter|Qua|Qui|Sex|Sáb)\\s(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s([A-Z]{3,})\\s\\d{4}";

        if (date.matches(ptBr)) {
            System.out.println(" date " + date + " bateu");

        }
        return date;
    }

    public static void main(String args[]) {
        System.out.println(format("04 16:09:05 BRT 2013"));
        System.out.println(format("Mon Mar 04 16:09:05 BRT 2013"));
        System.out.println(format("Seg Mar 04 16:09:05 BRT 2013"));
        System.out.println(format("Mon Mar 04 16:08:49 2013"));
        System.out.println(format("Seg Mar 04 16:08:49 2013"));
        System.out.println(format("segunda-feira, 4 de março de 2013 16h13min27s BRT"));
        System.out.println(format("Wed Mar 01 00:00:00 GMT-03:00 1950"));
        System.out.println(format("2015-05-25 12:23:02"));

//        Locale pt = new Locale("pt", "BR");
//
//        DateTimeFormatter formate = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
//        LocalDateTime test = LocalDateTime.parse("Mon Mar 04 16:09:05 BRT 2013", formate);
//
//        System.out.println("teste " +test.toString());


    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }


}