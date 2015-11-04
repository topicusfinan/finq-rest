package nl.finan.finq.common.converter;

import nl.finan.finq.common.to.UserTO;
import nl.finan.finq.entities.User;

public final class UserConverter
{

    private UserConverter()
    {
    }

    public static User convert(UserTO userTO)
    {
        User user = new User();
        user.setFirstName(userTO.getFirstname());
        user.setLastName(userTO.getLastname());
        user.setEmail(userTO.getEmail());
        user.setPassword(userTO.getPassword());
        return user;
    }
}
