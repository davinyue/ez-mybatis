package org.rdlinux.ezmybatis.java.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.rdlinux.ezmybatis.core.mapper.EzBaseMapper;
import org.rdlinux.ezmybatis.java.entity.PayRequest;

@Mapper
public interface PayVoucherMapper extends EzBaseMapper<PayRequest, String> {
}
