package org.gonnaup.accountplatform.account.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gonnaup
 * @version created at 2023/7/9 下午4:41
 */
@ConfigurationProperties(prefix = "accountplatform.account.default")
public class AccountProperties {

    /**
     * default account name properties
     */
    private AccountName accountName;

    /**
     * default avatar properties
     */
    private Avatar avatar;


    public AccountName getAccountName() {
        return accountName;
    }

    public void setAccountName(AccountName accountName) {
        this.accountName = accountName;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }


    public static class AccountName {
        /**
         * account name length
         */
        private int length = 20;

        /**
         * account name prefix
         */
        private String prefix = "xid";

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    /**
     * avatar url = host + path + fileName
     */
    public static class Avatar {
        /**
         * the url host
         */
        private String host = "http://localhost";

        /**
         * the url path
         */
        private String path = "/static/";

        /**
         * the default avatar file name
         */
        private String defaultFileName = "default_avatar.svg";

        public String defaultAvatarUrl() {
            return host + path + defaultFileName;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDefaultFileName() {
            return defaultFileName;
        }

        public void setDefaultFileName(String defaultFileName) {
            this.defaultFileName = defaultFileName;
        }
    }
}
