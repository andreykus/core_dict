package com.bivgroup.dbmodel.plugin.inobject;

/**
 * Created by bush on 07.11.2016.
 * Праметр плагина - БД
 */
public class DataBase {
    /**
     * url коннекта к БД
     **/
    String url;
    /**
     * логин коннекта к БД
     **/
    String login;
    /**
     * пароль коннекта к БД
     **/
    String password;
    /**
     * класс  DataSource коннекта к БД
     **/
    String dataSourceClass;

    /**
     * логин коннекта к БД
     *
     * @return -логин коннекта к БД
     */
    public String getLogin() {
        return login;
    }

    /**
     * установить логин коннекта к БД
     *
     * @param login - логин коннекта к БД
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * пароль коннекта к БД
     *
     * @return -пароль коннекта к БД
     */
    public String getPassword() {
        return password;
    }

    /**
     * установить пароль коннекта к БД
     *
     * @param password - пароль коннекта к БД
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * класс  DataSource коннекта к БД
     *
     * @return -класс  DataSource коннекта к БД
     */
    public String getDataSourceClass() {
        return dataSourceClass;
    }

    /**
     * установить класс  DataSource коннекта к БД
     *
     * @param dataSourceClass - класс  DataSource коннекта к БД
     */
    public void setDataSourceClass(String dataSourceClass) {
        this.dataSourceClass = dataSourceClass;
    }

    /**
     * url коннекта к БД
     *
     * @return -url коннекта к БД
     */
    public String getUrl() {
        return url;
    }

    /**
     * установить url коннекта к БД
     *
     * @param url - url коннекта к БД
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
