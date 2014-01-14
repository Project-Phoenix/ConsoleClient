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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.filter.EduFilter;
import de.phoenix.filter.TextFilter;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;

public class DownloadMenu extends Menu {

    private static Scanner scanner = new Scanner(System.in);



    public DownloadMenu() {
        super();
    }

    /* ask the user where to save downloaded files */
    public String desiredPath() {

        System.out.println("Please enter the path, you wanna save your files in");

        String path = scanner.nextLine();

        // test if the path exists by trying to create a file there
        File stats = new File(path);
        if (!stats.exists()) {
            System.out.println("Path doesn't exist.");
            return null;
        }

        return path;
    }



    public void execute(String[] args) {

        String path = desiredPath();

        showAll("download");

        //user enteres string where to save downloaded files
        String title = scanner.nextLine();

        Client client = PhoenixClient.create();
        WebResource wr = client.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETBYTITLE);

        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, title);

        PhoenixTask reqTitle = post.getEntity(PhoenixTask.class);

        File dir = new File(path);
        File file = new File(dir, title + ".txt");
        if (!file.exists()) {
            System.out.println("File doesn't exist and will hopefully be build now.");
        } else {
            System.out.println("File already exists.");
            return;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File couldn't be created.");
            e.printStackTrace();
        }


        // TODO: attachements?
        TextFilter t = EduFilter.INSTANCE;
        String description = reqTitle.getDescription();
        String descrFiltered = t.filter(description);

        List<PhoenixText> pattern = reqTitle.getPattern();

        for (PhoenixText hans : pattern) {
            try {
                Writer fw = new FileWriter(file);
                Writer bw = new BufferedWriter(fw);
                bw.write(hans.getText());
//                bw.write(hans.filterText(EduFilter.INSTANCE));
                bw.write(descrFiltered);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
