/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package talking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.String;

/**
 *
 * @author gazimeem
 */
public class TalkingServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
            
     int port = 8000;
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e);

        }
        while (true) {
            Socket connection = null;
            try {
                // wait for request
                connection = socket.accept();
            } catch (Exception e) {
                System.err.println("Could not start server: " + e);

            }
            // Construct an object to process the HTTP request message.
            newRequest request = null;
            try {
                request = new newRequest(connection);
            } catch (Exception ex) {
                System.err.println("Could not start server: " + ex);
            }
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            // Start the thread.
            System.out.println("Request accepted");
            thread.start();
        }
    }
    
}
