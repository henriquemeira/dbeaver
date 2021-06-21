/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2021 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ui.controls.resultset.generator;

import org.jkiss.dbeaver.model.data.DBDAttributeBinding;
import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCTable;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSEntity;
import org.jkiss.dbeaver.ui.controls.resultset.IResultSetController;
import org.jkiss.dbeaver.ui.controls.resultset.ResultSetRow;

import java.util.Collection;

public class SQLGeneratorDeleteFromData extends SQLGeneratorResultSet {

    @Override
    public void generateSQL(DBRProgressMonitor monitor, StringBuilder sql, IResultSetController object) {
        DBSEntity dbsEntity = getSingleEntity();
        String entityName = getEntityName(dbsEntity);
        for (ResultSetRow firstRow : getSelectedRows()) {
            Collection<DBDAttributeBinding> keyAttributes = getKeyAttributes(monitor, object);
            if (object instanceof JDBCTable) {
                sql.append(((JDBCTable) object).generateTableDeleteFrom(entityName));
            } else {
                sql.append("DELETE FROM ").append(entityName);
            }
            sql.append(getLineSeparator()).append("WHERE ");
            boolean hasAttr = false;
            for (DBDAttributeBinding binding : keyAttributes) {
                if (hasAttr) sql.append(" AND ");
                appendValueCondition(getController(), sql, binding, firstRow);
                hasAttr = true;
            }
            sql.append(";\n");
        }
    }
}
