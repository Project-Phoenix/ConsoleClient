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
import java.util.ArrayList;
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
import de.phoenix.rs.entity.PhoenixTaskSheet;
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

    public static void showTask(PhoenixTaskSheet taskSheet){
        
        List<String> titles = new ArrayList<String>();
        List<PhoenixTask> taskTitles = taskSheet.getTasks();
        for (int i = 0; i < taskTitles.size(); i++) {
            System.out.println((i + 1) + ". " + taskTitles.get(i).getTitle());
            titles.add(i, taskTitles.get(i).getTitle());
        }
        
    }
    
    

    public void execute(String[] args) {

        String path = desiredPath();

//        List<String> allTitles = showAllTitles("download");
        List<String> allTaskSheets = showAllTaskSheets();
        String title = userChoice(allTaskSheets);
        
        //selber Client wie unten und dann vllt einfach direkt das TaskSheet übergeben um nicht den ganzen Quatsch mit SelectEntity zu machen
        Client client = PhoenixClient.create();
        WebResource wr = PhoenixTask.getResource(client, BASE_URL);
        
        SelectEntity<PhoenixTaskSheet> selectByTitle = new SelectEntity<PhoenixTaskSheet>().addKey("title", title);
        WebResource hallo = PhoenixTaskSheet.getResource(client, BASE_URL);
        ClientResponse post = hallo.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        PhoenixTaskSheet taskByTitle = EntityUtil.extractEntity(post);
        
        System.out.println("hallo " + title);
        showTask(taskByTitle);



       
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
//        File file = new File(dir, title + ".java");
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
