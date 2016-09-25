package br.com.hrstatus.repository;

import br.com.hrstatus.model.OperatingSystem;
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
    String registerUser(User user) throws Exception;

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
    String update(User user);

    /*
    * Get the locked users
    */
    List<User> getLockedUsers();

    /***************************************************************
    * Resources repository - Operating Systems
    ****************************************************************/
    /*
    * Save the give Resource (Operating System)
    */
    void save(OperatingSystem operatingSystem);

    /*
    * Save the give Resource (Operating System)
    */
    List<OperatingSystem> load();
}