/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talking.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author gazimeem
 */
final class newRequest implements Runnable {

    final static String NEWLINE = "\n";
    Socket socket;
    Map newmap = new HashMap();

    // Constructor
    public newRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            System.out.println("Calling Process");
            processRequest();

        } catch (Exception e) {
            System.out.println("Run Exception" + e);
        }

    }

    private void processRequest() throws Exception {
        System.out.println("Here");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        PrintStream pout = new PrintStream(os);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //Reading from Client
        String statusline = "000";
        out.println(statusline);
        String reply = br.readLine();
        System.out.println(reply);
        while (reply.equalsIgnoreCase("Quit") == false ) {
            System.out.println(reply);
            //out.println("100");

            String[] tokenized_word = reply.split("\\s");
            String firstWord = tokenized_word[0];
            // if first word is PUT
            if (firstWord.equalsIgnoreCase("PUT")) {
                String secondWord = tokenized_word[1];
                int len = Integer.parseInt(secondWord);
                //Third word
                String ki = tokenized_word[2];
                if (newmap.containsKey(ki)) {
                    statusline = "102";
                } else {
                    statusline = "001";
                    out.println(statusline);
                    String data = data = br.readLine();
                    String stopSign = br.readLine();
                    if (stopSign.equalsIgnoreCase(".") == true) {
                        if (len > (data.length())) {
                            statusline = "101";
                        } else {
                             data=data.substring(0,len);
                            System.out.println("storing "+data);        
                            newmap.put(ki, data);
                            System.out.println(ki + " " + data);
                            statusline = "000";;
                        }
                    } else {
                        statusline = "100";
                    }
                }
                out.println(statusline);

            } else if (firstWord.equalsIgnoreCase("GET")) {
                // GET
                String secondWord = tokenized_word[1];
                int len = Integer.parseInt(secondWord);
                //Third word
                String ki = tokenized_word[2];
                if (newmap.containsKey(ki)) {
                    String data = (String) newmap.get(ki);
                    if (len > data.length()) {
                        statusline = "101";
                    } else {
                       //out.println(data);
                       //out.flush();
                       statusline = data+"\n"+"000";
                    }

                } else {
                    statusline = "102";
                }
                out.println(statusline);
            } else if (firstWord.equalsIgnoreCase("CLEAR")) {
                // ClEAR
                String ki = tokenized_word[1];
                if (newmap.containsKey(ki)) {
                    newmap.remove(ki);
                    statusline = "000";
                } else {
                    statusline = "102";
                }

                out.println(statusline);
            } else {
                statusline = "100";
                out.println(statusline);

            }
            reply = br.readLine();

        }
        //Closing Everything
        out.close();
        br.close();
        socket.close();

    }

}
