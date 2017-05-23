package com.bivgroup.common.orm.page;

import com.bivgroup.common.Constants;
import com.bivgroup.common.orm.OrmProviderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ScrollableResults;
import org.hibernate.transform.Transformers;

import java.util.*;

/**
 * Created by bush on 11.08.2016.
 * Коллекция для педжтрованного списка
 * на основе курсора
 */
public class ScrollableResultsCollectionOnList extends AbstractCollection {
    /**
     * логер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * стартовать с первой страницы
     */
    private Boolean START_INIT_ZERO_PAGE = Boolean.FALSE;
    /**
     * стартовать с первой страницы
     */
    private final static String SUF_COUNT_QUERY = "Count";
    /**
     * страница размер DEFAULT
     */
    private int DEFAULT_PAGE_SIZE = 100;
    /**
     * размер страницы
     */
    private Integer pageSize = null;
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
     * Query - запрос hibernate
     */
    private org.hibernate.query.Query query;

    /**
     * Конструктор Коллекция для педжтрованного списка
     *
     * @param orm      - провайдер для работы с БД
     * @param params   - параметры
     * @param queryAll - sql запрос на получение всех данных списка
     */
    public ScrollableResultsCollectionOnList(OrmProviderImpl orm, Map params, String queryAll) {
        this(orm, params, queryAll, null, (Integer) params.get(Constants.PAGEABLE_LIST_PAGE_SIZE_NAME));
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
    public ScrollableResultsCollectionOnList(OrmProviderImpl orm, Map params, String queryAll, String queryCount, Integer pageSize) {
        this.pageSize = pageSize;
        if (this.pageSize == null) {
            this.pageSize = (Integer) params.get(Constants.PAGEABLE_LIST_PAGE_SIZE_NAME);
        }
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
     * итератор по страницам
     *
     * @return - итератор
     */
    public PagedIterator iterator() {
        return new PagedIterator(size());
    }

    /**
     * размер списка
     *
     * @return - размер списка
     */
    public synchronized int size() {
        if (collectionSize == null) {
            javax.persistence.Query query = orm.getEntityManager().createNamedQuery(this.queryCount);
            orm.predProcessParam(query, params);
            collectionSize = ((Number) query.getSingleResult()).intValue();
        }
        logger.debug(String.format("orm: size list %1s %2s", this.queryCount, collectionSize));
        return collectionSize;
    }

    /**
     * реализация итератора по страницам
     */
    public class PagedIterator implements PageIterator {
        /**это последняя страница*/
        public boolean lastPage = false;
        /**с*/
        private int offset;
        private List currentCollection;
        private Iterator currentIterator;
        /**количество записей*/
        private int size;
        /**результат*/
        private ScrollableResults scrollebleResults;

        /**
         * создать запрос
         *
         * @return - Query
         */
        private org.hibernate.query.Query createQuery() {
            query = orm.getSession().createNamedQuery(queryAll);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setMaxResults(pageSize);
            orm.predProcessParam(query, params);
            return query;
        }

        public PagedIterator(int size) {
            this.size = size;
            query = createQuery();
            if (START_INIT_ZERO_PAGE) {
                this.currentCollection = loadRowPage(0);
                this.currentIterator = currentCollection.iterator();
            }
            logger.debug(String.format("orm: count rows %1s for %2s ", size, queryAll));
            logger.debug(String.format("orm: count rows in page %1s for %2s ", pageSize, queryAll));
        }

        /**
         * количество страниц
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
            logger.debug(String.format("orm: count pages %1s for %2s ", pages, queryAll));
            return pages;

        }

        /**
         * загрузить страницу
         *
         * @param page -  страница
         * @return - список
         */
        public List loadPage(int page) {
            logger.debug(String.format("orm: load page %1s for %2s ", page, queryAll));
            if (page == 0 || (page != 1 && page * pageSize > size)) {
                throw new NoSuchElementException();
            }
            this.offset = page == 1 ? 0 : page * pageSize - 1;
            return loadRowPage(this.offset);
        }

        /**
         * загрузить с такой то записи
         *
         * @param offsets - начальная запись
         * @return - список
         */
        private List loadRowPage(int offsets) {
            logger.debug(String.format("orm: load start row %1s for %2s ", offsets, queryAll));
            if (offset > size) {
                throw new NoSuchElementException();
            }
            this.offset = offsets;
            query.setFirstResult(offset);
            currentCollection = query.list();
            return currentCollection;
        }

        /**
         * есть следующая страница ?
         *
         * @return
         */
        public boolean hasNext() {
            return size() > 0 && (currentIterator.hasNext() || !lastPage);
        }

        /**
         * следующая страница
         *
         * @return - страница данных
         */
        public Object next() {
            if (!currentIterator.hasNext()) {
                unloadPage(currentCollection);
                currentCollection = loadPage(offset + pageSize);
                currentIterator = currentCollection.iterator();
            }
            return currentIterator.next();
        }

        protected void unloadPage(List currentCollection) {
            for (Object o : currentCollection) {
                //TODO CLEAN FLUSH;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}

