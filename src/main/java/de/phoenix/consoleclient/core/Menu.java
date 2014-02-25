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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;

public abstract class Menu {
    
    int menuType;
    String userInput;
    
    public static Scanner scanner = new Scanner(System.in);
    public final static String BASE_URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";
    
    Client client = PhoenixClient.create();
    WebResource wrTask = PhoenixTask.getResource(client, BASE_URL);
    WebResource wrSheet = PhoenixTaskSheet.getResource(client, BASE_URL);
    
    
    public Menu(){}
    
    public abstract void execute(String[] args) throws Exception;
    
    /* show all available files */
    public List<String> showAllTitles(String menuType) {

        // friendly words for self-affirmation
        System.out.println("Jopjopjopjop");
        WebResource wr = client.resource(BASE_URL).path(PhoenixTask.WEB_RESOURCE_ROOT).path(PhoenixTask.WEB_RESOURCE_GETALL_TITLES);
        ClientResponse cresp = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        //all tasks in the database
        List<String> taskTitles = cresp.getEntity(new GenericType<List<String>>() {
        });

        if (taskTitles.isEmpty()) {
            System.out.println("There aren't any assignments to " + menuType + "!");
        } else {
            System.out.println("There are the following assignments to " + menuType + ". Please enter the name. \n");
            for (int i = 0; i < taskTitles.size(); i++) {
                System.out.println((i + 1) + ". " + taskTitles.get(i) + "\n");
            }
            return taskTitles;
        }
        return null; 
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

    /*show all task of a tasksheet*/
    public static List<String> showTasks(PhoenixTaskSheet taskSheet){
        
        List<String> titles = new ArrayList<String>();
        List<PhoenixTask> taskTitles = taskSheet.getTasks();
        for (int i = 0; i < taskTitles.size(); i++) {
            System.out.println((i + 1) + ". " + taskTitles.get(i).getTitle());
            titles.add(i, taskTitles.get(i).getTitle());
        }
        return titles;
        
    }
    
    /*convert a title to the assigned tasksheet*/
    public PhoenixTaskSheet titleToTask(String title){
         
        SelectEntity<PhoenixTaskSheet> selectByTitle = new SelectEntity<PhoenixTaskSheet>().addKey("title", title);
        ClientResponse post = wrSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        PhoenixTaskSheet taskByTitle = EntityUtil.extractEntity(post);
        
        return taskByTitle;
        
    }
    
    /*return a list with the names of all tasksheets*/
    public List<String> showAllTaskSheets(){
        
        System.out.println("mh");
        
        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(client, BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        if(response.getStatus() == 404){
            System.out.println("Sorry, there are no Tasks available");
            return null;
        }
        
        List<PhoenixTaskSheet> sheets = EntityUtil.extractEntityList(response);
        List<String> sheetTitles = new ArrayList<String>();
          
        if (sheets.isEmpty()) {
            System.out.println("noenoe");
            return null;
        } else {
            System.out.println("Jojojo");
            for (int i = 0; i < sheets.size(); i++) {
                System.out.println((i + 1) + ". " + sheets.get(i).getTitle());
                sheetTitles.add(i, sheets.get(i).getTitle());
            }
            return sheetTitles;
        }
              
    }
    
    public static String userChoice(List<String> listedTitles){
      
      String title;  
        
        //user enters name or number he wants to download
      String input = scanner.nextLine();
      
      // String consists only of a number
      if(input.matches("[0-9]+")){
          int inputInt = Integer.parseInt(input);
          
          if(inputInt > listedTitles.size()){
              System.out.println("invalid input");
              return null;
          }
          
          title = listedTitles.get(Integer.parseInt(input) - 1);
         
      // User entered the title
      } else {
          title = input;
          if(!listedTitles.contains(title)){
              System.out.println("Title doesn't exist.");
              return null;
          }
      }
      
      return title;
    }
    
    
    
    
}
