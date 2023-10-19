package Chatroom;

import java.net.*;
import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.ParseException;


public class Server {
    static class Clock {
        private long t;

        public Clock() throws IOException
        {
            t = Read_Write.getTS();
        }

        // increment the timestamp and return the current time
        public synchronized long tick() throws IOException
        {
            long TS = ++t;
            Read_Write.setTS(TS);
            return TS;
        }
    }


    static class ClientHandler extends Thread
    {

        // shared logical clock
        private static Clock clock;

        static
        {
            try
            {
                clock = new Clock();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        // number of messages that were read by this client already
        private int read;

        // the identity field will be null if not set by an OPEN request
        private String Identity;

        //keeps track of which channels the client is subscribed to
        private List<String> SubscribedChannels = new ArrayList<>();
        private List<Message> subbedMessages= new ArrayList<>();
        private Socket client;

        //in and out are used for communication with clients
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException
        {
            client = socket;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            read = 0;
            Identity = null;
        }

        public void run() {
            try
            {
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                {
                    // used for timestamps
                    long ts = clock.tick();

                    //for showing which client sent what request in the server console
                    if (Identity != null)
                        System.out.println(Identity + ": " + inputLine);
                    else
                        System.out.println(inputLine);

                    // the inputLine is the request from the client, it is cast to a JSONObject
                    // and then deserialized by the fromJSON method
                    Object json = JSONValue.parse(inputLine);
                    Request req;






                    // If submitting an open request, the user cannot have already
                    // done so
                    if (Identity == null && (req = OPENRequest.fromJSON(json)) != null)
                    {
                        // set client Identity
                        Identity = ((OPENRequest)req).getIdentity();
                        //the server creates a new message board for this
                        //client channel, as well as a file for the messages to be saved to
                        File directory = new File("Channels/");
                        File file = new File(directory, (Identity + ".json"));
                        if(!file.exists())
                        {
                            try
                            {
                                file.createNewFile();
                            }
                            catch(IOException e)
                            {
                                out.println(new ErrorResponse("cannot create channel. [OPENRequest Server Resp.]"));
                            }
                        }

                        //the client will also be subscribed to their own channel
                        SubscribedChannels.add(((OPENRequest) req).getIdentity());


                        //add these messages to the list of messages for the client
                        Read_Write.getSubscribedMessages(String.valueOf(file), subbedMessages);

                        // a success response is sent when the request is successful
                        out.println(new SuccessResponse());
                        continue;
                    }





                    //  The Publish request can be made only if the client already has an identity
                    if (Identity != null && (req = PublishRequest.fromJSON(json)) != null)
                    {
                        //give the message a timestamp
                        ((PublishRequest) req).getMessage().setWhen(ts);

                        //the file to write to is the identity sent with the request
                        String channelName = ((PublishRequest) req).getIdentity();

                        //a client can only publish on channels they are subscribed to
                        Boolean subscribed = false;
                        for(String s : SubscribedChannels)
                        {
                            if(s.contains(channelName))
                            {
                                subscribed = true;
                                break;
                            }
                        }

                        if(subscribed == true)
                        {
                            File file = new File("Channels/" + channelName + ".json");
                            //the file must exist in order to publish
                            if(Read_Write.checkStrLen(((PublishRequest) req).getMessage().getBody()))
                            {
                                if (file.exists())
                                {
                                    //add the message to the file
                                    Read_Write.appendMsg(("Channels/" + channelName + ".json"), ((PublishRequest) req).getMessage());
                                } else
                                {
                                    out.println(new ErrorResponse("Error: file doesn't exist. error: Server[PostReq.]"));
                                }
                            }
                            else
                            {
                                out.println(new ErrorResponse("Error: Your message was too long"));
                            }


                            // add message with login and timestamp

                        }
                        else
                        {
                            out.println(new ErrorResponse("ERROR: You are not subscribed to that channel"));
                        }

                        // response acknowledging the post request
                        out.println(new SuccessResponse());
                        continue;
                    }




                    //  The Get request can be made only if the client already has an identity
                    if (Identity != null && GETRequest.fromJSON(json) != null)
                    {
                            List<Message> MessageList = Read_Write.subbedChannelMessagesList(SubscribedChannels);
                            out.println(new MessageListResponse(MessageList));
                        continue;
                    }




                    if (Identity != null && (req = KeywordRequest.fromJSON(json)) != null)
                    {
                        List<Message>MessageList = Read_Write.subbedChannelMessagesList(SubscribedChannels);


                        String keyword = ((KeywordRequest) req).getKeyword();

                        List<Message> subList = Read_Write.filterByKeyword(MessageList, keyword);

                        out.println(new MessageListResponse(subList));
                        continue;
                    }





                    if (Identity != null && (req = TimeRequest.fromJSON(json)) != null)
                    {
                        List<Message>MessageList = Read_Write.subbedChannelMessagesList(SubscribedChannels);

                        int start = Integer.parseInt(((TimeRequest) req).getStart());
                        int end = Integer.parseInt(((TimeRequest) req).getEnd());

                        List<Message> subListTS = Read_Write.filterByTimestamp(start, end, MessageList);

                        out.println(new MessageListResponse(subListTS));
                        continue;
                    }






                    //  The Subscribe request can be made only if the client already has an identity
                    if (Identity != null && (req = SubscribeRequest.fromJSON(json)) != null)
                    {

                        String channel = ("Channels/" + String.valueOf(((SubscribeRequest) req).getChannel() + ".json"));
                        File checkChannel = new File(channel);
                        if(checkChannel.exists())
                        {
                            // the channel is added to the list of subscribed to channels
                            SubscribedChannels.add(((SubscribeRequest)req).getChannel());
                            //add these messages to the list of messages for the client
                            Read_Write.getSubscribedMessages(channel, subbedMessages);
                        }
                        else
                        {
                            out.println(new ErrorResponse("Error: That channel does not exist"));
                        }

                        out.println(new SuccessResponse());
                        continue;
                    }







                    // This request also requires an identity
                    if (Identity != null && (req = UnsubscribeRequest.fromJSON(json)) != null)
                    {
                        //this searches through the channels that have been subscribed to. If the
                        //channel exists in the list, it is removed
                        File checkChannel = new File("Channels/" + ((UnsubscribeRequest) req).getChannel() + ".json");
                        if(checkChannel.exists())
                        {
                            Iterator<String> iter = SubscribedChannels.listIterator();
                            while (iter.hasNext()) {
                                String channel = iter.next();
                                if (channel.contains(((UnsubscribeRequest) req).getChannel())) {
                                    iter.remove();
                                }
                            }
                        }

                        out.println(new SuccessResponse());
                        continue;
                    }







                    // quit request requires a successful OPENRequest
                    if (Identity != null && QuitRequest.fromJSON(json) != null)
                    {
                        in.close();
                        out.close();
                        return;
                    }


                    // error response for requests that do not exist
                    out.println(new ErrorResponse("~~Non Existent Request Made~~"));
                }
            }
            //for when a client unexpectedly disconnects
            catch (IOException e)
            {
                System.out.println("There was an interruption with a connection");
                System.out.println(e.getMessage());
            }
        }
    }


    public static void main(String[] args) {


        //the port to listen for connections on
        int portNumber = 12345;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);

        )
        {
            while (true)
            {
                // .accept() allows the server to listen for ports and accept incoming conncetions.
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e)
        {
            System.out.println("Error: listening on port " +
                    portNumber + ". [Server: main method]");
            System.out.println(e.getMessage());
        }
    }}
