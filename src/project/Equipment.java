package project;

/*
 * Name: Tiara, Tarik, William
 * Date: 2026.01.13
 * Description: Equipment like laptops, projectors, sports gear
 */

public class Equipment extends Resource{
	 	private String equipmentType; // "Laptop Cart", "Projector", "Basketballs", etc.

	    
	    public Equipment(String id, String equipmentType, int quantity) {
	        super(id, equipmentType);
	        this.equipmentType = equipmentType;
	    }
	    
	    public String getEquipmentType() {
	        return equipmentType;
	    }
}
