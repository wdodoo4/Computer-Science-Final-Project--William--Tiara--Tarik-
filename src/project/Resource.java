package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: coding resource class
 */

public class Resource {

	// Attributes

	public String id;
	public String name;

	// constructor

	public Resource (String id, String name) {
		this.id = id;
		this.name = name;
	}

	// Getter method

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

}
