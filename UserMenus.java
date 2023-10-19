package Chatroom;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMenus
{
    public static List<String> channelNames = new ArrayList<>();
    public static void StartMenu()
    {
        System.out.println("__________________________________________________________________");
        System.out.println("\t\t\t\tWelcome to the Community!");
        System.out.println("__________________________________________________________________");
        System.out.println("01010101010101\t\tType 'login' to start\t\t\t10101010101010");
        System.out.println("__________________________________________________________________");
        System.out.print("\t\t\t\t\t\t\t");

    }

    public static void LoginMenu()
    {
        System.out.println("__________________________________________________________________");
        System.out.print("\t\t\t\t\t\tUsername: ");
    }
    public static void userDashboard(String identity)
    {
        System.out.println("__________________________________________________________________");
        System.out.println();
        System.out.println("__________________________________________________________________");
        System.out.println("__________________________________________________________________");
        System.out.println("\t\t\t\t\t\tHello " + identity + "!");
        System.out.println("__________________________________________________________________");
        System.out.println("\t\t10101010\t\tChoose an option:\t\t01010101");
        System.out.println("\t\t01010101\t\tSubscribe: 'S'\t\t\t10101010");
        System.out.println("\t\t10101010\t\tUnsubscribe: 'U'\t\t01010101");
        System.out.println("\t\t01010101\t\tPublish: 'P'\t\t\t10101010");
        System.out.println("\t\t01010101\t\tGET: 'G'\t\t\t\t10101010");
        System.out.println("\t\t10101010\t\tKeyword: 'K'\t\t\t01010101");
        System.out.println("\t\t10101010\t\tTimestamp: 'TS'\t\t\t01010101");
        System.out.println("\t\t10101010\t\tLeave: 'Q'\t\t\t\t01010101");
        System.out.println("__________________________________________________________________");
        System.out.print("\t\t\t\t\t\t");
    }

    //reads in channels available
    public static void readInChannelNames(String self)
    {
        File file = new File("Channels");
        String[] channels = file.list();
        channelNames = Arrays.asList(channels);
    }

    //display the available channels
    public static void SubscribeMenu(String self)
    {
        readInChannelNames(self);
        //get rid of the .json when displaying the file names
        String getRid = ".json";
        String replaceWith ="";

        System.out.println(self);

        System.out.println("\t\t\t\t\t\t\t~~SUBSCRIBE~~");
        System.out.println("__________________________________________________________________");
        System.out.println("\t\t\tHere are the channels you can subscribe to: ");
        System.out.println("__________________________________________________________________");
        for(int i = 0; i < channelNames.size(); i++)
        {
            if(!channelNames.get(i).contains(self))
            {
                replaceWith ="\t\t\t\t\t****|";
                String finalName = channelNames.get(i).replace(getRid, replaceWith);
                System.out.println("|****\t\t\t\t\t" + i + ") " + finalName);
                System.out.println("__________________________________________________________________");
            }
        }
        System.out.print("\t\tEnter the channel: ");
    }

    public static void Logout(String self)
    {
        System.out.println("__________________________________________________________________");
        System.out.println();
        System.out.println("__________________________________________________________________");
        System.out.println("__________________________________________________________________");
        System.out.println("\t\t\t\t\t\tBye " + self + "!");
        System.out.println("__________________________________________________________________");
    }



}
