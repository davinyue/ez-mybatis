package org.linuxprobe.crud.model;

import org.linuxprobe.crud.core.annoatation.PrimaryKey;
import org.linuxprobe.crud.core.annoatation.PrimaryKey.Strategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseModel {
	@PrimaryKey(Strategy.UUID)
	private String id;
}
