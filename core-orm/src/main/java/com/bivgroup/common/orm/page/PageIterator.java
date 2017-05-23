package com.bivgroup.common.orm.page;

import java.util.Iterator;
import java.util.List;

/**
 * Created by bush on 10.08.2016.
 * Интефейс итератора для педжинайии
 */
public interface PageIterator<E> extends Iterator<E> {
    /**
     * загрузить страницу
     *
     * @param offset - с страницы
     * @return список
     */
    List loadPage(int offset);

    /**
     * страниц
     * @return - страниц
     */
    int pages();

}
