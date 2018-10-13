package org.linuxprobe.crud.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseUpdatedInfoModel extends BaseCreatedInfoModel {
	private Date lastUpdateDate;
	private String lastUpdateBy;
}
