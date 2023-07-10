package org.gonnaup.accountplatform.account;

import org.gonnaup.accountplatform.account.config.AccountProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 帐号平台启动类
 *
 * @author gonnaup
 * @version created at 2023/5/31 下午7:00
 */
@SpringBootApplication
@EnableConfigurationProperties({AccountProperties.class})
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
