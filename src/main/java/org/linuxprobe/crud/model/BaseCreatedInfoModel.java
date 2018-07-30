package org.linuxprobe.crud.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCreatedInfoModel extends BaseModel {
	/** 创建时间 */
	private Date createTime;
	/** 创建人id */
	private String createrId;

}
