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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

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


    Client client = PhoenixClient.create();
    WebResource wrTask = PhoenixTask.getResource(client, BASE_URL);
    WebResource wrSheet = PhoenixTaskSheet.getResource(client, BASE_URL); 


    public DownloadMenu() {
        super();
    }


    public String firststart() throws Exception {
        
        String path;
        File f = new File("C:/Users/Tabea/Desktop/Phoenix/target/test.txt");
        if(!f.exists()){
            PrintWriter pw = new PrintWriter(new FileWriter("test.txt")); 
            System.out.println("It seems to be your first start. Please enter where you wanna save your files:");
            path = scanner.nextLine();
            f.createNewFile();
            pw.write(path);
            pw.close();
             
        } else {
            FileReader fr = new FileReader("test.txt");
            BufferedReader br = new BufferedReader(fr);
            path = br.readLine();
            br.close();
        }
        return path;
    }
    

    public void execute(String[] args) throws Exception {
      
        //storage location
        String path = firststart();

        List<String> allTaskSheets = showAllTaskSheets();
        String sheetTitle = userChoice(allTaskSheets);
        if (sheetTitle == null) return;
        
        PhoenixTaskSheet wantedTaskSheet = titleToTask(sheetTitle);
        
        System.out.println("SheetTitle is " + sheetTitle);
        
        List<String> allTasks= showTasks(wantedTaskSheet);
        String taskTitle = userChoice(allTasks);
        
        System.out.println("TaskTitle is " + taskTitle);

        
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", taskTitle);
        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        
        PhoenixTask reqTitle = EntityUtil.extractEntity(post);
        
        
        File dir = new File(path);
        File file = new File(dir, taskTitle + ".java");
        if (!file.exists()) {
            System.out.println("BUILT!");
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


        // TODO: attachements? bilder?
        TextFilter t = EduFilter.INSTANCE;
        String description = reqTitle.getDescription();
        String descrFiltered = t.filter(description);

        List<PhoenixText> pattern = reqTitle.getPattern();

        for (PhoenixText hans : pattern) {
            try {
                Writer fw = new FileWriter(file);
                Writer bw = new BufferedWriter(fw);
                bw.write(hans.getText());
                bw.write(descrFiltered);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
