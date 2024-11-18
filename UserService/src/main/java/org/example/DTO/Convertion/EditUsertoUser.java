package org.example.DTO.Convertion;

import org.example.DTO.EditUser;
import org.example.Models.User;

public class EditUsertoUser {
    public static User convert(String email,String role,String profilePhoto,EditUser user) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(user.firstName);
        newUser.setLastName(user.lastName);
        newUser.setRole(role);
        newUser.setAddressLine1(user.addressLine1);
        newUser.setAddressLine2(user.addressLine2);
        newUser.setAddressLine3(user.addressLine3);
        newUser.setPhoneNumber(user.phoneNumber);
        newUser.setProfilePhoto(profilePhoto);
        return newUser;
    }
}
