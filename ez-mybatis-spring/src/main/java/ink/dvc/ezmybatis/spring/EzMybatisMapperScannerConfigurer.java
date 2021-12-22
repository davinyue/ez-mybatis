package ink.dvc.ezmybatis.spring;

import ink.dvc.ezmybatis.core.mapper.EzMapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;

public class EzMybatisMapperScannerConfigurer extends MapperScannerConfigurer {
    @Override
    public void setBasePackage(String basePackage) {
        basePackage = basePackage + "," + EzMapper.class.getPackage().getName();
        super.setBasePackage(basePackage);
    }
}
