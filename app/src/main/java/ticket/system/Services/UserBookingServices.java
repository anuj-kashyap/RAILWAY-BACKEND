package ticket.system.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.system.entities.Ticket;
import ticket.system.entities.Train;
import ticket.system.entities.User;
import ticket.system.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingServices {

    private User user;
    private List<User> userList;
    private static final String USERS_PATH = "app/src/main/java/ticket/system/localDB/users.json";
    private ObjectMapper objectMapper = new ObjectMapper();
    private Ticket[] ticket;

    public UserBookingServices(User user1) throws IOException {
        this.user = user1;
       loadUsers();
    }
    public UserBookingServices() throws IOException{
       loadUsers();
    }
    public List<User> loadUsers() throws IOException{
        File Users = new File(USERS_PATH);
        return objectMapper.readValue(Users, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File userFile = new File(USERS_PATH);
        objectMapper.writeValue(userFile, userList);
    }

    //json--> Object(User)--> Deserialize
    // Object(User)-->json --> serialize
    public void fetchBooking() {
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) {
        //Ttodo
        List<Ticket> userTickets = user.getTickets();//we get the list of ticket associated with user
        for (int i = 0; i < userTickets.size(); i++) {//iterate the list using loop
            Ticket ticket = userTickets.get(i);//
            if (ticket.getTicketId().equals(ticketId)) {//to check the ticket is found
                userTickets.remove(i);//ticket found then remove it from the list
                try {
                    saveUserListToFile(); // Save changes to file
                    return Boolean.TRUE; // Return true if cancellation is successful
                } catch (IOException e) {
                    e.printStackTrace();
                    return Boolean.FALSE; // Return false if there's an error saving changes
                }
            }
        }
        return Boolean.FALSE;
    }
    public List<Train> getTrains(String source,String destination){
        TrainService trainService=new TrainService();
        return trainService.searchTrains(source,destination);


    }
}