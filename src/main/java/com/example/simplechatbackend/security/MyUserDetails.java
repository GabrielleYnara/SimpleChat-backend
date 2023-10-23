package com.example.simplechatbackend.security;

import com.example.simplechatbackend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class MyUserDetails implements UserDetails {

    /**
     * The user associated with this instance of MyUserDetails
     */
    private final User user;

    /**
     * Constructor that associates a specific User with this instance.
     * @param user A representation of a User.
     */
    public MyUserDetails(User user) {
        this.user = user;
    }

    /**
     * Getter for what database roles this User has.
     * @return The Set of database roles that this User was configured with.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    /**
     * Getter for the User's password.
     * @return The User's password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Getter for the User's username.
     * @return The User's username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Checks the database to see if this User is expired because it has been inactive for
     * too long.
     * @return True if User is still active, false if not.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks the database to see if this User is prevented from participating due to a ban
     * or other reason.
     * @return True if User is allowed to participate, false if not.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks to see if User has had their credentials for too long and needs to update them.
     * @return True if credentials don't need to be updated, false if not.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks to see if User is enabled in the database.
     * @return True if enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Getter for the User associated with this instance.
     * @return A data representation of the User associated with this instance.
     */
    public User getUser() {
        return user;
    }
}
