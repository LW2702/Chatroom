package Chatroom;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Read_Write
{
   public static synchronized void getSubscribedMessages(String FileName, List<Message> SubbedMessages)
   {
        //get messages from the file and put them into the client's list of messages they can access
       try
       {
           File file = new File(FileName);
           FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr);
            JSONParser parser = new JSONParser();

           Object l = br.readLine();
            if(l != null)
            {
                JSONObject messages = (JSONObject) parser.parse((String) l);
                JSONArray msgList = (JSONArray) messages.get("list");
                Iterator<JSONObject> iter = msgList.iterator();
                while (iter.hasNext())
                {
                    Message m = Message.fromJSON(iter.next());
                    SubbedMessages.add(m);
                }
            }

       }
       catch (FileNotFoundException e)
       {
           System.out.println("ERROR: File Not Found");
       }
       catch (IOException e)
       {
           System.out.println("ERROR");
       }
       catch (ParseException e) {
           System.out.println("ERROR: cannot parse");
       }
   }

   //append message to JSONArray in .json file so the parser can
   //successfully parse multiple {} braced objects in the file
   public static synchronized void appendMsg (String file, Message msg)
   {
       try
       {
           File jsonFile = new File(file);
           JSONParser parser = new JSONParser();

           FileReader fr = new FileReader(jsonFile);
           BufferedReader br = new BufferedReader(fr);

           Object obj = br.readLine();

           //create obj of messages if one doesn't exist
           if(obj == null)
           {
               JSONArray messages = new JSONArray();
               messages.add(msg.toJSON());

               JSONObject container = new JSONObject();
               container.put("list", messages);

               FileWriter fw = new FileWriter(jsonFile);
               fw.write(container.toJSONString());
               fw.flush();
               fw.close();
           }
           //append to list if it exists
           else if(obj != null)
           {
               Object array = parser.parse((String) obj);
               JSONObject messageList = (JSONObject) array;
               JSONArray jsonArray = (JSONArray) messageList.get("list");
               jsonArray.add(msg.toJSON());
               JSONArray newJ = new JSONArray();
               newJ.addAll(jsonArray);
               messageList.put("list", newJ);
               FileWriter fw = new FileWriter(jsonFile);
               fw.write(messageList.toJSONString());
               fw.flush();
               fw.close();
           }
       }
       catch (IOException e)
       {
           throw new RuntimeException(e);
       }
       catch (ParseException e)
       {
           throw new RuntimeException(e);
       }
   }


   //used for getting the messages from a file
   public static synchronized List<Message> subbedChannelMessagesList (List<String> channels) {
       List<Message> listOfMsg = new ArrayList<>();
       for (String s : channels) {
           File directory = new File("Channels/");
           File file = new File(directory, (s + ".json"));
           if (file.exists()) {
               try {
                   FileReader fr = new FileReader(file);
                   BufferedReader br = new BufferedReader(fr);
                   JSONParser parser = new JSONParser();

                   Object l = br.readLine();
                   if (l != null) {
                       JSONObject messages = (JSONObject) parser.parse((String) l);
                       JSONArray msgList = (JSONArray) messages.get("list");
                       Iterator<JSONObject> iter = msgList.iterator();
                       while (iter.hasNext())
                       {
                           Message m = Message.fromJSON(iter.next());
                           listOfMsg.add(m);
                       }
                   }

               } catch (FileNotFoundException e) {
                   throw new RuntimeException(e);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               } catch (ParseException e) {
                   throw new RuntimeException(e);
               }


           }
       }
       return listOfMsg;
   }


   //used for returning messages based on the words they contain
   public static List<Message> filterByKeyword (List<Message> wholeList, String keyW)
   {
       //holds messages with the keyword in it
       List<Message> sublist = new ArrayList<>();
       for(Message m : wholeList)
       {
           String msg = m.getBody();
           if(msg.contains(keyW))
           {
               sublist.add(m);
           }
       }

       return sublist;
   }


   //used for returning messages within a range based on their timestamp
    public static List<Message> filterByTimestamp(int start, int end, List<Message> wholeList)
    {
        List<Message> sublist = new ArrayList<>();
        for(Message m : wholeList)
        {
            int msgTS = (int) m.getWhen();
            if(msgTS >= start && msgTS <= end)
            {
                sublist.add(m);
            }
        }

        return sublist;
    }

    //keeps track of last timestamp
    public static synchronized void setTS (long TS) throws IOException
    {
        File directory = new File("Channels/");
        File TSfile = new File(directory + "TS.txt");

        FileWriter fw = new FileWriter(TSfile);

        fw.write((int) TS);
        fw.flush();
        fw.close();
    }

    //used to set the timestamp in the server
    public static synchronized int getTS() throws IOException
    {
        File directory = new File("Channels/");
        File TSfile = new File(directory + "TS.txt");

        int TS = 0;
        if(TSfile.exists())
        {
            FileReader fr = new FileReader(TSfile);
            TS = fr.read();
            fr.close();
        }
        return TS;
    }

    //makes sure the strLen of the msg body does not succeed 25 chars
    public static boolean checkStrLen(String body)
    {
        boolean allowed = false;
        if(body.length() < 25)
        {
            allowed = true;
        }
        return allowed;
    }

}
