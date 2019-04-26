package cn.xrz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = {"cn.xrz.dao"}) //配置扫描Mapper的包
@EnableTransactionManagement //开启注解事务管理
public class SmbmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmbmsApplication.class, args);
    }

}
