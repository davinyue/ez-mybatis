package org.rdlinux.ezmybatis.demo.ezlistener;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class ModelUpdateListener extends AbstractEnc implements EzMybatisUpdateListener {


    public ModelUpdateListener(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void onUpdate(Object entity) {
        this.onBatchUpdate(Collections.singletonList(entity));
    }

    @Override
    public void onBatchUpdate(Collection<?> entities) {
        log.info("监听到更新事件");
        for (Object entity : entities) {
            if (entity instanceof BaseEntity) {
                ((BaseEntity) entity).setUpdateTime(new Date());
            }
        }
        this.enc(entities);
    }

    @Override
    public void onReplace(Object entity) {
        this.onUpdate(entity);
    }

    @Override
    public void onBatchReplace(Collection<?> models) {
        this.onBatchUpdate(models);
    }

    @Override
    public void onEzUpdate(EzUpdate ezUpdate) {
        Table table = ezUpdate.getTable();
        if (table instanceof EntityTable) {
            EntityTable entityTable = (EntityTable) table;
            Class<?> etType = entityTable.getEtType();
            if (BaseEntity.class.isAssignableFrom(etType)) {
                boolean existsUpdateTime = false;
                for (UpdateItem item : ezUpdate.getSet().getItems()) {
                    if (item == null) {
                        continue;
                    }
                    if (item instanceof UpdateFieldItem) {
                        if (((UpdateFieldItem) item).getField().equals(BaseEntity.Fields.updateTime)) {
                            existsUpdateTime = true;
                            break;
                        }
                    }
                }
                if (!existsUpdateTime) {
                    ezUpdate.getSet().getItems().add(new UpdateFieldItem((entityTable),
                            BaseEntity.Fields.updateTime, ObjArg.of(new Date())));
                }
            }
        }
    }

    @Override
    public void onEzBatchUpdate(Collection<EzUpdate> ezUpdates) {
        ezUpdates.forEach(this::onEzUpdate);
    }
}
