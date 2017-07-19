package common;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.quidsi.core.platform.DefaultAppConfig;
import com.quidsi.core.platform.PlatformScopeResolver;
import com.quidsi.core.platform.concurrent.TaskExecutor;
import com.quidsi.core.platform.runtime.RuntimeSettings;
import com.suryani.manage.util.ApiClient;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackageClasses = AppConfig.class, basePackages = { "com.suryani.manage" }, scopeResolver = PlatformScopeResolver.class)
public class AppConfig extends DefaultAppConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/fyyy_manage?useUnicode=true&characterEncoding=utf8&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setValidationQuery("select 1");
        return dataSource;
    }

    @Override
    public RuntimeSettings runtimeSettings() {
        return RuntimeSettings.get();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.suryani.manage");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(MySQLDialect.class.getName());
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return TaskExecutor.fixedSizeExecutor(20);
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public HttpClient httpClient() {

        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        HttpClient defaultHttpClient = new DefaultHttpClient(connectionManager);

        connectionManager.setMaxTotal(100);

        defaultHttpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000);
        return defaultHttpClient;
    }

    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }

}