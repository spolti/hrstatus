/*
Copyright (C) 2012 Filippe Costa Spolti

This file is part of Hrstatus.

Hrstatus is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/*
 * @Author spolti - spolti@hrstatus.com.br
 */

public class DateParser {

    Logger log = Logger.getLogger(DateParser.class.getCanonicalName());

    public Date parser(String data) {

        SimpleDateFormat formatador = null;
        Date date1 = null;


        if (!data.equals(null) || !("").equals(data) || data != "") {
            // replacing spaces
            data = data.replace("  ", " ");

            data = data.replace("\n", "");
        }

        log.fine("Date received to parse: " + data);

        //supported formats
        final SimpleDateFormat formato1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); // pattern Mon Mar 04 16:09:05 BRT 2013
        final String patternFormato1 = "(Sun|Mon|Tue|Wed|Thu|Fri|Sat)\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s(BRA|[A-Z]{3,})\\s\\d{4}";

        final SimpleDateFormat formato2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", new Locale("pt", "BR")); // pattern Seg Mar 04 16:09:05 BRT 2013
        final String patternFormato2 = "(Dom|Seg|Ter|Qua|Qui|Sex|Sáb)\\s(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s([A-Z]{3,})\\s\\d{4}";

        final SimpleDateFormat formato3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy"); // patern Mon Mar 04 16:08:49 2013
        final String patternFormato3 = "(Sun|Mon|Tue|Wed|Thu|Fri|Sat)\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s\\d{4}";

        final SimpleDateFormat formato4 = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy", new Locale("pt", "BR")); // pattern  Seg Mar 04 16:08:49 2013
        final String patternFormato4 = "(Dom|Seg|Ter|Qua|Qui|Sex|Sab)\\s(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s\\d{4}";

        final SimpleDateFormat formato5 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", new Locale("pt", "BR")); // pattern segunda-feira,  4 de março de 2013 16h13min27s BRT

        final SimpleDateFormat formato6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String patternFormato6 = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";

        //pattern Wed Mar 01 00:00:00 GMT-03:00 1950
        final SimpleDateFormat formato7 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");


