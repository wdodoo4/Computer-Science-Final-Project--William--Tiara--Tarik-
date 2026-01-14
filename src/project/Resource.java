package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: coding resource class
 */

public abstract class Resource {

	// Attributes

	private String id;
	private String name;

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
	
	 public abstract String getDescription();{
	}

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " (ID: " + id + ")";
    }

}
