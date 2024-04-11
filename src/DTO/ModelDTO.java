package DTO;

public class ModelDTO {

	private String name;
	private String company;
	private double price_per_ptoken;
	private double price_per_ctoken;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public double getPrice_per_ptoken() {
		return price_per_ptoken;
	}

	public void setPrice_per_ptoken(double price_per_ptoken) {
		this.price_per_ptoken = price_per_ptoken;
	}

	public double getPrice_per_ctoken() {
		return price_per_ctoken;
	}

	public void setPrice_per_ctoken(double price_per_ctoken) {
		this.price_per_ctoken = price_per_ctoken;
	}

	@Override
	public String toString() {
		return "ModelDTO [name=" + name + ", company=" + company + ", price_per_ptoken=" + price_per_ptoken
				+ ", price_per_ctoken=" + price_per_ctoken + "]";
	}

}
