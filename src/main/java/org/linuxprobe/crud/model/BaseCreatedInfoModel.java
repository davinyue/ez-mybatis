package org.linuxprobe.crud.model;

import java.util.Date;
import org.linuxprobe.crud.persistence.annotation.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCreatedInfoModel extends BaseModel {
	/** 创建时间 */
	@Column(updateIgnore = true)
	private Date createTime;
	/** 创建人id */
	@Column(updateIgnore = true)
	private String createrId;

}
