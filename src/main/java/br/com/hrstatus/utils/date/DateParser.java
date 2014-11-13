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

	Logger log =  Logger.getLogger(DateParser.class.getCanonicalName());
	
	public Date parser (String data){
		
		SimpleDateFormat formatador = null;
		Date date1 = null;

		//Removendo acentos caso exista - Função depreciada
//		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//    	String nfdNormalizedString = Normalizer.normalize(data, Normalizer.Form.NFD); 
//    	data = pattern.matcher(nfdNormalizedString).replaceAll("");
    	
		
    	if (!data.equals(null) || !data.equals("") || data != "") {
    		// Substituindo espaços duplicados
    		data = data.replace("  ", " ");
		 	// 	Substituindo quebra de linhas
		 	data = data.replace("\n", "");
    	}
		
		log.fine("Data recebida para parse: " + data);
		
		//Formatos Aceitos
		SimpleDateFormat formato1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); // padrao Mon Mar 04 16:09:05 BRT 2013
		String patternFormato1 = "(Sun|Mon|Tue|Wed|Thu|Fri|Sat)\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s(BRA|[A-Z]{3,})\\s\\d{4}";
		
		SimpleDateFormat formato2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",new Locale("pt", "BR")); // Seg Mar 04 16:09:05 BRT 2013
		String patternFormato2 = "(Dom|Seg|Ter|Qua|Qui|Sex|Sáb)\\s(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s([A-Z]{3,})\\s\\d{4}";
		
		SimpleDateFormat formato3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy"); // padrao Mon Mar 04 16:08:49 2013
		String patternFormato3 = "(Sun|Mon|Tue|Wed|Thu|Fri|Sat)\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s\\d{4}";
		
		SimpleDateFormat formato4 = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy",new Locale("pt", "BR")); // padrao Seg Mar 04 16:08:49 2013
		String patternFormato4 = "(Dom|Seg|Ter|Qua|Qui|Sex|Sab)\\s(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\s\\d{1,}\\s\\d{2}:\\d{2}:\\d{2}\\s\\d{4}";
		
		SimpleDateFormat formato5 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",new Locale("pt", "BR")); // padrão segunda-feira,  4 de março de 2013 16h13min27s BRT
		
		SimpleDateFormat formato6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String patternFormato6 = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
		
		//padrao Wed Mar 01 00:00:00 GMT-03:00 1950
		SimpleDateFormat formato7 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		
		
		//verificando se o Timezone é nativo AIX GRNLNDST e GRNLNDDT e GRNLNDST3, se for ignora timezone.
		if (data.contains("GRNLNDST") || data.contains("GRNLNDDT") || data.contains("GRNLNDST3")){
			log.fine("Padrao AIX reconhecido, ignorando timezone.");
			if (data.contains("GRNLNDST")){
				data = data.replace("GRNLNDST", "");
			}else if(data.contains("GRNLNDDT")){
				data = data.replace("GRNLNDDT","");
			}else if (data.contains("GRNLNDST3")){
				data =  data.replace("GRNLNDST3","");
			}
			formatador = formato3;

		}else if(data.equals(null) || data.equals("") || data == ""){
			log.severe("Nenhuma data foi recebida, setando valor default.");
			data = ("Tue Mar 01 00:00:00 BRT 1950");
			formatador = formato1;
		//validando primeiro padrão
		}else if(data.matches(patternFormato1) ){
			log.fine("Padrão reconhecido EEE MMM dd HH:mm:ss z yyyy");
			data = data.replace("BRA", "BRT");
			formatador = formato1;
		//validando segundo padrão	
		}else if (data.matches(patternFormato2)){
			log.fine("Padrão reconhecido EEE MMM dd HH:mm:ss z yyyy Locale pt_BR");
			formatador = formato2;
		//validando terceiro padrão
		}else if (data.matches(patternFormato3)){
			log.fine("Padrão reconhecido EEE MMM dd HH:mm:ss yyyy ");
			formatador = formato3;
		//validando quarto padrão
		}else if (data.matches(patternFormato4)){
			log.fine("Padrão reconhecido EEE MMM dd HH:mm:ss yyyy Locale pt_BR");
			formatador = formato4;
		//validando quinto padrão
		}else if (data.toLowerCase().startsWith("domingo") || data.toLowerCase().startsWith("segunda")
				|| data.toLowerCase().startsWith("terça") || data.toLowerCase().startsWith("terca")
				|| data.toLowerCase().startsWith("quarta") || data.toLowerCase().startsWith("quinta")
				|| data.toLowerCase().startsWith("sexta") || data.toLowerCase().startsWith("sabado")
				|| data.toLowerCase().startsWith("sábado")) {
			
			log.info("Padrão reconhecido: Data completa por extenso");
			
			String dataPorExtenso[] = data.split(" ");
			
			//Obtendo dia da semana
			String diaSemana = null;
			if (dataPorExtenso[0].endsWith(",")){
				dataPorExtenso[0] = dataPorExtenso[0].substring(0,dataPorExtenso[0].length() -1); 
			}
			String diaSemanaVal = dataPorExtenso[0].toLowerCase();
			
			if (diaSemanaVal.equals("domingo")){
				diaSemana = "Dom";
			}else if(diaSemanaVal.equals("segunda-feira")){
				diaSemana = "Seg";
			}else if(diaSemanaVal.equals("segunda")){
				diaSemana = "Seg";
			}else if(diaSemanaVal.equals("terça-feira")){
				diaSemana = "Ter";
			}else if(diaSemanaVal.equals("terça")){
				diaSemana = "Ter";
			}else if(diaSemanaVal.equals("terca-feira")){
				diaSemana = "Ter";
			}else if(diaSemanaVal.equals("terca")){
				diaSemana = "Ter";
			}else if(diaSemanaVal.equals("quarta-feira")){
				diaSemana = "Qua";
			}else if(diaSemanaVal.equals("quarta")){
				diaSemana = "Qua";
			}else if(diaSemanaVal.equals("quinta-feira")){
				diaSemana = "Qui";
			}else if(diaSemanaVal.equals("quinta")){
				diaSemana = "Qui";
			}else if(diaSemanaVal.equals("sexta-feira")){
				diaSemana = "Sex";
			}else if(diaSemanaVal.equals("sexta")){
				diaSemana = "Sex";
			}else if(diaSemanaVal.equals("sabado")){
				diaSemana = "Sab";
			}else if(diaSemanaVal.equals("sábado")){
				diaSemana = "Sab";
			}
			
			//Obtendo dia do mês
			String diaMes = dataPorExtenso[1];
			
			//obtendo mes do ano
			String mesAno = null;
			String mesAnoVal = dataPorExtenso[3].toLowerCase();
			
			if (mesAnoVal.equals("janeiro")){
				mesAno = "Jan";
			}else if(mesAnoVal.equals("fevereiro")){
				mesAno = "Fev";
			}else if(mesAnoVal.equals("março")){
				mesAno = "Mar";
			}else if(mesAnoVal.equals("marco")){
				mesAno = "Mar";
			}else if(mesAnoVal.equals("abril")){
				mesAno = "Abr";
			}else if(mesAnoVal.equals("maio")){
				mesAno = "Mai";
			}else if(mesAnoVal.equals("junho")){
				mesAno = "Jun";
			}else if(mesAnoVal.equals("julho")){
				mesAno = "Jul";
			}else if(mesAnoVal.equals("agosto")){
				mesAno = "Ago";
			}else if(mesAnoVal.equals("setembro")){
				mesAno = "Set";
			}else if(mesAnoVal.equals("outubro")){
				mesAno = "Out";
			}else if(mesAnoVal.equals("novembro")){
				mesAno = "Nov";
			}else if(mesAnoVal.equals("dezembro")){
				mesAno = "Dez";
			}
			
			//obtendo hora
			String[] horaTemp = dataPorExtenso[6].split("h");
			String hora = horaTemp[0];
			
			//obtendo minuto
			String[] minF = dataPorExtenso[6].split("min");
			String minTemp[] = minF[0].split("h");
			String min = minTemp[1];
			
			//obtendo segundo
			String seg = minF[1].replace("s", "");
			
			//concatenando a hora completa
			String horaFinal = hora + ":" + min + ":" + seg;
			
			//Obtendo ano
			String ano = dataPorExtenso[5];
			
			//obtendo Locale
			String locale = dataPorExtenso[7];
			
			//Concatenando data no padrão EEE MMM dd HH:mm:ss yyyy Locale pt_BR"
			String dataFinal = diaSemana + " " + mesAno + " " + diaMes + " " + horaFinal + " " + locale + " " + ano;
			data = dataFinal;
			formatador = formato5;
	
		//validando padrão 6
		}else if (data.matches(patternFormato6)){
			log.fine("Padrão reconhecido yyy-dd-mm HH:mm:ss");
			formatador = formato6;
			
		//validando padrão 7
		}else if (data.contains("GMT+") || data.contains("GMT-")){
			log.fine("Padrão reconhecido EEE MMM dd HH:mm:ss z yyyy");
			formatador = formato7;
		}
		
		else{
			log.severe("Nenhum padrão reconhecido");
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
	
//	public static void main(String args[]){
//		
//		DateParser test = new DateParser();
//		//Arrumar bug da data Sab, não consegue realizar parse.
//		//Date NewFormat = test.parser("Sat Mar 04 16:09:05 BRT 2013");
//		Date NewFormat = test.parser("Mon Sep 15 22:27:57 GMT-03:00 2014");
//		System.out.println("Data formadata: " + NewFormat);			
//	}	
}