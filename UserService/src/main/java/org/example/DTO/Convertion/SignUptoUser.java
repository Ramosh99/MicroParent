package org.example.DTO.Convertion;

import org.example.DTO.SignUpRequest;
import org.example.Models.User;

public class SignUptoUser {
    public static User convert(SignUpRequest user) {
        User newUser = new User();
        newUser.setEmail(user.email);
        newUser.setFirstName(user.firstName);
        newUser.setLastName(user.lastName);
        newUser.setRole(user.role);
        newUser.setAddressLine1(user.addressLine1);
        newUser.setAddressLine2(user.addressLine2);
        newUser.setAddressLine3(user.addressLine3);
        newUser.setPhoneNumber(user.phoneNumber);
        newUser.setProfilePhoto(user.profilePhoto);
        return newUser;
    }
}
