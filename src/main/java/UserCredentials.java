public class UserCredentials {

    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(User user){
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public static UserCredentials withIncorrectEmail(User user){
        return new UserCredentials(user.getEmail() +"1", user.getPassword());
    }

    public static UserCredentials withIncorrectPassword(User user){
        return new UserCredentials(user.getEmail() , user.getPassword()+"1");
    }


}
