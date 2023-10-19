package Chatroom;

import java.net.*;
import java.io.*;
import java.util.*;
import org.json.simple.*;

public class Client {

    public static void main(String[] args) throws IOException
    {
        //identity holds the username given by the user
        String identity = null;

        //for the socket
        String hostName = "localhost";
        int portNumber = 12345;

        //the socket allows a connection to the server to be made, the printwriter allows for communication between the
        //server and the client. The buffers make communication more efficient.
        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            String userInput;
            UserMenus.StartMenu();
            while ((userInput = stdIn.readLine()) != null)
            {
                // Parse user and build request
                Request req;
                Scanner sc = new Scanner(userInput);
                try
                {
                    switch (sc.next())
                    {
                        case "login":
                            Scanner scanner = new Scanner(System.in);
                            UserMenus.LoginMenu();
                            identity = scanner.nextLine();
                            UserMenus.userDashboard(identity);
                            req = new OPENRequest(identity);
                            break;

                        case "P":
                            Scanner scan = new Scanner(System.in);
                            String message = sc.nextLine();
                            Message m = new Message(identity, 0, message);
                            System.out.print("\t\t\t\t\tchannel: ");
                            String channel = scan.nextLine();
                            UserMenus.userDashboard(identity);
                            req = new PublishRequest(m, channel);
                            break;

                        case "S":
                            Scanner scanner2 = new Scanner(System.in);
                            UserMenus.SubscribeMenu(identity);
                            String channelname = scanner2.nextLine();
                            UserMenus.userDashboard(identity);
                            req = new SubscribeRequest(channelname);
                            break;

                        case "U":
                            Scanner scanner3 = new Scanner(System.in);
                            System.out.println("\t\t\t\t\t~~ Unsubscribe from a Channel ~~");
                            System.out.println("Enter Channel:");
                            String unsubChannel =scanner3.nextLine();
                            UserMenus.userDashboard(identity);
                            req = new UnsubscribeRequest(unsubChannel);
                            break;

                        case "G":
                            System.out.println("\t\t\t\t\tMessages: ");
                            req = new GETRequest();
                            break;

                        case "Q":
                            UserMenus.Logout(identity);
                            req = new QuitRequest();
                            break;

                        case "K":
                            Scanner s = new Scanner(System.in);
                            System.out.print("\t\t\t\t\tEnter word: ");
                            String keyword = s.nextLine();
                            System.out.println("\t\t\t\tMessages containing: " + keyword);
                            req = new KeywordRequest(keyword);
                            break;

                        case "TS":
                            System.out.println("\t\t\t\tFilter by Timestamp");
                            Scanner n = new Scanner(System.in);
                            System.out.print("\t\t\t\tEnter start: ");
                            String start = n.nextLine();
                            System.out.print("\t\t\t\tEnter end: ");
                            String end = n.nextLine();
                            System.out.println("\t\t\t\tMessages Between: " + start + " and " + end + ":");
                            req = new TimeRequest(start, end);
                            break;

                        default:
                            System.out.println("~~That was not recognised as an option~~");
                            continue;
                    }
                }
                catch (NoSuchElementException e)
                {
                    System.out.println("Error: Client[switch statement]");
                    continue;
                }

                // Send request to server
                out.println(req);

                // If the server is no longer responding
                String serverResponse;
                if ((serverResponse = in.readLine()) == null)
                    break;

                // Parse JSON response, then try to deserialize JSON
                Object json = JSONValue.parse(serverResponse);
                Response resp;

                // Try to deserialize a success response
                if (SuccessResponse.fromJSON(json) != null)
                    continue;

                // Try to deserialize a list of messages
                if ((resp = MessageListResponse.fromJSON(json)) != null) {
                    for (Message m : ((MessageListResponse)resp).getMessages())
                        System.out.println(m);
                    continue;
                }

                // Try to deserialize an error response
                if ((resp = ErrorResponse.fromJSON(json)) != null) {
                    System.out.println(((ErrorResponse)resp).getError());
                    continue;
                }



                // The response sent by the server is not a recognised one
                System.out.println("Unrecognised Response: " + serverResponse +
                        " parsed as " + json);
                break;
            }
        } catch (UnknownHostException e)
        {
            System.err.println("Unrecognised host: " + hostName);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Error in connecting to the Server: " +
                    hostName);
            System.exit(1);
        }
    }
}
