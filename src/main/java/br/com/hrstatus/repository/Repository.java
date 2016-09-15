package br.com.hrstatus.repository;

import br.com.hrstatus.model.Role;
import br.com.hrstatus.model.Setup;
import br.com.hrstatus.model.User;

import java.util.List;

/**
 * Created by fspolti on 9/9/16.
 */
public interface Repository {


    /*
    * load all configurations
    */
    Setup loadConfiguration();

    /*
    * Return the mail jndi
    */
    String mailJndi();

    /*
    * Return the mail from
    */
    String mailFrom();

    /*
    * Register the given user
    * @param Object Users
    */
    void registerUser(User user) throws Exception;

    /*
    * Delete the given user object
    * @param Users
    */
    void delete(User user);

    /*
    * List the registered users
    * @returns list containing all users
    */
    List<User> getUsers();

    /*
    * Search the given user
    * @returns the User object if found
    */
    User searchUser (String username);

    /*
    * Update the given user
    */
    void update (User user);

    /*
    * Get the locked users
    */
    List<User> getLockedUsers();

    /*
    *   Map user to target role
    * @param Object Roles
    */
    void save(Role role);

    /*
    * Delete all roles for the given username
    * @param String username
    */
    void delete(String username);

    /*
    * Select all roles from the given user
    * @returns a List containing the roles
    */
    List<String> getRoles(String username) throws Exception;

}
