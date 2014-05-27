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
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.security.LoginFilter;
import de.phoenix.security.Token;
import de.phoenix.security.TokenFilter;

public class LoginMenu extends Menu {


    public LoginMenu() {

    }

    public void execute(String[] args) {

        if (args.length != 3) {
            System.out.println("[USAGE]: java -jar ... login username password");
            return;
        }

        String username = args[1];
        String password = args[2];

        // Create client to connect to jersey webservice
        Client client = Client.create();

        WebResource requestTokenRes = client.resource(Core.BASE_URL).path("token").path("request");

        
        requestTokenRes.addFilter(new LoginFilter(username, password));
        // tells if validation is possible
        ClientResponse response = requestTokenRes.get(ClientResponse.class);
        // user or password wrong, non existent
        if(response.getStatus() != 200){
            System.out.println("Wrong username or password!");
            return;
        }

        Token token = response.getEntity(Token.class);
        System.out.println("You're logged in now.");

        // Check if token is valid
        WebResource validateTokenRes = client.resource(Core.BASE_URL).path("token").path("validate");
        // from here the token is added to every client-request
        client.addFilter(new TokenFilter(token));

        response = validateTokenRes.get(ClientResponse.class);

    }
}
