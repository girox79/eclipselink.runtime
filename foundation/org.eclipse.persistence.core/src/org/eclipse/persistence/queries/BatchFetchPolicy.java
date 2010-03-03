/*******************************************************************************
 * Copyright (c) 1998, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     James Sutherland - initial API and implementation
 ******************************************************************************/  
package org.eclipse.persistence.queries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.persistence.annotations.BatchFetchType;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.internal.expressions.QueryKeyExpression;
import org.eclipse.persistence.mappings.DatabaseMapping;

/**
 * BatchFetchPolicy defines batch reading configuration.
 *
 * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setBatchFetchPolicy(BatchFetchPolicy)
 * @author James Sutherland
 */
public class BatchFetchPolicy implements Serializable, Cloneable {
    /** Define the type of batch fetching to use. */
    protected BatchFetchType type;
    /** Define the batch size for IN style batch fetching. */
    protected int size;
    /** Define the attributes to be batch fetched. */
    protected List<Expression> attributeExpressions;
    /** PERF: Used internally to store the prepared mapping queries. */
    protected transient Map<DatabaseMapping, ReadQuery> mappingQueries;
    /** PERF: Cache the local batch read attribute names. */
    protected List<String> attributes;

    public BatchFetchPolicy() {
        this.type = BatchFetchType.JOIN;
    }
    
    public BatchFetchPolicy(BatchFetchType type) {
        this.type = type;
    }
    
    public BatchFetchPolicy clone() {
        try {
            return (BatchFetchPolicy)super.clone();
        } catch (CloneNotSupportedException error) {
            throw new InternalError(error.getMessage());
        }
    }
    
    /**
     * Return the batch fetch type, (JOIN, IN, EXISTS).
     */
    public BatchFetchType getType() {
        return type;
    }

    /**
     * Set the batch fetch type, (JOIN, IN, EXISTS).
     */
    public void setType(BatchFetchType type) {
        this.type = type;
    }

    /**
     * Return the batch fetch size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the batch fetch size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * PERF: Return the internally stored prepared mapping queries.
     */
    public Map<DatabaseMapping, ReadQuery> getMappingQueries() {
        return mappingQueries;
    }

    /**
     * PERF: Set the internally stored prepared mapping queries.
     */
    public void setMappingQueries(Map<DatabaseMapping, ReadQuery> mappingQueries) {
        this.mappingQueries = mappingQueries;
    }

    /**
     * PERF: Return the cached local (only) batch read attribute names.
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * PERF: Set the cached local (only) batch read attribute names.
     */
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public void setAttributeExpressions(List<Expression> attributeExpressions) {
        this.attributeExpressions = attributeExpressions;
    }

    /**
     * INTERNAL:
     * Return all attributes specified for batch reading.
     */
    public List<Expression> getAttributeExpressions() {
        if (this.attributeExpressions == null) {
            this.attributeExpressions = new ArrayList<Expression>();
        }        
        return this.attributeExpressions;
    }

    /**
     * INTERNAL:
     * Return true is this query has batching
     */
    public boolean hasAttributes() {
        return (this.attributeExpressions != null) && (!this.attributeExpressions.isEmpty());
    }

    /**
     * INTERNAL:
     * Return if the attribute is specified for batch reading.
     */
    public boolean isAttributeBatchRead(String attributeName) {
        if (this.attributeExpressions == null) {
            return false;
        }
        List<Expression> batchReadAttributeExpressions = this.attributeExpressions;
        int size = batchReadAttributeExpressions.size();
        for (int index = 0; index < size; index++) {
            QueryKeyExpression expression = (QueryKeyExpression)batchReadAttributeExpressions.get(index);
            while (!expression.getBaseExpression().isExpressionBuilder()) {
                expression = (QueryKeyExpression)expression.getBaseExpression();
            }
            if (expression.getName().equals(attributeName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * INTERNAL:
     * Return if the attribute is specified for batch reading.
     */
    public boolean isAttributeBatchRead(ClassDescriptor mappingDescriptor, String attributeName) {
        if (this.attributeExpressions == null) {
            return false;
        }
        if (this.attributes != null) {
            return this.attributes.contains(attributeName);
        }
        return isAttributeBatchRead(attributeName);
    }
}
