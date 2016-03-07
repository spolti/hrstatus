package br.com.hrstatus.resrources;

public interface ResourcesManagement {

	public boolean lockRecurso (String resourceName);
	
	public boolean releaseLock (String resourceName);
	
	public boolean islocked (String resourceName);
	
}