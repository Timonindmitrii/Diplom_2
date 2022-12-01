import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerate {

    public static User getRandomUser(){

        return  new User(
                RandomStringUtils.randomAlphanumeric(5)+"@yandex.ru",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static  User getUserWithoutEmail(){
        return  new User(
                "",
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static  User getUserWithoutName(){
        return  new User(
                RandomStringUtils.randomAlphanumeric(10)+"@yandex.ru",
                "",
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public static  User getUserWithoutPassword(){
        return  new User(
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphabetic(10),
                ""
        );
    }

    public static User getUserWithModified(User user){
        return new User("timonindv@yandex.ru", "Дмитрий", "Пароль");
    }




}
