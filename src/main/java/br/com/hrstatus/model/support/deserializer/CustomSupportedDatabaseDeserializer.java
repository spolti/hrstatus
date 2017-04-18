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

package br.com.hrstatus.model.support.deserializer;

import br.com.hrstatus.model.support.SupportedDatabase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class CustomSupportedDatabaseDeserializer extends JsonDeserializer<SupportedDatabase> {

    /**
     * Deserialize SupportedDatabase json request
     *
     * @param jsonParser             Parser
     * @param deserializationContext DeserializationContext
     * @return Enum SupportedDatabase
     * @throws IOException for IO exceptions
     */
    @Override
    public SupportedDatabase deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return SupportedDatabase.valueOf(jsonParser.getText());
    }
}