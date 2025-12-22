package com.fs.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FS_System 财务会计管理系统启动类
 * 
 * @author FS_System
 * @since 2025-12-20
 */
@SpringBootApplication
@MapperScan("com.fs.system.mapper")
public class FsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FsSystemApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  FS_System 财务会计管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080/api");
        System.out.println("  Druid监控: http://localhost:8080/api/druid");
        System.out.println("========================================\n");
    }
}

