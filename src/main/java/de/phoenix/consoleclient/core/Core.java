/*
 * Copyright (C) 2013 Project-Phoenix
 * 
 * This file is part of ConsoleClient.
 * 
 * ConsoleClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * ConsoleClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ConsoleClient.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.consoleclient.core;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class Core {

    public static void main(String[] args){
//        
//        System.out.println("Hello World");
//        
        Client c = Client.create();
        
        WebResource wr = c.resource("http://meldanor.dyndns.org:8080/PhoenixWebService/rest/helloworld");
        
        System.out.println(wr.get(String.class));
        
    }
    
}
