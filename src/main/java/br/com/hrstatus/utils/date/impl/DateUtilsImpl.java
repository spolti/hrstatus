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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DateUtilsImpl implements DateUtils {

    /**
     * Examples of supported formats:
     * Mon Mar 04 16:09:05 BRT 2013
     * Mon Mar 04 16:08:49 2013
     * Wed Mar 01 00:00:00 GMT-03:00 1950
     * 2015-05-25 12:23:02
     */
    private final DateTimeFormatter globalFormatter = DateTimeFormatter.ofPattern(""
            + "[EEE MMM dd HH:mm:ss z yyyy]"
            + "[E MMM dd HH:mm:ss yyyy]"
            + "[dd HH:mm:ss yyyy]"
            + "[EEE dd HH:mm:ss z yyyy]"
            + "[E MMM dd HH:mm:ss yyyy]"
            + "[yyyy-MM-dd HH:mm:ss]"
            + "[eeee, d 'de' MMMM 'de' yyyy HH'h'mm'min'ss's' z]"
    );

    /**
     * Allow to parse dates in portuguese (The month should be in lower case)
     * Seg mar 04 16:08:49 2013
     * Seg Mar 04 16:09:05 BRT 2013
     * segunda-feira, 4 de março de 2013 16h13min27s BRT
     */
    private final DateTimeFormatter formatterPtBR = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(globalFormatter)
            .toFormatter(new Locale("pt", "BR"));

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDateTime parse(String date) {
        try {
            return LocalDateTime.parse(date, isPtBr(date) ? formatterPtBR : globalFormatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Falha ao converter data " + date, e.getMessage(), e.getErrorIndex());
        }
    }

    /**
     * Test if the string date contains pt-BR names
     *
     * @param date is a string containing the date that will be parsed
     * @return True or False
     */
    private boolean isPtBr(String date) {
        final String ptBr = "(?i)(Dom|Dom\\w.+|Seg|Seg\\w.+|Ter|Ter\\w.+|Qua|Qua\\w.+|Qui|Qui\\w.+|Sex|Sex\\w.+|S?b|S?b\\w.+)(\\s\\w.+|,\\s\\w.+)";
        return true ? date.matches(ptBr) : false;
    }

}