        //Verifying if the native timezone is AIX GRNLNDST e GRNLNDDT e GRNLNDST3, if so it is ignored.
        if (data.contains("GRNLNDST") || data.contains("GRNLNDDT") || data.contains("GRNLNDST3")) {
            log.fine("AIX pattern recognized, ignoring the timezone.");
            if (data.contains("GRNLNDST")) {
                data = data.replace("GRNLNDST", "");
            } else if (data.contains("GRNLNDDT")) {
                data = data.replace("GRNLNDDT", "");
            } else if (data.contains("GRNLNDST3")) {
                data = data.replace("GRNLNDST3", "");
            }
            formatador = formato3;

        } else if (data.equals(null) || ("").equals(data) || data == "") {
            log.severe("No date has been received, setting the default value.");
            data = ("Tue Mar 01 00:00:00 BRT 1950");
            formatador = formato1;
            // validating first pattern
        } else if (data.matches(patternFormato1)) {
            log.fine("pattern recognized EEE MMM dd HH:mm:ss z yyyy");
            data = data.replace("BRA", "BRT");
            formatador = formato1;
            // validating second pattern
        } else if (data.matches(patternFormato2)) {
            log.fine("pattern recognized EEE MMM dd HH:mm:ss z yyyy Locale pt_BR");
            formatador = formato2;
            // validating third pattern
        } else if (data.matches(patternFormato3)) {
            log.fine("pattern recognized EEE MMM dd HH:mm:ss yyyy ");
            formatador = formato3;
            // validating fourth pattern
        } else if (data.matches(patternFormato4)) {
            log.fine("pattern recognized EEE MMM dd HH:mm:ss yyyy Locale pt_BR");
            formatador = formato4;
            // validating fifth pattern
        } else if (data.toLowerCase().startsWith("domingo") || data.toLowerCase().startsWith("segunda")
                || data.toLowerCase().startsWith("terça") || data.toLowerCase().startsWith("terca")
                || data.toLowerCase().startsWith("quarta") || data.toLowerCase().startsWith("quinta")
                || data.toLowerCase().startsWith("sexta") || data.toLowerCase().startsWith("sabado")
                || data.toLowerCase().startsWith("sábado")) {

            log.info("pattern recognized: Data completa por extenso");

            final String []dataPorExtenso = data.split(" ");

            //Rretrieving day of week
            String diaSemana = null;
            if (dataPorExtenso[0].endsWith(",")) {
                dataPorExtenso[0] = dataPorExtenso[0].substring(0, dataPorExtenso[0].length() - 1);
            }
            final String diaSemanaVal = dataPorExtenso[0].toLowerCase();

            if ("domingo".equals(diaSemanaVal)) {
                diaSemana = "Dom";
            } else if ("segunda-feira".equals(diaSemanaVal)) {
                diaSemana = "Seg";
            } else if ("segunda".equals(diaSemanaVal)) {
                diaSemana = "Seg";
            } else if ("terça-feira".equals(diaSemanaVal)) {
                diaSemana = "Ter";
            } else if ("terça".equals(diaSemanaVal)) {
                diaSemana = "Ter";
            } else if ("terca-feira".equals(diaSemanaVal)) {
                diaSemana = "Ter";
            } else if ("terca".equals(diaSemanaVal)) {
                diaSemana = "Ter";
            } else if ("quarta-feira".equals(diaSemanaVal)) {
                diaSemana = "Qua";
            } else if ("quarta".equals(diaSemanaVal)) {
                diaSemana = "Qua";
            } else if ("quinta-feira".equals(diaSemanaVal)) {
                diaSemana = "Qui";
            } else if ("quinta".equals(diaSemanaVal)) {
                diaSemana = "Qui";
            } else if ("sexta-feira".equals(diaSemanaVal)) {
                diaSemana = "Sex";
            } else if ("sexta".equals(diaSemanaVal)) {
                diaSemana = "Sex";
            } else if ("sabado".equals(diaSemanaVal)) {
                diaSemana = "Sab";
            } else if ("sábado".equals(diaSemanaVal)) {
                diaSemana = "Sab";
            }

            //Rretrieving day of month
            final String diaMes = dataPorExtenso[1];

            //Rretrieving month of year
            String mesAno = null;
            final String mesAnoVal = dataPorExtenso[3].toLowerCase();

            if ("janeiro".equals(mesAnoVal)) {
                mesAno = "Jan";
            } else if ("fevereiro".equals(mesAnoVal)) {
                mesAno = "Fev";
            } else if ("março".equals(mesAnoVal)) {
                mesAno = "Mar";
            } else if ("marco".equals(mesAnoVal)) {
                mesAno = "Mar";
            } else if ("abril".equals(mesAnoVal)) {
                mesAno = "Abr";
            } else if ("maio".equals(mesAnoVal)) {
                mesAno = "Mai";
            } else if ("junho".equals(mesAnoVal)) {
                mesAno = "Jun";
            } else if ("julho".equals(mesAnoVal)) {
                mesAno = "Jul";
            } else if ("agosto".equals(mesAnoVal)) {
                mesAno = "Ago";
            } else if ("setembro".equals(mesAnoVal)) {
                mesAno = "Set";
            } else if ("outubro".equals(mesAnoVal)) {
                mesAno = "Out";
            } else if ("novembro".equals(mesAnoVal)) {
                mesAno = "Nov";
            } else if ("dezembro".equals(mesAnoVal)) {
                mesAno = "Dez";
            }

            //Rretrieving hour
            final String[] horaTemp = dataPorExtenso[6].split("h");
            final String hora = horaTemp[0];

            //Rretrieving minute
            final String[] minF = dataPorExtenso[6].split("min");
            final String []minTemp = minF[0].split("h");
            final String min = minTemp[1];

            //Rretrieving second
            final String seg = minF[1].replace("s", "");

            //concatenating full hour
            final String horaFinal = hora + ":" + min + ":" + seg;

            //Rretrieving year
            final String ano = dataPorExtenso[5];

            //Rretrieving Locale
            final String locale = dataPorExtenso[7];

            //concatenating pattern EEE MMM dd HH:mm:ss yyyy Locale pt_BR"
            final String dataFinal = diaSemana + " " + mesAno + " " + diaMes + " " + horaFinal + " " + locale + " " + ano;
            data = dataFinal;
            formatador = formato5;

            // validating pattern 6
        } else if (data.matches(patternFormato6)) {
            log.fine("pattern recognized yyy-dd-mm HH:mm:ss");
            formatador = formato6;

            // validating pattern 7
        } else if (data.contains("GMT+") || data.contains("GMT-")) {
            log.fine("pattern recognized EEE MMM dd HH:mm:ss z yyyy");
            formatador = formato7;
        } else {
            log.severe("No pattern recognized.");
            log.severe("Unparsable date: " + data);
            data = ("Tue Mar 01 00:00:00 BRT 1950");
            formatador = formato1;
        }
        try {
            date1 = formatador.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }
}