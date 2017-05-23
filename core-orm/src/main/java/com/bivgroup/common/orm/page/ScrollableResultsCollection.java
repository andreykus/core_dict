package com.bivgroup.common.orm.page;

import com.bivgroup.common.orm.OrmProviderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import java.util.*;

/**
 * Created by bush on 10.08.2016.
 * Коллекция для педжтрованного списка
 * на основе ограничения начальной и конечной записи
 */
public class ScrollableResultsCollection extends AbstractCollection {
    /**
     * логер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * стартовать с первой страницы
     */
    private Boolean START_INIT_ZERO_PAGE = Boolean.FALSE;
    /**
     * префикс запроса посчета количества записей
     */
    private final static String SUF_COUNT_QUERY = "Count";
    /**
     * страница размер DEFAULT
     */
    private int DEFAULT_PAGE_SIZE = 1;
    /**
     * размер страницы
     */
    private Integer pageSize = null;
    /***/
    private Integer collectionSize = null;
    /**
     * sql запрос на получение количество записей в списке
     */
    private String queryCount;

    /**
     * sql запрос на получение всех данных списка
     */
    private String queryAll;
    /**
     * параметры
     */
    private Map params;
    /**
     * провайдер для работы с БД
     */
    private OrmProviderImpl orm;

    /**
     * Конструктор Коллекция для педжтрованного списка
     *
     * @param orm      - провайдер для работы с БД
     * @param params   - параметры
     * @param queryAll - sql запрос на получение всех данных списка
     */
    public ScrollableResultsCollection(OrmProviderImpl orm, Map params, String queryAll) {
        this(orm, params, queryAll, null, null);
    }

    /**
     * Конструктор Коллекция для педжтрованного списка
     *
     * @param orm        - провайдер для работы с БД
     * @param params     - параметры
     * @param queryAll   - sql запрос на получение всех данных списка
     * @param queryCount - sql запрос на получение количество записей в списке
     * @param pageSize   -  записей на странице
     */
    public ScrollableResultsCollection(OrmProviderImpl orm, Map params, String queryAll, String queryCount, Integer pageSize) {
        this.pageSize = pageSize;
        this.queryCount = queryCount;
        this.queryAll = queryAll;
        if (this.pageSize == null) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        if (this.queryCount == null) {
            this.queryCount = this.queryAll + SUF_COUNT_QUERY;
        }
        this.params = params;
        this.orm = orm;
    }

    /**
     * итератор
     *
     * @return - итератор
     */
    public PagedIteratorImpl iterator() {
        return new PagedIteratorImpl(size());
    }

    /**
     * инициялизация
     *
     * @return количество записей
     */
    public synchronized int size() {
        if (collectionSize == null) {
            javax.persistence.Query query = orm.getEntityManager().createNamedQuery(this.queryCount);
            orm.predProcessParam(query, params);
            collectionSize = ((Number) query.getSingleResult()).intValue();
        }
        return collectionSize;
    }

    /**
     * Итератор для педжированног списка
     * {@link PageIterator}
     */
    public class PagedIteratorImpl implements PageIterator {
        /**
         * это последняя страница
         */
        public boolean lastPage = false;
        /**
         * с записи
         */
        private int offset;
        private List currentCollection;
        private Iterator currentIterator;
        /**
         * количество записей
         */
        private int size;
        /**
         * результат
         */
        private ScrollableResults scrollebleResults;

        /**
         * Конструктор итератор для педжированног списка
         *
         * @param size
         */
        public PagedIteratorImpl(int size) {
            this.size = size;
            org.hibernate.query.Query query = orm.getSession().createNamedQuery(queryAll);
            orm.predProcessParam(query, params);
            this.scrollebleResults = query.setCacheMode(CacheMode.IGNORE).setFetchSize(pageSize).setMaxResults(pageSize).scroll(ScrollMode.SCROLL_INSENSITIVE);
            if (START_INIT_ZERO_PAGE) {
                this.currentCollection = loadRowPage(0);
                this.currentIterator = currentCollection.iterator();
            }
        }

        /**
         * страниц
         *
         * @return - количество страниц
         */
        public int pages() {
            int pages = 1;
            if (size > pageSize) {
                pages = (int) Math.ceil(size / pageSize);
            }
            if (size == 0) {
                pages = 0;
            }
            return pages;
        }

        /**
         * загрузить страницу
         *
         * @param page - страница
         * @return - список
         */
        public List loadPage(int page) {
            if (page == 0 || (page != 1 && page * pageSize > size)) {
                throw new NoSuchElementException();
            }
            this.offset = page == 1 ? 0 : page * pageSize - 1;
            return loadRowPage(this.offset);
        }

        /**
         * загрузить с записи
         *
         * @param offsets - номер записи
         * @return - список
         */
        private List loadRowPage(int offsets) {
            if (offset > size) {
                throw new NoSuchElementException();
            }
            this.offset = offsets;
            currentCollection = new ArrayList(pageSize);
            if ((this.offset + pageSize) >= size) {
                lastPage = true;
            }
            int scrollSize = lastPage ? size - offset : pageSize;
            for (int i = 0; i < scrollSize; i++) {
                if (!scrollebleResults.next()) {
                    throw new NoSuchElementException();
                }
                currentCollection.add(scrollebleResults.get());
            }
            currentIterator = currentCollection.iterator();
            return currentCollection;
        }

        /**
         * есть следующая страница
         *
         * @return - true если есть
         */
        public boolean hasNext() {
            return size() > 0 && (currentIterator.hasNext() || !lastPage);
        }

        /**
         * следующая страница
         *
         * @return - список
         */
        public Object next() {
            if (!currentIterator.hasNext()) {
                unloadPage(currentCollection);
                currentCollection = loadPage(offset + pageSize);
                currentIterator = currentCollection.iterator();
            }
            return currentIterator.next();
        }

        /**
         * перезагрузка
         *
         * @param currentCollection
         */
        protected void unloadPage(List currentCollection) {
            for (Object o : currentCollection) {
                //TODO CLEAN FLUSH;
            }
        }

        /**
         * очистка
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
