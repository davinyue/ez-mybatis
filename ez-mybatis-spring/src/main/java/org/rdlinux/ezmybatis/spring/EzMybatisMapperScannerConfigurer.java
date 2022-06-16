package org.rdlinux.ezmybatis.spring;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;

public class EzMybatisMapperScannerConfigurer extends MapperScannerConfigurer {
    public EzMybatisMapperScannerConfigurer() {
        super.setBasePackage(EzMapper.class.getPackage().getName());
    }

    @Override
    public void setBasePackage(String basePackage) {
        String defaultPackage = EzMapper.class.getPackage().getName();
        if (!basePackage.contains(defaultPackage)) {
            basePackage = basePackage + "," + defaultPackage;
        }
        super.setBasePackage(basePackage);
    }
}
