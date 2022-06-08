package my.edu.utar.passwordmanager;

public class WebObj {

    public String website;
    public String username;
    public String password;

    private int id;

    public WebObj( int id,String website, String password, String username) {
        this.website = website;
        this.username = username;
        this.password = password;
        this.id = id;
    }

    @Override
    public String toString() {
        return "WebObj{" +
                "website='" + website + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
