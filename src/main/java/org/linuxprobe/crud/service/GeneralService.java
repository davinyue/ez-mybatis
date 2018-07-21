package org.linuxprobe.crud.service;

import java.io.IOException;
import java.util.List;
import org.linuxprobe.crud.model.BaseModel;
import org.linuxprobe.crud.query.BaseQuery;

/** 通用service */
public interface GeneralService {
	/** 添加 */
	public BaseModel add(BaseModel model);

	/**
	 * 批量添加
	 * 
	 * @throws Exception
	 */
	public List<BaseModel> batchAdd(List<BaseModel> models) throws Exception;

	public <Model extends BaseModel> int remove(String id, Class<Model> modelClass) throws Exception;

	public <Model extends BaseModel> long batchRemove(List<String> ids, Class<Model> modelClass) throws Exception;

	public <T> List<T> select(BaseQuery param, Class<T> type) throws IOException;

	public long selectCount(BaseQuery param);

	/** 增量更新 */
	public int localUpdate(BaseModel model);

	/** 全量更新 */
	public int globalUpdate(BaseModel model);

}
