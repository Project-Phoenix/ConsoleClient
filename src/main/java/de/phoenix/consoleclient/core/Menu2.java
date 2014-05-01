/*
 * Copyright (C) 2014 Project-Phoenix
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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.SelectAllEntity;

public class Menu2 {
    
    /* prints and returns a list with the names of all tasksheets */
    public List<String> showAllTaskSheets() {

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(Core.client, Core.BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        if (response.getStatus() == 404) {
            System.out.println("Sorry, there are no Tasks available");
            return null;
        }

//        System.out.println("ResponseStatus in showAllTaskSheets ist: " + response.getStatus());

        List<PhoenixTaskSheet> sheets = EntityUtil.extractEntityList(response);
        List<String> sheetTitles = new ArrayList<String>();

        if (sheets.isEmpty()) {
            System.out.println("Sorry, there are no tasksheets available");
            return null;
        } else {
            for (int i = 0; i < sheets.size(); i++) {
                System.out.println("(" + (i + 1) + ") " + sheets.get(i).getTitle());
                sheetTitles.add(i, sheets.get(i).getTitle());
            }
            return sheetTitles;
        }
    }

    /* returns the title the user chose */
    public String userChosenTitle(List<String> listedTitles) {

        String title;

        // user enters name or number he wants to download
        String input = Core.scanner.nextLine();

        // String consists only of a number
        if (input.matches("[0-9]+")) {
            int inputInt = Integer.parseInt(input);

            while (inputInt > listedTitles.size()) {
                System.out.println("invalid input, try again: ");
                input = Core.scanner.nextLine();
            }

            title = listedTitles.get(Integer.parseInt(input) - 1);

            // User entered the title
        } else {
            title = input;
            while (!listedTitles.contains(title)) {
                System.out.println("Title doesn't exist, try again: ");
                title = Core.scanner.nextLine();
            }
        }
        return title;
    }
}
