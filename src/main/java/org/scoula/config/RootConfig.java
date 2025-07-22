package org.scoula.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"org.scoula.chapi.service", "org.scoula.chapi.scheduler"})
@MapperScan(basePackages={"org.scoula.chapi.mapper"})
@EnableTransactionManagement
public class RootConfig {
    @Value("${jdbc.driver}") String driver;
    @Value("${jdbc.url}") String url;
    @Value("${jdbc.username}") String username;
    @Value("${jdbc.password}") String password;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    @Autowired
    ApplicationContext applicationContext;



    // MyBatis용 SqlSessionFactory를 생성하는 빈 등록 메서드
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        // SqlSessionFactoryBean은 FactoryBean 인터페이스를 구현한 클래스
        // FactoryBean을 사용하면 getObject()가 반환하는 실제 객체(SqlSessionFactory)를 빈으로 등록
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        // DB 연결을 위한 DataSource 주입
        sqlSessionFactory.setDataSource(dataSource());
        // FactoryBean의 getObject() 메서드를 호출해 실제 SqlSessionFactory 인스턴스를 리턴
        return (SqlSessionFactory) sqlSessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        return transactionManager;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}


