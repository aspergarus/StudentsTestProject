package beans;

public class FileBean {
	int fid;
	String type;
	String name;
	int ownerId;
	
	public FileBean(int fid, String type, String name, int ownerId) {
		this.fid = fid;
		this.type = type;
		this.name = name;
		this.ownerId = ownerId;
	}
	
	public FileBean() { }

	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
}
