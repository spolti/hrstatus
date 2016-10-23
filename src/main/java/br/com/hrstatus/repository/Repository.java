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
    * Import the initial configuration in the database if it is a fresh database.
    */
    void initialImport();

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
    * Persists the given Object in the database
    */
    <T, O> T register (O obj);


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