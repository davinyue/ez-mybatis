package org.rdlinux.ezmybatis.java.oracle;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleDatasourceInit {
    public static JdbcTemplate jdbcTemplate;
    public static HikariDataSource dataSource;

    static {
        //实例化类
        HikariConfig hikariConfig = new HikariConfig();
        //设置url
        hikariConfig.setJdbcUrl("jdbc:oracle:thin:@192.168.1.7:1521/orclpdb1");
        //数据库帐号
        hikariConfig.setUsername("yyy");
        //数据库密码
        hikariConfig.setPassword("a");
        hikariConfig.setAutoCommit(false);
        hikariConfig.setDriverClassName("oracle.jdbc.OracleDriver");
        hikariConfig.setIdleTimeout(60000 * 5);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setConnectionInitSql("select 1 from dual");
        hikariConfig.setConnectionTestQuery("select 1 from dual");
        hikariConfig.setKeepaliveTime(10 * 6000);
        hikariConfig.setConnectionTimeout(5 * 6000);
        dataSource = new HikariDataSource(hikariConfig);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.afterPropertiesSet();
    }
}
