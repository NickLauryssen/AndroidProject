package be.xios.model;

public class CustomButton 
{
	private String title;
	private String description;
	private Class<?> link;
	private int icon;
	private String type;
	
	public CustomButton(String title, String description, Class<?> link, int icon, String type) 
	{
		super();
		this.title = title;
		this.description = description;
		this.link = link;
		this.icon = icon;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class<?> getLink() {
		return link;
	}

	public void setLink(Class<?> link) {
		this.link = link;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
