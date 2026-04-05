import java.util.Date;

public class Message {
    private String message;
    private Date d;

    public Message(String m){
        this.message = m;
        this.d = new Date();
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return this.d;
    }

    public void setD(Date d) {
        this.d = d;
    }

    
}
