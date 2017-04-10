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

package br.com.hrstatus.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class GetLocalAddress {

    static InetAddress ipv4;


    /**
     * @return the first available ip address, except localhost, and virtual interfaces
     */
    public static InetAddress getHostAddress() {

        NetworkInterface networkInterface;

        try {
            networkInterface = Collections.list(NetworkInterface.getNetworkInterfaces()).stream().filter(ia -> {
                try {
                    return !ia.isLoopback() && !ia.getName().toString().contains("docker") && !ia.getName().toString().contains("virbr");
                } catch (SocketException e) {
                    e.printStackTrace();
                    return false;
                }
            }).findFirst().get();

            for (InetAddress i : Collections.list(networkInterface.getInetAddresses())) {
                if (!i.toString().contains(":")){
                    ipv4 = i;
                }
            }

            return ipv4;
        } catch (Exception e) {
            System.err.println("Erro ao obter endereço ip das interfaces locais.");
            return null;
        }
    }
}