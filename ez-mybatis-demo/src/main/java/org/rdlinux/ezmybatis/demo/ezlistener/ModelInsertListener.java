package org.rdlinux.ezmybatis.demo.ezlistener;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class ModelInsertListener extends AbstractEnc implements EzMybatisInsertListener {
    public ModelInsertListener(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void onInsert(Object entity) {
        this.onBatchInsert(Collections.singletonList(entity));
    }

    @Override
    public void onBatchInsert(Collection<?> entities) {
        log.info("监听到插入事件");
        for (Object entity : entities) {
            if (entity instanceof BaseEntity) {
                if (((BaseEntity) entity).getId() == null) {
                    ((BaseEntity) entity).setId(UUID.randomUUID().toString().replaceAll("-", ""));
                }
                ((BaseEntity) entity).setCreateTime(new Date());
                ((BaseEntity) entity).setUpdateTime(new Date());
            }
        }
        this.enc(entities);
    }
}
