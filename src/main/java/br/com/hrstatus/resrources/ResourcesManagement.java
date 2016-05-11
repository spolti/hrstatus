package br.com.hrstatus.resrources;

public interface ResourcesManagement {

    boolean lockRecurso(String resourceName);

    boolean releaseLock(String resourceName);

    boolean islocked(String resourceName);

}