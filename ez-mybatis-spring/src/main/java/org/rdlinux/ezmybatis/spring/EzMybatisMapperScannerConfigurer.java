package org.rdlinux.ezmybatis.spring;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;

public class EzMybatisMapperScannerConfigurer extends MapperScannerConfigurer {
    @Override
    public void setBasePackage(String basePackage) {
        basePackage = basePackage + "," + EzMapper.class.getPackage().getName();
        super.setBasePackage(basePackage);
    }
}
