package org.poem.core.entity;

/**
 * Created by poem on 2016/4/15.
 * 基础ID Entity
 */

import java.io.Serializable;

public interface BaseId<E extends Serializable> {


    /**
     * Gets the id.
     *
     * @return the id
     */
    /** The id. */

    public E getId();

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(E id);


}

