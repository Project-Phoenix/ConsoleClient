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
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectEntity;

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

        String title;
        String path = desiredPath();

        List<String> allTitles = showAllTitles("download");
        showAllTaskSheets();

//        //user enters name or number he wants to download
//        String input = scanner.nextLine();
//        
//        // String consists only of a number
//        if(input.matches("[0-9]+")){
//            int inputInt = Integer.parseInt(input);
//            
//            if(inputInt > allTitles.size()){
//                System.out.println("invalid input");
//                return;
//            }
//            
//            title = allTitles.get(Integer.parseInt(input) - 1);
//           
//        // User entered the title
//        } else {
//            title = input;
//            if(!allTitles.contains(title)){
//                System.out.println("Title doesn't exist.");
//                return;
//            }
//        }
//
//        Client client = PhoenixClient.create();
//        WebResource wr = PhoenixTask.getResource(client, BASE_URL);
//        
//        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", title);
//        ClientResponse post = wr.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
//        
//        List<PhoenixTask> list = EntityUtil.extractEntityList(post);
//        PhoenixTask reqTitle = list.get(0);
//        
//        
//        File dir = new File(path);
//        File file = new File(dir, title + ".txt");
//        if (!file.exists()) {
//            System.out.println("BUILT!");
//        } else {
//            System.out.println("File already exists.");
//            return;
//        }
//
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            System.out.println("File couldn't be created.");
//            e.printStackTrace();
//        }
//
//
//        // TODO: attachements?
//        TextFilter t = EduFilter.INSTANCE;
//        String description = reqTitle.getDescription();
//        String descrFiltered = t.filter(description);
//
//        List<PhoenixText> pattern = reqTitle.getPattern();
//
//        for (PhoenixText hans : pattern) {
//            try {
//                Writer fw = new FileWriter(file);
//                Writer bw = new BufferedWriter(fw);
//                bw.write(hans.getText());
//                bw.write(descrFiltered);
//                bw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

}
