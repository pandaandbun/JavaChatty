package GUI.Model;

public class Message {
    private String email;
    private String fullname;
    private String message;

    public Message(String email, String fullname, String message) {
        this.email = email;
        this.fullname = fullname;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
