package com.driver;

import java.io.IOException;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    HashMap<String,User> userMap = new HashMap<>();
    HashMap<Integer,Message> messageMap = new HashMap<>();
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group,User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }


    public String createUser(String name, String mobile) {
        if(userMobile.contains(mobile)){
            return "User already exists";
        }else{
            User user = new User();
            user.setName(name);
            user.setMobile(mobile);
            userMap.put(mobile,user);
            userMobile.add(mobile);
        }

        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group = new Group();
        if(users.size()==2){
            User secondUser = users.get(1);
            String secondName = secondUser.getName();
            group.setName(secondName);

        }else{
            customGroupCount++;
            String groupName = "Group "+customGroupCount;
            group.setName(groupName);
            groupUserMap.put(group,users);
        }
        adminMap.put(group,users.get(0));

        return group;
    }

    public int createMessage(String content) {
        Date newDate = new Date();
        Message message = new Message(messageId++,content,newDate);
        messageMap.put(messageId,message);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {


        if(!groupUserMap.containsKey(group)){
            return 1; // Group does not exist
        }
        List<User> userlist = groupUserMap.get(group);
        if(!userlist.contains(sender)){
            return 2; // You are not allowed to send message
        }

        List<Message> messageList = groupMessageMap.get(group);
        messageList.add(message);
        groupMessageMap.put(group,messageList);

        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!groupUserMap.containsKey(group)){
            return  "Group does not exist";
        }
        if(adminMap.get(group)!=approver){
            return "Approver does not have rights";
        }
        List<User> userlist = groupUserMap.get(group);
        if(!userlist.contains(user)){
            return "User is not a participant";
        }

        adminMap.put(group,user);
        int index= userlist.indexOf(user);
        userlist.set(index,approver);
        userlist.set(0,user);

        groupUserMap.put(group,userlist);

        return "SUCCESS";
    }

    public int removeUser(User user) {

        return 0;
    }

    public String findMessage(Date start, Date end, int k) {

        return " ";
    }
}
