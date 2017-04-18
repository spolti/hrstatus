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

package br.com.hrstatus.tests.date;

import br.com.hrstatus.utils.date.DateUtils;
import br.com.hrstatus.utils.date.impl.DateUtilsImpl;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class DataParserTest {

    private DateUtils dateUtils = new DateUtilsImpl();

    @Test
    public void testAllSupportedDateFormats() {

        final List<String> dates = Arrays.asList(
                "Mon Mar 04 16:09:05 BRT 2013",
                "Mon Mar 04 16:09:05 2013",
                "Wed Mar 22 04:10:59 2017",
                "Seg Mar 04 16:09:05 BRT 2013",
                "Mon Mar 04 16:08:49 2013",
                "Seg Mar 04 16:08:49 2013",
                "Seg mar 04 16:08:32 2013",
                "Seg mar 04 16:08:39 BRT 2013",
                "segunda-feira, 4 de março de 2013 16h13min27s BRT",
                "Wed Mar 01 00:00:00 GMT-03:00 1950",
                "2015-05-25 12:23:02"
        );

        dates.stream().forEach(date -> {
            Assert.assertTrue(dateUtils.parse(date) instanceof LocalDateTime);
        });

    }

}
