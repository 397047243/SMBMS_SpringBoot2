package cn.xrz.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Role{
	
	private Integer id;   //id
	private String roleCode; //角色编码
	private String roleName; //角色名称
	private Integer createdBy; //创建者
	private Date creationDate; //创建时间
	private Integer modifyBy; //更新者
	private Date modifyDate;//更新时间

    public Role(){}

	public Role( String roleCode, String roleName, Integer createdBy, Date creationDate, Integer modifyBy, Date modifyDate) {
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

}
